import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
//import org.dyn4j.examples.Graphics2DRenderer;
import org.dyn4j.geometry.Convex;

/**
 * Custom Body class to add drawing functionality.
 * @author William Bittle
 * @version 3.2.1
 * @since 3.0.0
 */
public class SimulationBody extends Body {
    /** The color of the object */
    protected Color color;
    protected Color initColor;

    private long lastTouch;

    public long getLastTouch() {
        return lastTouch;
    }

    public void setLastTouch(long lastTouch) {
        this.lastTouch = lastTouch;
    }

    public void resetColor(){


        if((System.currentTimeMillis()-lastTouch)>750){
            this.setColor(initColor);
        }
    }
    /**
     * Default constructor.
     */
    public SimulationBody() {
        // black
        this.color = new Color(0,0,0);
    }

    /**
     * Constructor.
     * @param color a set color
     */
    public SimulationBody(Color color) {
        this.initColor = color;
        this.color = color;
    }

    /**
     * Draws the body.
     * <p>
     * Only coded for polygons and circles.
     * @param g the graphics object to render to
     * @param scale the scaling factor
     */
    public void render(Graphics2D g, double scale) {
        this.render(g, scale, this.color);
    }

    /**
     * Draws the body.
     * <p>
     * Only coded for polygons and circles.
     * @param g the graphics object to render to
     * @param scale the scaling factor
     * @param color the color to render the body
     */
    public void render(Graphics2D g, double scale, Color color) {
        // point radius
        final int pr = 4;

        // save the original transform
        AffineTransform ot = g.getTransform();

        // transform the coordinate system from world coordinates to local coordinates
        AffineTransform lt = new AffineTransform();
        lt.translate(this.transform.getTranslationX() * scale, this.transform.getTranslationY() * scale);
        lt.rotate(this.transform.getRotation());

        // apply the transform
        g.transform(lt);

        // loop over all the body fixtures for this body
        for (BodyFixture fixture : this.fixtures) {
            this.renderFixture(g, scale, fixture, color);
        }


        // set the original transform
        g.setTransform(ot);
    }

    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return this.color;
    }

    /**
     * Renders the given fixture.
     * @param g the graphics object to render to
     * @param scale the scaling factor
     * @param fixture the fixture to render
     * @param color the color to render the fixture
     */
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        // get the shape on the fixture
        Convex convex = fixture.getShape();

        // brighten the color if asleep
        if (this.isAsleep()) {
            color = color.brighter();
        }

        // render the fixture
        Graphics2DRenderer.render(g, convex, scale, color);
    }


}