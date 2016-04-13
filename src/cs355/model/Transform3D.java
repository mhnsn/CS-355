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
	 */
	private static void addRotateToPipeline(double xAxis, double yAxis, double zAxis)
	{
		// if (outOfToleranceRange(xAxis, 0, .00001))
		// {
		// double[][] rX = new double[][] { { 1, 0, 0, 0 }, { 0,
		// Math.cos(xAxis), Math.sin(xAxis), 0 },
		// { 0, -Math.sin(xAxis), Math.cos(xAxis), 0 }, { 0, 0, 0, 1 } };
		// pipeline.add(rX);
		// }
		if (outOfToleranceRange(yAxis, 0, .01))
		{
			
			double[][] rY = new double[][] { { Math.cos(yAxis), 0, Math.sin(yAxis), 0 }, { 0, 1, 0, 0 },
					{ -Math.sin(yAxis), 0, Math.cos(yAxis), 0 }, { 0, 0, 0, 1 } };
			// double[][] rY = new double[][]
			//
			// { { Math.cos(yAxis), 0, -Math.sin(yAxis), 0 },
			// { 0, 1, 0, 0 },
			// { Math.sin(yAxis), 0, Math.cos(yAxis), 0 },
			// { 0, 0, 0, 1 } };

			pipeline.add(rY);

		}
		// if (outOfToleranceRange(zAxis, 0, .00001))
		// {
		// double[][] rZ = new double[][] { { Math.cos(zAxis), Math.sin(zAxis),
		// 0, 0 },
		// { -Math.sin(zAxis), Math.cos(zAxis), 0, 0 }, { 0, 0, 1, 0 }, { 0, 0,
		// 0, 1 } };
		// pipeline.add(rZ);
		// }
		
		// int i;
		// for (i = 0; i < 3; i++)
		// {
		// leftMultiply(rY, rX[i]);
		// }
		// for (i = 0; i < 3; i++)
		// {
		// leftMultiply(rZ, rX[i]);
		// }
	}

	/**
	 *
	 * @param p
	 */
	private static void addTranslateToPipeline(Point3D p)
	{
		if (outOfToleranceRange(p.x, 0, .01) || outOfToleranceRange(p.y, 0, .01) || outOfToleranceRange(p.z, 0, .01))
		{
			double[][] t = new double[][] { { 1, 0, 0, p.x }, { 0, 1, 0, p.y }, { 0, 0, 1, p.z }, { 0, 0, 0, 1 } };
			pipeline.add(t);
		}
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
				// System.out.println("Clipping line.");
				continue;
			}
			clippedLines.add(v);
		}
		
		if (clippedLines.size() > 0)
		{
			System.out.print("");
		}

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
	 * will handle case of 1 column in A
	 *
	 * @param L
	 * @param A
	 * @return
	 */
	public static double[][] fullLeftMultiply(double[][] L, double[][] A)
	{
		int j = A.length;

		for (int i = 0; i < j; i++)
		{
			leftMultiply(L, A[i]);
		}

		return A;
	}

	/**
	 *
	 * @param zX
	 * @param zY
	 * @param n
	 * @param f
	 * @return
	 */
	public static double[][] generateClipMatrix(double zX, double zY, double n, double f)
	{
		// TODO: verify I didn't just break the clip matrix with this change.
		double[][] clipMatrix = { { Math.atan(degreesToRadians((float) zX)), 0, 0, 0 },
				{ 0, Math.atan(degreesToRadians((float) zY)), 0, 0 }, { 0, 0, (f + n) / (f - n), 1 },
				{ 0, 0, 2 * (n * f / (f - n)), 0 } };

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
			// System.out.println("1. Failed left-right test. (clipSpaceV[0] = "
			// + clipSpaceV[0] + ").");
			return clipSpaceV[0] > 0 ? 1 : -1;
		}
		else if (outOfToleranceRange(clipSpaceV[1], 0, w)) // top and bottom
		{
			// System.out.println("2. Failed top-bottom test. (clipSpaceV[1] = "
			// + clipSpaceV[1] + ").");
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

	/**
	 * Rotate the passed LineVector. This will overwrite the current values
	 * stored there.
	 *
	 * @param v
	 * @param rotationOrigin
	 * @param xAxis
	 * @param yAxis
	 * @param zAxis
	 * @param valuesPassedInRadians
	 * @return
	 */
	public static double[] rotate(LineVector3D v, Point3D rotationOrigin, double xAxis, double yAxis, double zAxis,
			boolean valuesPassedInRadians)
	{
		if (!valuesPassedInRadians)
		{
			xAxis = degreesToRadians((float) xAxis);
			yAxis = degreesToRadians((float) yAxis);
			zAxis = degreesToRadians((float) zAxis);
		}

		addRotateToPipeline(xAxis, yAxis, zAxis);

		double[] r = transform(v);

		return r;
	}

	/**
	 *
	 * @param v
	 * @param c
	 * @return
	 */
	public static double[] scale(LineVector3D v, double c)
	{
		double[][] scaleV = { { c, c, c, c } };
		double[] scaleVals = v.getVArray();

		leftMultiply(scaleV, scaleVals);

		// return scaleVals;
		return leftMultiply(scaleV, scaleVals);
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
	 * @param v
	 * @param amountToTranslate
	 * @return
	 */
	public static double[] translate(LineVector3D v, Point3D amountToTranslate)
	{
		addTranslateToPipeline(amountToTranslate);
		double[] t = transform(v);

		return t;
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
}