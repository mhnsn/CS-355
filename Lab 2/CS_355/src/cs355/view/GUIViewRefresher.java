package cs355.view;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.GUIController;
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
		if(GUIController.getCurrentShape() != null)
		{
			usingControllerShape = true;
			modelShapes.add(GUIController.getCurrentShape());
		}
				
		for(Shape s : modelShapes)
		{
			g2d.setColor(s.getColor());
			if(s.getClass().equals(cs355.model.drawing.Line.class))
			{
				Line line = (Line) s;
				
				g2d.drawLine( 	(int) line.getCenter().getX(),
								(int) line.getCenter().getY(),
								(int) line.getEnd().getX(),
								(int) line.getEnd().getY());
			}
			else if(s.getClass().equals(cs355.model.drawing.Square.class))
			{
				Square sq = (Square) s;

				g2d.fillRect((int) (sq.getUpperLeft().getX()),
								(int) (sq.getUpperLeft().getY()),
								(int) sq.getSize(),
								(int) sq.getSize());
			}
			else if(s.getClass().equals(cs355.model.drawing.Rectangle.class))
			{
				Rectangle r = (Rectangle) s;

				g2d.fillRect(	(int) r.getUpperLeft().getX(),
								(int) r.getUpperLeft().getY(),
								(int) r.getWidth(),
								(int) r.getHeight());		
			}
			else if(s.getClass().equals(cs355.model.drawing.Circle.class))
			{
				Circle c = (Circle) s;
				g2d.fillOval(	(int) (c.getCenter().getX()-c.getRadius()),
								(int) (c.getCenter().getY()-c.getRadius()),
								(int) c.getRadius()*2,
								(int) c.getRadius()*2);
			}
			else if(s.getClass().equals(cs355.model.drawing.Ellipse.class))
			{
				Ellipse e = (Ellipse) s;
				g2d.fillOval(	(int) (e.getCenter().getX()-e.getWidth()),
								(int) (e.getCenter().getY()-e.getHeight()),
								(int) e.getWidth()*2,
								(int) e.getHeight()*2);
			}
			else if(s.getClass().equals(cs355.model.drawing.Triangle.class))
			{
				Triangle t = (Triangle) s;
				int xCoords[] = { (int)t.getA().getX(), (int) t.getB().getX(), (int) t.getC().getX()};
				int yCoords[] = { (int)t.getA().getY(), (int) t.getB().getY(), (int) t.getC().getY()};
				
				g2d.fillPolygon(xCoords, yCoords, 3);
			}

		}
		
		if(usingControllerShape)
		{
			modelShapes.remove(modelShapes.size()-1);	// pop off data from the controller if necessary
		}
	}
}
