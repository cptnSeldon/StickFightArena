package view.background;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import view.BodyRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Background {

    /** ATTRIBUTES */
    private World world;

    private BodyRenderer background;
    private List<BodyRenderer> shapes;

    /**
     * Constructor
     * @param world
     */
    public Background (World world) {

        this.world = world;
        shapes = new ArrayList<>();
    }

    /**
     * Creates a background and add some shapes
     */
    public void createBackground () {

        background = new BodyRenderer(new Color(50,50,255,95));
        background.addFixture(new BodyFixture(Geometry.createRectangle(100,40)));

        addShapesToBG(background);
    }

    /**
     * Adds Shape to background
     * @param br : shape to add
     */
    private void addShapesToBG(BodyRenderer br) {

        br.setActive(false);
        shapes.add(br);
        world.addBody(br);
    }

}