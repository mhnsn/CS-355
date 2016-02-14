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
	private Point2D.Double endV;

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
		
		setBounds( Math.abs(end.x-start.x), Math.abs(end.y-start.y));

		// Set the field.
		endV = new Point2D.Double(end.x-start.x, end.y-start.y);
		updateEnd();
	}

	private void updateEnd()
	{
		if(endV == null)
		{
			endV = new Point2D.Double(end.x-center.x, end.y-center.y);
		}

		end = new Point2D.Double(center.x + endV.x, center.y + endV.y);
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

	@Override
	public void setCenter(Point2D.Double pt)
	{			
		center.setLocation(pt.x, pt.y);
		updateEnd();
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
		if(!inBounds(pt, tolerance))
		{
			return false;
		}
		
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
		double xDist = x2 - x1; // x2-x1
		
		double numerator 	= Math.abs((yDist * x0) - (xDist * y0) + (x2*y1) - (y2*x1));
		double denominator 	= Math.sqrt( ((yDist)*(yDist)) + ((xDist)*(xDist)) );
		
		if( (numerator / denominator) < tolerance)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean inBounds(Point2D.Double point, double tolerance)
	{
		if(center.x - tolerance	> point.x
		|| end.x 	+ tolerance	< point.x
		|| center.y - tolerance > point.y
		|| end.y 	+ tolerance	< point.y)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public Rectangle getBoundingBox()
	{
		Point2D.Double tl = new Point2D.Double();
		if(center.x < end.x)
		{
			if(center.y < end.y)
			{
				tl.setLocation(center.x, center.y);
			}
			else
			{
				tl.setLocation(center.x, end.y);
			}
		}
		else
		{
			if(center.y < end.y)
			{
				tl.setLocation(end.x, center.y);
			}
			else
			{
				tl.setLocation(end.x, end.y);
			}			
		}
		
		// in all cases, tl is the top-left of the bounding box.
		// add half of height and width to box, then pass in
		tl.setLocation(tl.x + (boundWidth), tl.y + (boundHeight) );
		
		return new Rectangle(Color.WHITE,tl,boundWidth*2,boundHeight*2);
	}
	
	
}
