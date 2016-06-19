package view.menu;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.*;
import view.BodyRenderer;

import java.awt.*;
import java.util.ArrayList;

public class GameMenuButton {

    /** ATTRIBUTES */
    BodyRenderer text;
    BodyRenderer background;
    IAction action;

    /**
     * Constructor
     * @param text
     * @param x
     * @param y
     */
    public GameMenuButton (String text, double x, double y) {

        this.text = new BodyRenderer(new Color(0,0,0));
        this.background = new BodyRenderer(new Color(255,255,255));

        this.text.addFixture(new BodyFixture(new TextShape(x,y,50, text)));
        this.background.addFixture(new BodyFixture(Geometry.createRectangle(8,1.5)));
        this.background.translate(0,-y+2.30);
    }

    /**
     * Gets buttons
     * @return
     */
    public ArrayList<BodyRenderer> getBodies() {
        ArrayList<BodyRenderer> bodyRenderers = new ArrayList<>();

        bodyRenderers.add(this.background);
        bodyRenderers.add(this.text);

        return bodyRenderers;
    }

    /**
     * Set Action on click
     * @param action
     */
    public void setAction(IAction action) {

        this.action = action;
    }

    /**
     * Executes action
     */
    public void execute () {

        if(action != null)
            action.execute();
    }

    /**
     * Checks if button is touched based on x y mouse click
     * @param x
     * @param y
     * @return
     */
    public boolean isTouched(double x, double y) {

        double bodyWidth =((org.dyn4j.geometry.Rectangle) background.getFixtures().get(0).getShape()).getWidth();
        double bodyHeight =((org.dyn4j.geometry.Rectangle) background.getFixtures().get(0).getShape()).getHeight();
        double bodyPosX = background.getTransform().getTranslationX()+11;
        double bodyPosY = background.getTransform().getTranslationY()+11;
        double xPoint = x/45 +1;
        double yPoint = 12-y/45;

        return xPoint>bodyPosX && xPoint<bodyPosX+bodyWidth && yPoint>bodyPosY-bodyHeight && yPoint<bodyPosY;
    }

    public String toString(){return ((org.dyn4j.geometry.TextShape) text.getFixtures().get(0).getShape()).getText();}
}
