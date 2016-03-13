package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Add your ellipse code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Ellipse extends Shape {

	// The width of this shape.
	private double width;

	// The height of this shape.
	private double height;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param width the width of the new shape.
	 * @param height the height of the new shape.
	 */
	public Ellipse(Color color, Point2D.Double center, double width, double height)
	{
		// Initialize the superclass.
		super(color, center);
		
		setBounds(width, height);

		// Set fields.
		this.width = width;
		this.height = height;
	}

	/**
	 * Getter for this shape's width.
	 * @return this shape's width as a double.
	 */
	public double getWidth()
	{
		return width;
	}

	/**
	 * Setter for this shape's width.
	 * @param width the new width.
	 */
	public void setWidth(double width)
	{
		this.width = width;
	}

	/**
	 * Getter for this shape's height.
	 * @return this shape's height as a double.
	 */
	public double getHeight()
	{
		return height;
	}

	/**
	 * Setter for this shape's height.
	 * @param height the new height.
	 */
	public void setHeight(double height)
	{
		this.height = height;
	}

	/**
	 * Add your code to do an intersection test
	 * here. You shouldn't need the tolerance.
	 * @param objPt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance)
	{
		Point2D.Double objPt = worldToObject(pt);

		if(!getBoundingBox().inBounds(objPt, tolerance))
		{
			return false;
		}
		
		double xNum = objPt.x;
		double yNum = objPt.y;
		
		xNum *= xNum;
		yNum *= yNum;
		
		double major = xNum / (this.width * this.width);
		double minor = yNum / (this.height * this.height);
		
		if( (major + minor) <= 1)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public Rectangle getBoundingBox()
	{
		return new Rectangle(Color.WHITE, center, width*2, height*2);
	}
	
	@Override
	public Point2D.Double getHandleCenter()
	{
		return new Point2D.Double(-(this.boundWidth)*2, -(this.boundHeight)*2);
	}
}