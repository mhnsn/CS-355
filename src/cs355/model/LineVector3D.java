/**
 *
 */
package cs355.model;

import java.awt.geom.Point2D;

import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;

/**
 * @author Mark
 *
 */
public class LineVector3D
{
	private static final Point3D	viewSpaceOrigin	= new Point3D(0, 0, 0);

	public double					x;
	public double					y;
	public double					z;
	public double					w;

	// where to start the line
	private Point3D					origin;
	private double					oW;

	// vector representation of line from origin
	private Point3D					end;

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
	public LineVector3D(double xVal, double yVal, double zVal, Point3D A, boolean v)
	{
		x = xVal;
		y = yVal;
		z = zVal;

		if (v)
		{
			normalize();
			if (A.x != 0 || A.y != 0 || A.z != 0)
			{
				System.out.println("Possible error: vector has non-0 origin.");
			}

			origin = new Point3D(0, 0, 0);
			w = 0;
			isVector = true;
		}
		else
		{
			origin = A;
			w = 1;
			isVector = false;
		}

		oW = 1;
		setEnd(new Point3D(x, y, z));
	}

	/**
	 * It's not elegant, but it sure works nice for debugging the pipeline.
	 *
	 * @param v
	 */
	public LineVector3D(double[] v)
	{
		this(new Line3D(viewSpaceOrigin, new Point3D(v[0], v[1], v[2])));
	}

	/**
	 *
	 * @param l
	 */
	public LineVector3D(Line3D l)
	{
		x = l.end.x;
		y = l.end.y;
		z = l.end.z;
		w = 1;
		
		end = l.end;
		origin = l.start;

		isVector = false;
		oW = 1;
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

		double crossX = y * b.z - z * b.y;
		double crossY = z * b.x - x * b.z;
		double crossZ = x * b.y - y * b.x;

		LineVector3D product = new LineVector3D(crossX, crossY, crossZ, viewSpaceOrigin, true);

		return product;
	}

	/**
	 *
	 * @param v
	 *            the vector or line to project onto
	 * @return the value of the dot product
	 */
	public double dotProduct(LineVector3D v)
	{
		double d = x * v.x + y * v.y + z * v.z;

		return d;
	}

	/**
	 * @return the end
	 */
	public Point3D getEnd()
	{
		return end;
	}

	/**
	 *
	 * @return
	 */
	public Point3D getOrigin()
	{
		return origin;
	}

	/**
	 *
	 * @return
	 */
	public double[] getPArray()
	{
		return new double[] { origin.x, origin.y, origin.z, 1 };
	}

	/**
	 *
	 * @return
	 */
	public double[] getVArray()
	{
		return new double[] { x, y, z, w };
	}

	/**
	 *
	 * @return
	 */
	public LineVector3D getVectorValues()
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

	/**
	 * @return the value of isVector
	 */
	public boolean isVector()
	{
		return isVector;
	}

	/**
	 * Normalize this LineVector3D. This operation will work independent of
	 * whether the given lineVector3D is a vector or line representation
	 *
	 * @return
	 */
	public void normalize()
	{
		// // calculate magnitude of vector
		// double magnitude = Math.sqrt((x * x) + (y * y) + (z * z));
		//
		//
		// // divide all distances by magnitude
		// x /= magnitude;
		// y /= magnitude;
		// z /= magnitude;
		x /= w;
		y /= w;
		z /= w;

		updateEnd();
		
		return;
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
		double[] newP = Transform3D.rotate(this, origin, xAxis, yAxis, zAxis, useRadians);
		
		x = newV[0];
		y = newV[1];
		z = newV[2];
		w = newV[3];

		updateEnd();
		
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

			updateEnd();

			return true;
		}
	}

	/**
	 * @param end
	 *            the end to set. DO NOT CALL.
	 */
	private void setEnd(Point3D e)
	{
		end = e;
		x = e.x - origin.x;
		y = e.y - origin.y;
		z = e.z - origin.z;
	}

	/**
	 *
	 * @param newOrigin
	 */
	public void setOrigin(double[] a)
	{
		origin.x = a[0];
		origin.y = a[1];
		origin.z = a[2];
		oW = a[3];

		updateEnd();
		return;
	}

	/**
	 *
	 * @param newOrigin
	 */
	public void setOrigin(Point3D newOrigin)
	{
		origin = newOrigin;
		updateEnd();
		return;
	}
	
	/**
	 * @param isVector
	 *            what to set isVector to
	 */
	public void setVector(boolean isVector)
	{
		this.isVector = isVector;
	}

	/**
	 *
	 */
	@Override
	public String toString()
	{
		return "[X: " + x + ", Y: " + y + ", Z:" + z + ']';
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

	private void updateEnd()
	{
		end = new Point3D(origin.x + x, origin.y + y, origin.y + y);
	}
}