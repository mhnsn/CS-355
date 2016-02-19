package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.util.ArrayList;

import cs355.GUIFunctions;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.GUIModel;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class StateMachine
{
	// create a current state object
	static String current;

	// enumerate states
	final static String init 				= "init";
	final static String drawing 			= "drawing";
	final static String submitShapeToModel 	= "submitShapeToModel";
	final static String selectShape			= "selectShape";

	// state flags
	static boolean	startDrawingFlag;
	static private boolean	drawingCompleteFlag;
	static private boolean 	selectButtonPressedFlag;
	static boolean  buttonClickedFlag;
	static boolean  mouseDraggedFlag;
	static boolean  mouseMovedFlag;
	static boolean	mouseClickedFlag;
	static boolean	mouseReleasedFlag;
	static boolean	mousePressedFlag;
	static boolean	mouseEnteredFlag;
	static boolean	mouseExitedFlag;

	static private boolean debug		= true;
	static private boolean printState;
	
	// state machine variables	
	static private Shape	currentShape;
	static private Color	currentColor;	
	static private int		buttonClicked;
	static ArrayList<Point2D> 	clickLocations;	
	static private Point2D.Double		currentMouseLocation;
	
	static MouseEvent e;
	Point2D.Double clickLocation;
	
	// initializer
	StateMachine()
	{
		current = init;
		setButtonClicked(GUIController.selectButton);

		currentShape = null;
		
		
		startDrawingFlag		=
		selectButtonPressedFlag =
		buttonClickedFlag		=
		drawingCompleteFlag 	= 
		mouseDraggedFlag		=
		mouseMovedFlag			=
		mouseClickedFlag		=
		mouseReleasedFlag		=
		mousePressedFlag		=
		mouseEnteredFlag		=
		mouseExitedFlag 		= false;
		
		printState 				= false;
		
		clickLocations			= new ArrayList<Point2D>();
	}
	
//	void tick()
	void tick()
	{
		if(e != null)
		{
			if(mouseClickedFlag || mousePressedFlag)
			{
				mouseClickedFlag = mousePressedFlag = false;
				clickLocation = new Point2D.Double(e.getX(), e.getY());
			}
			setCurrentMouseLocation(new Point2D.Double(e.getX(), e.getY()));
		}

		if(printState && debug)
		{
			GUIFunctions.printf("Current state: " + StateMachine.current);
			System.out.println("Current state: " + StateMachine.current);

			printState = false;
		}
		
// perform state actions
		switch(StateMachine.current)
		{
			case StateMachine.init:
				break;
			case StateMachine.drawing:
				handleDrawing(getCurrentMouseLocation());
				GUIFunctions.refresh();
				break;
			case StateMachine.selectShape:
				if(mouseDraggedFlag = true)
				{
					mouseDraggedFlag = false;
					if(GUIModel.getSelectedShape() != null)
					{
						Circle handle = rotationHandleClicked(e);
						if(handle != null)
						{
							handleRotation(e,handle);
						}
						else
						{
							handleShapeMovement(e);
						}
					}
				}
				if(mousePressedFlag = true)
				{
					mousePressedFlag = false;
				}
				
				handleShapeOperations(getCurrentMouseLocation());
				break;
			default:
				break;
		}
		
// update state
		switch(StateMachine.current)
		{
			case StateMachine.init:
				if(startDrawingFlag)
				{
					StateMachine.current	= StateMachine.drawing;
					startDrawingFlag 		= false;
					printState 				= true;
				}
				if(buttonClickedFlag)
				{
					buttonClickedFlag = false;
					switch(buttonClicked)
					{
						case GUIController.selectButton:
							StateMachine.current	= StateMachine.selectShape;
							selectButtonPressedFlag	= false;
							printState		= true;
							break;
						default:
							break;
					}
				}
				break;
			case StateMachine.drawing:
				if(drawingCompleteFlag)
				{
					StateMachine.current 	= StateMachine.init;
					submitShape();
					clearDrawingInfo();
					
					drawingCompleteFlag = false;
					printState		= true;
				}
				break;
			case StateMachine.selectShape:
				if(getButtonClicked() != GUIController.selectButton)
				{
					StateMachine.current = StateMachine.init;
				}
				break;
			default:
				break;
		}
	}

//	end tick function

	private void handleShapeOperations(Double currentMouseLocation)
	{
				
	}
	private void handleShapeMovement(MouseEvent e)
	{
		AffineTransform t = new AffineTransform();
		double dX = (e.getX() - clickLocations.get(0).getX());
		double dY = (e.getY() - clickLocations.get(0).getY());
		t.translate(dX, dY);
		
		Point2D.Double newCenter = (Double) t.transform(GUIModel.getSelectedCenter(), null);
//		System.out.print("New center: (" + newCenter.x + ", " + newCenter.y + ")");
//		System.out.print("\tdX: " + dX);
//		System.out.println(", dY: " + dY);
//		System.out.println("\tClickLocations[0]: (" + clickLocations.get(0).getX() + ", " + clickLocations.get(0).getY() + ")");
		
		GUIModel.getSelectedShape().setCenter(newCenter);
		GUIFunctions.refresh();
	}
	/**
	 * 
	 */
	private void handleDrawing(Point2D location)
	{
		// draw based on shape selection
		if(StateMachine.current == StateMachine.drawing)
		{
			switch (getButtonClicked())
			{
				case GUIController.colorButton:
					break;
				case GUIController.lineButton:
					createNewLine(location);
					break;
				case GUIController.squareButton:
					try
					{
						if(clickLocations.size() > 0)
						{
							createNewSquare(location);
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					break;
				case GUIController.rectangleButton:
					createNewRectangle(location);
					break;
				case GUIController.circleButton:
					createNewCircle(location);
					break;
				case GUIController.ellipseButton:
					createNewEllipse(location);
					break;
				case GUIController.triangleButton:
					try
					{
						createNewTriangle(location);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					break;
				case GUIController.selectButton:
					StateMachine.current = StateMachine.selectShape;
					break;
				default:
			}
		}
	}
	void handleRotation(MouseEvent e, Circle handle)
	{
		GUIModel.getSelectedShape().setRotation(calculateAngleOfRotation(e, handle));
	}
	
	boolean shapeClicked(Double clickLocation)
	{
		// deselect current clicked shape if there is one, then return
		if(GUIModel.getSelectedShape() != null)	
		{
			GUIModel.setSelectedShape(null);			
			return false;
		}
		else	// if not, check for a clicked shape
		{
			Shape s = GUIController.getModel().getClickedShape(clickLocation);
			if(s != null)
			{
				GUIModel.setSelectedShape(s);
				GUIFunctions.refresh();
				return true;
			}
			
			GUIModel.setSelectedShape(null);
			GUIFunctions.refresh();
			return false;
		}
	}
	void saveCenter(MouseEvent e)
	{
		GUIModel.setSelectedCenter(GUIModel.getSelectedShape().getCenter());
	}
	
	private void createNewLine(Point2D mouseLoc)
	{		
		if(clickLocations.size() == 2)
		{
			setCurrentShape((Shape) new Line(getCurrentColor() , (Point2D.Double) clickLocations.get(0), (Point2D.Double) clickLocations.get(1)));
			drawingCompleteFlag = true;
			System.out.println("Line complete.");
		}
		else if(clickLocations.size() > 0)
		{
			setCurrentShape((Shape) new Line(getCurrentColor() , (Point2D.Double) clickLocations.get(0), (Point2D.Double) mouseLoc));
		}
	}
	private void createNewSquare(Point2D mouseLoc) throws IOException
	{
		boolean xAxis 	= false;
		boolean yAxis 	= false;
		double 	xDif 	= 0;
		double 	yDif 	= 0;
		double 	dist	= 0;
		
		if(clickLocations.size() == 0)
		{
			throw new IOException();
		}

		Point2D.Double upperLeft = new Point2D.Double(clickLocations.get(0).getX(), clickLocations.get(0).getY());
		Point2D.Double refPoint = null;
		
		if(clickLocations.size() == 1)
		{
			refPoint = (Double) mouseLoc;
		}
		if(clickLocations.size() == 2)
		{
			refPoint = new Point2D.Double(clickLocations.get(1).getX(), clickLocations.get(1).getY());
			drawingCompleteFlag = true;
		}
		
		xDif = refPoint.getX() - upperLeft.getX();
		yDif = refPoint.getY() - upperLeft.getY();
		
		if((xDif < 0) ? xAxis = true : (xAxis = false)){ }	// for code obfuscation.
		if((yDif < 0) ? yAxis = true : (yAxis = false)){ }	//  actually, just kidding. this checks which quadrant to draw the square in.
		
		if(Math.abs(yDif) <= Math.abs(xDif))
		{
			dist = Math.abs(xDif);
		}
		else
		{
			dist = Math.abs(yDif);
		}

		if(xAxis)
		{
			upperLeft.setLocation(upperLeft.getX() - dist, upperLeft.getY());
		}
		if(yAxis)
		{
			upperLeft.setLocation(upperLeft.getX(), upperLeft.getY() - dist);			
		}
		
		Point2D.Double center = new Point2D.Double(upperLeft.x + dist/2, upperLeft.y + dist/2);		
		setCurrentShape((Shape) new Square(getCurrentColor(), center, dist));
	}
	private void createNewRectangle(Point2D mouseLoc)
	{
		boolean xAxis 	= false;
		boolean yAxis 	= false;
		double 	xDif 	= 0;
		double 	yDif 	= 0;
		
		if(clickLocations.size() == 0)
		{
			return;
		}

		Point2D.Double upperLeft = new Point2D.Double(clickLocations.get(0).getX(), clickLocations.get(0).getY()) ;
		Point2D.Double refPoint = null;
		
		if(clickLocations.size() == 1)
		{
			refPoint = (Double) mouseLoc;
		}
		if(clickLocations.size() == 2)
		{
			refPoint = new Point2D.Double(clickLocations.get(1).getX(), clickLocations.get(1).getY());
			drawingCompleteFlag = true;
		}
		
		xDif = refPoint.getX() - upperLeft.getX();
		yDif = refPoint.getY() - upperLeft.getY();
		
		if((xDif < 0) ? xAxis = true : (xAxis = false)){ }	// for code obfuscation.
		if((yDif < 0) ? yAxis = true : (yAxis = false)){ }	//
		
		if(xAxis)
		{
			upperLeft.setLocation(mouseLoc.getX() - xDif, upperLeft.getY());
		}
		if(yAxis)
		{
			upperLeft.setLocation(upperLeft.getX(), mouseLoc.getY() - yDif);
		}
		
		Point2D.Double center = new Point2D.Double(upperLeft.x + xDif/2, upperLeft.y + yDif/2);		
		setCurrentShape((Shape) new Rectangle(getCurrentColor(), center, Math.abs(xDif), Math.abs(yDif)));
	}
	private void createNewCircle(Point2D mouseLoc)
	{
		boolean xAxis 	= false;
		boolean yAxis 	= false;
		double 	xDif 	= 0;
		double 	yDif 	= 0;
		double 	dist	= 0;
		
		if(clickLocations.size() == 0)
		{
			return;
		}

		Point2D.Double upperLeft = new Point2D.Double(clickLocations.get(0).getX(), clickLocations.get(0).getY()) ;
		Point2D.Double refPoint = null;
		Point2D.Double center 	= null;
		
		if(clickLocations.size() == 1)
		{
			refPoint = (Double) mouseLoc;
		}
		if(clickLocations.size() == 2)
		{
			refPoint = new Point2D.Double(clickLocations.get(1).getX(), clickLocations.get(1).getY());
			drawingCompleteFlag = true;
		}
		
		
		xDif = refPoint.getX() - upperLeft.getX();
		yDif = refPoint.getY() - upperLeft.getY();
		
		if((xDif < 0) ? xAxis = true : (xAxis = false)){ }	// for code obfuscation.
		if((yDif < 0) ? yAxis = true : (yAxis = false)){ }	//
		
		dist =  Math.sqrt((xDif * xDif) + (yDif * yDif)) / (2.0);	// begin singing the Pythagorean theorem song.
		
		if(xAxis)
		{
			upperLeft.setLocation(upperLeft.getX() - (dist*2), upperLeft.getY());
		}
		if(yAxis)
		{
			upperLeft.setLocation(upperLeft.getX(), upperLeft.getY() - (dist*2));			
		}
		
		center = new Point2D.Double(upperLeft.getX() + dist, upperLeft.getY() + dist);
		setCurrentShape((Shape) new Circle(getCurrentColor(), center, dist));
	}
	private void createNewEllipse(Point2D mouseLoc)
	{
		boolean xAxis 	= false;
		boolean yAxis 	= false;
		double 	xDif 	= 0;
		double 	yDif 	= 0;
		
		if(clickLocations.size() == 0)
		{
			return;
		}

		Point2D.Double upperLeft = new Point2D.Double(clickLocations.get(0).getX(), clickLocations.get(0).getY()) ;
		Point2D.Double refPoint = null;
		Point2D.Double center 	= null;
		
		if(clickLocations.size() == 1)
		{
			refPoint = (Double) mouseLoc;
		}
		if(clickLocations.size() == 2)
		{
			refPoint = new Point2D.Double(clickLocations.get(1).getX(), clickLocations.get(1).getY());
			drawingCompleteFlag = true;
		}
		
		
		xDif = refPoint.getX() - upperLeft.getX();
		yDif = refPoint.getY() - upperLeft.getY();
		
		if((xDif < 0) ? xAxis = true : (xAxis = false)){ }	// for code obfuscation.
		if((yDif < 0) ? yAxis = true : (yAxis = false)){ }	//
				
		if(xAxis)
		{
			upperLeft.setLocation(upperLeft.getX() - Math.abs(xDif), upperLeft.getY());
		}
		if(yAxis)
		{
			upperLeft.setLocation(upperLeft.getX(), upperLeft.getY() - Math.abs(yDif));			
		}
		
		center = new Point2D.Double(upperLeft.getX() + Math.abs(xDif/2), upperLeft.getY() + Math.abs(yDif/2));
		setCurrentShape((Shape) new Ellipse(getCurrentColor(),center,Math.abs(xDif),Math.abs(yDif)));
	}
	private void createNewTriangle(Point2D mouseLoc) throws IOException
	{
		Point2D.Double p3 = null;
		switch(clickLocations.size())
		{
			case 0:
				return;
			case 1:
				createNewLine(mouseLoc);
				return;
			case 2:
				p3 = (Double) mouseLoc;
				break;
			case 3:
				p3 = (Double) clickLocations.get(2);
				drawingCompleteFlag = true;
				break;
			default:
				return;
		}
		
		Point2D.Double center = Triangle.calculateCenter((Double) clickLocations.get(0), (Double) clickLocations.get(1), p3);
		
		setCurrentShape((Shape) new Triangle(getCurrentColor(), center, (Double) clickLocations.get(0), (Double) clickLocations.get(1), p3));
	}

	private void submitShape()
	{
		if(getCurrentShape() != null)
		{
			int modelSize 	= GUIController.getModel().getShapes().size();
			int newSize 	= GUIController.getModel().addShape(getCurrentShape());
			setCurrentShape(null);
			
			if(modelSize == newSize)
			{
				System.out.println("Error: Model size did not change.");
			}
		}
		else
		{
			System.out.println("Error? There's no current shape.");
		}
	}
	
	Circle rotationHandleClicked(MouseEvent e)
	{
		if(GUIModel.getSelectedShape() == null)
		{
			return null;
		}
		Rectangle r = GUIModel.getSelectedShape().getBoundingBox();
		Point2D.Double p = new Point2D.Double(r.getUpperLeft().getX()-10, r.getUpperLeft().getY()-10);
		Circle handle = new Circle(Color.WHITE, p, 5);
		
		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());
		
		if(handle.pointInShape(clickLocation, 0))
		{
			return handle;
		}
		
		return null;
	}
	void clearDrawingInfo()
	{
		clickLocations.clear();
		System.out.println("Click locations cleared.");
		printState = true;
	}
	
	public static Color getCurrentColor()	
	{
		return currentColor;
	}
	public static int getButtonClicked()	
	{
		return buttonClicked;
	}
	public static Shape getCurrentShape()
	{
		return currentShape;
	}

	private double calculateAngleOfRotation(MouseEvent e, Circle handle)
	{
		Line2D vectorToHandle = new Line2D.Double(handle.getCenter(), GUIModel.getSelectedCenter());
		Line2D vectorToMouse  = new Line2D.Double(e.getPoint(), GUIModel.getSelectedCenter());
		
	    double angle1 = Math.atan2(vectorToHandle.getY1() - vectorToHandle.getY2(),
	                               vectorToHandle.getX1() - vectorToHandle.getX2());
	    double angle2 = Math.atan2(vectorToMouse.getY1() - vectorToMouse.getY2(),
	                               vectorToMouse.getX1() - vectorToMouse.getX2());
	    return angle1-angle2;
	}

	public static void setButtonClicked(int buttonClicked)	
	{
		StateMachine.buttonClicked = buttonClicked;
		buttonClickedFlag = true;
	}
	public static void setCurrentShape(Shape shape)			
	{
		currentShape = shape;
	}
	public static void setCurrentColor(Color currentColor)	
	{
		StateMachine.currentColor = currentColor;
	}

	public static Point2D.Double getCurrentMouseLocation()
	{
		return currentMouseLocation;
	}

	public static void setCurrentMouseLocation(Point2D.Double currentMouseLocation)
	{
		StateMachine.currentMouseLocation = currentMouseLocation;
	}
	int registerClick(Point2D.Double p)
	{
		clickLocations.add(p);
		clickLocation = p;
//		System.out.println("New click count: " + clickLocations.size());
//		for(Point2D q : clickLocations)
//		{
//			System.out.println("P.x: " + q.getX());
//			System.out.println("\tP.y: " + q.getY());
//		}
		return clickLocations.size();
	}
}
