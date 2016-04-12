package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.solution.CS355;

public class ProjectTests
{
	protected static int serverPort = 60652;
	
	public static void main(String[] args)
	{
		
		String[] testClasses = new String[] { "test.CircleTest", "test.EllipseTest", "test.LineTest", "test.SquareTest",
				"test.RectangleTest", "test.TriangleTest", "test.ShapeTest", "test.GUIModelTest",
				"test.Transform3DTest", "test.LineVector3DTest", };
		
		setup();
		org.junit.runner.JUnitCore.main(testClasses);
		teardown();
	}
	
	@BeforeClass
	public static void setup()
	{
	}
	
	@AfterClass
	public static void teardown()
	{
	}
	
	@Test
	public void test_1()
	{
		assertEquals("OK", "OK");
		assertTrue(true);
		assertFalse(false);
	}
}