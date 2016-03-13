package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import cs355.controller.StateMachine;

/**
 * This is the base class for all of your shapes.
 * Make sure they all extend this class.
 */
public abstract class Shape {

	// The color of this shape.
	protected Color color;

	// The center of this shape.
	protected Point2D.Double center;

	// The rotation of this shape.
	protected double rotation;
	
	// This shape's transformation stack
	protected AffineTransform[] transforms;
	
	protected double boundWidth;
	protected double boundHeight;

	/**
	 	* Basic constructor that sets fields.
	 * It initializes rotation to 0.
	 * @param color the color for the new shape.
	 * @param center the center point of the new shape.
	 */
	public Shape(Color color, Point2D.Double center)
	{
		this.color 		= color;
		this.center 	= center;
		this.rotation	= 0.0;
		this.boundWidth		= 0.0;
		this.boundHeight	= 0.0;
	}

	/**
	 	* Getter for this shape's color.
	 * @return the color of this shape.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 	* Setter for this shape's color
	 * @param color the new color for the shape.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 	* Getter for this shape's center.
	 * @return this shape's center as a Java point.
	 */
	public Point2D.Double getCenter()
	{
		return center;
	}

	/**
	 	* Setter for this shape's center.
	 * @param center the new center as a Java point.
	 */
	public void setCenter(Point2D.Double center)
	{
		this.center = center;
	}

	/**
	 	* Getter for this shape's rotation.
	 * @return the rotation as a double.
	 */
	public double getRotation()
	{
		return rotation;
	}

	/**
	 	* Setter for this shape's rotation.
	 * @param rotation the new rotation.
	 */
	public void setRotation(double rotation)
	{
		this.rotation = rotation;
	}

	/**
	 	* Used to test for whether the user clicked inside a shape or not.
	 * @param pt = the point to test whether it's in the shape or not.
	 * @param tolerance = the tolerance for testing. Mostly used for lines.
	 * @return true if pt is in the shape, false otherwise.
	 */
	public abstract boolean pointInShape(Point2D.Double pt, double tolerance);
	
	public boolean setBounds(double width, double height)
	{
		if(width <= 0 || height <= 0)
		{
			return false;
		}
		
		this.boundWidth 	= width/2;
		this.boundHeight	= height/2;
		
		return true;
	}
	
	protected boolean inBounds(Point2D.Double point, double tolerance)
	{
		if(-boundWidth	- tolerance > point.x
		||  boundWidth	+ tolerance < point.x
		|| -boundHeight	- tolerance > point.y
		||  boundHeight	+ tolerance < point.y)
		{
			return false;
		}
		return true;
	}

	public Rectangle getBoundingBox()
	{
		//TODO: this could be optimized into an AABB
		Rectangle bounds = new Rectangle(Color.WHITE,this.center,boundWidth*2,boundHeight*2);
		bounds.setBounds(boundWidth*2, boundHeight*2);
		return bounds;
	}

	/**
	 * @param s
	 * @return
	 */
	public AffineTransform getObjectToWorld()
	{
//		AffineTransform objToWorld = new AffineTransform();
//		objToWorld.translate(this.getCenter().getX(), this.getCenter().getY());
//		objToWorld.rotate(this.getRotation());
//		return objToWorld;
		return StateMachine.objectToWorld(this);
	}

	/**
	 * @param pointClicked TODO
	 * @param s
	 * @return
	 */
	public Point2D.Double objectToWorld(Point2D.Double pointClicked)
	{
		return (Double) getObjectToWorld().transform(pointClicked, new Point2D.Double());
	}
	
	/**
	 * @param pointClicked
	 * @param s
	 * @return
	 */
	public Point2D.Double worldToObject(Point2D.Double pointClicked)
	{
//		AffineTransform worldToObject	= new AffineTransform();
//		worldToObject.rotate(-this.getRotation());
//		worldToObject.translate(-this.getCenter().getX(), -this.getCenter().getY());
//		return (Double) worldToObject.transform(pointClicked,new Point2D.Double());
		
		try
		{
			return (Double) StateMachine.objectToWorld(this).createInverse().transform(pointClicked, null);
		}
		catch (NoninvertibleTransformException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public AffineTransform getBoundingBoxTransform()
	{
		return getObjectToWorld();
	}
	
	public Point2D.Double getHandleCenter()
	{
		return new Point2D.Double(-(this.boundWidth), -(this.boundHeight));
	}

	public Circle getHandle()
	{		
		return new Circle(Color.WHITE,(Double) getBoundingBoxTransform().transform(getHandleCenter(),null),10);
	}
}
