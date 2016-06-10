package view.hud;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.TextShape;
import view.BodyRenderer;
import view.foreground.Player;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 *  view.hud.HUD : Head Up Display
 */
public class HUD {

    /**
     *  ATTRIBUTES
     */
    int lifePoints_p1;
    int lifePoints_p2;
    World world;

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
        world.addBody(br);
    }
}
