package view.background;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import view.BodyRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Background {

    /** ATTRIBUTES */
    private World world;



    BodyRenderer background;
    List<BodyRenderer> shapes;

    public Background (World world) {

        this.world = world;
        shapes = new ArrayList<>();
    }

    /**
     * Create a background and add some shapes
     */
    public void createBackground () {

        background = new BodyRenderer(new Color(50,50,255,95));
        background.addFixture(new BodyFixture(Geometry.createRectangle(100,40)));

        addShapesToBG(background);
    }

    /**
     * Create some Shapes
     * @param shape
     * @param color
     */

    public void createShapes (Body shape, Color color) {

        shape = new BodyRenderer(color);
        shape.addFixture(new BodyFixture(Geometry.createSegment(new Vector2(0,5))));
    }

    /**
     * Add Shape to background
     * @param br : shape to add
     */
    private void addShapesToBG(BodyRenderer br) {

        br.setActive(false);
        shapes.add(br);
        world.addBody(br);
    }

    public BodyRenderer getBackground() {
        return background;
    }
}