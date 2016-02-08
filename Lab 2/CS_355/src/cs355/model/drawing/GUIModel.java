package cs355.model.drawing;

import java.util.ArrayList;
import java.util.List;

public class GUIModel extends CS355Drawing {

	static ArrayList<Shape> shapeList;
	
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
}
