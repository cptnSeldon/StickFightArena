package org.dyn4j.geometry;
import org.dyn4j.DataContainer;
import org.dyn4j.geometry.AABB;
import org.dyn4j.geometry.AbstractShape;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Feature;
import org.dyn4j.geometry.Interval;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.PointFeature;
import org.dyn4j.geometry.Shape;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Transformable;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.resources.Messages;


public class TextShape extends AbstractShape implements Convex, Shape, Transformable, DataContainer{
	private String text;
	private int textScale;

	protected TextShape(Vector2 center, double radius, String text) {
		this(center, radius);
		this.text=text;
	}
	
	protected TextShape(Vector2 center, double radius) {
		super(center, radius);
	}

	protected TextShape(double radius) {
		super(radius);
	}

	public TextShape(double x, double y, int scale ,String text) {
		this(new Vector2(x,y),5,text);
		this.textScale = scale;
	}
	public String getText() {return text;}

	public int getTextScale() {return textScale;}
	/* (non-Javadoc)
	 * @see org.dyn4j.geometry.Shape#getRadius(org.dyn4j.geometry.Vector2)
	 */
	@Override
	public double getRadius(Vector2 center) {
		return this.radius + center.distance(this.center);
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.geometry.Wound#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Circle[").append(super.toString())
		.append("]");
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.geometry.Shape#contains(org.dyn4j.geometry.Vector, org.dyn4j.geometry.Transform)
	 */
	@Override
	public boolean contains(Vector2 point, Transform transform) {
		// transform the center
		Vector2 v = transform.getTransformed(this.center);
		// get the transformed radius squared
		double radiusSquared = this.radius * this.radius;
		// create a vector from the center to the given point
		v.subtract(point);
		if (v.getMagnitudeSquared() <= radiusSquared) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.dyn4j.geometry.Shape#project(org.dyn4j.geometry.Vector2, org.dyn4j.geometry.Transform)
	 */
	@Override
	public Interval project(Vector2 vector, Transform transform) {
		// if the transform is not null then transform the center
		Vector2 center = transform.getTransformed(this.center);
		// project the center onto the given axis
		double c = center.dot(vector);
		// the interval is defined by the radius
		return new Interval(c - this.radius, c + this.radius);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * For a {@link Circle} this will always return a {@link PointFeature}.
	 */
	@Override
	public PointFeature getFarthestFeature(Vector2 vector, Transform transform) {
		// obtain the farthest point along the given vector
		Vector2 farthest = this.getFarthestPoint(vector, transform);
		// for a circle the farthest feature along a vector will always be a vertex
		return new PointFeature(farthest);
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.geometry.Convex#getFarthestPoint(org.dyn4j.geometry.Vector2, org.dyn4j.geometry.Transform)
	 */
	@Override
	public Vector2 getFarthestPoint(Vector2 vector, Transform transform) {
		// make sure the axis is normalized
		Vector2 nAxis = vector.getNormalized();
		// get the transformed center
		Vector2 center = transform.getTransformed(this.center);
		// add the radius along the vector to the center to get the farthest point
		center.x += this.radius * nAxis.x;
		center.y += this.radius * nAxis.y;
		// return the new point
		return center;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Circular shapes are handled specifically in the SAT algorithm since
	 * they have an infinite number of axes. As a result this method returns
	 * null.
	 * @return null
	 */
	@Override
	public Vector2[] getAxes(Vector2[] foci, Transform transform) {
		// a circle has infinite separating axes and zero voronoi regions
		// therefore we return null
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.geometry.Convex#getFoci(org.dyn4j.geometry.Transform)
	 */
	@Override
	public Vector2[] getFoci(Transform transform) {
		Vector2[] foci = new Vector2[1];
		// a circle only has one focus
		foci[0] = transform.getTransformed(this.center);
		return foci;
	}
	
	/**
	 * {@inheritDoc}
	 * <p style="white-space: pre;"> m = d * &pi; * r<sup>2</sup>
	 * I = m * r<sup>2</sup> / 2</p>
	 */
	@Override
	public Mass createMass(double density) {
		double r2 = this.radius * this.radius;
		// compute the mass
		double mass = density * Math.PI * r2;
		// compute the inertia tensor
		double inertia = mass * r2 * 0.5;
		// use the center supplied to the circle
		return new Mass(this.center, mass, inertia);
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.geometry.Shape#createAABB(org.dyn4j.geometry.Transform)
	 */
	@Override
	public AABB createAABB(Transform transform) {
		// if the transform is not null then transform the center
		Vector2 center = transform.getTransformed(this.center);
		// return a new aabb
		return new AABB(center, this.radius);
	}


}
