/**
 *
 */
package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.LineVector3D;
import cs355.model.Transform3D;
import cs355.model.scene.Point3D;
import cs355.view.GUIViewRefresher;

/**
 * @author Mark
 *
 */
public class Transform3DTest
{
	private static final double[][] I = new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 },
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
	
	Point3D			origin			= new Point3D(0, 0, 0);

	Point3D			pX				= new Point3D(1, 0, 0);
	Point3D			pY				= new Point3D(0, 1, 0);
	Point3D			pZ				= new Point3D(0, 0, 1);

	LineVector3D	lvX				= new LineVector3D(1, 0, 0, origin, false);

	LineVector3D	lvY				= new LineVector3D(0, 1, 0, origin, false);

	LineVector3D	lvZ				= new LineVector3D(0, 0, 1, origin, false);

	private double	screenTolerance	= 1;

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
	 * Test method for {@link cs355.model.Transform3D#clearPipeline()}.
	 */
	@Test
	public final void testClearPipeline()
	{
		Transform3D.clearPipeline();
		assertEquals(0, Transform3D.pipeline.size());

		Transform3D.translate(lvX, pX);
		assertEquals(1, Transform3D.pipeline.size());

		Transform3D.translate(lvY, pY);
		assertEquals(2, Transform3D.pipeline.size());

		Transform3D.translate(lvZ, pZ);
		assertEquals(3, Transform3D.pipeline.size());

		Transform3D.clearPipeline();
		assertEquals(0, Transform3D.pipeline.size());
	}

	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#clip(java.util.ArrayList, double[][])}.
	 */
	@Test
	public final void testClip()
	{
		double fovX = 89.23, fovY = 89.23;
		double f = 2, n = 1;

		double[][] clipMatrix = Transform3D.generateClipMatrix(fovX, fovY, n, f);

		ArrayList<LineVector3D> al = new ArrayList<LineVector3D>();
		ArrayList<LineVector3D> alClipped;

		al.add(lvX);
		al.add(lvY);
		al.add(lvZ);

		alClipped = Transform3D.clip(al, clipMatrix);

		assertEquals(1, alClipped.size());

		Transform3D.translate(lvX, pY);
		Transform3D.translate(lvX, pZ);

		alClipped.add(lvX);
		
		alClipped = Transform3D.clip(alClipped, clipMatrix);
		assertEquals(2, alClipped.size());
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

		double[][] scale = { { s }, { s }, { s }, { s } };
		d = Transform3D.fullLeftMultiply(I, scale);

		assertEquals(s, d[0][0], .01);
		assertEquals(s, d[1][0], .01);
		assertEquals(s, d[2][0], .01);
		assertEquals(s, d[3][0], .01);
		
		double[][] testReverse = { { 0, 0, 0, 1 }, { 0, 0, 1, 0 }, { 0, 1, 0, 0 }, { 1, 0, 0, 0 } };
		double[][] sampleVectors = { { 1, 1, 1, 1 }, { 2, 2, 2, 2 }, { 3, 3, 3, 3 }, { 4, 4, 4, 4 } };
		
		double[][] rVal = Transform3D.fullLeftMultiply(testReverse, sampleVectors);
		
		assertEquals(rVal[0][0], 4, .01);
		assertEquals(rVal[1][0], 3, .01);
		assertEquals(rVal[2][0], 2, .01);
		assertEquals(rVal[3][0], 1, .01);
	}

	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#generateClipMatrix(double, double, double, double)}
	 * .
	 */
	@Test
	public final void testGenerateClipMatrix()
	{
		double fovX = 89.23, fovY = 89.23;
		double f = 2, n = 1;

		double[][] clipMatrix = Transform3D.generateClipMatrix(fovX, fovY, n, f);

		assertEquals(1, clipMatrix[0][0], .01);
		assertEquals(0, clipMatrix[0][1], .01);
		assertEquals(0, clipMatrix[0][2], .01);
		assertEquals(0, clipMatrix[0][3], .01);

		assertEquals(0, clipMatrix[1][0], .01);
		assertEquals(1, clipMatrix[1][1], .01);
		assertEquals(0, clipMatrix[1][2], .01);
		assertEquals(0, clipMatrix[1][3], .01);

		assertEquals(0, clipMatrix[2][0], .01);
		assertEquals(0, clipMatrix[2][1], .01);
		assertEquals(3, clipMatrix[2][2], .01);
		assertEquals(1, clipMatrix[2][3], .01);

		assertEquals(0, clipMatrix[3][0], .01);
		assertEquals(0, clipMatrix[3][1], .01);
		assertEquals(4, clipMatrix[3][2], .01);
		assertEquals(0, clipMatrix[3][3], .01);
	}

	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#mapToDrawingSpace(cs355.model.LineVector3D, int)}
	 * .
	 */
	@Test
	public final void testMapToDrawingSpace()
	{
		Transform3D.mapToDrawingSpace(lvX, GUIViewRefresher.dimensions);
		Transform3D.mapToDrawingSpace(lvY, GUIViewRefresher.dimensions);
		Transform3D.mapToDrawingSpace(lvZ, GUIViewRefresher.dimensions);
		
		assertEquals(GUIViewRefresher.dimensions, lvX.x, screenTolerance);
		assertEquals(0, lvX.y, screenTolerance);
		
		assertEquals(0, lvY.x, screenTolerance);
		assertEquals(GUIViewRefresher.dimensions, lvY.y, screenTolerance);

		assertEquals(0, lvZ.x, screenTolerance);
		assertEquals(0, lvZ.y, screenTolerance);

	}

	/**
	 * Test method for {@link cs355.model.Transform3D#popTransform(int)}.
	 */
	@Test
	public void testPopTransform()
	{
	}

	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#rotate(cs355.model.LineVector3D, cs355.model.scene.Point3D, double, double, double, boolean)}
	 * .
	 */
	@Test
	public void testRotate()
	{
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
	 * Test method for {@link cs355.model.Transform3D#setPush(boolean)}.
	 */
	@Test
	public void testSetPush()
	{
	}

	/**
	 * Test method for {@link cs355.model.Transform3D#setWorldToCamera()}.
	 */
	@Test
	public final void testSetWorldToCamera()
	{
		Transform3D.translate(lvX, pX);
		Transform3D.translate(lvY, pY);

		Transform3D.setWorldToCamera();

		assertEquals(2, Transform3D.worldToCamera.size());
		assertFalse(Transform3D.getPush());
	}

	/**
	 * Test method for
	 * {@link cs355.model.Transform3D#transform(cs355.model.LineVector3D)}.
	 */
	@Test
	public final void testTransform()
	{
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
	 * Test method for {@link cs355.model.Transform3D#unSetWorldToCamera()}.
	 */
	@Test
	public final void testUnSetWorldToCamera()
	{
		Transform3D.translate(lvX, pX);
		Transform3D.translate(lvY, pY);

		Transform3D.setWorldToCamera();
		Transform3D.unSetWorldToCamera();
		assertTrue(Transform3D.getPush());
	}

}
