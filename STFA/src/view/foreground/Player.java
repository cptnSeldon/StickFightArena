package view.foreground;

import view.BodyRenderer;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import view.hud.LifeBarPoints;

import java.awt.Color;

import java.util.*;

public class Player extends Observable{

    /**
     *  ATTRIBUTES
     */
    //STICKMAN
     String name;
    //sizes
    double rHead = 0.35;
    double rHand = 0.2;
    double hTrunk = 2.5;
    double wTrunk = 0.25;
    double hMember = 0.15;
    double wMember = 1.5;

    double positionX = 1;
    double positionY = 0;

    Collection<RevoluteJoint> joints;

    //COLLISION MANAGEMENT
    List<Body> bodyParts;

    Body head;              //-> inflicts damage
    Body trunk;             //-> receives damage
    //upper body
    Body leftArm;           //-> receives damage
    Body rightArm;
    Body leftHand;          //-> inflicts damage
    Body rightHand;
    //lower body
    Body leftLeg;           //-> receives damage
    Body rightLeg;
    Body leftFoot;          //-> inflicts damage
    Body rightFoot;

    Color color;

    //COLLISION DETECTION
    //life points
    private int lifePoints;
    private int maxLifePoints;
    //damages
    private int damageIn;
    private int damageOut;
    //collision state
    private boolean isTouching;
    //grab & throw state
    private boolean isDragged;

    private boolean isAlive=true;
    private boolean isVulnerable = true;
    private long unvulnerabilityTime = 100;

    private Timer vulnerableTimer;


    float force = 300;
    float maxSpeed = 5;

    Vector2 directionForce;
    Boolean directionKeys[] = {false, false, false, false};

    //WORLD
    World world;

    public static int keyStatments;

