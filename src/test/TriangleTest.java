package test;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.drawing.GUIModel;
import cs355.model.drawing.Triangle;

public class TriangleTest {
	
	static Triangle 	T;
	static GUIModel		model;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		T = new Triangle	(Color.WHITE,	new Point2D.Double(0, 0),
											new Point2D.Double(0, 50),
											new Point2D.Double(-50, -25),
											new Point2D.Double(50, -25));
		model = new GUIModel();
		model.addShape(T);
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
	public final void testSetCenter() 
	{
	}

	@Test
	public final void testPointInShape() 
	{
	}

	@Test
	public final void testInBounds() 
	{
	}

	@Test
	public final void testGetBoundingBox() 
	{
	}

	@Test
	public final void testLineAB() 
	{
	}

	@Test
	public final void testLineBC() 
	{
	}

	@Test
	public final void testLineCA() 
	{
	}

	@Test
	public final void testGetA() 
	{
	}

	@Test
	public final void testSetA() 
	{
	}

	@Test
	public final void testGetB() 
	{
	}

	@Test
	public final void testSetB() 
	{
	}

	@Test
	public final void testGetC() 
	{
	}

	@Test
	public final void testSetC() 
	{
	}

	@Test
	public final void testObjectToWorld() 
	{
	}
}
