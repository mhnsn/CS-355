/**
 *
 */
package cs355.model;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;

/**
 * @author Mark
 *
 */
public class Transform3D
{
	private static boolean				push			= false;
	public static ArrayList<double[][]>	worldToCamera	= new ArrayList<double[][]>();
	public static ArrayList<double[][]>	pipeline		= new ArrayList<double[][]>();

	public static final double[][]		IDENTITY		= new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 },
			{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
	public static final LineVector3D	IDENTITY_V		= new LineVector3D(
			new Line3D(new Point3D(0, 0, 0), new Point3D(1, 1, 1)));

	//////////////////////////////////////////////////////////////////////
	// Transformation functions
	//

	/**
	 *
	 * @param xAxis
	 * @param yAxis
	 * @param zAxis
	 * @return TODO
	 */
	private static double[][] addRotateToPipeline(double xAxis, double yAxis, double zAxis)
	{
		if (outOfToleranceRange(yAxis, 0, .01))
		{
			
			double[][] rY = new double[][] { { Math.cos(yAxis), 0, Math.sin(yAxis), 0 }, { 0, 1, 0, 0 },
					{ -Math.sin(yAxis), 0, Math.cos(yAxis), 0 }, { 0, 0, 0, 1 } };

			pipeline.add(rY);
			
			return rY;
		}
		return null;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	private static double[][] addTranslateToPipeline(Point3D p)
	{
		if (outOfToleranceRange(p.x, 0, .01) || outOfToleranceRange(p.y, 0, .01) || outOfToleranceRange(p.z, 0, .01))
		{
			double[][] t = new double[][] { { 1, 0, 0, p.x }, { 0, 1, 0, p.y }, { 0, 0, 1, p.z }, { 0, 0, 0, 1 } };
			pipeline.add(t);

			return t;
		}
		return null;
	}

	/**
	 *
	 * @param p
	 */
	private static void addUntranslateToPipeline(Point3D p)
	{
		double[][] tInv = new double[][] { { 1, 0, 0, -p.x }, { 0, 1, 0, -p.y }, { 0, 0, 1, -p.z }, { 0, 0, 0, 1 } };

		pipeline.add(tInv);
	}

	/**
	 *
	 * @param d
	 * @param v
	 * @return
	 */
	private static boolean canMultiply(double[][] d, double[] v)
	{
		if (d[0].length != v.length)
		{
			return false;
		}

		return true;
	}

	//////////////////////////////////////////////////////////////////////
	// Sundry helper functions for this class

	/**
	 *
	 */
	public static void clearPipeline()
	{
		pipeline.clear();
	}

	/**
	 *
	 * @param h
	 * @param frustum
	 * @return
	 */
	public static ArrayList<LineVector3D> clip(ArrayList<LineVector3D> h, double[][] frustum)
	{
		/*******************************************************************
		 * Apply this clip matrix to the 3D homogeneous camera-space point to
		 * get 3D homogeneous points in clip space.
		 *******************************************************************
		 * Apply the clipping tests described in class and in your textbook.
		 * Reject a line if both points fail the same view frustum test OR if
		 * either endpoint fails the near-plane test. For this lab, we'll let
		 * Java's 2D line-drawing handing any other clipping.
		 *******************************************************************
		 * n.b. The clip tests and the result of clip application are all
		 * handled within Transform3D.clip(), storing the values in the returned
		 * array
		 ******************************************************************/

		double[] cameraSpaceV, cameraSpaceP, clipSpaceV, clipSpaceP;
		ArrayList<LineVector3D> clippedLines = new ArrayList<LineVector3D>();
		int i, j;

		for (LineVector3D v : h)
		{
			cameraSpaceV = v.getVArray();
			cameraSpaceP = v.getPArray();

			clipSpaceV = Transform3D.leftMultiply(frustum, cameraSpaceV);
			clipSpaceP = Transform3D.leftMultiply(frustum, cameraSpaceP);

			i = passesClipTests(clipSpaceV);
			j = passesClipTests(clipSpaceP);

			// TODO: make sure that operator precedence holds as expected
			if (i == j && i != 0 || j == -3 || i == -3)
			{
				continue;
			}
			clippedLines.add(v);
		}
		
		// if (clippedLines.size() > 0)
		// {
		// System.out.print("");
		// }

		return clippedLines;
	}

	/**
	 *
	 * @param d
	 * @return
	 */
	private static double degreesToRadians(float d)
	{
		return d * Math.PI / 180;
	}

	private static double dotProduct(double[] a, double[] b)
	{
		return a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3];
	}

	/**
	 *
	 * @param pipeline2
	 * @return
	 */
	private static ArrayList<double[][]> fullCopy(ArrayList<double[][]> pipeline2)
	{
		ArrayList<double[][]> copy = new ArrayList<double[][]>();
		double[][] t, tc;
		double[] v, vc;

		for (int i = 0; i < pipeline.size(); i++)
		{
			t = pipeline.get(i);
			tc = new double[t.length][t[0].length];

			for (int j = 0; j < t.length; j++)
			{
				v = t[j];
				vc = new double[v.length];
				System.arraycopy(v, 0, vc, 0, v.length);

				tc[j] = vc;
			}

			copy.add(tc);
		}

		return copy;
	}

	/**
	 * Must pass two 4x4 matrices.
	 *
	 * @param L
	 * @param A
	 * @return
	 */
	public static double[][] fullLeftMultiply(double[][] L, double[][] A)
	{
		// int j = A.length;
		
		// double x1, y1, z1, w1;
		// double x2, y2, z2, w2;
		// double x3, y3, z3, w3;
		// double x4, y4, z4, w4;
		
		// x1 = L[0][0] * A[0][0] + L[0][1] * A[1][0] + L[0][2] * A[2][0] +
		// L[0][3] * A[3][0];
		// y1 = L[0][0] * A[0][0] + L[0][1] * A[1][0] + L[0][2] * A[2][0] +
		// L[0][3] * A[3][0];
		// z1 = L[0][0] * A[0][0] + L[0][1] * A[1][0] + L[0][2] * A[2][0] +
		// L[0][3] * A[3][0];
		// w1 = L[0][0] * A[0][0] + L[0][1] * A[1][0] + L[0][2] * A[2][0] +
		// L[0][3] * A[3][0];
		//
		// x2 = L[1][0] * A[0][1] + L[1][1] * A[1][1] + L[0][2] * A[2][1] +
		// L[0][3] * A[3][1];
		// y2 = L[1][0] * A[0][1] + L[1][1] * A[1][1] + L[1][2] * A[2][1] +
		// L[1][3] * A[3][1];
		// z2 = L[1][0] * A[0][1] + L[1][1] * A[1][1] + L[2][2] * A[2][1] +
		// L[2][3] * A[3][1];
		// w2 = L[1][0] * A[0][1] + L[1][1] * A[1][1] + L[3][2] * A[2][1] +
		// L[3][3] * A[3][1];
		//
		// x3 =
		// y3 =
		// z3 =
		// w3 =
		//
		// x4 =
		// y4 =
		// z4 =
		// w4 =

		double[][] aT = transpose(A);
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				leftMultiply(L, aT[i]);
			}
		}

		for (int i = 0; i < 4; i++)
		{
			System.arraycopy(aT[i], 0, A[i], 0, 4);
		}
		
		return A;
	}

	/**
	 *
	 * @param fovX
	 * @param fovY
	 * @param n
	 * @param f
	 * @return
	 */
	public static double[][] generateClipMatrix(double fovX, double fovY, double n, double f)
	{
		// TODO: verify I didn't just break the clip matrix with this change.
		double[][] clipMatrix = {

				{ Math.atan(degreesToRadians((float) fovX)), 0, 0, 0 },
				{ 0, Math.atan(degreesToRadians((float) fovY)), 0, 0 }, { 0, 0, (f + n) / (f - n), 1 },
				{ 0, 0, -2 * (n * f / (f - n)), 0 } };

		return clipMatrix;
	}

	/**
	 *
	 * @return
	 */
	public static boolean getPush()
	{
		return push;
	}

	/**
	 *
	 * @param d
	 * @param v
	 * @return
	 */
	public static double[] leftMultiply(double[][] d, double[] v)
	{
		if (!canMultiply(d, v))
		{
			System.out.println("Error: incompatible matrix multiplication of " + d[0].length + " and " + v.length);
			return new double[] {};
		}

		double a, b, c, p = Double.MAX_VALUE;

		if (d.length == 4)
		{
			a = d[0][0] * v[0] + d[0][1] * v[1] + d[0][2] * v[2] + d[0][3] * v[3];
			b = d[1][0] * v[0] + d[1][1] * v[1] + d[1][2] * v[2] + d[1][3] * v[3];
			c = d[2][0] * v[0] + d[2][1] * v[1] + d[2][2] * v[2] + d[2][3] * v[3];

			if (v.length == 4)
			{
				p = d[3][0] * v[0] + d[3][1] * v[1] + d[3][2] * v[2] + d[3][3] * v[3];
				// v[3] = p;
			}
		}
		else
		{
			a = d[0][0] * v[0];
			b = d[0][1] * v[1];
			c = d[0][2] * v[2];

			if (v.length == 4)
			{
				p = d[0][3] * v[3];
				// v[3] = p;
			}

		}

		// v[0] = a;
		// v[1] = b;
		// v[2] = c;

		if (p != Double.MAX_VALUE)
		{
			return new double[] { a, b, c, p };
		}
		else
		{
			return new double[] { a, b, c };
		}
	}
	
	/**
	 * it is assumed that this line has been normalized.
	 *
	 * @param vt
	 * @param dimensions
	 * @param clipSpaceV
	 * @param clipSpaceP
	 */
	public static void mapToDrawingSpace(Line3D vt, int dimensions, double clipSpaceP, double clipSpaceV)
	{
		
		vt.start.x = vt.start.x * dimensions / (2 * clipSpaceP) + 1024;
		vt.start.y = -(vt.start.y * dimensions / (2 * clipSpaceP)) + 1024;

		vt.end.x = vt.end.x * dimensions / (2 * clipSpaceV) + 1024;
		vt.end.y = -(vt.end.y * dimensions) / (2 * clipSpaceV) + 1024;
	}

	/**
	 * Scales a normalized clip-space vector based on given screen dimension.
	 * This assumes viewport width and height to be equal. A separate method to
	 * handle varying screen sizes would not be too difficult. (TODO: implement
	 * this)
	 *
	 * @param vt
	 * @param dimensions
	 */
	public static void mapToDrawingSpace(LineVector3D vt, int dimensions)
	{
		// TODO: this needs to update origin and end values as well
		vt.x *= dimensions;
		vt.y *= dimensions;
		// ignore w and z outright = since z is depth, we really don't care
		// about it anyway at this point.
	}

	/**
	 * Check if this double falls in the given bounds. Give an expected (mean)
	 * value, a tolerance (can be either positive or negative) band, and the
	 * value to check.
	 *
	 * @param check
	 * @param expected
	 * @param tolerance
	 * @return
	 */
	private static boolean outOfToleranceRange(double check, double expected, double tolerance)
	{
		double a = expected - tolerance;
		double b = expected + tolerance;

		double lowBound = Math.min(a, b);
		double hiBound = Math.max(a, b);

		if (lowBound > check || check > hiBound)
		{
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param clipSpaceV
	 * @return
	 */
	public static int passesClipTests(double[] clipSpaceV)
	{
		// System.out.println("======================");
		double w = clipSpaceV[3];
		// System.out.println("w = " + w);

		if (outOfToleranceRange(clipSpaceV[0], 0, w)) // test left and right
		{
			return clipSpaceV[0] > 0 ? 1 : -1;
		}
		else if (outOfToleranceRange(clipSpaceV[1], 0, w)) // top and bottom
		{
			return clipSpaceV[1] > 0 ? 2 : -2;
		}
		else if (outOfToleranceRange(clipSpaceV[2], 0, w)) // front and back
		{
			// System.out.println("Failed front-back test. (clipSpaceV[2] = " +
			// clipSpaceV[2] + ").");
			return clipSpaceV[0] > 0 ? 3 : -3;
		}

		return 0;
	}

	/**
	 *
	 * @param i
	 * @return
	 */
	public static int popTransform(int i)
	{
		for (int n = 0; n < i; n++)
		{
			pipeline.remove(pipeline.size() - 1);
		}

		return pipeline.size();
	}

	// /**
	// * Rotate the passed LineVector. This will overwrite the current values
	// * stored there.
	// *
	// * @param v
	// * @param rotationOrigin
	// * @param xAxis
	// * @param yAxis
	// * @param zAxis
	// * @param valuesPassedInRadians
	// * @return
	// * @deprecated Use
	// * {@link #rotate(LineVector3D,double,double,double,boolean)}
	// * instead
	// */
	// @Deprecated
	// public static double[] rotate(LineVector3D v, Point3D rotationOrigin,
	// double xAxis, double yAxis, double zAxis,
	// boolean valuesPassedInRadians)
	// {
	// return rotate(v, xAxis, yAxis, zAxis, valuesPassedInRadians);
	// }

	/**
	 * Rotate the passed LineVector. This will overwrite the current values
	 * stored there.
	 *
	 * @param v
	 * @param xAxis
	 * @param yAxis
	 * @param zAxis
	 * @param valuesPassedInRadians
	 * @return
	 */
	public static double[][] rotate(LineVector3D v, double xAxis, double yAxis, double zAxis,
			boolean valuesPassedInRadians)
	{
		if (!valuesPassedInRadians)
		{
			xAxis = degreesToRadians((float) xAxis);
			yAxis = degreesToRadians((float) yAxis);
			zAxis = degreesToRadians((float) zAxis);
		}
		
		// double[] r = transform(v);

		return addRotateToPipeline(xAxis, yAxis, zAxis);
	}

	/**
	 *
	 * @param v
	 * @param c
	 * @return
	 */
	public static void scale(Line3D v, double c)
	{
		double[][] scaleV = { { c, c, c, c } };
		double[] scaleValsP = { v.start.x, v.start.y, v.start.z, 1 };
		double[] scaleValsV = { v.end.x, v.end.y, v.end.z, 1 };

		leftMultiply(scaleV, scaleValsP);
		leftMultiply(scaleV, scaleValsV);

		v.start.x = scaleValsP[0] / scaleValsP[3];
		v.start.y = scaleValsP[1] / scaleValsP[3];
		v.start.z = scaleValsP[2] / scaleValsP[3];
		
		v.end.x = scaleValsV[0] / scaleValsV[3];
		v.end.y = scaleValsV[1] / scaleValsV[3];
		v.end.z = scaleValsV[2] / scaleValsV[3];

		return;
	}

	/**
	 *
	 */
	private static void setIdentityTransform()
	{
		pipeline.clear();
		pipeline.add(IDENTITY);
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public static int setPush(boolean p)
	{
		push = p;
		return pipeline.size();
	}

	/**
	 * Set the worldToCamera transform to be a copy of the current pipeline.
	 * Until unsetWorldToCamera() is called, all further transforms added to the
	 * pipeline will (should) be popped immediately after execution.
	 */
	public static void setWorldToCamera()
	{
		worldToCamera = fullCopy(pipeline);

		push = false;
	}
	
	public static void transform(Line3D v)
	{
		double[] rArray = new double[] { v.end.x, v.end.y, v.end.z, 1 };
		double[] qArray = new double[] { v.start.x, v.start.y, v.start.z, 1 };
		
		// this should no longer have in-place modification issues.
		for (double[][] d : pipeline)
		{
			rArray = leftMultiply(d, rArray);
			// System.arraycopy(rArray, 0, x, 0, x.length);
			
			qArray = leftMultiply(d, qArray);
			// System.arraycopy(qArray, 0, p, 0, p.length);
		}
		
		v.start = new Point3D(qArray[0] / qArray[3], qArray[1] / qArray[3], qArray[2] / qArray[3]);
		v.end = new Point3D(rArray[0] / rArray[3], rArray[1] / rArray[3], rArray[2] / rArray[3]);

	}

	/**
	 * Execute all transformations in the pipeline on this vector and give back
	 * the new values in an array. This will modify the passed argument v
	 *
	 * @param v
	 *            The vector to transform. It will be modified and original
	 *            values overwritten.
	 * @return
	 */
	public static double[] transform(LineVector3D v)
	{
		// TODO: verify that this is properly storing and updating
		// transformations in the passed objects.

		if (pipeline.size() == 0)
		{
			setIdentityTransform();
		}

		double[] x = v.getVArray();
		double[] p = v.getPArray();
		
		double[] rArray = new double[x.length];
		double[] qArray = new double[x.length];

		// this should no longer have in-place modification issues.
		for (double[][] d : pipeline)
		{
			rArray = leftMultiply(d, x);
			System.arraycopy(rArray, 0, x, 0, x.length);
			
			qArray = leftMultiply(d, p);
			System.arraycopy(qArray, 0, p, 0, p.length);
		}

		v.x = rArray[0];
		v.y = rArray[1];
		v.z = rArray[2];
		v.w = rArray[3];

		v.setOrigin(qArray);
		
		return rArray;
	}

	/**
	 *
	 * @param amountToTranslate
	 * @return
	 */
	public static double[][] translate(Point3D amountToTranslate)
	{
		return addTranslateToPipeline(amountToTranslate);
	}
	
	private static double[][] transpose(double[][] a)
	{
		double[][] temp = new double[][] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				temp[i][j] = a[j][i];
			}
		}
		
		return temp;
	}
	
	/**
	 *
	 */
	public static void unSetWorldToCamera()
	{
		ArrayList<double[][]> temp =

				worldToCamera = new ArrayList<double[][]>();

		push = true;
	}
	
	public static double[][] worldToCamera(Line3D l)
	{
		double[][] lArr = new double[][] { { l.start.x, l.start.y, l.start.z, 1 }, { l.end.x, l.end.y, l.end.z, 1 },
				{ 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
		
		if (worldToCamera.size() == 0)
		{
			worldToCamera.add(IDENTITY);
		}
		
		for (double[][] t : worldToCamera)
		{
			double[][] temp = fullLeftMultiply(t, lArr);
			System.arraycopy(temp[0], 0, lArr[0], 0, 4);
			System.arraycopy(temp[1], 0, lArr[1], 0, 4);
		}

		// normalize assignments
		l.start.x = lArr[0][0] / lArr[0][3];
		l.start.y = lArr[0][1] / lArr[0][3];
		l.start.z = lArr[0][2] / lArr[0][3];
		l.end.x = lArr[1][0] / lArr[1][3];
		l.end.y = lArr[1][1] / lArr[1][3];
		l.end.z = lArr[1][2] / lArr[1][3];

		return lArr;
	}
}