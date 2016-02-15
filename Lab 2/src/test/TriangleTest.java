package test;

import static org.junit.Assert.fail;

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
		T = new Triangle	(Color.WHITE, 	new Point2D.Double(0, 50),
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
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testPointInShape() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testInBounds() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetBoundingBox() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testTriangleColorDoubleDoubleDoubleDouble() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testLineAB() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testLineBC() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testLineCA() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testTriangleColorDoubleDoubleDouble() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetA() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetA() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetB() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetB() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetC() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetC() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testUpdatePoints() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetaV() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetbV() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetcV() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testObjectToWorld() 
	{
		fail("Not yet implemented"); // TODO
	}

}