    /**
     * Constructor
     * Adds the stickman into the world
     * @param positionX
     * @param positionY
     * @param world
     * @param color
     */
    public Player(String name, double positionX, double positionY, World world, Color color) {

        directionForce = new Vector2(0,0);
        bodyParts = new ArrayList<>();

        //BASIC SETTINGS
        //initialization
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.world = world;
        this.vulnerableTimer = new Timer();
        this.color=color;
        //collision detection
            //life points
        this.maxLifePoints = 100;
        this.lifePoints = this.maxLifePoints;
            //damages
        this.damageIn  = 0;
        this.damageOut = 5;
            //grab & throw state
        this.isDragged = false;

        joints = new ArrayList<>();

        /** BODY ELEMENTS */
        //HEAD
        head = new BodyRenderer(color, BodyPartType.HEAD);
        Convex c = Geometry.createCircle(rHead);
        BodyFixture bf = new BodyFixture(c);
        head.addFixture(bf);
        head.setMass(MassType.NORMAL);
        head.translate(positionX+0, positionY+hTrunk/2+rHead);
        world.addBody(head);
            //add body part to list -> removal
        bodyParts.add(head);

        //TRUNK
        trunk = new BodyRenderer(color, BodyPartType.MEMBER);
        c = Geometry.createRectangle(wTrunk, hTrunk);
        bf = new BodyFixture(c);
        trunk.addFixture(bf);
        trunk.setMass(MassType.NORMAL);
        trunk.translate(positionX, positionY);
        world.addBody(trunk);
            //add body part to list -> removal
        bodyParts.add(trunk);

        //ARM : left
        leftArm = new BodyRenderer(color, BodyPartType.MEMBER);
        c = Geometry.createRectangle(wMember, hMember);
        bf = new BodyFixture(c);
        leftArm.addFixture(bf);
        leftArm.setMass(MassType.NORMAL);
        leftArm.translate(new Vector2(positionX-wTrunk/2-wMember/2, positionY+hTrunk/2*0.75));
        world.addBody(leftArm);
            //add body part to list -> removal
        bodyParts.add(leftArm);

        //ARM : right
        rightArm = new BodyRenderer(color, BodyPartType.MEMBER);
        c = Geometry.createRectangle(wMember, hMember);
        bf = new BodyFixture(c);
        rightArm.addFixture(bf);
        rightArm.setMass(MassType.NORMAL);
        rightArm.translate(new Vector2(positionX+wTrunk/2+wMember/2, positionY+hTrunk/2*0.75));
        world.addBody(rightArm);
            //add body part to list -> removal
        bodyParts.add(rightArm);

        //HAND : left
        leftHand = new BodyRenderer(color, BodyPartType.INVULNERABLE);
        c = Geometry.createCircle(rHand);
        bf = new BodyFixture(c);
        leftHand.addFixture(bf);
        leftHand.setMass(MassType.NORMAL);
        leftHand.translate(new Vector2(positionX-wTrunk/2-wMember, positionY+hTrunk/2*0.75));
        world.addBody(leftHand);
            //add body part to list -> removal
        bodyParts.add(leftHand);

        RevoluteJoint leftHandLeftArm = new RevoluteJoint(leftArm, leftHand, new Vector2(positionX-wTrunk/2-wMember, positionY+hTrunk/2*0.75));
        world.addJoint(leftHandLeftArm);
        joints.add(leftHandLeftArm);

        //HAND : right
        rightHand = new BodyRenderer(color, BodyPartType.INVULNERABLE);
        c = Geometry.createCircle(rHand);
        bf = new BodyFixture(c);
        rightHand.addFixture(bf);
        rightHand.setMass(MassType.NORMAL);
        rightHand.translate(new Vector2(positionX+wTrunk/2+wMember, positionY+hTrunk/2*0.75));
        world.addBody(rightHand);
            //add body part to list -> removal
        bodyParts.add(rightHand);

        RevoluteJoint rightArmRightHand = new RevoluteJoint(rightArm, rightHand, new Vector2(positionX+wTrunk/2+wMember, positionY+hTrunk/2*0.75));
        world.addJoint(rightArmRightHand);
        joints.add(rightArmRightHand);

        //LEG : left
        leftLeg = new BodyRenderer(color, BodyPartType.MEMBER);
        c = Geometry.createRectangle(wMember, hMember);
        bf = new BodyFixture(c);
        leftLeg.addFixture(bf);
        leftLeg.setMass(MassType.NORMAL);
        leftLeg.translate(new Vector2(positionX-wTrunk/2-wMember/2, positionY-hTrunk/2));
        world.addBody(leftLeg);
            //add body part to list -> removal
        bodyParts.add(leftLeg);

        //LEG : right
        rightLeg = new BodyRenderer(color, BodyPartType.MEMBER);
        c = Geometry.createRectangle(wMember, hMember);
        bf = new BodyFixture(c);
        rightLeg.addFixture(bf);
        rightLeg.setMass(MassType.NORMAL);
        rightLeg.translate(new Vector2(positionX+wTrunk/2+wMember/2, positionY-hTrunk/2));
        world.addBody(rightLeg);
            //add body part to list -> removal
        bodyParts.add(rightLeg);

        //FOOT : left
        leftFoot = new BodyRenderer(color, BodyPartType.INVULNERABLE);
        c = Geometry.createCircle(rHand);
        bf = new BodyFixture(c);
        leftFoot.addFixture(bf);
        leftFoot.setMass(MassType.NORMAL);
        leftFoot.translate(new Vector2(positionX-wTrunk/2-wMember, positionY-hTrunk/2));
        world.addBody(leftFoot);
            //add body part to list -> removal
        bodyParts.add(leftFoot);

        RevoluteJoint leftFootLeftLeg = new RevoluteJoint(leftLeg, leftFoot, new Vector2(positionX-wTrunk/2-wMember, positionY-hTrunk/2));
        world.addJoint(leftFootLeftLeg);
        joints.add(leftFootLeftLeg);

        //FOOT : right
        rightFoot = new BodyRenderer(color, BodyPartType.INVULNERABLE);
        c = Geometry.createCircle(rHand);
        bf = new BodyFixture(c);
        rightFoot.addFixture(bf);
        rightFoot.setMass(MassType.NORMAL);
        rightFoot.translate(new Vector2(positionX+wTrunk/2+wMember, positionY-hTrunk/2));
        world.addBody(rightFoot);
            //add body part to list -> removal
        bodyParts.add(rightFoot);

        RevoluteJoint rightFootRightLeg = new RevoluteJoint(rightLeg, rightFoot, new Vector2(positionX+wTrunk/2+wMember, positionY-hTrunk/2));
        world.addJoint(rightFootRightLeg);
        joints.add(rightFootRightLeg);

        /** JOINTS */
        //TRUNK - HEAD
        RevoluteJoint trunkHead = new RevoluteJoint(trunk, head, new Vector2(positionX+0,positionY+hTrunk/2));
        trunkHead.setLimitEnabled(true);
        trunkHead.setLimits(Math.toRadians(-45.0), Math.toRadians(45.0));
        //trunkHead.setReferenceAngle(Math.toRadians(0.0));
        trunkHead.setMotorEnabled(false);
        trunkHead.setMotorSpeed(Math.toRadians(0.0));
        trunkHead.setMaximumMotorTorque(0.0);
        trunkHead.setCollisionAllowed(false);
        world.addJoint(trunkHead);
        joints.add(trunkHead);

        //TRUNK - ARM(left)
        RevoluteJoint trunkLeftArm = new RevoluteJoint(trunk, leftArm, new Vector2(positionX-wTrunk/2, positionY+hTrunk/2*0.75));
        trunkLeftArm.setLimitEnabled(true);
        trunkLeftArm.setLimits(Math.toRadians(-45.0), Math.toRadians(45.0));
        //trunkHead.setReferenceAngle(Math.toRadians(0.0));
        trunkLeftArm.setMotorEnabled(false);
        trunkLeftArm.setMotorSpeed(Math.toRadians(0.0));
        trunkLeftArm.setMaximumMotorTorque(0.0);
        trunkLeftArm.setCollisionAllowed(false);
        world.addJoint(trunkLeftArm);
        joints.add(trunkLeftArm);

        //TRUNK - ARM(right)
        RevoluteJoint trunkRightArm = new RevoluteJoint(trunk, rightArm, new Vector2(positionX+wTrunk/2, positionY+hTrunk/2*0.75));
        trunkRightArm.setLimitEnabled(true);
        trunkRightArm.setLimits(Math.toRadians(-45.0), Math.toRadians(45.0));
        //trunkRightArm.setReferenceAngle(Math.toRadians(0.0));
        trunkRightArm.setMotorEnabled(false);
        trunkRightArm.setMotorSpeed(Math.toRadians(0.0));
        trunkRightArm.setMaximumMotorTorque(0.0);
        trunkRightArm.setCollisionAllowed(false);
        world.addJoint(trunkRightArm);
        joints.add(trunkRightArm);

        //TRUNK - LEG(left)
        RevoluteJoint trunkLeftLeg = new RevoluteJoint(trunk, leftLeg, new Vector2(positionX-wTrunk/2, positionY-hTrunk/2));
        trunkLeftLeg.setLimitEnabled(true);
        trunkLeftLeg.setLimits(Math.toRadians(-30.0), Math.toRadians(45.0));
        trunkLeftLeg.setReferenceAngle(Math.toRadians(-45.0));
        trunkLeftLeg.setMotorEnabled(false);
        trunkLeftLeg.setMotorSpeed(Math.toRadians(0.0));
        trunkLeftLeg.setMaximumMotorTorque(0.0);
        trunkLeftLeg.setCollisionAllowed(false);
        world.addJoint(trunkLeftLeg);
        joints.add(trunkLeftLeg);

        //TRUNK - LEG(right)
        RevoluteJoint trunkRightLeg = new RevoluteJoint(trunk, rightLeg, new Vector2(positionX+wTrunk/2, positionY-hTrunk/2));
        trunkRightLeg.setLimitEnabled(true);
        trunkRightLeg.setLimits(Math.toRadians(-30.0), Math.toRadians(45.0));
        trunkRightLeg.setReferenceAngle(Math.toRadians(45.0));
        trunkRightLeg.setMotorEnabled(false);
        trunkRightLeg.setMotorSpeed(Math.toRadians(0.0));
        trunkRightLeg.setMaximumMotorTorque(0.0);
        trunkRightLeg.setCollisionAllowed(false);
        world.addJoint(trunkRightLeg);
        joints.add(trunkRightLeg);

    }

