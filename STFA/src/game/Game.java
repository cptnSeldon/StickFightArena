package game;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import view.BodyRenderer;
import org.dyn4j.collision.manifold.Manifold;
import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionListener;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import view.background.Background;
import view.foreground.BodyPartType;
import view.foreground.Foreground;
import view.foreground.Player;
import view.hud.HUD;
import view.menu.GameMenu;
import view.menu.IAction;

import java.lang.System;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class Game extends GameManager {


    /** ATTRIBUTES */
    //PLAYERS
    public Player stick1;
    public Player stick2;

    Foreground foreground;
    Background background;
    HUD hud;

    AudioClip music;


    /**
     * GAME : CONSTRUCTOR
     * @param scale
     */
    Game(double scale){
        super("StickFightArena", scale);
    }

    /**
     *  WORLD : INITIALIZATION
     */
    protected void initializeWorld() {

        /** BACKGROUND */
        background = new Background(world);
        background.createBackground();

        /** FOREGROUND */
        foreground = new Foreground(world);
        foreground.makeWall();
        foreground.addSomeInertBodies();


        //GAME MENU
        menu = new GameMenu(world);

        menu.setStartAction(new IAction() {
            @Override
            public void execute() {
                startGame();
            }
        });

            //pause
        menu.setContinueAction(new IAction() {

            @Override
            public void execute() {
                resume();
            }
        });
        menu.setQuitAction(new IAction() {

            @Override
            public void execute() {
                System.exit(0);
            }
        });
        menu.setBackToStartMenuAction(new IAction() {
            @Override
            public void execute() {

                if(stick1 != null)
                    stick1.removePlayer();
                if(stick2 != null)
                    stick2.removePlayer();

                menu.showStartMenu();
            }
        });

        //start
        menu.showStartMenu();

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
                         //moveBodyToPointer(control2,e.getPoint(), 50, 100);
                             menu.clickOnMenu(e.getX(), e.getY());
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
                //IN GAME

                //PLAYER
                if(stick1 == null || stick2 == null)
                    return;
                stick2.delDirection((e.getKeyCode()==KeyEvent.VK_UP), (e.getKeyCode()==KeyEvent.VK_DOWN), (e.getKeyCode()==KeyEvent.VK_RIGHT), (e.getKeyCode()==KeyEvent.VK_LEFT));
                stick1.delDirection((e.getKeyCode()==KeyEvent.VK_W), (e.getKeyCode()==KeyEvent.VK_S), (e.getKeyCode()==KeyEvent.VK_D), (e.getKeyCode()==KeyEvent.VK_A));
            }

            //KEY PRESSED -> USED
            @Override
            public void keyPressed(KeyEvent e) {
                //IN GAME
                //paused game
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){

                    if(!isPaused()){
                        pause();
                        hud.pauseChrono();
                        System.out.println("is paused");

                     } else {
                        resume();
                        hud.resumeChrono();
                        System.out.println("is resumed");
                    }
                }

                //PLAYER
                stick2.addDirection((e.getKeyCode()==KeyEvent.VK_UP), (e.getKeyCode()==KeyEvent.VK_DOWN), (e.getKeyCode()==KeyEvent.VK_RIGHT), (e.getKeyCode()==KeyEvent.VK_LEFT));
                stick1.addDirection((e.getKeyCode()==KeyEvent.VK_W), (e.getKeyCode()==KeyEvent.VK_S), (e.getKeyCode()==KeyEvent.VK_D), (e.getKeyCode()==KeyEvent.VK_A));
            }

        });

        /** COLLISIONS */
        world.addListener(new CollisionListener() {

            //SIMPLE TOUCH
            @Override
            public boolean collision(Body body, BodyFixture bodyFixture, Body body1, BodyFixture bodyFixture1) {

                if(body.isActive() && body1.isActive()&& stick1 != null && stick1.isAlive() && stick2 != null && stick2.isAlive()) {
                   collisionManagement(body, body1);
                }
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
     * Start the game
     * Create Stickman and HUD
     */
    public void startGame () {

        //clear
        menu.clear();
        if(hud != null)
            hud.clearChrono();

        //PLAYER : 1
        stick1 = new Player("Player 1", -5, 0, world, new Color(44, 100, 232));

        //PLAYER : 2
        stick2 = new Player("Player 2", 5,0, world, new Color(44, 232, 82));

        //HUD
        hud = new HUD(world);

        hud.addLifePointBar(stick1.getMaxLifePoints(), -5, stick1);
        hud.addLifePointBar(stick2.getMaxLifePoints(),  5, stick2);

        hud.addPlayerName(stick1.getName(),  6.75, stick1.getColor());
        hud.addPlayerName(stick2.getName(), 16.75, stick2.getColor());

        hud.addChrono(12.85, Color.black);

        //MUSIC
        //Sounds.playSound("/resources/fightmusic.wav");

    }

    @Override
    public void workToDoInGameLoop() {

        if(hud != null)
            hud.updateChrono(12.85, System.currentTimeMillis());
    }

    /**
     *  COLLISION MANAGEMENT
     *  TODO : avanced damage calculation
     */
    private void collisionManagement(Body body0, Body body1) {


        BodyPartType bpt0;
        BodyPartType bpt1;
        //INITIALIZATION
        if(body0 instanceof BodyRenderer){
            bpt0 = ((BodyRenderer) body0).getType();


        }else bpt0 = BodyPartType.NONE;

        if(body1 instanceof BodyRenderer){
            bpt1 = ((BodyRenderer) body1).getType();


        }else bpt1 = BodyPartType.NONE;


        //TODO : refactor to create a unique method
        //test : check which body part is touched
        if((bpt0 != BodyPartType.NONE && bpt1 != BodyPartType.NONE)){

            BodyRenderer sBody0 = (BodyRenderer)body0;
            BodyRenderer sBody1 = (BodyRenderer)body1;

            if(stick1.getBodyParts().contains(sBody0) && !stick1.getBodyParts().contains(sBody1)){
                body0.applyImpulse(0.75);

                if((bpt0 == BodyPartType.MEMBER || bpt0 == BodyPartType.HEAD) && stick1.isVulnerable()){
                    stick1.applyDamage(1, sBody0);
                }


            }

            if(stick1.getBodyParts().contains(sBody1) && !stick1.getBodyParts().contains(sBody0)){
                body1.applyImpulse(0.75);
                if((bpt1 == BodyPartType.MEMBER || bpt1 == BodyPartType.HEAD) && stick1.isVulnerable()){
                    stick1.applyDamage(1, sBody1);
                }
            }

            if(stick2.getBodyParts().contains(sBody0) && !stick2.getBodyParts().contains(sBody1)){
                body0.applyImpulse(0.75);
                if((bpt0 == BodyPartType.MEMBER || bpt0 == BodyPartType.HEAD) && stick2.isVulnerable()){
                    stick2.applyDamage(1, sBody0);
                }
            }

            if(stick2.getBodyParts().contains(sBody1) && !stick2.getBodyParts().contains(sBody0)){
                body1.applyImpulse(0.75);
                if((bpt1 == BodyPartType.MEMBER || bpt1 == BodyPartType.HEAD) && stick2.isVulnerable()){
                    stick2.applyDamage(1, sBody1);
                }
            }

                //end menu
            if(stick1 != null && !stick1.isAlive() || stick2 != null && !stick2.isAlive()){
                menu.showEndMenu();
                hud.pauseChrono();
            }


            return;
        }
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
        Game game = new Game(45);

        game.run();
    }
    
  
}