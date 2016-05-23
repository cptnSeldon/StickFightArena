/**
 * Created by nolvulon on 25.04.2016.
 */

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import java.lang.System;

public class Stickman extends SimulationFrame{

    /** ATTRIBUTES */
    //PLAYERS
    Player stick1;
    Player stick2;

    /**
     *  CONSTRUCTOR
     */
    Stickman(double scale){
        super("Stickman" , scale);
    }

    /**
     *  WORLD : INITIALIZATION
     */
    protected void initializeWorld() {

        /** FLOORS */
        //BOTTOM
        Body floorbot = new SimulationBody();{

            Convex c = Geometry.createRectangle(100.0, 1.0);
            BodyFixture bf = new BodyFixture(c);
            floorbot.addFixture(bf);
        }
        floorbot.translate(new Vector2(0, -14.0));
        floorbot.setMass(MassType.INFINITE);
        world.addBody(floorbot);

        //TOP
        Body floortop = new SimulationBody();{

            Convex c = Geometry.createRectangle(100.0, 1.0);
            BodyFixture bf = new BodyFixture(c);
            floortop.addFixture(bf);
        }
        floortop.translate(new Vector2(0, 2));
        floortop.setMass(MassType.INFINITE);
        world.addBody(floortop);

        //LEFT
        Body floorleft = new SimulationBody();{

            Convex c = Geometry.createRectangle(1.0, 100.0);
            BodyFixture bf = new BodyFixture(c);
            floorleft.addFixture(bf);
        }
        floorleft.translate(new Vector2(-12.75, -8.75));
        floorleft.setMass(MassType.INFINITE);
        world.addBody(floorleft);

        //RIGHT
        Body floorright = new SimulationBody();{

            Convex c = Geometry.createRectangle(1.0, 100.0);
            BodyFixture bf = new BodyFixture(c);
            floorright.addFixture(bf);
        }
        floorright.translate(new Vector2(12.75, -8.75));
        floorright.setMass(MassType.INFINITE);
        world.addBody(floorright);

        //STICKMAN : SETTINGS
        double stickman1x = -5;
        double stickman1y = 0;

        //PLAYER : 1
        stick1 = new Player(stickman1x, stickman1y, world, new Color(0, 122, 60));
        Body control = stick1.getGravityCenter();

        //PLAYER : 2
        stick2 = new Player(5,0.5, world, new Color(60, 30, 120));
        Body control2 = stick2.getGravityCenter();

        /** LISTENERS */
        //MOUSE LISTENER
        this.addMouseListenerToCanvas(new MouseListener() {

            //RELEASED
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            //PRESSED -> USED
            @Override
            public void mousePressed(MouseEvent e) {
                moveBodyToPointer(control,e.getPoint(), 50, 100);
            }

            //EXITED
            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            //ENTERED
            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            //CLICKED
            @Override
            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
            }
        });

