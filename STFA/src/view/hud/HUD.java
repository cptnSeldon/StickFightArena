package view.hud;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.TextShape;
import sun.util.resources.cldr.ru.CalendarData_ru_RU;
import view.BodyRenderer;
import view.foreground.Player;

import java.awt.*;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

/**
 *  view.hud.HUD : Head Up Display
 */
public class HUD {

    /**
     *  ATTRIBUTES
     */
    World world;
    BodyRenderer chrono;
    boolean chronoIsPaused;
    long startTime;
    long pausedTime;
    Object lockHUD;

    /**
     *  METHODS
     */

    /**
     *
     * @param world
     */
    public HUD(World world){
        this.world = world;
    }

    /**
     *
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

    public void addPlayerName(String name, double posX, Color color) {

        //TEXT
        BodyRenderer br = new BodyRenderer(color);
        br.addFixture(new TextShape(posX,1.5,30,name));
        br.setActive(false);
        world.addBody(br);
    }

    public void addChrono(double posX, Color color) {

        chronoIsPaused = false;
        startTime = System.currentTimeMillis();

        //CHRONO
        chrono = new BodyRenderer(color);
        updateChrono(posX, startTime);
        chrono.setActive(false);
        world.addBody(chrono);
    }

    public void updateChrono(double posX, long timeInMillis) {

        if(chrono != null && !chronoIsPaused) {
            chrono.removeAllFixtures();
            chrono.addFixture(new TextShape(posX,1.5,30,getDateString(timeInMillis - this.startTime)));
        }
    }

    public void pauseChrono () {

        chronoIsPaused = true;

        pausedTime = System.currentTimeMillis();

    }
    public void resumeChrono() {

        chronoIsPaused = false;
        startTime += System.currentTimeMillis() - pausedTime;
    }

    private String getDateString(long timeInMillis) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int mil = calendar.get(Calendar.MILLISECOND);
        int sec = calendar.get(Calendar.SECOND);
        int min = calendar.get(Calendar.MINUTE);

        return (min > 9 ? min : "0" + min) + ":" + (sec > 9 ? sec : "0" + sec) + ":" + mil;

    }

    public void clearChrono () {

        world.removeBody(chrono);
    }
}
