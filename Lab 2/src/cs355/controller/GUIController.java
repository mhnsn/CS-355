package cs355.controller;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import cs355.GUIFunctions;
import cs355.model.drawing.CS355Drawing;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.GUIModel;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;
import cs355.view.GUIViewRefresher;

public class GUIController implements CS355Controller, MouseListener, MouseMotionListener
{
	static final int noButton			= -1;
	static final int colorButton		= 1;
	static final int lineButton			= 2;
	static final int squareButton		= 3;
	static final int rectangleButton	= 4;
	static final int circleButton		= 5;
	static final int ellipseButton		= 6;
	static final int triangleButton		= 7;
	static final int selectButton		= 8;
	static final int zoomInButton		= 9;
	static final int zoomOutButton		= 10;

	// state flags
	static private boolean	startDrawing;
	static private boolean	drawingComplete;
	static private boolean 	selectButtonPressed;

	static private Shape	currentShape;
	static private Color	currentColor;
	
	@SuppressWarnings("unused")
	static private State 	state;
	static private int 		buttonClicked;
	static private Point2D.Double		currentMouseLocation;
	static private ArrayList<Point2D> 	clickLocations;
	
	static private boolean debug		= true;
	static private boolean printState;
	
	@SuppressWarnings("unused")
	static private GUIViewRefresher viewRefresher;
	static private GUIModel			model;

	public GUIController()
	{
		new State();
		State.current	= State.init;
		buttonClicked	= lineButton;
		drawingComplete = false;
		clickLocations	= new ArrayList<Point2D>();
		setCurrentShape(null);
		setCurrentColor(new Color(255, 255, 0));
		printState 		= false;
	}
	
	public void setModelView(GUIModel m, GUIViewRefresher vr)
	{
		model = m;
		viewRefresher = vr;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		switch (State.current)
		{
			case State.selectShape:
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
				break;
			default:
				mouseMoved(e);
				break;
		}
	}
	
	private void handleRotation(MouseEvent e, Circle handle)
	{
		GUIModel.getSelectedShape().setRotation(calculateAngleOfRotation(e, handle));
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

	private Circle rotationHandleClicked(MouseEvent e)
	{
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

	@Override
	public void mouseMoved(MouseEvent e)
	{
		currentMouseLocation = new Point2D.Double(e.getX(), e.getY());
		handleStateMachine();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());
				
		if(State.current == State.init)
		{
			startDrawing = true;
			clickLocations.add(clickLocation);
		}
		
		handleStateMachine();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());

		if(State.current == State.selectShape)
		{
			if(shapeClicked(clickLocation))
			{
				clickLocations.clear();
				clickLocations.add(clickLocation);
				saveCenter(e);
			}
		}
	}

