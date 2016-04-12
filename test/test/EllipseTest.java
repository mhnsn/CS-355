package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.GUIModel;
import cs355.model.drawing.Ellipse;

public class EllipseTest
{
	
	static Ellipse	E;
	static GUIModel	model;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		E = new Ellipse(Color.WHITE, new Point2D.Double(0, 0), 50, 100);
		model = new GUIModel();
		model.addShape(E);
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
	public final void testPointInShape()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testGetBoundingBox()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testEllipse()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testGetWidth()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testSetWidth()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testGetHeight()
	{
		// fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testSetHeight()
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
	
}
