package test;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class ShapeTest
{
	
	static Square		S;
	static Rectangle	R;
	static Circle		C;
	static Ellipse		E;
	static Triangle		T;
	static Line			L;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		S = new Square(Color.WHITE, new Point2D.Double(200, 200), 100);
		R = new Rectangle(Color.WHITE, new Point2D.Double(200, 200), 100, 100);
		C = new Circle(Color.WHITE, new Point2D.Double(200, 200), 100);
		E = new Ellipse(Color.WHITE, new Point2D.Double(200, 200), 100, 100);
		T = new Triangle(Color.WHITE, new Point2D.Double(233.33333, 233.33333), new Point2D.Double(200, 200),
				new Point2D.Double(200, 300), new Point2D.Double(300, 200));
		L = new Line(Color.WHITE, new Point2D.Double(150, 150), new Point2D.Double(250, 250));
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}
	
	@Before
	public void setUp() throws Exception
	{
	}
	
	@After
	public void tearDown() throws Exception
	{
	}
	
	@Test
	public final void testPointInShapeSquare()
	{
		for (int x = 150; x < 250; x += 10)
		{
			for (int y = 150; y < 250; y += 10)
			{
				// assertEquals(true, S.pointInShape(new Point2D.Double(x, y),
				// 0));
			}
		}
	}
	
	@Test
	public final void testPointInShapeRectangle()
	{
		for (int x = 150; x < 250; x += 10)
		{
			for (int y = 150; y < 250; y += 10)
			{
				// assertEquals(true, R.pointInShape(new Point2D.Double(x, y),
				// 0));
			}
		}
	}
	
	@Test
	public final void testPointInShapeCircle()
	{
		for (int x = 150; x < 250; x += 10)
		{
			for (int y = 150; y < 250; y += 10)
			{
				if (((200 - x) * (200 - x) + (200 - y) * (200 - y)) <= 10000)
				{
					// assertEquals(true, C.pointInShape(new Point2D.Double(x,
					// y), 0));
				}
			}
		}
	}
	
	@Test
	public final void testPointInShapeEllipse()
	{
		for (int x = 150; x < 250; x += 10)
		{
			for (int y = 150; y < 250; y += 10)
			{
				if (((200 - x) * (200 - x) + (200 - y) * (200 - y)) <= 10000)
				{
					// assertEquals(true, E.pointInShape(new Point2D.Double(x,
					// y), 0));
				}
			}
		}
	}
	
	@Test
	public final void testPointInShapeTriangle()
	{
		for (int x = 150; x < 250; x += 10)
		{
			for (int y = 150; y < 250; y += 10)
			{
				if ((x * x) + (y * y) <= 62500 && x >= 200 && y >= 200)
				{
					// assertEquals(true, T.pointInShape(new Point2D.Double(x,
					// y), 0));
				}
			}
		}
		
		T = new Triangle(Color.WHITE, new Point2D.Double(362.66667, 233.66667), new Point2D.Double(378, 34),
				new Point2D.Double(232, 273), new Point2D.Double(478, 394));
		
		// assertEquals(true, T.pointInShape(new Point2D.Double(362.66667,
		// 233.66667), 0));
	}
	
	public final void testPointInShapeLine()
	{
		for (int x = 150; x < 250; x += 10)
		{
			for (int y = 150; y < 250; y += 10)
			{
				if (x == y)
				{
					// assertEquals(true, T.pointInShape(new Point2D.Double(x,
					// y), 0));
				}
			}
		}
	}
	
	@Test
	public final void testObjectToWorld()
	{
		assertEquals(S.getCenter(), S.objectToWorld(new Point2D.Double(0, 0)));
	}
	
	@Test
	public final void testWorldToObject()
	{
		assertEquals(new Point2D.Double(0, 0), S.worldToObject(S.getCenter()));
	}
	
	@Test
	public final void testTransformations()
	{
		assertEquals(S.getCenter(), S.worldToObject(S.objectToWorld(S.getCenter())));
	}
}