	private void saveCenter(MouseEvent e)
	{
		GUIModel.setSelectedCenter(GUIModel.getSelectedShape().getCenter());
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	private boolean shapeClicked(Double clickLocation)
	{
		Shape s = model.getClickedShape(clickLocation);
		if(s != null)
		{
			GUIModel.setSelectedShape(s);
//			System.out.println("\t" + s.toString());
			GUIFunctions.refresh();
			return true;
		}
		
		GUIModel.setSelectedShape(null);
		GUIFunctions.refresh();
		return false;
	}

	/**
	 * 
	 */
	private void handleStateMachine()
	{
		if(printState && debug)
		{
			GUIFunctions.printf("Current state: " + State.current);
			System.out.println("Current state: " + State.current);

			printState = false;
		}
		
		// perform state actions
		switch(State.current)
		{
			case State.init:
				break;
			case State.drawing:
				handleDrawing(currentMouseLocation);
				GUIFunctions.refresh();
				break;
			case State.selectShape:
				handleShapeOperations(currentMouseLocation);
				break;
				default:			
		}
		
		// update state
		switch(State.current)
		{
			case State.init:
				if(startDrawing)
				{
					State.current 	= State.drawing;
					startDrawing 	= false;
					printState 		= true;
				}
				if(selectButtonPressed)
				{
					State.current	= State.selectShape;
					selectButtonPressed	= false;
					printState		= true;
				}
				break;
			case State.drawing:
				if(drawingComplete)
				{
					State.current 	= State.init;
					submitShape();
					clearDrawingInfo();
					
					drawingComplete = false;
					printState		= true;
				}
				break;
			case State.selectShape:
				if(buttonClicked != selectButton)
				{
					State.current = State.init;
				}
				break;
				default:
		}
	}

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
	
	private void submitShape()
	{
		if(getCurrentShape() != null)
		{
			int modelSize 	= model.getShapes().size();
			int newSize 	= model.addShape(getCurrentShape());
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
	
	private void handleDrawing(Point2D location)
	{
		// draw based on shape selection
		if(State.current == State.drawing)
		{
			switch (buttonClicked)
			{
				case colorButton:
					break;
				case lineButton:
					createNewLine(location);
					break;
				case squareButton:
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
				case rectangleButton:
					createNewRectangle(location);
					break;
				case circleButton:
					createNewCircle(location);
					break;
				case ellipseButton:
					createNewEllipse(location);
					break;
				case triangleButton:
					try
					{
						createNewTriangle(location);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					break;
				case selectButton:
					State.current = State.selectShape;
					break;
				default:
			}
		}
	}
	
	private void createNewLine(Point2D mouseLoc)
	{		
		if(clickLocations.size() == 2)
		{
			setCurrentShape((Shape) new Line(getCurrentColor() , (Point2D.Double) clickLocations.get(0), (Point2D.Double) clickLocations.get(1)));
			drawingComplete = true;
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
			drawingComplete = true;
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
			drawingComplete = true;
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
			drawingComplete = true;
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
			drawingComplete = true;
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
				drawingComplete = true;
				break;
			default:
				return;
		}
				
		setCurrentShape((Shape) new Triangle(getCurrentColor(), (Double) clickLocations.get(0), (Double) clickLocations.get(1), p3));
	}


	@Override
	public void colorButtonHit(Color c)
	{
		clearDrawingInfo();
		setCurrentColor(c);
		GUIFunctions.changeSelectedColor(getCurrentColor());
	}
	
	@Override
	public void lineButtonHit() 		{	buttonClicked = lineButton;	 clearDrawingInfo();		}

	@Override
	public void squareButtonHit()		{	buttonClicked = squareButton; clearDrawingInfo();		}

	@Override
	public void rectangleButtonHit()	{	buttonClicked = rectangleButton; clearDrawingInfo();	}

	@Override
	public void circleButtonHit()		{	buttonClicked = circleButton; clearDrawingInfo();		}

	@Override
	public void ellipseButtonHit()		{	buttonClicked = ellipseButton; clearDrawingInfo();		}

	@Override
	public void triangleButtonHit()		{	buttonClicked = triangleButton; clearDrawingInfo();		}
	
	@Override
	public void selectButtonHit()		{	buttonClicked = selectButton; clearDrawingInfo();		}

	@Override
	public void zoomInButtonHit()		{	buttonClicked = zoomInButton; clearDrawingInfo();		}

	@Override
	public void zoomOutButtonHit()		{	buttonClicked = zoomOutButton; clearDrawingInfo();		}

	private void clearDrawingInfo()
	{
		clickLocations.clear();
		printState = true;
	}

	@Override
	public void hScrollbarChanged(int value)
	{
	}

	@Override
	public void vScrollbarChanged(int value)
	{
	}

	@Override
	public void openScene(File file)
	{
	}

	@Override
	public void toggle3DModelDisplay()
	{
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator)
	{
	}

	@Override
	public void openImage(File file)
	{
	}

	@Override
	public void saveImage(File file)
	{
	}

	@Override
	public void toggleBackgroundDisplay()
	{
	}

	@Override
	public void saveDrawing(File file)
	{
		model.save(file);
	}

	@Override
	public void openDrawing(File file)
	{
		model.open(file);
	}

	@Override
	public void doDeleteShape()
	{
		// TODO: this
	}

	@Override
	public void doEdgeDetection()
	{
		// TODO: this
	}

	@Override
	public void doSharpen()
	{
	}

	@Override
	public void doMedianBlur()
	{
	}

	@Override
	public void doUniformBlur()
	{
	}

	@Override
	public void doGrayscale()
	{
	}

	@Override
	public void doChangeContrast(int contrastAmountNum)
	{
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum)
	{
	}

	@Override
	public void doMoveForward()
	{
		// TODO: this
	}

	@Override
	public void doMoveBackward()
	{
		// TODO: this
	}

	@Override
	public void doSendToFront()
	{
		// TODO: this
	}

	@Override
	public void doSendtoBack()
	{
		// TODO: this
	}

	public static Color getCurrentColor()
	{
		return currentColor;
	}

	public static void setCurrentColor(Color currentColor)
	{
		GUIController.currentColor = currentColor;
		if(GUIModel.getSelectedShape() != null)
		{
			GUIModel.getSelectedShape().setColor(currentColor);
			GUIFunctions.refresh();
		}
	}

	public static Shape getCurrentShape()
	{
		return currentShape;
	}

	public static void setCurrentShape(Shape currentShape)
	{
		GUIController.currentShape = currentShape;
	}

}
