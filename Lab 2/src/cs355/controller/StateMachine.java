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
	static final String haveShape 			= "haveShape";
	static final String move 				= "move";
	static final String rotate 				= "rotate";

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
	private static boolean rotationFlag;

	static private boolean debug		= true;
	static private boolean printState;
	
	// state machine variables	
	static private Shape	currentShape;
	static private Color	currentColor;	
	static private int		buttonClicked;
	static ArrayList<Point2D> 	clickLocations;	
	static private Point2D.Double		currentMouseLocation;
	static private Circle	rotationHandle;
	
	static MouseEvent e;
	Point2D.Double clickLocation;

	// translation offset
	private double dX;
	private double dY;
	
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
				break;
			case StateMachine.selectShape:
				handleShapeOperations(getCurrentMouseLocation());
				break;
			case StateMachine.haveShape:
				break;
			case StateMachine.move:
				handleMove(e);
				break;
			case StateMachine.rotate:
				handleRotation(e);
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
					printState		= true;
				}
				if(GUIModel.getSelectedShape() != null)
				{
					StateMachine.current = StateMachine.haveShape;
					printState		= true;
				}
				break;
			case StateMachine.haveShape:
				if(rotationFlag)
				{
					StateMachine.current = StateMachine.rotate;
					printState		= true;
				}
				if(mouseClickedFlag && GUIModel.getSelectedShape() != null)
				{
					mouseClickedFlag = false;
					StateMachine.current = StateMachine.move;
					printState		= true;
				}
				if(buttonClicked != GUIController.selectButton)
				{
					StateMachine.current = StateMachine.init;
					GUIModel.setSelectedCenter(null);
					GUIModel.setSelectedShape(null);
					printState		= true;
				}
				if(GUIModel.getSelectedShape() == null)
				{
					StateMachine.current = StateMachine.init;
					printState		= true;
				}
				break;
			case StateMachine.move:
				if(mouseClickedFlag)
				{
					if(GUIController.getModel().getClickedShape(clickLocation) == GUIModel.getSelectedShape())
					{
						StateMachine.current = StateMachine.rotate;
					}
					else
					{
						StateMachine.current = StateMachine.haveShape;						
					}
					printState		 = true;
					mouseClickedFlag = false;
				}
				break;
			case StateMachine.rotate:
				if(!rotationFlag)
				{
//					StateMachine.current = StateMachine.haveShape;
					printState		= true;
				}
				if(mouseClickedFlag)
				{
					mouseClickedFlag = false;
					rotationFlag 	 = false;
					if(GUIController.getModel().getClickedShape(clickLocation) == GUIModel.getSelectedShape())
					{
						StateMachine.current = StateMachine.move;
					}
					else
					{
						StateMachine.current = StateMachine.haveShape;						
					}
					printState		 = true;
				}
				break;

			default:
				break;
		}
		GUIFunctions.refresh();
	}

//	end tick function

	private void handleShapeOperations(Double currentMouseLocation)
	{
				
	}
	void handleMove(MouseEvent e)
	{
		if(!isRotationFlag())
		{
			AffineTransform t = new AffineTransform();
			if(dX == dY && dX == 0)
			{
				dX = (e.getX() - clickLocations.get(0).getX());
				dY = (e.getY() - clickLocations.get(0).getY());
			}
			t.translate(dX, dY);
			
			Point2D.Double newCenter = (Double) t.transform(GUIModel.getSelectedCenter(), null);
			
			GUIModel.getSelectedShape().setCenter(newCenter);
			GUIFunctions.refresh();
		}
		else
		{
			GUIFunctions.printf("Entered else statement of handleMove.");
			dX = dY = 0;
		}
	}
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
	void handleRotation(MouseEvent e)
	{
		GUIModel.getSelectedShape().setRotation(calculateAngleOfRotation(e, rotationHandle));
		GUIFunctions.refresh();
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
				
		setCurrentShape((Shape) new Triangle(getCurrentColor(), null, (Double) clickLocations.get(0), (Double) clickLocations.get(1), p3));
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
		if(GUIModel.getSelectedShape() == null || e.getClickCount() == 0)
		{
			return null;
		}

		Rectangle r = GUIModel.getSelectedShape().getBoundingBox();
		Point2D.Double p = new Point2D.Double(r.getUpperLeft().getX()-10, r.getUpperLeft().getY()-10);
		Circle handle = new Circle(Color.WHITE, p, 20);
		
		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());
		
		if(handle.pointInShape(clickLocation, 0))
		{
			return handle;
		}
		
		return null;
	}
	static void clearDrawingInfo()
	{
		clickLocations.clear();
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
		if(rotationHandle == null)
		{
			rotationHandle = GUIModel.getSelectedShape().getHandle();
		}
		
		Line2D vectorToHandle = new Line2D.Double(rotationHandle.getCenter(), GUIModel.getSelectedCenter());
		Line2D vectorToMouse  = new Line2D.Double(e.getPoint(), GUIModel.getSelectedCenter());
		
	    double angle1 = Math.atan2(vectorToHandle.getY1() - vectorToHandle.getY2(),
	                               vectorToHandle.getX1() - vectorToHandle.getX2());
	    double angle2 = Math.atan2(vectorToMouse.getY1() - vectorToMouse.getY2(),
	                               vectorToMouse.getX1() - vectorToMouse.getX2());
	    
	    return angle2-angle1;
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

	public static void setCurrentMouseLocation(Point2D.Double c)
	{
		currentMouseLocation = c;
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

	public static boolean isRotationFlag() {
		return rotationFlag;
	}

	public static void setRotationFlag(boolean val) {
		rotationFlag = val;
	}

	public void setRotationHandle(Circle handle)
	{
		rotationHandle = handle;
	}

	public static void lowerFlags()
	{
		setRotationFlag(false);
	}
}
