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
import cs355.model.drawing.Square;

public class SquareTest {
	
	static Square 		S;
	static GUIModel		model;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		S = new Square		(Color.WHITE, 	new Point2D.Double(0, 0), 75);
		model = new GUIModel();		
		model.addShape(S);

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
	public final void testSquare() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetSize() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetSize() 
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetUpperLeft() 
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
