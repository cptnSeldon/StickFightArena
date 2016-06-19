package view.hud;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.TextShape;
import view.BodyRenderer;
import java.awt.*;
import java.util.Calendar;
import java.util.Observable;

/**
 *  Head Up Display
 */
public class HUD {

    /** ATTRIBUTES */
    World world;
    BodyRenderer chrono;
    boolean chronoIsPaused;
    long startTime;
    long pausedTime;
    Object lockHUD;

    /**
     * Constructor
     * @param world
     */
    public HUD(World world){
        this.world = world;
    }

    /**
     * Adds a life points bar
     * @param maxLifePoints
     */
    public void addLifePointBar(int maxLifePoints, int posX, Observable player){

        //behind
        LifeBarPoints maxLifePointBar = new LifeBarPoints(maxLifePoints, new Color(232, 44, 69));
        maxLifePointBar.translate(posX, 0);
        this.world.addBody(maxLifePointBar);
        //front
        LifeBarPoints lifePointBar = new LifeBarPoints(maxLifePoints, new Color( 20,181, 33));
        lifePointBar.translate(posX, 0);
        this.world.addBody(lifePointBar);
            //observer
        player.addObserver(lifePointBar);
    }

    /**
     * Adds player name
     * @param name
     * @param posX
     * @param color
     */
    public void addPlayerName(String name, double posX, Color color) {

        //TEXT
        BodyRenderer br = new BodyRenderer(color);
        br.addFixture(new TextShape(posX,1.5,30,name));
        br.setActive(false);
        world.addBody(br);
    }

    /**
     * Adds chrono
     * @param posX
     * @param color
     */
    public void addChrono(double posX, Color color) {

        chronoIsPaused = false;
        startTime = System.currentTimeMillis();

        //CHRONO
        chrono = new BodyRenderer(color);
        updateChrono(posX, startTime);
        chrono.setActive(false);
        world.addBody(chrono);
    }

    /**
     * Updates chrono
     * @param posX
     * @param timeInMillis
     */
    public void updateChrono(double posX, long timeInMillis) {

        if(chrono != null && !chronoIsPaused) {
            chrono.removeAllFixtures();
            chrono.addFixture(new TextShape(posX,1.5,30,getDateString(timeInMillis - this.startTime)));
        }
    }

    /**
     * Pauses chrono
     */
    public void pauseChrono () {

        chronoIsPaused = true;

        pausedTime = System.currentTimeMillis();

    }

    /**
     * Resumes chrono
     */
    public void resumeChrono() {

        chronoIsPaused = false;
        startTime += System.currentTimeMillis() - pausedTime;
    }

    /**
     * Gets formated date string, used in the chrono
     * @param timeInMillis
     * @return
     */
    private String getDateString(long timeInMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int mil = calendar.get(Calendar.MILLISECOND);
        int sec = calendar.get(Calendar.SECOND);
        int min = calendar.get(Calendar.MINUTE);

        return (min > 9 ? min : "0" + min) + ":" + (sec > 9 ? sec : "0" + sec) + ":" + mil;

    }

    /**
     * Clears chrono
     */
    public void clearChrono () {

        world.removeBody(chrono);
    }
}
