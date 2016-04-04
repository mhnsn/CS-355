package cs355.controller;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.util.Iterator;

import cs355.GUIFunctions;
import cs355.model.drawing.Circle;
import cs355.model.drawing.GUIModel;
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

	static private StateMachine 	state;	
	static private GUIViewRefresher viewRefresher;
	static private GUIModel			model;
	private static int zoomLevel;

	public GUIController()
	{
		state = new StateMachine();
		StateMachine.current	= StateMachine.init;
		zoomLevel = 9;

		StateMachine.setCurrentColor(new Color(255, 255, 0));
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
		StateMachine.mouseDraggedFlag = true;
		StateMachine.e = e;
		state.tick();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		StateMachine.mouseMovedFlag = true;

		Point2D.Double pt =	new Point2D.Double(e.getX(), e.getY());
		
		pt = (Point2D.Double) StateMachine.viewToWorld(StateMachine.getViewOrigin()).transform(pt, null);
		
		if(StateMachine.current == StateMachine.rotate)
		{
			state.handleRotation(pt);
		}
		else if(StateMachine.current == StateMachine.move)
		{
			state.handleMove(pt);
		}
		else if(StateMachine.current == StateMachine.haveShape)
		{
		}
		state.tick();
		GUIFunctions.refresh();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		StateMachine.setMouseClickedFlag(true);
//		StateMachine.e = e;
		Point2D.Double pt =	new Point2D.Double(e.getX(), e.getY());
//		state.registerClick((Point2D.Double) clickLocation);
//		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());
		Point2D.Double clickLocation = (Double) StateMachine.viewToWorld(StateMachine.getViewOrigin()).transform(pt, null);
		state.registerClick((Point2D.Double) clickLocation);
		
		switch(StateMachine.current)
		{
			case StateMachine.init:
				StateMachine.startDrawingFlag = true;
				break;
			case StateMachine.selectShape:
				if(state.shapeClicked(clickLocation))
				{
					StateMachine.clickLocations.clear();
				}
				break;
			case StateMachine.haveShape:
				//rotation check
				Circle handle = state.rotationHandleClicked(e);
				if(handle != null)	
				{
					StateMachine.setRotationFlag(true);
				}
				else
				{
					state.shapeClicked(clickLocation);
				}
				break;
			case StateMachine.drawing:
				break;
			default:
				state.shapeClicked(clickLocation);
				break;
		}
		state.tick();
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
		StateMachine.mouseEnteredFlag = true;
		StateMachine.mouseExitedFlag  = false;
		StateMachine.e = e;
		state.tick();
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
		StateMachine.mouseExitedFlag  = true;
		StateMachine.mouseEnteredFlag = false;
		StateMachine.clearDrawingInfo();
		StateMachine.lowerFlags();
		StateMachine.e = e;
		state.tick();
	}
	@Override
	public void mousePressed(MouseEvent e)
	{
		StateMachine.mousePressedFlag = true;
		StateMachine.e = e;
//		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());
//		if(StateMachine.current == StateMachine.selectShape)
//		{
//			Circle handle = state.rotationHandleClicked(e);
//			if(handle != null)
//			{
//				state.setRotationFlag(true);
//				state.setRotationHandle(handle);
//			}
//
//			if(state.shapeClicked(clickLocation))
//			{
//				StateMachine.clickLocations.clear();
//				StateMachine.clickLocations.add(clickLocation);
//				state.saveCenter(e);
//			}
//		}
		state.tick();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		StateMachine.mouseReleasedFlag = true;
		StateMachine.e = e;
		state.tick();
	}

	@Override
	public void colorButtonHit(Color c)	
	{
		StateMachine.clearDrawingInfo();
		StateMachine.setCurrentColor(c);
		GUIFunctions.changeSelectedColor(StateMachine.getCurrentColor());
		state.tick();
	}
	
	@Override
	public void lineButtonHit()			
	{
		StateMachine.setButtonClicked(lineButton);	 StateMachine.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void squareButtonHit()		
	{
		StateMachine.setButtonClicked(squareButton); StateMachine.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void rectangleButtonHit()	
	{
		StateMachine.setButtonClicked(rectangleButton); StateMachine.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void circleButtonHit()		
	{
		StateMachine.setButtonClicked(circleButton); StateMachine.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void ellipseButtonHit()		
	{
		StateMachine.setButtonClicked(ellipseButton); StateMachine.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void triangleButtonHit()		
	{
		StateMachine.setButtonClicked(triangleButton); StateMachine.clearDrawingInfo();
		state.tick();
	}
	
	@Override
	public void selectButtonHit()		
	{
		StateMachine.setButtonClicked(selectButton);
		StateMachine.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void zoomInButtonHit()		
	{
		StateMachine.setButtonClicked(zoomInButton);
		zoomIn();
		StateMachine.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void zoomOutButtonHit()		
	{
		StateMachine.setButtonClicked(zoomOutButton);
		zoomOut();
		StateMachine.clearDrawingInfo();
		state.tick();
	}
	
	private void zoomIn()
	{
		if(zoomLevel >= 8)
		{
			zoomLevel -= 1;
		}
				
		GUIViewRefresher.setScrollBars(zoomLevel);
	}

	private void zoomOut()
	{
		if(zoomLevel <= 10)
		{
			zoomLevel += 1;
		}

		// TODO: handle case of zooming out enabling user to see outside of viewport
//		if((int) Math.pow(2, zoomLevel) + (int) StateMachine.getViewOrigin().getX() > 2048)
//		{
//			StateMachine.setViewOrigin(new Point2D.Double( 2048 - Math.pow(2, zoomLevel), StateMachine.getViewOrigin().getY()));
//		}
//		if((int) Math.pow(2, zoomLevel) + (int) StateMachine.getViewOrigin().getY() > 2048)
//		{
//			StateMachine.setViewOrigin(new Point2D.Double( StateMachine.getViewOrigin().getX(), 2048 - Math.pow(2, zoomLevel)));			
//		}
		
		GUIViewRefresher.setScrollBars(zoomLevel);
	}

	@Override
	public void hScrollbarChanged(int value)
	{
		GUIFunctions.printf("New horizontal position: %d", value);
		Double curOrigin = StateMachine.getViewOrigin();
		
		curOrigin.setLocation(-value, curOrigin.getY());
		
		StateMachine.setViewOrigin(curOrigin);
		GUIFunctions.refresh();
	}
	
	@Override
	public void vScrollbarChanged(int value)
	{
		GUIFunctions.printf("New vertical position: %d", value);
		
		Double curOrigin = StateMachine.getViewOrigin();
		
		curOrigin.setLocation(curOrigin.getX(), -value);
		
		StateMachine.setViewOrigin(curOrigin);
		GUIFunctions.refresh();
	}
	
	@Override
	public void openScene(File file)
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
	public void openDrawing(File file)
	{
		getModel().open(file);
	}

	@Override
	public void saveImage(File file)
	{
	}
	
	@Override
	public void doDeleteShape()
	{
	}
	@Override
	public void doEdgeDetection()
	{
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
	public void toggleBackgroundDisplay()
	{
	}

	@Override
	public void saveDrawing(File file)
	{
		getModel().save(file);
	}
	@Override
	public void toggle3DModelDisplay()
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
	}

	@Override
	public void doSendtoBack()
	{
	}

	public static GUIModel getModel()
	{
		return model;
	}

	public static void setModel(GUIModel model)
	{
		GUIController.model = model;
	}
	public void setModelView(GUIModel m, GUIViewRefresher vr)
	{
		setModel(m);
		viewRefresher = vr;
	}
	
	public static int getZoomLevel()
	{
		return zoomLevel;
	}
}
