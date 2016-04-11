/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.LineVector3D;
import cs355.model.Transform3D;
import cs355.model.scene.Point3D;

/**
 * @author Mark
 *
 */
public class Transform3DTest
{
	Point3D							origin	= new Point3D(0, 0, 0);
	Point3D							pX		= new Point3D(1, 0, 0);
	Point3D							pY		= new Point3D(0, 1, 0);
	Point3D							pZ		= new Point3D(0, 0, 1);
	
	LineVector3D					lvX		= new LineVector3D(1, 0, 0, origin, false);
	LineVector3D					lvY		= new LineVector3D(0, 1, 0, origin, false);
	LineVector3D					lvZ		= new LineVector3D(0, 0, 1, origin, false);
	
	private static final double[][]	I		= new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 },
			{ 0, 0, 0, 1 } };
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		Transform3D.clearPipeline();
	}
	
	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#rotate(cs355.model.LineVector3D, cs355.model.scene.Point3D, double, double, double, boolean)}
	 * .
	 */
	@Test
	public void testRotate()
	{
		System.out.println(lvX.toString());
		
		double[] x = Transform3D.rotate(lvX, origin, 0, 90, 0, false);
		
		assertEquals(0, x[0], .01);
		assertEquals(0, x[1], .01);
		assertEquals(1, x[2], .01);
		
		double[] y = Transform3D.rotate(lvY, origin, 0, 0, 90, false);
		
		assertEquals(1, y[0], .01);
		assertEquals(0, y[1], .01);
		assertEquals(0, y[2], .01);
		
		double[] z = Transform3D.rotate(lvZ, origin, 90, 0, 0, false);
		
		assertEquals(0, z[0], .01);
		assertEquals(0, z[1], .01);
		assertEquals(-1, z[2], .01);
	}
	
	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#translate(cs355.model.LineVector3D, cs355.model.scene.Point3D)}
	 * .
	 */
	@Test
	public void testTranslate()
	{
		double[] tX = Transform3D.translate(lvX, pX);
		double[] tY = Transform3D.translate(lvY, pY);
		double[] tZ = Transform3D.translate(lvZ, pZ);
		
		assertEquals(2, tX[0], .01);
		assertEquals(2, tY[1], .01);
		assertEquals(2, tZ[2], .01);
	}
	
	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#scale(cs355.model.LineVector3D, double)}.
	 */
	@Test
	public void testScale()
	{
		double[] tX = Transform3D.scale(lvX, 5);
		double[] tY = Transform3D.scale(lvY, 5);
		double[] tZ = Transform3D.scale(lvZ, 5);
		
		assertEquals(5, tX[0], .01);
		assertEquals(0, tX[1], .01);
		assertEquals(0, tX[2], .01);
		assertEquals(5, tX[3], .01);
		
		assertEquals(0, tY[0], .01);
		assertEquals(5, tY[1], .01);
		assertEquals(0, tY[2], .01);
		assertEquals(5, tY[3], .01);
		
		assertEquals(0, tZ[0], .01);
		assertEquals(0, tZ[1], .01);
		assertEquals(5, tZ[2], .01);
		assertEquals(5, tZ[3], .01);
		
		// negative scaling
		tX = Transform3D.scale(lvX, -5);
		tY = Transform3D.scale(lvY, -5);
		tZ = Transform3D.scale(lvZ, -5);
		
		assertEquals(-5, tX[0], .01);
		assertEquals(0, tX[1], .01);
		assertEquals(0, tX[2], .01);
		assertEquals(-5, tX[3], .01);
		
		assertEquals(0, tY[0], .01);
		assertEquals(-5, tY[1], .01);
		assertEquals(0, tY[2], .01);
		assertEquals(-5, tY[3], .01);
		
		assertEquals(0, tZ[0], .01);
		assertEquals(0, tZ[1], .01);
		assertEquals(-5, tZ[2], .01);
		assertEquals(-5, tZ[3], .01);
		
		// zero scaling
		tX = Transform3D.scale(lvX, 0);
		tY = Transform3D.scale(lvY, 0);
		tZ = Transform3D.scale(lvZ, 0);
		
		assertEquals(0, tX[0], .01);
		assertEquals(0, tX[1], .01);
		assertEquals(0, tX[2], .01);
		assertEquals(0, tX[3], .01);
		
		assertEquals(0, tY[0], .01);
		assertEquals(0, tY[1], .01);
		assertEquals(0, tY[2], .01);
		assertEquals(0, tY[3], .01);
		
		assertEquals(0, tZ[0], .01);
		assertEquals(0, tZ[1], .01);
		assertEquals(0, tZ[2], .01);
		assertEquals(0, tZ[3], .01);
	}
	
	/**
	 * Test method for {@link cs355.model.Transform3D#popTransform(int)}.
	 */
	@Test
	public void testPopTransform()
	{
	}
	
	/**
	 * Test method for {@link cs355.model.Transform3D#setPush(boolean)}.
	 */
	@Test
	public void testSetPush()
	{
	}
	
	/**
	 * Test method for {@link cs355.model.Transform3D#fullLeftMultiply}.
	 */
	@Test
	public void testFullLeftMultiply()
	{
		// identity
		double[][] d = Transform3D.fullLeftMultiply(I, I);
		
		assertEquals(1, d[0][0], .01);
		assertEquals(0, d[0][1], .01);
		assertEquals(0, d[0][2], .01);
		assertEquals(0, d[0][3], .01);
		
		assertEquals(0, d[1][0], .01);
		assertEquals(1, d[1][1], .01);
		assertEquals(0, d[1][2], .01);
		assertEquals(0, d[1][3], .01);
		
		assertEquals(0, d[2][0], .01);
		assertEquals(0, d[2][1], .01);
		assertEquals(1, d[2][2], .01);
		assertEquals(0, d[2][3], .01);
		
		assertEquals(0, d[3][0], .01);
		assertEquals(0, d[3][1], .01);
		assertEquals(0, d[3][2], .01);
		assertEquals(1, d[3][3], .01);
		
		double s = 5;
		
		double[][] scale = { { s, s, s, s } };
		d = Transform3D.fullLeftMultiply(I, scale);
		
		assertEquals(s, d[0][0], .01);
		assertEquals(s, d[0][1], .01);
		assertEquals(s, d[0][2], .01);
		assertEquals(s, d[0][3], .01);
		
	}
}
