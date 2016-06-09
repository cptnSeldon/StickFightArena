import java.awt.Color;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Rectangle;

/**
 * Created by auntfox on 30.05.2016.
 */
public class LifeBarPoints extends BodyRenderer {


	private BodyFixture bf;
	private int maxlife ;
	public LifeBarPoints(int totalLife, Color color){
		super(color);
		maxlife = totalLife;
		this.setActive(false);
		this.setLife(maxlife);
	}
	
	public void setLife(int life)
	{
		if(bf !=null)
			this.removeFixture(bf);
		
		if(getWidth(life) == 0)
			return;
		
		bf = new BodyFixture(Geometry.createRectangle(getWidth(life), 0.5));
		this.addFixture(bf);
		 
	}
	private double getWidth(int life){
		
		return (5/(double)maxlife)*life;
	}
	
}
