/**
 * 
 */
package cs355.model;

import java.util.ArrayList;

import cs355.model.scene.Point3D;

/**
 * @author Mark
 *
 */
public class Transform3D
{
	public static ArrayList<double[][]>	pipeline	= new ArrayList<double[][]>();
	private static boolean				push		= false;
	private static final double[][]		I			= new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 },
			{ 0, 0, 0, 1 } };
	
	//////////////////////////////////////////////////////////////////////
	// Transformation functions
	//
	
	/**
	 * Execute all transformations in the pipeline on this vector, then give
	 * back the new values in an array.
	 * 
	 * @param v
	 * @return
	 */
	public static double[] transform(LineVector3D v)
	{
		double[] x = LVToArray(v);
		
		for (double[][] d : pipeline)
		{
			x = leftMultiply(d, x);
		}
		
		return x;
	}
	
	public static double[] rotate(LineVector3D v, Point3D rotationOrigin, double xAxis, double yAxis, double zAxis,
			boolean useRadians)
	{
		if (!useRadians)
		{
			xAxis = (double) degreesToRadians((float) xAxis);
			yAxis = (double) degreesToRadians((float) yAxis);
			zAxis = (double) degreesToRadians((float) zAxis);
		}
		
		// addTranslateToPipeline(rotationOrigin);
		addRotateToPipeline(xAxis, yAxis, zAxis);
		// addUntranslateToPipeline(rotationOrigin);
		
		double[] r = transform(v);
		
		// if (!push)
		// {
		// popTransform(5);
		// }
		
		return r;
	}
	
	public static double[] translate(LineVector3D v, Point3D amountToTranslate)
	{
		addTranslateToPipeline(amountToTranslate);
		double[] t = transform(v);
		
		return t;
	}
	
	public static double[] scale(LineVector3D v, double c)
	{
		double[][] scaleV = { { c, c, c, c } };
		double[] scaleVals = LVToArray(v);
		
		leftMultiply(scaleV, scaleVals);
		
		// return scaleVals;
		return leftMultiply(scaleV, scaleVals);
	}
	
	//////////////////////////////////////////////////////////////////////
	// Sundry helper functions for this class
	
	private static double[] LVToArray(LineVector3D v)
	{
		return new double[] { v.x, v.y, v.z, v.w };
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
	
	private static double[] leftMultiply(double[][] d, double[] v)
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
	
	private static boolean canMultiply(double[][] d, double[] v)
	{
		if (d[0].length != v.length)
		{
			return false;
		}
		
		return true;
	}
	
	private static double degreesToRadians(float d)
	{
		return (d * Math.PI) / 180;
	}
	
	private static void addTranslateToPipeline(Point3D p)
	{
		double[][] t = new double[][] { { 1, 0, 0, p.x }, { 0, 1, 0, p.y }, { 0, 0, 1, p.z }, { 0, 0, 0, 1 } };
		
		pipeline.add(t);
	}
	
	private static void addUntranslateToPipeline(Point3D p)
	{
		double[][] tInv = new double[][] { { 1, 0, 0, -p.x }, { 0, 1, 0, -p.y }, { 0, 0, 1, -p.z }, { 0, 0, 0, 1 } };
		
		pipeline.add(tInv);
	}
	
	private static void addRotateToPipeline(double xAxis, double yAxis, double zAxis)
	{
		if (-.00001 < xAxis && xAxis > .00001)
		{
			double[][] rX = new double[][] { { 1, 0, 0, 0 }, { 0, Math.cos(xAxis), Math.sin(xAxis), 0 },
					{ 0, -Math.sin(xAxis), Math.cos(xAxis), 0 }, { 0, 0, 0, 1 } };
			pipeline.add(rX);
		}
		if (-.00001 < zAxis && yAxis > .00001)
		{
			
			double[][] rY = new double[][] { { Math.cos(yAxis), 0, -Math.sin(yAxis), 0 }, { 0, 1, 0, 0 },
					{ Math.sin(yAxis), 0, Math.cos(yAxis), 0 }, { 0, 0, 0, 1 } };
			pipeline.add(rY);
			
		}
		if (-.00001 < zAxis && zAxis > .00001)
		{
			double[][] rZ = new double[][] { { Math.cos(zAxis), Math.sin(zAxis), 0, 0 },
					{ -Math.sin(zAxis), Math.cos(zAxis), 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
			pipeline.add(rZ);
		}
		
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
	
	private static void setIdentityTransform()
	{
		pipeline.clear();
		pipeline.add(I);
	}
	
	public static int popTransform(int i)
	{
		for (int n = 0; n < i; n++)
		{
			pipeline.remove(pipeline.size() - 1);
		}
		
		return pipeline.size();
	}
	
	public static int setPush(boolean p)
	{
		push = p;
		return pipeline.size();
	}
	
	public static void clearPipeline()
	{
		pipeline.clear();
	}
}