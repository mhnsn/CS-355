package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

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
	public Triangle(Color color,
					Point2D.Double center,
					Point2D.Double a,
					Point2D.Double b,
					Point2D.Double c)
	{

		// Initialize the superclass.
		super(color, center);
		
		// Set fields.
		this.a = a;
		this.b = b;
		this.c = c;
		
		setBounds();
		calculateEquations();
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
	}
	
	double lineAB(Point2D.Double pt)
	{
		return Aab*pt.x + Bab*pt.y + Cab;
	}
	
	double lineBC(Point2D.Double pt)
	{
		return Abc*pt.x + Bbc*pt.y + Cbc;
	}
	
	double lineCA(Point2D.Double pt)
	{
		return Aca*pt.x + Bca*pt.y + Cca;
	}
	
	/**
	 * Alternate constructor that sets fields based on locations of corners.
	 * @param color the color for the new shape.
	 * @param p1 the first point,  relative to the center.
	 * @param p2 the second point, relative to the center.
	 * @param p3 the third point,  relative to the center.
	 */
	public Triangle(Color currentColor, Double p1, Double p2, Double p3)
	{
		super(currentColor, p1);
		
		a = p1;
		b = p2;
		c = p3;
		
		calculateCenter();
		setBounds();
		calculateEquations();
	}

	private void calculateCenter()
	{
		Point2D.Double center 	= new Point2D.Double();
		
		Point2D.Double midpoint = new Point2D.Double((a.x + b.x)/2, (a.y + b.y)/2);
		center.setLocation(c.x + (2/3)*(midpoint.x - c.x), c.y + (2/3)*(midpoint.y - c.y));
		this.center = center;
		
		aV = new Point2D.Double(a.x-center.x, a.y-center.y);
		bV = new Point2D.Double(b.x-center.x, b.y-center.y);
		cV = new Point2D.Double(c.x-center.x, c.y-center.y);
		
		return;
	}

	/**
	 * Getter for the first point.
	 * @return the first point as a Java point.
	 */
	public Point2D.Double getA()
	{
		return a;
	}

	/**
	 * Setter for the first point.
	 * @param a the new first point.
	 */
	public void setA(Point2D.Double a)
	{
		this.a = a;
	}

	/**
	 * Getter for the second point.
	 * @return the second point as a Java point.
	 */
	public Point2D.Double getB()
	{
		return b;
	}

	/**
	 * Setter for the second point.
	 * @param b the new second point.
	 */
	public void setB(Point2D.Double b)
	{
		this.b = b;
	}

	/**
	 * Getter for the third point.
	 * @return the third point as a Java point.
	 */
	public Point2D.Double getC()
	{
		return c;
	}

	/**
	 * Setter for the third point.
	 * @param c the new third point.
	 */
	public void setC(Point2D.Double c)
	{
		this.c = c;
	}

	/**
	 * Add your code to do an intersection test
	 * here. You shouldn't need the tolerance.
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
		
		if(lineAB(center) * (lineAB(pt) * lineBC(pt)* lineCA(pt)) >= 0 )
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

	/**
	 * @return
	 */
	private Rectangle setBounds()
	{
		double minX = a.x;
		double minY = a.y;
		double maxX = a.x;
		double maxY = a.y;
		
		// find minX
		if(minX > b.x)
		{
			minX = b.x;
		}
		if(minX > c.x)
		{
			minX = c.x;
		}
		// find minY		
		if(minY > b.y)
		{
			minY = b.y;
		}
		if(minY > c.y)
		{
			minY = c.y;
		}
		// find maxX
		if(maxX < b.x)
		{
			maxX = b.x;
		}
		if(maxX < c.x)
		{
			maxX = c.x;
		}
		// find maxY
		if(maxY < b.y)
		{
			maxY = b.y;
		}
		if(maxY < c.y)
		{
			maxY = c.y;
		}
		
		boundWidth 	= (maxX-minX);
		boundHeight = (maxY-minY);
		
		Point2D.Double tl = new Point2D.Double((maxX+minX)/2, (maxY+minY)/2);
				
		boundingBox = new Rectangle(Color.WHITE,tl,boundWidth,boundHeight);
				
		return boundingBox;
	}
	
	@Override
	public boolean inBounds(Point2D.Double pt, double tolerance)
	{
		if(boundingBox == null)
		{
			setBounds();
			calculateEquations();
		}
		return boundingBox.inBounds(pt, 0);
	}
	
	@Override
	public void setCenter(Point2D.Double pt)
	{				
		center.setLocation(pt.x, pt.y);
		updatePoints();
	}
	
	public void updatePoints()
	{
		if(aV == null || bV == null || cV == null)
		{
			aV = new Point2D.Double(a.x-center.x, a.y-center.y);
			bV = new Point2D.Double(b.x-center.x, b.y-center.y);
			cV = new Point2D.Double(c.x-center.x, c.y-center.y);
		}
		
		a = new Point2D.Double(center.x + aV.x, center.y + aV.y);
		b = new Point2D.Double(center.x + bV.x, center.y + bV.y);
		c = new Point2D.Double(center.x + cV.x, center.y + cV.y);
		setBounds();
		calculateEquations();
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
}
