package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * This is the base class for all of your shapes.
 * Make sure they all extend this class.
 */
public abstract class Shape {

	// The color of this shape.
	protected Color color;

	// The center of this shape.
	protected Point2D.Double center;

	// The rotation of this shape.
	protected double rotation;

	/**
	 * Basic constructor that sets fields.
	 * It initializes rotation to 0.
	 * @param color the color for the new shape.
	 * @param center the center point of the new shape.
	 */
	public Shape(Color color, Point2D.Double center)
	{
		this.color = color;
		this.center = center;
		rotation = 0.0;
	}

	/**
	 * Getter for this shape's color.
	 * @return the color of this shape.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Setter for this shape's color
	 * @param color the new color for the shape.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Getter for this shape's center.
	 * @return this shape's center as a Java point.
	 */
	public Point2D.Double getCenter()
	{
		return center;
	}

	/**
	 * Setter for this shape's center.
	 * @param center the new center as a Java point.
	 */
	public void setCenter(Point2D.Double center)
	{
		this.center = center;
	}

	/**
	 * Getter for this shape's rotation.
	 * @return the rotation as a double.
	 */
	public double getRotation()
	{
		return rotation;
	}

	/**
	 * Setter for this shape's rotation.
	 * @param rotation the new rotation.
	 */
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
	}

	/**
	 * Used to test for whether the user clicked inside a shape or not.
	 * @param pt = the point to test whether it's in the shape or not.
	 * @param tolerance = the tolerance for testing. Mostly used for lines.
	 * @return true if pt is in the shape, false otherwise.
	 */
	public abstract boolean pointInShape(Point2D.Double pt, double tolerance);
	
	public double crossProduct(Point2D.Double p1, Point2D.Double p2)
	{
		double product;
		//	[i,		j, 		k]
		//	[p1.x, 	p1.y, 	0]
		//	[p2.x,	p2.y,	0]
		//
		//	k(p1.x*p2.y - p1.y*p2.x)
		
		product = (p1.x*p2.y - p1.y*p2.x);
		
		return product;
	}
	
	public double dotProduct(double cp1, double cp2)
	{
		double product = 0;
		
		product = cp1 * cp2;
		
		return product;
	}
	
	public boolean sameSideAsCenter(Point2D.Double check, Point2D.Double a, Point2D.Double b)
	{
		Point2D.Double bToA 		= new Point2D.Double(b.x 		- a.x, b.y 		- a.y);
		Point2D.Double checkToA 	= new Point2D.Double(check.x 	- a.x, check.y 	- a.y);
		Point2D.Double centerToA 	= new Point2D.Double(center.x	- a.x, center.y - a.y);
		
		double cp1 = crossProduct(bToA, checkToA);
		double cp2 = crossProduct(bToA, centerToA);
		
		if(dotProduct(cp1, cp2) >= 0)
		{
			return true;
		}
		
		return false;
	}
}