        //KEY LISTENER
        this.addKeyListenerToCanvas(new KeyListener() {

            //TYPED
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            //RELEASED -> USED
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                stick2.delDirection((e.getKeyCode()==KeyEvent.VK_UP), (e.getKeyCode()==KeyEvent.VK_DOWN), (e.getKeyCode()==KeyEvent.VK_RIGHT), (e.getKeyCode()==KeyEvent.VK_LEFT));
                stick1.delDirection((e.getKeyCode()==KeyEvent.VK_W), (e.getKeyCode()==KeyEvent.VK_S), (e.getKeyCode()==KeyEvent.VK_D), (e.getKeyCode()==KeyEvent.VK_A));
            }

            //KEY PRESSED -> USED
            @Override
            public void keyPressed(KeyEvent e) {
                stick2.addDirection((e.getKeyCode()==KeyEvent.VK_UP), (e.getKeyCode()==KeyEvent.VK_DOWN), (e.getKeyCode()==KeyEvent.VK_RIGHT), (e.getKeyCode()==KeyEvent.VK_LEFT));
                stick1.addDirection((e.getKeyCode()==KeyEvent.VK_W), (e.getKeyCode()==KeyEvent.VK_S), (e.getKeyCode()==KeyEvent.VK_D), (e.getKeyCode()==KeyEvent.VK_A));
            }



        });

        /** COLLISIONS */
        world.addListener(new CollisionListener() {

            //SIMPLE TOUCH
            @Override
            public boolean collision(Body body, BodyFixture bodyFixture, Body body1, BodyFixture bodyFixture1) {

                collisionManagement(body, body1);

                return true;
            }

            //INSIDE ONE ANOTHER
            @Override
            public boolean collision(Body body, BodyFixture bodyFixture, Body body1, BodyFixture bodyFixture1, Penetration penetration) {
                return true;
            }

            @Override
            public boolean collision(Body body, BodyFixture bodyFixture, Body body1, BodyFixture bodyFixture1, Manifold manifold) {
                return true;
            }

            @Override
            public boolean collision(ContactConstraint contactConstraint) {
                return true;
            }
        });

    }

    /**
     *  COLLISION MANAGEMENT
     */
    private void collisionManagement(Body body0, Body body1) {



        //INITIALIZATION
        BodyPartType bpt1 = stick1.getBodyPartType(body0);
        BodyPartType bpt2 = stick2.getBodyPartType(body1);
        //basic settings
        stick1.setPlayerTouched(false);
        stick2.setPlayerTouched(false);

        //test : check which body part is touched
        if(bpt1 != BodyPartType.NONE && bpt2 != BodyPartType.NONE){

            SimulationBody sBody0 = (SimulationBody)body0;
            SimulationBody sBody1 = (SimulationBody)body1;




            //System.out.println("Stickman 1 touched with " + bpt1 + " Stickman 2 at " + bpt2);

            //MANAGING COLLISIONABLE BODY PARTS
            //STICKMAN 1 vs STICKMAN 2
            //head - hand - foot vs head - hand - foot
            if((bpt1 == BodyPartType.HEAD ||
                    bpt1 == BodyPartType.LEFTHAND || bpt1 == BodyPartType.RIGHTHAND ||
                    bpt1 == BodyPartType.LEFTFOOT || bpt1 == BodyPartType.RIGHTFOOT) && (bpt2 == BodyPartType.HEAD ||
                    bpt2 == BodyPartType.LEFTHAND || bpt2 == BodyPartType.RIGHTHAND ||
                    bpt2 == BodyPartType.LEFTFOOT || bpt2 == BodyPartType.RIGHTFOOT)) {

               //System.out.println("no damage");

            }
            //head - hand - foot vs other parts
            if((bpt1 == BodyPartType.HEAD ||
                    bpt1 == BodyPartType.LEFTHAND || bpt1 == BodyPartType.RIGHTHAND ||
                    bpt1 == BodyPartType.LEFTFOOT || bpt1 == BodyPartType.RIGHTFOOT) &&
                   !(bpt2 == BodyPartType.HEAD ||
                    bpt2 == BodyPartType.LEFTHAND || bpt2 == BodyPartType.RIGHTHAND ||
                    bpt2 == BodyPartType.LEFTFOOT || bpt2 == BodyPartType.RIGHTFOOT)) {

                if((System.currentTimeMillis()-sBody1.getLastTouch())>750){
                    sBody1.setLastTouch(System.currentTimeMillis());
                    sBody1.setColor(Color.RED);
                }



                //System.out.println("Stickman 1 inflicts damages to Stickman 2");

                //set touch - true
                //stick1.setPlayerTouched(true);
                //stick1.setLifePoints(stick1.getLifePoints() - stick2.getDamageOut());
                //System.out.println("Stickman 1 Life : " + stick1.getLifePoints());

            }

            //STICKMAN 2
            //head - hand - foot vs other parts
            if((bpt2 == BodyPartType.HEAD ||
                    bpt2 == BodyPartType.LEFTHAND || bpt2 == BodyPartType.RIGHTHAND ||
                    bpt2 == BodyPartType.LEFTFOOT || bpt2 == BodyPartType.RIGHTFOOT) &&
                   !(bpt1 == BodyPartType.HEAD ||
                    bpt1 == BodyPartType.LEFTHAND || bpt1 == BodyPartType.RIGHTHAND ||
                    bpt1 == BodyPartType.LEFTFOOT || bpt1 == BodyPartType.RIGHTFOOT)) {

                if((System.currentTimeMillis()-sBody0.getLastTouch())>750){
                    sBody0.setLastTouch(System.currentTimeMillis());
                    sBody0.setColor(Color.RED);
                }

                //System.out.println("Stickman 2 inflicts damages to Stickman 1");

                //set touch - true
                //stick2.setPlayerTouched(true);
                //stick2.setLifePoints(stick2.getLifePoints() - stick1.getDamageOut());
                //System.out.println("Stickman 2 Life : " + stick1.getLifePoints());

            }

            //TODO : new method allowing impulse customization
            body0.applyImpulse(0.75);
            body1.applyImpulse(0.75);

            return;
        }






    }

    /**
     *  MOVE BODY TO POINTER
     */
    private void moveBodyToPointer(Body b, Point p, double forceX, double forceY) {

        double bodyPosX = b.getTransform().getTranslationX()+11;
        double bodyPosY = b.getTransform().getTranslationY()+11;
        double xPoint = p.x/this.scale;
        double yPoint = 11-p.y/this.scale;
        double moveX = -(bodyPosX-xPoint);
        double moveY = -(bodyPosY-yPoint);

        //System.out.println(bodyPosY+", "+yPoint+", "+moveY);
        b.applyForce(new Vector2(moveX*forceX,moveY*forceY));

    }

    /** RENDER : GRAPHICS */
    @Override
    protected void render(Graphics2D g, double elapsedTime) {
        // move the view a bit
        g.translate(0, 300);

        super.render(g, elapsedTime);
    }

    /** MAIN */
    public static void main(String[] args) {
        Stickman simulation = new Stickman(50);

        simulation.run();
    }
}
