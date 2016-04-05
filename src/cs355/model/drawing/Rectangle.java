package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import cs355.controller.StateMachine;

/**
 * Add your rectangle code here. You can add fields, but you cannot change the
 * ones that already exist. This includes the names!
 */
public class Rectangle extends Shape
{

	// The width of this shape.
	private double xAxis;

	// The height of this shape.
	private double yAxis;

	/**
	 * Basic constructor that sets all fields.
	 * 
	 * @param color
	 *            the color for the new shape.
	 * @param center
	 *            the center of the new shape.
	 * @param width
	 *            the width of the new shape.
	 * @param height
	 *            the height of the new shape.
	 */
	public Rectangle(Color color, Point2D.Double center, double width, double height)
	{
		// Initialize the superclass.
		super(color, center);

		// setBounds( width, height );

		// Set fields.
		this.xAxis = width;
		this.yAxis = height;
	}

	/**
	 * Getter for this shape's width.
	 * 
	 * @return this shape's width as a double.
	 */
	public double getWidth()
	{
		return xAxis;
	}

	/**
	 * Setter for this shape's width.
	 * 
	 * @param width
	 *            the new width.
	 */
	public void setWidth(double width)
	{
		this.xAxis = width;
	}

	/**
	 * Getter for this shape's height.
	 * 
	 * @return this shape's height as a double.
	 */
	public double getHeight()
	{
		return yAxis;
	}

	/**
	 * Setter for this shape's height.
	 * 
	 * @param height
	 *            the new height.
	 */
	public void setHeight(double height)
	{
		this.yAxis = height;
	}

	/**
	 * Add your code to do an intersection test here. You shouldn't need the
	 * tolerance.
	 * 
	 * @param pt
	 *            = the point to test against.
	 * @param tolerance
	 *            = the allowable tolerance.
	 * @return true if pt is in the shape, false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance)
	{
		// Point2D.Double objPt = worldToObject(pt);
		Point2D.Double objPt = pt;

		// if (!inBounds(objPt, tolerance))
		// {
		// return false;
		// }

		if (objPt.x <= this.xAxis / 2 && objPt.x >= (-this.xAxis / 2))
		{
			if (objPt.y <= this.yAxis / 2 && objPt.y >= (-this.yAxis / 2))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Getter for this Rectangle's upper left corner.
	 * 
	 * @return the upper left corner as a Java point.
	 */
	public Point2D.Double getUpperLeft()
	{
		Point2D.Double upperLeft = new Point2D.Double(this.center.getX() - (this.xAxis / 2),
				this.center.getY() - (this.yAxis / 2));

		AffineTransform t = StateMachine.rotate(rotation);
		return (Double) t.transform(upperLeft, null);
	}

	@Override
	public String toString()
	{
		String str = "Center: " + this.center.toString() + "\r\n" + "Top left: " + this.getUpperLeft().toString()
				+ "\r\n" + "Width: " + this.xAxis + "\r\n" + "Height: " + this.yAxis + "\r\n";

		return str;
	}
}
