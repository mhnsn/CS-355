package cs355.controller;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

////////////////////////////////////////////////
// Click tests

public class ShapeBound
{
	protected double boundWidth;
	protected double boundHeight;
	protected Shape curShape;
	protected Rectangle bounds;

	public ShapeBound(Shape s)
	{
		// TODO Auto-generated constructor stub
		// TODO at some point I need to assign real bounds...
		this.boundWidth = 100.0;
		this.boundHeight = 100.0;
		curShape = s;
		assignBounds();
	}

	public Shape getShape()
	{
		return curShape;
	}

	public Rectangle getBoundingBox()
	{
		// TODO: this could be optimized into an AABB
		Rectangle bounds = new Rectangle(Color.WHITE, curShape.getCenter(), boundWidth * 2, boundHeight * 2);

		return bounds;
	}

	public AffineTransform getBoundingBoxTransform()
	{
		if (curShape.getClass().equals(Line.class))
		{
			// this is good
			Line l = (Line) curShape;
			AffineTransform bt = l.getObjectToWorld();
			bt.concatenate(StateMachine.translate(new Point2D.Double(l.getEndV().x / 2, l.getEndV().y / 2)));
			bt.preConcatenate(StateMachine.worldToView(StateMachine.getViewOrigin()));
			return bt;
		}
		else if (curShape.getClass().equals(Triangle.class))
		{
			// this is good
			Triangle t = (Triangle) curShape;
			AffineTransform bt = t.getObjectToWorld();
			bt.concatenate(StateMachine.translate(new Point2D.Double(bounds.getCenter().x, bounds.getCenter().y)));
			bt.preConcatenate(StateMachine.worldToView(StateMachine.getViewOrigin()));

			return bt;
		}
		else
		{
			return StateMachine.objectToView(curShape);
		}
	}

	public Circle getHandle()
	{
		return new Circle(Color.WHITE, (Double) getBoundingBoxTransform().transform(getHandleCenter(), null),
				10 * (Math.pow(2, GUIController.getZoomLevel() - 9)));
	}

	public Point2D.Double getHandleCenter()
	{
		return new Point2D.Double(-(this.boundWidth), -(this.boundHeight));
	}

	private void assignBounds()
	{
		if (curShape.getClass().equals(cs355.model.drawing.Line.class))
		{
			Line l = (Line) curShape;

			boundWidth = Math.abs(l.getEnd().x - l.getCenter().x) / 2;
			boundHeight = Math.abs(l.getEnd().y - l.getCenter().y) / 2;
		}
		else if (curShape.getClass().equals(cs355.model.drawing.Square.class))
		{
			boundWidth = boundHeight = ((Square) curShape).getSize() / 2;
		}
		else if (curShape.getClass().equals(cs355.model.drawing.Rectangle.class))
		{
			boundWidth = ((Rectangle) curShape).getWidth() / 2;
			boundHeight = ((Rectangle) curShape).getHeight() / 2;
		}
		else if (curShape.getClass().equals(cs355.model.drawing.Circle.class))
		{
			boundWidth = boundHeight = ((Circle) curShape).getRadius();
		}
		else if (curShape.getClass().equals(cs355.model.drawing.Ellipse.class))
		{
			boundWidth = ((Ellipse) curShape).getWidth();
			boundHeight = ((Ellipse) curShape).getHeight();
		}
		else if (curShape.getClass().equals(cs355.model.drawing.Triangle.class))
		{
			assignTriangleBounds();
			return;
		}

		bounds = new Rectangle(Color.white, curShape.getCenter(), boundWidth * 2, boundHeight * 2);
	}

	private void assignTriangleBounds()
	{
		Triangle t = (Triangle) curShape;

		double minX = t.getA().x;
		double minY = t.getA().y;
		double maxX = t.getA().x;
		double maxY = t.getA().y;

		// find minX
		if (minX > t.getB().x)
		{
			minX = t.getB().x;
		}
		if (minX > t.getC().x)
		{
			minX = t.getC().x;
		}
		// find minY
		if (minY > t.getB().y)
		{
			minY = t.getB().y;
		}
		if (minY > t.getC().y)
		{
			minY = t.getC().y;
		}
		// find maxX
		if (maxX < t.getB().x)
		{
			maxX = t.getB().x;
		}
		if (maxX < t.getC().x)
		{
			maxX = t.getC().x;
		}
		// find maxY
		if (maxY < t.getB().y)
		{
			maxY = t.getB().y;
		}
		if (maxY < t.getC().y)
		{
			maxY = t.getC().y;
		}

		boundWidth = (maxX - minX) / 2;
		boundHeight = (maxY - minY) / 2;

		Point2D.Double pt = new Point2D.Double(maxX - (boundWidth), maxY - (boundHeight));

		bounds = new Rectangle(Color.WHITE, pt, boundWidth, boundHeight);
	}

	public boolean inBounds(Point2D.Double point, double tolerance)
	{
		if (this.getShape().getClass().equals(Line.class))
		{
			// TODO: this bound check is used because an AABB of a line just
			// doesn't work here.
			Line l = (Line) this.getShape();

			if (l.pointInShape(point, tolerance))
			{
				return true;
			}
		}
		else if (-boundWidth - tolerance > point.x || boundWidth + tolerance < point.x
				|| -boundHeight - tolerance > point.y || boundHeight + tolerance < point.y)
		{
			return false;
		}
		return true;
	}
}
// Click tests
////////////////////////////////////////////////