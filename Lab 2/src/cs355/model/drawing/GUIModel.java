package cs355.model.drawing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

public class GUIModel extends CS355Drawing {

	static ArrayList<Shape> shapeList;
	static Shape 			selectedShape;
	private static Double 	selectedCenter;
	
	public GUIModel()
	{
		shapeList = new ArrayList<Shape>();
	}

	@Override
	public Shape getShape(int index)
	{
		return shapeList.get(index);
	}

	@Override
	public int addShape(Shape s)
	{
		shapeList.add(0, s);
		this.setChanged();
		notifyObservers();
		return 0;
	}
	
	@Override
	public void deleteShape(int index)
	{
		shapeList.remove(index);
		this.setChanged();
		notifyObservers();
	}

	@Override
	public void moveToFront(int index)
	{
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		shapeList.add(s);
		this.setChanged();
		notifyObservers();
	}

	@Override
	public void movetoBack(int index)
	{
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		shapeList.add(0,s);
		this.setChanged();
		notifyObservers();
	}

	@Override
	public void moveForward(int index)
	{
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		shapeList.add((index + 1), s);
		this.setChanged();
		notifyObservers();
	}

	@Override
	public void moveBackward(int index)
	{
		Shape s = shapeList.get(index);
		shapeList.remove(index);
		shapeList.add((index - 1), s);
		this.setChanged();
		notifyObservers();
	}

	@Override
	public List<Shape> getShapes()
	{
		return shapeList;
	}

	@Override
	public List<Shape> getShapesReversed()
	{
		ArrayList<Shape> reverseList = new ArrayList<Shape>();
		
		for(Shape s : shapeList)
		{
			reverseList.add(0, s);
		}
		
		return reverseList;
	}

	@Override
	public void setShapes(List<Shape> shapes)
	{
		shapeList = (ArrayList<Shape>) shapes;
		this.setChanged();
		notifyObservers();
	}
	
	public Shape getClickedShape(Point2D.Double pointClicked)
	{
		for(Shape s : getShapes())
		{
			Point2D.Double objectCoord = worldToObject(pointClicked, s);
			
			if(s.pointInShape(objectCoord,4))
			{
				return s;
			}
		}
		return null;
	}

	/**
	 * @param pointClicked
	 * @param s
	 * @return
	 */
	public static Point2D.Double worldToObject(Point2D.Double pointClicked, Shape s)
	{
		AffineTransform worldToObject	= new AffineTransform();
		Point2D.Double objectCoord 		= new Point2D.Double();
		worldToObject.rotate(-s.getRotation());
		worldToObject.translate(-s.getCenter().getX(), -s.getCenter().getY());
		worldToObject.transform(pointClicked,objectCoord);
		return objectCoord;
	}
	
	/**
	 * @return the selectedShape
	 */
	public static Shape getSelectedShape()
	{
		return selectedShape;
	}

	/**
	 * @param selectedShape the selectedShape to set
	 */
	public static void setSelectedShape(Shape shape)
	{
		selectedShape = shape;
		if(shape == null)
		{
			setSelectedCenter(null);
			return;
		}
		setSelectedCenter(shape.center);
	}

	public static Double getSelectedCenter()
	{
		return selectedCenter;
	}

	public static void setSelectedCenter(Double selectedCenter)
	{
		if(selectedCenter == null)
		{
			GUIModel.selectedCenter = null;
			return;
		}
		GUIModel.selectedCenter = new Point2D.Double(selectedCenter.getX(), selectedCenter.getY());
	}

}
