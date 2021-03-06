package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Add your square code here. You can add fields, but you cannot change the ones
 * that already exist. This includes the names!
 */
public class Square extends Rectangle
{

	// The size of this Square.
	private double size;

	/**
	 * Basic constructor that sets all fields.
	 * 
	 * @param color
	 *            the color for the new shape.
	 * @param center
	 *            the center of the new shape.
	 * @param size
	 *            the size of the new shape.
	 */
	public Square(Color color, Point2D.Double center, double size)
	{
		// Initialize the superclass.
		super(color, center, size, size);
		// setBounds( size, size );

		// Set the field.
		this.size = size;
	}

	/**
	 * Getter for this Square's size.
	 * 
	 * @return the size as a double.
	 */
	public double getSize()
	{
		return size;
	}

	/**
	 * Setter for this Square's size.
	 * 
	 * @param size
	 *            the new size.
	 */
	public void setSize(double size)
	{
		this.size = size;
	}

	// /**
	// * Add your code to do an intersection test here. You shouldn't need the
	// * tolerance.
	// *
	// * @param objPt
	// * = the point to test against.
	// * @param tolerance
	// * = the allowable tolerance.
	// * @return true if pt is in the shape, false otherwise.
	// */
	// @Override
	// public boolean pointInShape(Point2D.Double pt, double tolerance)
	// {
	// Point2D.Double objPt = worldToObject(pt);
	//
	// if (!inBounds(objPt, tolerance))
	// {
	// return false;
	// }
	//
	// if (objPt.x <= ((this.size / 2)) && objPt.x >= ((-this.size / 2)))
	// {
	// if (objPt.y <= ((this.size / 2)) && objPt.y >= ((-this.size / 2)))
	// {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// /**
	// * Getter for this Square's upper left corner.
	// *
	// * @return the upper left corner as a Java point.
	// */
	// public Point2D.Double getUpperLeft()
	// {
	// Point2D.Double upperLeft = new Point2D.Double(this.center.getX() -
	// (this.size / 2),
	// this.center.getY() - (this.size / 2));
	// return upperLeft;
	// }
}
