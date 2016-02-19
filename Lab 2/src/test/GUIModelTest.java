package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.GUIModel;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class GUIModelTest {

	static Circle 		C;
	static Line 		L;
	static Ellipse 		E;
	static Rectangle 	R;
	static Triangle 	T;
	static Square 		S;
	static GUIModel		model;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		C = new Circle		(Color.WHITE, 	new Point2D.Double(0, 0), 60);
		L = new Line		(Color.WHITE, 	new Point2D.Double(0, 0),
											new Point2D.Double(50, 100));
		E = new Ellipse		(Color.WHITE, 	new Point2D.Double(0, 0), 50, 100);
		R = new Rectangle	(Color.WHITE, 	new Point2D.Double(0, 0), 50, 100);
	
//		T = new Triangle	(Color.WHITE, 	new Point2D.Double(0, 50),
//											new Point2D.Double(-50, -25),
//											new Point2D.Double(50, -25));
		
		S = new Square		(Color.WHITE, 	new Point2D.Double(0, 0), 75);
		
		model = new GUIModel();
		
//		model.addShape(C);
		model.addShape(L);
//		model.addShape(E);
//		model.addShape(R);
//		model.addShape(T);
//		model.addShape(S);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		
	}
	
	@Before
	public void before()
	{
		model.addShape(L);
	}
	
	@After
	public void after()
	{
		model.setShapes(new ArrayList<Shape>());
	}

	@Test
	public final void testGetShape()
	{
//		assertEquals(model.getShape(0), S);
//		assertEquals(model.getShape(1), T);
//		assertEquals(model.getShape(2), R);
//		assertEquals(model.getShape(3), E);
		assertEquals(model.getShape(0), L);
//		assertEquals(model.getShape(5), C);
	}

	@Test
	public final void testGetClickedShape()
	{
//		assert(model.getClickedShape(new Point2D.Double(0, -50)) == C);
//		assertEquals(model.getClickedShape(new Point2D.Double(, )) == E);
//		assertEquals(model.getClickedShape(new Point2D.Double(, )) == R);
//		assertEquals(model.getClickedShape(new Point2D.Double(, )) == T);
//		assertEquals(model.getClickedShape(new Point2D.Double(, )) == S);
		
		Point2D.Double testPoint = new Point2D.Double(25, 50);
		assertEquals(L, model.getClickedShape(testPoint));
		assertEquals(L.getClass(), model.getClickedShape(testPoint).getClass());
	}
}
