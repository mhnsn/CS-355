package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Add your triangle code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Triangle extends Shape {

	// The three points of the triangle.
	Point2D.Double a;
	Point2D.Double b;
	Point2D.Double c;
	private Rectangle boundingBox;
	private double Aab;
	private double Bab;
	private double Cab;
	private double Abc;
	private double Bbc;
	private double Cbc;
	private double Aca;
	private double Bca;
	private double Cca;
	double minX;
	double minY;
	double maxX;
	double maxY;
	
	Point2D.Double aV;
	Point2D.Double bV;
	Point2D.Double cV;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param a the first point, relative to the center.
	 * @param b the second point, relative to the center.
	 * @param c the third point, relative to the center.
	 */
	public Triangle(Color color, Point2D.Double center, Point2D.Double a, Point2D.Double b, Point2D.Double c)
	{
		// Initialize the superclass.
		super(color, center);
		
		// Set fields.
		this.a = a;
		this.b = b;
		this.c = c;
		
		a = new Point2D.Double(a.x - center.x, a.y - center.y);
		b = new Point2D.Double(b.x - center.x, b.y - center.y);
		c = new Point2D.Double(c.x - center.x, c.y - center.y);
		
		cleanup();
	}
	/**
	 * 
	 */
	private void cleanup()
	{
		setBounds();
		calculateEquations();
	}
	/**
	 * Alternate constructor that sets fields based on locations of corners.
	 * @param color the color for the new shape.
	 * @param p1 the first point,  relative to the center.
	 * @param p2 the second point, relative to the center.
	 * @param p3 the third point,  relative to the center.
	 */

	/**
	 * @return
	 */
	private Rectangle setBounds()
	{
		minX = a.x;
		minY = a.y;
		maxX = a.x;
		maxY = a.y;
		
		// find minX
		if(minX > b.x) { minX = b.x; }
		if(minX > c.x) { minX = c.x; }
		// find minY		
		if(minY > b.y) { minY = b.y; }
		if(minY > c.y) { minY = c.y; }
		// find maxX
		if(maxX < b.x) { maxX = b.x; }
		if(maxX < c.x) { maxX = c.x; }
		// find maxY
		if(maxY < b.y) { maxY = b.y; }
		if(maxY < c.y) { maxY = c.y; }
		
		boundWidth 	= (maxX-minX);
		boundHeight = (maxY-minY);
		
		Point2D.Double center = new Point2D.Double((maxX+minX)/2, (maxY+minY)/2);

		boundingBox = new Rectangle(Color.WHITE,center,boundWidth,boundHeight);
				
		return boundingBox;
	}
	
	private void calculateEquations()
	{
		Aab = -(b.y-a.y);
		Bab = b.x-a.x;
		Cab = -(Aab*a.x + Bab*a.y);
		
		Abc = -(c.y-b.y);
		Bbc = c.x-b.x;
		Cbc = -(Abc*b.x + Bbc*b.y);

		Aca = -(a.y-c.y);
		Bca = a.x-c.x;		
		Cca = -(Aca*c.x + Bca*c.y);
		
		a = new Point2D.Double(a.x - center.x, a.y - center.y);
		b = new Point2D.Double(b.x - center.x, b.y - center.y);
		c = new Point2D.Double(c.x - center.x, c.y - center.y);		
	}
	
	public static Point2D.Double calculateCenter(Point2D.Double a, Point2D.Double b, Point2D.Double c)
	{
		Point2D.Double center 	= new Point2D.Double();
		
		Point2D.Double midpoint = new Point2D.Double((a.x + b.x)/2, (a.y + b.y)/2);
		center.setLocation(c.x + (2/3)*(midpoint.x - c.x), c.y + (2/3)*(midpoint.y - c.y));

		a = new Point2D.Double(a.x-center.x, a.y-center.y);
		b = new Point2D.Double(b.x-center.x, b.y-center.y);
		c = new Point2D.Double(c.x-center.x, c.y-center.y);
		
		return center;
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

		if(!inBounds(objPt, tolerance))
		{
			return false;
		}
		
		if( lineAB(new Point2D.Double(0,0)) * lineAB(objPt) * lineBC(objPt) * lineCA(objPt)  >= 0 )	// math hack!
		{
			return true;
		}

		return false;
	}
	
	@Override
	public Rectangle getBoundingBox()
	{		
		return setBounds();
	}

	@Override
	public boolean inBounds(Point2D.Double pt, double tolerance)
	{
		cleanup();
		if(minX < pt.x && pt.x < maxX )
		{
			if(minY < pt.y && pt.y < maxY )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the aV
	 */
	public Point2D.Double getaV() { return aV; }
	/**
	 * @return the bV
	 */
	public Point2D.Double getbV() { return bV; }
	/**
	 * @return the cV
	 */
	public Point2D.Double getcV() { return cV; }	
	/**
	 * Getter for the first point.
	 * @return the first point as a Java point.
	 */

	public double lineAB(Point2D.Double pt)
	{
		return Aab*pt.x + Bab*pt.y + Cab;
	}
	public double lineBC(Point2D.Double pt)
	{
		return Abc*pt.x + Bbc*pt.y + Cbc;
	}
	public double lineCA(Point2D.Double pt)
	{
		return Aca*pt.x + Bca*pt.y + Cca;
	}
}
