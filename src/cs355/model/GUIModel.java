package cs355.model;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import cs355.controller.GUIController;
import cs355.controller.ShapeBound;
import cs355.model.drawing.CS355Drawing;
import cs355.model.drawing.Line;
import cs355.model.drawing.Shape;
import cs355.model.image.Image;

public class GUIModel extends CS355Drawing
{
	static ArrayList<Shape> shapeList;
	static Shape selectedShape;
	private static Image backgroundImage;

	public GUIModel()
	{
		shapeList = new ArrayList<Shape>();
		setBackgroundImage(new Image());
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
		if (index != -1)
		{
			shapeList.remove(index);
			selectedShape = null;
			this.setChanged();
			notifyObservers();
		}
	}

	@Override
	public void moveToFront(int index)
	{
		if (index != -1)
		{
			Shape s = shapeList.get(index);
			shapeList.remove(index);
			shapeList.add(0, s);
			this.setChanged();
			notifyObservers();
		}

	}

	@Override
	public void movetoBack(int index)
	{
		if (index != -1)
		{
			Shape s = shapeList.get(index);
			shapeList.remove(index);
			shapeList.add(s);
			this.setChanged();
			notifyObservers();
		}
	}

	@Override
	public void moveForward(int index)
	{
		if (index != -1)
		{
			Shape s = shapeList.get(index);
			shapeList.remove(index);
			if (index != 0)
			{
				shapeList.add((index - 1), s);
			}
			else
			{
				shapeList.add((index), s);
			}
			this.setChanged();
			notifyObservers();
		}
	}

	@Override
	public void moveBackward(int index)
	{
		if (index != -1)
		{
			Shape s = shapeList.get(index);
			shapeList.remove(index);
			if (index == shapeList.size())
			{
				shapeList.add(index, s);
			}
			else
			{
				shapeList.add(index + 1, s);
			}
			this.setChanged();
			notifyObservers();
		}
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

		for (Shape s : shapeList)
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

	public Shape getClickedShape(Point2D.Double pointClicked, double tolerance)
	{
		Double objectPointClicked = pointClicked;
		double scaleFactor = Math.pow(2, GUIController.getZoomLevel() - 9);
		for (Shape s : getShapes())
		{
			ShapeBound sb = new ShapeBound(s);
			if (!s.getClass().equals(Line.class))
			{
				objectPointClicked = s.worldToObject(pointClicked);
			}
			else
			{
				// tolerance *= scaleFactor;
			}

			if (sb.inBounds(objectPointClicked, tolerance))
			{
				if (s.pointInShape(objectPointClicked, tolerance))
				{
					return s;
				}
			}
		}
		return null;
	}

	/**
	 * @return the selectedShape
	 */
	public static Shape getSelectedShape()
	{
		return selectedShape;
	}

	/**
	 * @param selectedShape
	 *            the selectedShape to set
	 */
	public static void setSelectedShape(Shape shape)
	{
		selectedShape = shape;
		if (shape == null)
		{
			// System.out.println("Selected shape is null");
			return;
		}
	}

	public static Double getSelectedCenter()
	{
		return selectedShape.getCenter();
	}

	public int getSelectedShapeIndex()
	{
		for (int i = 0; i < shapeList.size(); i++)
		{
			if (shapeList.get(i).equals(selectedShape))
			{
				return i;
			}
		}

		return -1;
	}

	public static Image getBackgroundImage()
	{
		return backgroundImage;
	}

	public static void setBackgroundImage(Image backgroundImage)
	{
		GUIModel.backgroundImage = backgroundImage;
	}

}
