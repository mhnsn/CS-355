package cs355.model.drawing;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TriangleTest {

	static Triangle T;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		T = new Triangle (Color.WHITE, new Point2D.Double(50,0), new Point2D.Double(0,50), new Point2D.Double(50,50));	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		
	}

	@Test
	public final void testLineFunctions()
	{
//		T = new Triangle (Color.WHITE, new Point2D.Double(50,0), new Point2D.Double(0,50), new Point2D.Double(50,50));
		
		assertTrue(T.lineAB(T.a) == T.lineAB(T.b));
		assertTrue(T.lineBC(T.b) == T.lineBC(T.c));
		assertTrue(T.lineCA(T.c) == T.lineCA(T.a));
		
		assertTrue(T.lineAB(T.c) != 0.0);
		assertTrue(T.lineBC(T.a) != 0.0);
		assertTrue(T.lineCA(T.b) != 0.0);
	}
}
