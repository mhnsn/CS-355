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
import cs355.model.drawing.Circle;

public class CircleTest {

	static Circle 		C;
	static GUIModel		model;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		C = new Circle		(Color.WHITE, 	new Point2D.Double(30, 30), 60);
		model = new GUIModel();
		model.addShape(C);
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
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetRadius() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetRadius() 
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
	public final void testObjectToWorld() 
	{
		fail("Not yet implemented"); // TODO
	}
}
