package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

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
	static boolean  buttonClickedFlag;
	static boolean  mouseDraggedFlag;
	static boolean  mouseMovedFlag;
	static private boolean	mouseClickedFlag;
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
	
	static MouseEvent e;

	private static Double viewOrigin;
	Point2D.Double clickLocation;

	// translation offset
	private Vector<Object> 	moveOffset;
	private Point2D.Double	rotationOffset;

	private boolean noTransition;

	///////////////////////////////////////////////////////
	// initializer

	StateMachine()
	{
		current = init;
		setButtonClicked(GUIController.selectButton);

		currentShape = null;
		
		
		startDrawingFlag		=
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
		
		moveOffset				= new Vector<Object>();
		rotationOffset			= null;
		setViewOrigin(new Point2D.Double(0, 0));
	}

	// initializer
	///////////////////////////////////////////////////////
	//	void tick()

	void tick()
	{
		if(e != null)
		{
			if(mouseClickedFlag || mousePressedFlag)
			{
				if(StateMachine.current == StateMachine.rotate)
				{
					
				}
				else
				{
//					clickLocation = new Point2D.Double(e.getX(), e.getY());
				}
			}
			
			Point2D.Double loc = new Point2D.Double(e.getX(), e.getY());
			
			loc = (Double) StateMachine.viewToWorld(StateMachine.getViewOrigin()).transform(loc, null);
			setCurrentMouseLocation(loc);
		}

		if(printState && debug)
		{
//			GUIFunctions.printf("Current state: " + StateMachine.current);
//			System.out.println("Current state: " + StateMachine.current);
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
				break;
			case StateMachine.haveShape:
				if(buttonClickedFlag && (buttonClicked == GUIController.colorButton))
				{
					
				}
				break;
			case StateMachine.move:
				break;
			case StateMachine.rotate:
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
				else if(mouseClickedFlag && GUIModel.getSelectedShape() != null)
				{
					if(!noTransition)
					{
						StateMachine.current = StateMachine.move;
						clickLocation 	= null;
						moveOffset		= null;
						printState		= true;
					}
					else
					{
						noTransition = false;
					}
				}
				if(buttonClicked != GUIController.selectButton)
				{
					StateMachine.current = StateMachine.init;
					GUIModel.setSelectedShape(null);
					printState		= true;
				}
				else if(GUIModel.getSelectedShape() == null)
				{
					StateMachine.current = StateMachine.selectShape;
					printState		= true;
				}
				break;
			case StateMachine.move:
				if(mouseClickedFlag)
				{
					StateMachine.current = StateMachine.haveShape;
					printState		 = true;
					clickLocation	 = null;
					moveOffset		 = null;
				}
				break;
			case StateMachine.rotate:
				if(mouseClickedFlag)
				{
					StateMachine.current = StateMachine.haveShape;
					rotationFlag 	= false;
					printState		= true;
					rotationOffset	= null;
				}
				break;

			default:
				break;
		}
		
		if(mouseClickedFlag)	// TODO: this could be optimized into a general handler for lowering all necessary flags
		{
			mouseClickedFlag = false;
		}
		
		GUIFunctions.refresh();
	}

	// tick function
	///////////////////////////////////////////////////////
	//		Shape selection and manipulation

	boolean shapeClicked(Double clickLocation)
	{
		boolean checkClickOnOtherShape = false;
		if(GUIModel.getSelectedShape() != null)
		{
			checkClickOnOtherShape = true;
		}
		
		Shape s = GUIController.getModel().getClickedShape(clickLocation);
		if(s != null)
		{
			if(checkClickOnOtherShape)
			{
				if(!s.equals(GUIModel.getSelectedShape()))
				{
					noTransition = true;
				}
			}
			
			GUIModel.setSelectedShape(s);
			GUIFunctions.refresh();
			
			return true;
		}
		
		GUIModel.setSelectedShape(null);
		GUIFunctions.refresh();
		return false;
	}
	
	private boolean doInitMove()
	{
		if(clickLocation == null || moveOffset == null)
		{
			return true;
		}
		else if(moveOffset.isEmpty())
		{
			return true;
		}
		return false;
	}	
	private boolean doInitRotate()
	{
		if(clickLocation == null || rotationOffset == null)
		{
			return true;
		}
		return false;
	}
	
	void handleMove(Point2D.Double p)
	{
		if(GUIModel.getSelectedShape() == null)
		{
			// TODO: this shouldn't be necessary! What's going on here?
			return;
		}
		// calculate offset if necessary. Store the initial position as well.
		if(doInitMove())
		{
			clickLocation 	= p;
			moveOffset		= new Vector<Object>();
			moveOffset.addElement(clickLocation.getX()-GUIModel.getSelectedShape().getCenter().getX());
			moveOffset.addElement(clickLocation.getY()-GUIModel.getSelectedShape().getCenter().getY());
		}
		// calculate dX and dY
		double dX = (p.getX() - clickLocation.getX());
		double dY = (p.getY() - clickLocation.getY());
		
		// new center = initial click + dY + dX - offset
		Point2D.Double newCenter = (Double) new Point2D.Double(clickLocation.getX() + dX - (double) moveOffset.get(0),
																clickLocation.getY() + dY - (double) moveOffset.get(1));
		// assign new location and refresh
		GUIModel.getSelectedShape().setCenter(newCenter);
		GUIFunctions.refresh();
	}	
	void handleRotation(Point2D.Double p)
	{
		if(GUIModel.getSelectedShape() == null)	// this shouldn't be necessary and could be optimized out.
		{
			return;
		}
		
		// calculate offset if necessary. Store the initial position as well.
		if(doInitRotate())
		{
			clickLocation 	= new Point2D.Double(p.getX(), p.getY());
			rotationOffset	= clickLocation;
		}
		// calculate dTheta
		double dTheta 		= calculateAngleOfRotation(p);
		
		// assign new rotation value and refresh
		GUIModel.getSelectedShape().setRotation(dTheta);
		GUIFunctions.refresh();
	}
	
	Circle rotationHandleClicked(MouseEvent e)
	{
		if(GUIModel.getSelectedShape() == null)
		{
			return null;
		}

	//create a point as the center of a new circle, then make circle
		Circle handle = GUIModel.getSelectedShape().getHandle();
	//create a click for the mouse event
		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());		
	//check if click is within handle
		if(handle.pointInShape(clickLocation, 0))
		{
			return handle;
		}
		
		return null;
	}
	private double calculateAngleOfRotation(Point2D p1)
	{
		Line2D vectorToHandle = new Line2D.Double(GUIModel.getSelectedShape().getBoundingBox().getUpperLeft(), GUIModel.getSelectedCenter());
		Line2D vectorToMouse  = new Line2D.Double(p1, GUIModel.getSelectedCenter());

		
	    double angle1 = Math.atan2(vectorToHandle.getY1() - vectorToHandle.getY2(),
	                               vectorToHandle.getX1() - vectorToHandle.getX2());
	    double angle2 = Math.atan2(vectorToMouse.getY1() - vectorToMouse.getY2(),
	                               vectorToMouse.getX1() - vectorToMouse.getX2());
	    
	    return angle2-angle1;
	}
		
	//		Shape selection and manipulation
	///////////////////////////////////////////////////////
	//				Shape creation
	
	private void createNewLine(Point2D mouseLoc)
	{		
		if(clickLocations.size() == 2)
		{
			setCurrentShape((Shape) new Line(getCurrentColor() , (Point2D.Double) clickLocations.get(0), (Point2D.Double) clickLocations.get(1)));
			drawingCompleteFlag = true;
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

	//				Shape creation
	///////////////////////////////////////////////////////
	//				Drawing
	
	int registerClick(Point2D.Double p)
	{
		clickLocations.add(p);
		clickLocation = p;

		return clickLocations.size();
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
	static void clearDrawingInfo()
	{
		clickLocations.clear();
		printState = true;
	}

	//				Drawing
	///////////////////////////////////////////////////////
	//				Getters and setters

	public static int 	getButtonClicked()	
	{
		return buttonClicked;
	}
	public static void setButtonClicked(int buttonClicked)	
	{
		StateMachine.buttonClicked = buttonClicked;
		buttonClickedFlag = true;
	}

	public static Color getCurrentColor()	
	{
		return currentColor;
	}
	public static void setCurrentColor(Color currentColor)	
	{
		StateMachine.currentColor = currentColor;
		if(StateMachine.current == StateMachine.haveShape)
		{
			GUIModel.getSelectedShape().setColor(currentColor);
		}
	}

	public static Shape getCurrentShape()
	{
		return currentShape;
	}
	public static void setCurrentShape(Shape shape)			
	{
		currentShape = shape;
	}

	public static Point2D.Double getCurrentMouseLocation()
	{
		return currentMouseLocation;
	}
	public static void setCurrentMouseLocation(Point2D.Double c)
	{
		currentMouseLocation = c;
	}
	
	//				Getters and setters
	///////////////////////////////////////////////////////
	//				Model Communication
	
	private void submitShape()
	{
		if(getCurrentShape() != null)
		{
			int modelSize 	= GUIController.getModel().getShapes().size();
			int newSize 	= GUIController.getModel().addShape(getCurrentShape());
			setCurrentShape(null);
		}
	}
	
	//				Model Communication
	///////////////////////////////////////////////////////
	//				Flags

	public static boolean doRotation()
	{
		return rotationFlag;
	}
	public static void setRotationFlag(boolean val)
	{
		rotationFlag = val;
	}

	public static boolean mouseClicked()
	{
		return mouseClickedFlag;
	}
	public static void setMouseClickedFlag(boolean mouseClickedFlag)
	{
		StateMachine.mouseClickedFlag = mouseClickedFlag;
	}

	public static void lowerFlags()
	{
		setRotationFlag(false);
	}

	//				Flags
	///////////////////////////////////////////////////////
	//				Transforms
	//	all of the base transforms (scale, translate, rotate,
	//	and their opposites) are implemented as required.
	//	The rest are created using .concatenate().
	
	public static AffineTransform objectToView(Shape s) 				// Mi
	{
		AffineTransform Mi = new AffineTransform();
		Mi.setToIdentity();
		Mi.concatenate(worldToView(getViewOrigin()));
		Mi.concatenate(objectToWorld(s));
		return Mi;
	}	
	public static AffineTransform worldToView(Point2D.Double origin)	// V
	{
		AffineTransform V = new AffineTransform();
		V.setToIdentity();
		V.concatenate(scale((GUIController.getZoomLevel())));
		V.concatenate(translate(new Point2D.Double(origin.getX(), origin.getY())));
		
		return V;
	}
	public static AffineTransform scale(int zoomLevel)					// S(f)
	{
		double zoomFactor = 1;
		if(zoomLevel > 9)
		{
			zoomFactor /= 2;
			if(zoomLevel==11)
			{
				zoomFactor /= 2;				
			}
		}
		else if(zoomLevel < 9)
		{
			zoomFactor *= 2;
			if(zoomLevel==7)
			{
				zoomFactor *= 2;				
			}
		}
		
		AffineTransform S = new AffineTransform(zoomFactor, 0, 0, zoomFactor, 0, 0);		
		return S;
	}
	public static AffineTransform translate(Point2D.Double p)			// T(p)
	{
		AffineTransform T = new AffineTransform(1, 0, 0, 1, p.getX(), p.getY());
		return T;
	}
	public static AffineTransform objectToWorld(Shape s)				// Oi
	{
		AffineTransform Oi = new AffineTransform();
		Oi.concatenate(translate(new Point2D.Double(s.getCenter().getX(), s.getCenter().getY())));
		Oi.concatenate(rotate(s.getRotation()));

		return Oi;
	}
	public static AffineTransform rotate(double r)						// R(theta)
	{
		AffineTransform R = new AffineTransform(Math.cos(r), Math.sin(r), -Math.sin(r), Math.cos(r), 0, 0);
		return R;
	}

	public static AffineTransform viewToObject(Shape s)
	{
		AffineTransform Mi = new AffineTransform();
		Mi.setToIdentity();
		Mi.concatenate(worldToObject(s));
		Mi.concatenate(viewToWorld(getViewOrigin()));
		return Mi;
	}
	public static AffineTransform viewToWorld(Point2D.Double origin)
	{
		AffineTransform V = new AffineTransform();
		V.setToIdentity();
		V.concatenate(unTranslate(new Point2D.Double(-origin.getX(), -origin.getY())));
		V.concatenate(unScale(GUIController.getZoomLevel()));
		
		return V;
	}
	public static AffineTransform unScale(int zoomLevel)
	{
		double zoomFactor = 1;
		if(zoomLevel > 9)
		{
			zoomFactor /= 2;
			if(zoomLevel==11)
			{
				zoomFactor /= 2;				
			}
		}
		else if(zoomLevel < 9)
		{
			zoomFactor *= 2;
			if(zoomLevel==7)
			{
				zoomFactor *= 2;				
			}
		}
		
		AffineTransform S = new AffineTransform(1/zoomFactor, 0, 0, 1/zoomFactor, 0, 0);		
		return S;
	}
	public static AffineTransform unTranslate(Point2D.Double p)
	{
		// this function redirects to translate, since they really do the same thing.
		// it just makes things a bit clearer for the code
		return translate(p);
	}
	public static AffineTransform worldToObject(Shape s)
	{
		AffineTransform Oi = new AffineTransform();
		Oi.concatenate(unRotate(s.getRotation()));
		Oi.concatenate(unTranslate(new Point2D.Double(-s.getCenter().getX(), -s.getCenter().getY())));

		return Oi;
	}
	public static AffineTransform unRotate(double r)
	{
		AffineTransform R = new AffineTransform(Math.cos(r), -Math.sin(r), Math.sin(r), Math.cos(r), 0, 0);
		return R;
	}
	
	public static Double getViewOrigin()
	{
		return viewOrigin;
	}
	public static void setViewOrigin(Double viewOrigin)
	{
		StateMachine.viewOrigin = viewOrigin;
	}
	
	//				Transforms
	///////////////////////////////////////////////////////


}
