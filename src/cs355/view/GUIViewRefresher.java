package cs355.view;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.GUIController;
import cs355.controller.ShapeBound;
import cs355.controller.StateMachine;
import cs355.model.GUIModel;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class GUIViewRefresher implements ViewRefresher
{
	static List<Shape> modelShapes;

	public GUIViewRefresher()
	{
		modelShapes = new ArrayList<Shape>();
	}

	public static void initScrollBars()
	{
		GUIFunctions.setHScrollBarPosit(0);
		GUIFunctions.setVScrollBarPosit(0);

		GUIFunctions.setHScrollBarKnob((int) Math.pow(2, GUIController.getZoomLevel()));
		GUIFunctions.setVScrollBarKnob((int) Math.pow(2, GUIController.getZoomLevel()));

		GUIFunctions.setHScrollBarMin(0);
		GUIFunctions.setVScrollBarMin(0);

		GUIFunctions.setHScrollBarMax(2048);
		GUIFunctions.setVScrollBarMax(2048);
	}

	public static void setScrollBars(Point2D.Double newOrigin)
	{
		boolean zooming = false;
		int zoomLevel = GUIController.getZoomLevel();

		if (StateMachine.getZoomFlag())
		{
			System.out.println(zoomLevel);
			zooming = true;

			if (zoomLevel == 11)
			{
				StateMachine.setViewOrigin(new Point2D.Double(0, 0));

				GUIFunctions.setHScrollBarPosit(0);
				GUIFunctions.setVScrollBarPosit(0);

				GUIFunctions.setHScrollBarKnob((int) Math.pow(2, zoomLevel));
				GUIFunctions.setVScrollBarKnob((int) Math.pow(2, zoomLevel));

				GUIFunctions.refresh();
				return;
			}
			else if (zoomLevel == 10)
			{
				GUIFunctions.setHScrollBarKnob((int) Math.pow(2, zoomLevel));
				GUIFunctions.setVScrollBarKnob((int) Math.pow(2, zoomLevel));
			}
		}

		StateMachine.setViewOrigin(newOrigin);

		// System.out.println("1. Setting x position to " +
		// newOrigin.toString());
		GUIFunctions.setHScrollBarPosit((int) -newOrigin.getX());
		// System.out.println("2. Setting y position to " +
		// newOrigin.toString());
		GUIFunctions.setVScrollBarPosit((int) -newOrigin.getY());

		// System.out.println("3. Setting horizintal scroll size.");
		GUIFunctions.setHScrollBarKnob((int) Math.pow(2, zoomLevel));
		// System.out.println("4. Setting vertical scroll size.");
		GUIFunctions.setVScrollBarKnob((int) Math.pow(2, zoomLevel));

		if (zooming)
		{
			StateMachine.lowerZoomFlag();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		GUIModel m = (GUIModel) arg0;
		modelShapes = m.getShapesReversed();
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d)
	{
		boolean usingControllerShape = false;
		if (StateMachine.getCurrentShape() != null)
		{
			usingControllerShape = true;
			modelShapes.add(StateMachine.getCurrentShape());
		}

		AffineTransform objToView;

		if (GUIModel.getBackgroundImage() != null)
		{
			BufferedImage bi = GUIModel.getBackgroundImage().getImage();
			if (bi != null)
			{
				// get view origin
				Point2D.Double viewOrigin = StateMachine.getViewOrigin();

				// calculate center point of current view
				double viewSize = Math.pow(2, GUIController.getZoomLevel());
				Point2D.Double viewCenter = new Point2D.Double(viewOrigin.getX() - viewSize / 2,
						viewOrigin.getY() - viewSize / 2);

				// calculate the x and y offset to use
				// in order to center the image
				int offsetX = bi.getWidth() / 2;
				int offsetY = bi.getHeight() / 2;

				// set the image's x and y values
				// to viewCenter-offsetX
				// and viewcenter-offsetY
				int imageTopLeftX = (int) viewCenter.getX() + offsetX;
				int imageTopLeftY = (int) viewCenter.getY() + offsetY;

				// objToView = StateMachine.objectToView(new Circle(Color.white,
				// new Point2D.Double(0, 0), 1));
				objToView = StateMachine.backgroundImageToView();

				g2d.setTransform(objToView);

				g2d.drawImage(bi, -imageTopLeftX, -imageTopLeftY, null);

			}
		}

		for (Shape s : modelShapes)
		{
			objToView = StateMachine.objectToView(s);
			g2d.setColor(s.getColor());
			g2d.setTransform(objToView);

			if (s.getClass().equals(cs355.model.drawing.Line.class))
			{
				Line line = (Line) s;

				// g2d.setTransform(objToWorld);

				g2d.drawLine((int) 0, (int) 0, (int) (line.getEndV().getX()), (int) (line.getEndV().getY()));
			}
			else if (s.getClass().equals(cs355.model.drawing.Square.class))
			{
				Square sq = (Square) s;

				g2d.fillRect((int) -(sq.getSize() / 2), (int) -(sq.getSize() / 2), (int) sq.getSize(),
						(int) sq.getSize());
			}
			else if (s.getClass().equals(cs355.model.drawing.Rectangle.class))
			{
				Rectangle r = (Rectangle) s;

				g2d.fillRect((int) -(r.getWidth() / 2), (int) -(r.getHeight() / 2), (int) r.getWidth(),
						(int) r.getHeight());
			}
			else if (s.getClass().equals(cs355.model.drawing.Circle.class))
			{
				Circle c = (Circle) s;

				g2d.fillOval((int) (-c.getRadius()), (int) (-c.getRadius()), (int) c.getRadius() * 2,
						(int) c.getRadius() * 2);
			}
			else if (s.getClass().equals(cs355.model.drawing.Ellipse.class))
			{
				Ellipse e = (Ellipse) s;

				g2d.fillOval((int) (-e.getWidth()), (int) (-e.getHeight()), (int) e.getWidth() * 2,
						(int) e.getHeight() * 2);
			}
			else if (s.getClass().equals(cs355.model.drawing.Triangle.class))
			{
				Triangle t = (Triangle) s;

				t.cleanup();

				int xCoords[] = { (int) t.getA().getX(), (int) t.getB().getX(), (int) t.getC().getX() };
				int yCoords[] = { (int) t.getA().getY(), (int) t.getB().getY(), (int) t.getC().getY() };

				g2d.fillPolygon(xCoords, yCoords, 3);
			}
		}

		ShapeBound sb = null;

		if (GUIModel.getSelectedShape() != null)
		{
			sb = new ShapeBound(GUIModel.getSelectedShape());
		}

		if (sb != null)
		{
			Rectangle c = sb.getBoundingBox();
			g2d.setTransform(sb.getBoundingBoxTransform());

			double scaleFactor = Math.pow(2, GUIController.getZoomLevel() - 9);

			g2d.setStroke(new BasicStroke((float) (1 * scaleFactor)));

			g2d.setColor(c.getColor());
			g2d.drawRect((int) -(c.getWidth() / 2), (int) -(c.getHeight() / 2), (int) c.getWidth(),
					(int) c.getHeight());
			if (sb.getShape().getClass().equals(cs355.model.drawing.Circle.class))
			{
				// don't draw the handle for circles - rotation is useless.
			}
			else
			{
				if (StateMachine.doRotation())
				{
					g2d.fillOval((int) (sb.getHandleCenter().getX() - 5 * scaleFactor),
							(int) (sb.getHandleCenter().getY() - 5 * scaleFactor), (int) (10 * scaleFactor),
							(int) (10 * scaleFactor));
				}
				else
				{
					g2d.drawOval((int) (sb.getHandleCenter().getX() - 5 * scaleFactor),
							(int) (sb.getHandleCenter().getY() - 5 * scaleFactor), (int) (10 * scaleFactor),
							(int) (10 * scaleFactor));
				}
			}
		}

		if (usingControllerShape)
		{
			modelShapes.remove(modelShapes.size() - 1); // pop off data from the
														// controller if
														// necessary
		}
	}

}
