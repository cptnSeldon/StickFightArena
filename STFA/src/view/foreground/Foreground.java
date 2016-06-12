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

/**
 *  this will display the GAME
 */
public class Foreground {

    World world;

    public Foreground(World world) {
        this.world = world;
    }

    public void makeWall(){
        /** FLOORS */
        //BOTTOM
        Body floorbot = new BodyRenderer();{

            Convex c = Geometry.createRectangle(100.0, 1.0);
            BodyFixture bf = new BodyFixture(c);
            floorbot.addFixture(bf);
        }
        floorbot.translate(new Vector2(0, -15.4));
        floorbot.setMass(MassType.INFINITE);
        world.addBody(floorbot);

        //TOP
        Body floortop = new BodyRenderer();{

            Convex c = Geometry.createRectangle(100.0, 1.0);
            BodyFixture bf = new BodyFixture(c);
            floortop.addFixture(bf);
        }
        floortop.translate(new Vector2(0, 2));
        floortop.setMass(MassType.INFINITE);
        world.addBody(floortop);

        //LEFT
        Body floorleft = new BodyRenderer();{

            Convex c = Geometry.createRectangle(1.0, 100.0);
            BodyFixture bf = new BodyFixture(c);
            floorleft.addFixture(bf);
        }
        floorleft.translate(new Vector2(-14.5, -40));
        floorleft.setMass(MassType.INFINITE);
        world.addBody(floorleft);

        //RIGHT
        Body floorright = new BodyRenderer();{

            Convex c = Geometry.createRectangle(1.0, 100.0);
            BodyFixture bf = new BodyFixture(c);
            floorright.addFixture(bf);
        }
        floorright.translate(new Vector2(14.5, -40));
        floorright.setMass(MassType.INFINITE);
        world.addBody(floorright);
    }

    public void addSomeInertBodies(){
        Body square1 = new BodyRenderer(Color.BLUE, BodyPartType.INERT);
        square1.addFixture(new BodyFixture(Geometry.createRectangle(0.5,0.5)));
        square1.translate(new Vector2(-2,-2));
        square1.setMass(MassType.INFINITE);
        world.addBody(square1);

    }


}
