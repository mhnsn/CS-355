package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
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

	public Triangle(Color color, Point2D.Double center, Point2D.Double a, Point2D.Double b, Point2D.Double c)
	{
		// Initialize the superclass.
		super(color, center);
		
		this.center = center = calculateCenter(a,b,c);
		
		// Set fields.
		a = new Point2D.Double(a.x - center.x, a.y - center.y);
		b = new Point2D.Double(b.x - center.x, b.y - center.y);
		c = new Point2D.Double(c.x - center.x, c.y - center.y);
		
		this.a = a;
		this.b = b;
		this.c = c;
				
		cleanup();
	}

	private Point2D.Double calculateCenter(Point2D.Double a, Point2D.Double b, Point2D.Double c)
	{
		Point2D.Double center 	= new Point2D.Double();
		
		center.setLocation((a.x + b.x + c.x)/3 , (a.y + b.y + c.y)/3 );

//		a = new Point2D.Double(a.x-center.x, a.y-center.y);
//		b = new Point2D.Double(b.x-center.x, b.y-center.y);
//		c = new Point2D.Double(c.x-center.x, c.y-center.y);
		
		return center;
	}
	
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
	
	public Point2D.Double getA() { return a; }

	public Point2D.Double getB() { return b; }

	public Point2D.Double getC() { return c; }	


	private double lineAB(Point2D.Double pt)
	{
		return Aab*pt.x + Bab*pt.y + Cab;
	}
	
	private double lineBC(Point2D.Double pt)
	{
		return Abc*pt.x + Bbc*pt.y + Cbc;
	}
	
	private double lineCA(Point2D.Double pt)
	{
		return Aca*pt.x + Bca*pt.y + Cca;
	}
	
	public void setCenter(Point2D.Double pt)
	{
		this.center = pt;
	}
	
	public void cleanup()
	{
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
		
//		a = new Point2D.Double(a.x - center.x, a.y - center.y);
//		b = new Point2D.Double(b.x - center.x, b.y - center.y);
//		c = new Point2D.Double(c.x - center.x, c.y - center.y);		
	}
	
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
		
		Point2D.Double pt = new Point2D.Double(maxX-(boundWidth/2), maxY-(boundHeight/2));
	
		boundingBox = new Rectangle(Color.WHITE,pt,boundWidth,boundHeight);
				
		return boundingBox;
	}

	@Override
	public AffineTransform getBoundingBoxTransform()
	{
		AffineTransform bt = getObjectToWorld();
		bt.translate(boundingBox.center.x, boundingBox.center.y );
		
		return bt;
	}

	@Override
	public Point2D.Double getHandleCenter()
	{
		return new Point2D.Double(-(this.boundWidth)/2, -(this.boundHeight)/2);
	}
}