package test;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.controller.KeyboardInterface;
import cs355.model.GUIModel;
import cs355.model.drawing.Rectangle;

public class RectangleTest
{
	
	static Rectangle	R;
	static GUIModel		model;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		R = new Rectangle(Color.WHITE, new Point2D.Double(0, 0), 50, 100);
		new KeyboardInterface();
		model = new GUIModel();
		model.addShape(R);
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
	public final void testGetBoundingBox()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testGetHeight()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testGetUpperLeft()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testGetWidth()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testInBounds()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testObjectToWorld()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testPointInShape()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testRectangle()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testSetHeight()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testSetWidth()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testToString()
	{
		// fail("Not yet implemented"); // TODO
	}
	
}
