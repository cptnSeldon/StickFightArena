/**
 * Created by nolvulon on 25.04.2016.
 */

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class Stickman extends SimulationFrame{

    /** ATTRIBUTES */
    private boolean isDragged =false;

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
        floorbot.translate(new Vector2(0.6875, -12.0));
        floorbot.setMass(MassType.INFINITE);
        world.addBody(floorbot);

        //TOP
        Body floortop = new SimulationBody();{

            Convex c = Geometry.createRectangle(100.0, 1.0);
            BodyFixture bf = new BodyFixture(c);
            floortop.addFixture(bf);
        }
        floortop.translate(new Vector2(3, 2.75));
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
        double stickman1x = 1;
        double stickman1y = 0;

        //PLAYER : 1
        Player stick1 = new Player(stickman1x, stickman1y, world);
        Body control = stick1.getGravityCenter();

        //PLAYER : 2
        Player stick2 = new Player(3,0.5, world);
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
                switch (e.getKeyChar()){
                    case('w'):
                        stick1.keyStatments=stick1.keyStatments & 0x1;
                        break;
                }

            }

            //KEY PRESSED -> USED
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyChar()=='w')
                {
                    stick1.keyStatments=stick1.keyStatments | 0x1;
                }
                if (e.getKeyChar()=='s')
                {
                    stick1.keyStatments=stick1.keyStatments | 0x2;
                }
                if (e.getKeyChar()=='a')
                {
                    stick1.keyStatments=stick1.keyStatments | 0x4;
                }
                if (e.getKeyChar()=='d')
                {
                    stick1.keyStatments=stick1.keyStatments | 0x8;
                }

                if (e.getKeyCode()==KeyEvent.VK_UP)
                {
                    control2.applyForce(new Vector2(0,300));
                }
                if (e.getKeyCode()==KeyEvent.VK_DOWN)
                {
                    control2.applyForce(new Vector2(0,-300));
                }
                if (e.getKeyCode()==KeyEvent.VK_LEFT)
                {
                    control2.applyForce(new Vector2(-300,0));
                }
                if (e.getKeyCode()==KeyEvent.VK_RIGHT)
                {
                    control2.applyForce(new Vector2(300,0));
                }
            }
        });

        /** COLLISIONS */
        
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
