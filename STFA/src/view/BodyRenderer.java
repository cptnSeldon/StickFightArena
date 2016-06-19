package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
//import org.dyn4j.examples.View.Graphics2DRenderer;
import org.dyn4j.geometry.Convex;
import view.foreground.BodyPartType;

/**
 * Inspired by Dyn4J example
 */
public class BodyRenderer extends Body {

    /** The color of the object */
    protected Color color;
    protected Color initColor;



    /** Type of the body **/
    protected BodyPartType type;

    /**
     * Default constructor.
     */
    public BodyRenderer() {
        // black
        this.color = new Color(0,0,0);
        this.type = BodyPartType.NONE;
    }

    /**
     * Constructor.
     * @param color a set color
     */
    public BodyRenderer(Color color) {
        this.initColor = color;
        this.color = color;
        this.type = BodyPartType.NONE;
    }

    /**
     * Constructor.
     * @param color a set color
     * @param type a BodyPartType
     */
    public BodyRenderer(Color color, BodyPartType type) {
        this.initColor = color;
        this.color = color;
        this.type = type;
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

    public BodyPartType getType() {
        return type;
    }

    public void setType(BodyPartType type) {
        this.type = type;
    }
}