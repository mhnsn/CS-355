/**
 * 
 */
package cs355.model;

import cs355.model.scene.Point3D;

/**
 * @author Mark
 *
 */
public class LineVector3D
{
	private static final Point3D	viewSpaceOrigin	= new Point3D(0, 0, 0);
	
	double							x;
	double							y;
	double							z;
	double							w;
	private Point3D					origin;
	
	private boolean					isVector;
	
	/**
	 * A new LineVector3D. This class consists of lines and unit vectors only -
	 * when isVector is true, the resulting LineVector3D will be normalized and
	 * cannot be scaled or translated.
	 * 
	 * @param x
	 *            the x magnitude of the LineVector3D
	 * @param y
	 *            the y magnitude of the LineVector3D
	 * @param z
	 *            the z magnitude of the LineVector3D
	 * @param isVector
	 *            whether or not this is to be a unit vector
	 */
	public LineVector3D(double xVal, double yVal, double zVal, Point3D A, boolean isVector)
	{
		x = xVal;
		y = yVal;
		z = zVal;
		
		if (isVector)
		{
			normalize();
			if (A.x != 0 || A.y != 0 || A.z != 0)
			{
				System.out.println("Possible error: vector has non-0 origin.");
			}
			
			origin = new Point3D(0, 0, 0);
			w = 0;
		}
		else
		{
			origin = A;
			w = 1;
		}
	}
	
	/**
	 * Translate this vector. This will overwrite the xyz values for this
	 * vector.
	 * 
	 * @param xVal
	 * @param yVal
	 * @param zVal
	 * @return
	 */
	public boolean translate(double xVal, double yVal, double zVal)
	{
		if (isVector)
		{
			return false;
		}
		
		// but only because this is easier than the matrix approach
		
		// TODO: use matrix multiplication just 'cuz
		
		origin.x += xVal;
		origin.y += yVal;
		origin.z += zVal;
		
		return true;
	}
	
	/**
	 * Rotate this vector. This will overwrite the current values for xyz.
	 * 
	 * @param xAxis
	 * @param yAxis
	 * @param zAxis
	 * @param useRadians
	 * @return
	 */
	public boolean rotate(double xAxis, double yAxis, double zAxis, boolean useRadians)
	{
		double[] newV = Transform3D.rotate(this, viewSpaceOrigin, xAxis, yAxis, zAxis, useRadians);
		
		x = newV[0];
		y = newV[1];
		z = newV[2];
		w = newV[3];
		
		return true;
	}
	
	/**
	 * Scale this vector. This will overwrite the xyzw values.
	 * 
	 * @param c
	 * @return true if this is a line and was scaled; false if this is a vector
	 */
	public boolean scale(double c)
	{
		if (isVector)
		{
			return false;
		}
		else
		{
			double[] s = Transform3D.scale(this, c);
			
			x = s[0];
			y = s[1];
			z = s[2];
			w = s[3];
			
			return true;
		}
	}
	
	/**
	 * Normalize this LineVector3D. This operation will only do anything if
	 * isVector is true - that is, only vectors will be normalized.
	 * 
	 * @return
	 */
	public boolean normalize()
	{
		if (!isVector)
		{
			return false;
		}
		
		// calculate magnitude of vector
		double magnitude = Math.sqrt((x * x) + (y * y) + (z * z));
		
		// divide all distances by magnitude
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
		
		return true;
	}
	
	/**
	 * 
	 * @param v
	 *            the vector or line to project onto
	 * @return the value of the dot product
	 */
	public double dotProduct(LineVector3D v)
	{
		double d = (this.x * v.x) + (this.y * v.y) + (this.z * v.z);
		
		return d;
	}
	
	/**
	 * 
	 * @param b
	 *            a line or vector to cross this LineVector with
	 * @return the cross product of the two, normalized.
	 */
	public LineVector3D crossProduct(LineVector3D b)
	{
		/**
		 * Cross product:
		 * 
		 * |i j k |
		 * 
		 * |aX aY aZ|
		 * 
		 * |bX bY bZ|
		 * 
		 * = i(aY*bZ - aZ*bY) - j(aX*bZ - aZ*bX) + k(aX*bY - aY*bX)
		 * 
		 * = (aY*bZ - aZ*bY, aZ*bX - aX*bZ, aX*bY - aY*bX)
		 */
		
		double crossX = (this.y * b.z) - (this.z * b.y);
		double crossY = (this.z * b.x) - (this.x * b.z);
		double crossZ = (this.x * b.y) - (this.y * b.x);
		
		LineVector3D product = new LineVector3D(crossX, crossY, crossZ, viewSpaceOrigin, true);
		
		return product;
	}
	
	/**
	 * 
	 * @return
	 */
	public LineVector3D getNormalizedVector()
	{
		if (isVector)
		{
			return this;
		}
		else
		{
			return new LineVector3D(x, y, z, viewSpaceOrigin, true);
		}
	}
	
	@Override
	public String toString()
	{
		return "[X: " + x + ", Y: " + y + ", Z:" + z + ']';
	}
}
