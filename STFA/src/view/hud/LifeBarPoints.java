package view.hud;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import view.BodyRenderer;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;

public class LifeBarPoints extends BodyRenderer implements Observer {

	/**
	 * ATTRIBUTES
	 */
	private BodyFixture bf;
	private int maxlife ;

	/**
	 * Constructor
	 * @param totalLife
	 * @param color
     */
	public LifeBarPoints(int totalLife, Color color){
		super(color);
		maxlife = totalLife;
		this.setActive(false);
		this.setLife(maxlife);
	}

	/**
	 * Sets life in life bar
	 * @param life
     */
	public void setLife(int life) {

		if(bf !=null)
			this.removeFixture(bf);
		
		if(getWidth(life) == 0)
			return;
		
		bf = new BodyFixture(Geometry.createRectangle(getWidth(life), 0.5));
		this.addFixture(bf);
	}

	/**
	 * Gets life bar width
	 * @param life
	 * @return
     */
	private double getWidth(int life) {
		
		return (5/(double)maxlife)*life;
	}

	/**
	 * Updates life regarding the player's life
	 * @param player
	 * @param life
     */
	@Override
	public void update(Observable player, Object life) {

		this.setLife((int)life);
	}
}
