package cs355.view;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.GUIController;
import cs355.controller.StateMachine;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.GUIModel;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class GUIViewRefresher implements ViewRefresher {
	
	static List<Shape> modelShapes;
	
	public GUIViewRefresher()
	{
		modelShapes = new ArrayList<Shape>();
	}

	public static void initScrollBars()
	{
		GUIFunctions.setHScrollBarPosit(0);
		GUIFunctions.setVScrollBarPosit(0);
		
		GUIFunctions.setHScrollBarKnob( (int) Math.pow(2, GUIController.getZoomLevel()) );
		GUIFunctions.setVScrollBarKnob( (int) Math.pow(2, GUIController.getZoomLevel()) );
		
		GUIFunctions.setHScrollBarMin(0);
		GUIFunctions.setVScrollBarMin(0);
		
		GUIFunctions.setHScrollBarMax( 2048 );
		GUIFunctions.setVScrollBarMax( 2048 );
	}
	
	public static void setScrollBars(int zoomLevel)
	{
		if(zoomLevel == 11)
		{
			GUIFunctions.setHScrollBarPosit(0);
			GUIFunctions.setVScrollBarPosit(0);			
		}		
		
		GUIFunctions.setHScrollBarKnob( (int) Math.pow(2, zoomLevel) );
		GUIFunctions.setVScrollBarKnob( (int) Math.pow(2, zoomLevel) );
		
		System.out.println("Scroll bar size: " + (int) Math.pow(2, zoomLevel));
					}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		GUIModel m 		= (GUIModel) arg0;
		modelShapes 	= m.getShapesReversed();
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d)
	{
		boolean usingControllerShape = false;
		if(StateMachine.getCurrentShape() != null)
		{
			usingControllerShape = true;
			modelShapes.add(StateMachine.getCurrentShape());
		}
				
		for(Shape s : modelShapes)
		{
			AffineTransform objToWorld = StateMachine.objectToView(s);
			g2d.setColor(s.getColor());	
			g2d.setTransform(objToWorld);
			
			if(s.getClass().equals(cs355.model.drawing.Line.class))
			{
				Line line = (Line) s;
				
				g2d.setTransform(objToWorld);
				
				g2d.drawLine(	(int) 0,
								(int) 0,
								(int) (line.getEndV().getX()),
								(int) (line.getEndV().getY()));
			}
			else if(s.getClass().equals(cs355.model.drawing.Square.class))
			{
				Square sq = (Square) s;

				g2d.fillRect(	(int) -(sq.getSize()/2),
								(int) -(sq.getSize()/2),
								(int) sq.getSize(),
								(int) sq.getSize());
			}
			else if(s.getClass().equals(cs355.model.drawing.Rectangle.class))
			{
				Rectangle r = (Rectangle) s;

				g2d.fillRect(	(int) -(r.getWidth()/2),
								(int) -(r.getHeight()/2),
								(int) r.getWidth(),
								(int) r.getHeight());
			}
			else if(s.getClass().equals(cs355.model.drawing.Circle.class))
			{
				Circle c = (Circle) s;

				g2d.fillOval(	(int) (-c.getRadius()),
								(int) (-c.getRadius()),
								(int) c.getRadius()*2,
								(int) c.getRadius()*2);
			}
			else if(s.getClass().equals(cs355.model.drawing.Ellipse.class))
			{
				Ellipse e = (Ellipse) s;

				g2d.fillOval(	(int) (-e.getWidth()),
								(int) (-e.getHeight()),
								(int) e.getWidth()*2,
								(int) e.getHeight()*2);
			}
			else if(s.getClass().equals(cs355.model.drawing.Triangle.class))
			{
				Triangle t = (Triangle) s;

				t.cleanup();
				
				int xCoords[] = { (int) t.getA().getX(), (int) t.getB().getX(), (int) t.getC().getX()};
				int yCoords[] = { (int) t.getA().getY(), (int) t.getB().getY(), (int) t.getC().getY()};
				
				g2d.fillPolygon(xCoords, yCoords, 3);
			}
		}
		
		Shape s = GUIModel.getSelectedShape();
		if(s != null)
		{
			Rectangle c = s.getBoundingBox();
			g2d.setTransform(s.getBoundingBoxTransform());
			
			g2d.setColor(c.getColor());
			g2d.drawRect(	(int) -(c.getWidth()/2),
							(int) -(c.getHeight()/2),
							(int) c.getWidth(),
							(int) c.getHeight());
			if(s.getClass().equals(cs355.model.drawing.Circle.class))
			{
				// don't draw the handle for circles - rotation is useless.
			}
			else
			{
				if(StateMachine.doRotation())
				{
					g2d.fillOval(	(int) (c.getHandleCenter().getX())-5,
									(int) (c.getHandleCenter().getY())-5,
									(int) 10,
									(int) 10);
				}
				else
				{
					g2d.drawOval(	(int) (c.getHandleCenter().getX())-5,
									(int) (c.getHandleCenter().getY())-5,
									(int) 10,
									(int) 10);
	
				}
			}
		}
		
		if(usingControllerShape)
		{
			modelShapes.remove(modelShapes.size()-1);	// pop off data from the controller if necessary
		}
	}
}