    public String getName () {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    /**
     * Tells the stickman where to move using keys
     * @param up
     * @param down
     * @param right
     * @param left
     */
    public void addDirection(boolean up, boolean down, boolean right, boolean left){
        if(isAlive){
            directionKeys[0] = (up | directionKeys[0]) & !down;
            directionKeys[1] = (down | directionKeys[1]) & !up;
            directionKeys[2] = (right | directionKeys[2]) & !left;
            directionKeys[3] = (left | directionKeys[3]) & !right;
            updateDirectionForce();
        }

    }

    /**
     * Helps in direction changes
     * @param up : boolean if key direction up is pressed
     * @param down : boolean if key direction down is pressed
     * @param right : boolean if key direction right is pressed
     * @param left : boolean if key direction left is pressed
     */
    public void delDirection(boolean up, boolean down, boolean right, boolean left){
        directionKeys[0] = (!up & directionKeys[0]);
        directionKeys[1] = (!down & directionKeys[1]);
        directionKeys[2] = (!right & directionKeys[2]);
        directionKeys[3] = (!left & directionKeys[3]);
        updateDirectionForce();
    }

    /**
     * Updates the direction's force using keys
     */
    private void updateDirectionForce(){

        float dVert = 0;
        float dHor = 0;

        if(directionKeys[0]){
            dVert = force;
        }else if(directionKeys[1]){
            dVert = -force;
        }

        if(directionKeys[2]){
            dHor = force;
        }else if(directionKeys[3]){
            dHor = -force;
        }

        this.getGravityCenter().applyForce(new Vector2(dHor, dVert));
    }


    /**
     *  SETTERs + GETTERs
     */
    //LIFE POINTS
        //setter
    public void setLifePoints(int points){

        this.lifePoints = points;
    }
        //getter
    public int getLifePoints(){ return this.lifePoints; }
    //LIFE POINTS : max
        //setter -> if customization
    public void setMaxLifePoints(int points){ this.maxLifePoints = points; }
        //getter
    public int getMaxLifePoints() { return this.maxLifePoints; }
    //DAMAGES : out
        //setter
    public void setDamageOut(int damage) { this.damageOut = damage; }
        //getter
    public int getDamageOut(){ return this.damageOut; }
    //COLLISION STATE
        //setter
    public void setPlayerTouched(boolean touched){ this.isTouching = touched; }
        //getter
    public boolean getPlayerTouched(){ return isTouching; }
    //GRAB & THROW
        //setter
    public void setPlayerDraggedState(boolean draggedState){ this.isDragged = draggedState; }
        //getter
    public boolean playerIsDragged(){ return this.isDragged; }

    public Body getGravityCenter(){
        return head;
    }

    /**
     *  Demembrates stickman if first to die
     */
    public void demembrate(){
        for(RevoluteJoint joint : joints){
            world.removeJoint(joint);
        }
    }

    /**
     * Removes stickman from the world
     */
    public void removePlayer() {

        for(Body bodyPart : bodyParts){
            world.removeBody(bodyPart);
        }
    }

    /**
     *  TESTS : life, vulnerability
     */
    public boolean isAlive(){return isAlive;}
    public boolean isVulnerable(){return isVulnerable;}

    /**
     * APPLY DAMAGE
     * @param damage
     * @param touchedBody
     */
    public void applyDamage(int damage, BodyRenderer touchedBody){

        this.lifePoints-=damage;
        if(lifePoints<=0) {
            this.lifePoints = 0;
            isAlive=false;
            this.demembrate();
            System.out.println("player dead");
        }else
        {
            isVulnerable=false;
            Color oldColor = touchedBody.getColor();
            touchedBody.setColor(Color.RED);
            this.vulnerableTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    touchedBody.setColor(oldColor);
                    isVulnerable=true;
                }
            },this.unvulnerabilityTime);
        }
            //observer notification
        this.setChanged();
        this.notifyObservers(this.lifePoints);
    }

    public List<Body> getBodyParts() {
        return bodyParts;
    }
}
