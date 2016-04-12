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
	
	@BeforeClass
	public static void setup()
	{
		// System.out.println("><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><\r\n\t\tSERVER
		// TEST");
		// DataImporter.main(new String[]
		// {"C:\\Users\\Mark\\Downloads\\record-indexer-data\\Records\\Records.xml"});
		// s = new Server(serverPort);
		// s.run();
	}
	
	@AfterClass
	public static void teardown()
	{
		// Server.iceNine();
	}
	
	@Test
	public void test_1()
	{
		assertEquals("OK", "OK");
		assertTrue(true);
		assertFalse(false);
	}
	
	public static void main(String[] args)
	{
		
		String[] testClasses = new String[] {
				// "server.ServerUnitTests",
				"test.CircleTest", "test.EllipseTest", "test.LineTest", "test.SquareTest", "test.RectangleTest",
				"test.TriangleTest", "test.ShapeTest", "test.GUIModelTest", "test.Transform3DTest", };
		
		setup();
		org.junit.runner.JUnitCore.main(testClasses);
		teardown();
	}
}