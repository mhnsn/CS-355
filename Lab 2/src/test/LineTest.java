package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.drawing.GUIModel;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;

public class LineTest {

	static Line L;
	static double centerX;
	static double centerY;
	static double endX;
	static double endY;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		System.out.println("\r\n----------------------------------------------------------------------------");
		System.out.println("\t\t\tLine Test");

		centerX = Math.random() * 100;
		centerY = Math.random() * 100;
		endX = Math.random() * 100;
		endY = Math.random() * 100;
		
		System.out.println("Center: (" + centerX + ", " + centerY + ")");
		System.out.println("End:    (" + endX + ", " + endY + ")");
		System.out.println("endV:    (" + (endX-centerX) + ", " + (endY-centerY) + ")");

		
		L = new Line(Color.WHITE, new Point2D.Double(centerX, centerY), new Point2D.Double(endX, endY));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Test
	public final void testPointInShape()
	{
		Point2D.Double objectCoord 		= new Point2D.Double();
		objectCoord = GUIModel.worldToObject(new Point2D.Double((centerX+endX)/2, (centerY+endY)/2), L);
		
		assertTrue(L.pointInShape(objectCoord, 4.0));
	}

	@Test
	public final void testInBounds()
	{
	}

	@Test
	public final void testGetBoundingBox()
	{
		Rectangle R = L.getBoundingBox();
		
		assertTrue(R.getWidth() - L.getEndV().x < 0.0001);
		assertTrue(R.getHeight()- L.getEndV().y < 0.0001);
		assertEquals(new Point2D.Double(L.getEndV().x/2, L.getEndV().y/2), R.getCenter());
		assertTrue(R.getUpperLeft().x - 0	< 0.0001);
		assertTrue(R.getUpperLeft().y - 0	< 0.0001);
	}
}
