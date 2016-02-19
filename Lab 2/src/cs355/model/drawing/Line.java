package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Add your line code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Line extends Shape {

	// The ending point of the line.
	Point2D.Double end;	// world-coordinate end
	private Point2D.Double endV;// object-coordinate vector of line

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

		// Set the vector for the end
		setEndV(new Point2D.Double(end.x-start.x, end.y-start.y));
		updateEnd();
	}

	private void updateEnd()
	{
		if(getEndV() == null)
		{
			setEndV(new Point2D.Double(end.x-center.x, end.y-center.y));
		}

		end = new Point2D.Double(center.x + getEndV().x, center.y + getEndV().y);
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
	 * @param end the new ending point for the Line in world coordinates
	 */
	public void setEnd(Point2D.Double end)
	{
		this.setEndV(end);
		updateEnd();
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
	 * @param objPt = the point to test against.
	 * @param tolerance = the allowable tolerance.
	 * @return true if pt is in the shape,
	 *		   false otherwise.
	 */
	@Override
	public boolean pointInShape(Point2D.Double pt, double tolerance)
	{
		Point2D.Double objPt = worldToObject(pt);

		if(!inBounds(objPt, tolerance))
		{
			return false;
		}
		
		// per wikipedia: formula for distance between a point and a line, using three points
		//								| (y2-y1)x0 - (x2-x1)y0 + x2y1-y2x1	|
		// distance (p1,p2, (x0,y0)) =   ---------------------------------
		//								   ( (y2-y1)^2 + (x2-x1)^2 )^(1/2)

		double x0, x1, x2;
		double y0, y1, y2;
		
		x0 = objPt.x;
		x1 = 0;
		x2 = this.getEndV().x;
		
		y0 = objPt.y;
		y1 = 0;
		y2 = this.getEndV().y;
		
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
		updateEnd();
		
		int quadrant = 0;
		if(getEndV().x > 0)
		{
			if(getEndV().y > 0)
			{
				quadrant = 1;
			}
			else
			{
				quadrant = 4;
			}
		}
		else
		{
			if(getEndV().y > 0)
			{
				quadrant = 2;
			}
			else
			{
				quadrant = 3;
			}
		}
		
		switch(quadrant)
		{
			case 1:
				if(0 - tolerance	> point.x				// left bound check
				|| (2*boundWidth) 	+ tolerance	< point.x
				|| 0 - tolerance > point.y					// bottom bound check
				|| (2*boundHeight) 	+ tolerance	< point.y)
				{
					return false;
				}
				break;
			case 2:
				if(0 + tolerance	< point.x				// right bound
				|| -(2*boundWidth) 	- tolerance	> point.x
				|| 0 - tolerance > point.y					// bottom bound
				|| (2*boundHeight) 	+ tolerance	< point.y)
				{
					return false;
				}
				break;
			case 3:
				if(0 + tolerance	< point.x
				|| -(2*boundWidth) 	- tolerance	> point.x
				|| 0 + tolerance < point.y
				|| -(2*boundHeight) - tolerance	> point.y)
				{
					return false;
				}
				break;
			case 4:
				if(0 - tolerance	> point.x				// left bound check
				|| (2*boundWidth) 	+ tolerance	< point.x				
				|| 0 + tolerance < point.y
				|| -(2*boundHeight) - tolerance	> point.y)
				{
					return false;
				}
				break;
			default:
				break;
		}		
		return true;
	}
	
	@Override
	public Rectangle getBoundingBox()
	{
		Point2D.Double rc = new Point2D.Double();
		rc.setLocation( (center.x) - getEndV().x / 2, (center.y) - getEndV().y / 2 );
		
		return new Rectangle(Color.WHITE,center,boundWidth*2,boundHeight*2);
	}
	
	@Override
	public String toString()
	{
		String centerData = "Center: (" + center.x + ", " + center.y + ")\r\n";
		String vectorData = "Vector: (" + getEndV().x + ", " + getEndV().y + ")\r\n";

		return centerData + vectorData;
	}

	public Point2D.Double getEndV()
	{
		return endV;
	}

	public void setEndV(Point2D.Double endV)
	{
		this.endV = endV;
	}
}
