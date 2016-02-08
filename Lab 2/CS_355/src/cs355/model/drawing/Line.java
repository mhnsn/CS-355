package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Add your line code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Line extends Shape {

	// The ending point of the line.
	private Point2D.Double end;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param start the starting point.
	 * @param end the ending point.
	 */
	public Line(Color color, Point2D.Double start, Point2D.Double end)
	{

		// Initialize the superclass.
		super(color, start);

		// Set the field.
		this.end = end;
	}

	/**
	 * Getter for this Line's ending point.
	 * @return the ending point as a Java point.
	 */
	public Point2D.Double getEnd()
	{
		return end;
	}

	/**
	 * Setter for this Line's ending point.
	 * @param end the new ending point for the Line.
	 */
	public void setEnd(Point2D.Double end)
	{
		this.end = end;
	}

	/**
	 * Add your code to do an intersection test
	 * here. You <i>will</i> need the tolerance.
	 * @param pt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance)
	{
		// per wikipedia: formula for distance between a point and a line, using three points
		//								| (y2-y1)x0 - (x2-x1)y0 + x2y1-y2x1	|
		// distance (p1,p2, (x0,y0)) =   ---------------------------------
		//								   ( (y2-y1)^2 + (x2-x1)^2 )^(1/2)

		double x0, x1, x2;
		double y0, y1, y2;
		
		x0 = pt.x;
		x1 = this.center.x;
		x2 = this.end.x;
		
		y0 = pt.y;
		y1 = this.center.y;
		y2 = this.end.y;
		
		double yDist = y2 - y1; // y2-y1
		double xDist = x2 - x2; // x2-x1
		
		double numerator 	= Math.abs((yDist * x0) - (xDist * y0) + (x2*y1) - (y2*x1));
		double denominator 	= Math.sqrt( ((yDist)*(yDist)) + ((xDist)*(xDist)) );
		
		if( (numerator / denominator) < 4)
		{
			return true;
		}
		return false;
	}
}
