package test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class MVCTests {

	@BeforeClass
	public static void setup()
	{
	}
	
	@AfterClass
	public static void teardown()
	{
	}
	
	public MVCTests()
	{
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
			String[] testClasses = new String[]
			{					
					"test.GUIModelTest",
					"test.LineTest",
					"test.TriangleTest",
					"test.CircleTest",
					"test.EllipseTest",
					"test.SquareTest",
					"test.RectangleTest"
			};
			
			setup();
			org.junit.runner.JUnitCore.main(testClasses);
			teardown();

	}

}
