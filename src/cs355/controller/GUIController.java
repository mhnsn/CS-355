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
import cs355.model.GUIModel;
import cs355.model.drawing.Circle;
import cs355.view.GUIViewRefresher;

public class GUIController implements CS355Controller, MouseListener, MouseMotionListener
{
	static final int				noButton		= -1;
	static final int				colorButton		= 1;
	static final int				lineButton		= 2;
	static final int				squareButton	= 3;
	static final int				rectangleButton	= 4;
	static final int				circleButton	= 5;
	static final int				ellipseButton	= 6;
	static final int				triangleButton	= 7;
	static final int				selectButton	= 8;
	static final int				zoomInButton	= 9;
	static final int				zoomOutButton	= 10;
	static private StateMachine		state;
	static private GUIViewRefresher	viewRefresher;
	static private GUIModel			model;
	private static int				zoomLevel;
	
	public GUIController()
	{
		state = new StateMachine();
		StateMachine.current = StateMachine.init;
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
		StateMachine.e = e;
		
		Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
		
		pt = (Point2D.Double) StateMachine.viewToWorld(StateMachine.getViewOrigin()).transform(pt, null);
		
		if (StateMachine.current == StateMachine.rotate)
		{
			state.handleRotation(pt);
		}
		else if (StateMachine.current == StateMachine.move)
		{
			state.handleMove(pt);
		}
		else if (StateMachine.current == StateMachine.haveShape)
		{
		}
		state.tick();
		GUIFunctions.refresh();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		StateMachine.setMouseClickedFlag(true);
		// StateMachine.e = e;
		Point2D.Double pt = new Point2D.Double(e.getX(), e.getY());
		// state.registerClick((Point2D.Double) clickLocation);
		// Point2D.Double clickLocation = new Point2D.Double(e.getX(),
		// e.getY());
		Point2D.Double clickLocation = (Double) StateMachine.viewToWorld(StateMachine.getViewOrigin()).transform(pt,
				null);
		state.registerClick((Point2D.Double) clickLocation);
		
		switch (StateMachine.current)
		{
			case StateMachine.init:
				StateMachine.startDrawingFlag = true;
				break;
			case StateMachine.selectShape:
				if (state.shapeClicked(clickLocation))
				{
					StateMachine.clickLocations.clear();
				}
				break;
			case StateMachine.haveShape:
				// rotation check
				Circle handle = state.rotationHandleClicked(e);
				if (handle != null)
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
		StateMachine.mouseExitedFlag = false;
		StateMachine.e = e;
		state.tick();
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		StateMachine.mouseExitedFlag = true;
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
		// Point2D.Double clickLocation = new Point2D.Double(e.getX(),
		// e.getY());
		// if(StateMachine.current == StateMachine.selectShape)
		// {
		// Circle handle = state.rotationHandleClicked(e);
		// if(handle != null)
		// {
		// state.setRotationFlag(true);
		// state.setRotationHandle(handle);
		// }
		//
		// if(state.shapeClicked(clickLocation))
		// {
		// StateMachine.clickLocations.clear();
		// StateMachine.clickLocations.add(clickLocation);
		// state.saveCenter(e);
		// }
		// }
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
		StateMachine.setButtonClicked(lineButton);
		StateMachine.clearDrawingInfo();
		state.tick();
	}
	
	@Override
	public void squareButtonHit()
	{
		StateMachine.setButtonClicked(squareButton);
		StateMachine.clearDrawingInfo();
		state.tick();
	}
	
	@Override
	public void rectangleButtonHit()
	{
		StateMachine.setButtonClicked(rectangleButton);
		StateMachine.clearDrawingInfo();
		state.tick();
	}
	
	@Override
	public void circleButtonHit()
	{
		StateMachine.setButtonClicked(circleButton);
		StateMachine.clearDrawingInfo();
		state.tick();
	}
	
	@Override
	public void ellipseButtonHit()
	{
		StateMachine.setButtonClicked(ellipseButton);
		StateMachine.clearDrawingInfo();
		state.tick();
	}
	
	@Override
	public void triangleButtonHit()
	{
		StateMachine.setButtonClicked(triangleButton);
		StateMachine.clearDrawingInfo();
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
		Point2D.Double curOrigin = StateMachine.getViewOrigin();
		boolean zoomIn = true;
		
		if (zoomLevel >= 8)
		{
			StateMachine.setZoomFlag();
			zoomLevel -= 1;
			Point2D.Double newOrigin = calculateNewOrigin(curOrigin, zoomIn);
			GUIViewRefresher.setScrollBars(newOrigin);
			GUIFunctions.refresh();
		}
	}
	
	private void zoomOut()
	{
		Point2D.Double curOrigin = StateMachine.getViewOrigin();
		boolean zoomIn = false;
		
		if (zoomLevel <= 10)
		{
			StateMachine.setZoomFlag();
			zoomLevel += 1;
			
			Point2D.Double newOrigin = calculateNewOrigin(curOrigin, zoomIn);
			GUIViewRefresher.setScrollBars(newOrigin);
			GUIFunctions.refresh();
		}
	}
	
	private Double calculateNewOrigin(Double curOrigin, boolean zoomIn)
	{
		Point2D.Double newOrigin = new Point2D.Double(0, 0);
		Point2D.Double viewCenter = newOrigin;
		
		if (zoomIn)
		{
			viewCenter = new Point2D.Double(curOrigin.getX() - Math.pow(2, zoomLevel),
					curOrigin.getY() - Math.pow(2, zoomLevel));
		}
		else
		{
			viewCenter = new Point2D.Double(curOrigin.getX() - Math.pow(2, zoomLevel - 2),
					curOrigin.getY() - Math.pow(2, zoomLevel - 2));
		}
		
		newOrigin = new Point2D.Double(viewCenter.getX() + Math.pow(2, zoomLevel - 1),
				viewCenter.getY() + Math.pow(2, zoomLevel - 1));
		
		checkBounds(newOrigin);
		
		return newOrigin;
	}
	
	private void checkBounds(Double newOrigin)
	{
		double originX = newOrigin.getX();
		double originY = newOrigin.getY();
		
		if (StateMachine.getViewOrigin().getX() - Math.pow(2, zoomLevel) < -2048)
		{
			originX = Math.pow(2, zoomLevel) - 2048;
		}
		if (StateMachine.getViewOrigin().getY() - Math.pow(2, zoomLevel) < -2048)
		{
			originY = Math.pow(2, zoomLevel) - 2048;
		}
		
		if (originX > 0)
		{
			originX = 0;
		}
		else if (zoomLevel == 11 && originX < -1024)
		{
			originX = -1024;
		}
		
		if (originY > 0)
		{
			originY = 0;
		}
		else if (zoomLevel == 11 && originY < -1024)
		{
			originY = -1024;
		}
		
		newOrigin.setLocation(originX, originY);
	}
	
	@Override
	public void hScrollbarChanged(int value)
	{
		Double curOrigin = StateMachine.getViewOrigin();
		
		if (!StateMachine.getZoomFlag())
		{
			curOrigin.setLocation(-value, curOrigin.getY());
		}
		StateMachine.setViewOrigin(curOrigin);
		GUIFunctions.refresh();
	}
	
	@Override
	public void vScrollbarChanged(int value)
	{
		Double curOrigin = StateMachine.getViewOrigin();
		
		if (!StateMachine.getZoomFlag())
		{
			curOrigin.setLocation(curOrigin.getX(), -value);
		}
		
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
		if (GUIModel.getBackgroundImage().open(file))
		{
			GUIModel.getBackgroundImage().initializeShellImage();
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void openDrawing(File file)
	{
		getModel().open(file);
		GUIFunctions.refresh();
	}
	
	@Override
	public void saveDrawing(File file)
	{
		getModel().save(file);
	}
	
	@Override
	public void saveImage(File file)
	{
		if (GUIModel.getBackgroundImage().save(file))
		{
			System.out.println("Image saved. Now display it.");
		}
	}
	
	@Override
	public void doDeleteShape()
	{
		int shapeIndex = model.getSelectedShapeIndex();
		model.deleteShape(shapeIndex);
		GUIFunctions.refresh();
	}
	
	@Override
	public void doEdgeDetection()
	{
		if (GUIModel.getBackgroundImage() != null)
		{
			GUIModel.getBackgroundImage().edgeDetection();
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void doSharpen()
	{
		if (GUIModel.getBackgroundImage() != null)
		{
			GUIModel.getBackgroundImage().sharpen();
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void doMedianBlur()
	{
		if (GUIModel.getBackgroundImage() != null)
		{
			GUIModel.getBackgroundImage().medianBlur();
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void doUniformBlur()
	{
		if (GUIModel.getBackgroundImage() != null)
		{
			GUIModel.getBackgroundImage().uniformBlur();
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void doGrayscale()
	{
		if (GUIModel.getBackgroundImage() != null)
		{
			GUIModel.getBackgroundImage().grayscale();
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void toggleBackgroundDisplay()
	{
		
	}
	
	@Override
	public void toggle3DModelDisplay()
	{
	}
	
	@Override
	public void doChangeContrast(int contrastAmountNum)
	{
		if (GUIModel.getBackgroundImage() != null)
		{
			GUIModel.getBackgroundImage().contrast(contrastAmountNum);
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void doChangeBrightness(int brightnessAmountNum)
	{
		if (GUIModel.getBackgroundImage() != null)
		{
			GUIModel.getBackgroundImage().brightness(brightnessAmountNum);
		}
		GUIFunctions.refresh();
	}
	
	@Override
	public void doMoveForward()
	{
		int shapeIndex = model.getSelectedShapeIndex();
		model.moveForward(shapeIndex);
	}
	
	@Override
	public void doMoveBackward()
	{
		int shapeIndex = model.getSelectedShapeIndex();
		model.moveBackward(shapeIndex);
	}
	
	@Override
	public void doSendToFront()
	{
		int shapeIndex = model.getSelectedShapeIndex();
		model.moveToFront(shapeIndex);
	}
	
	@Override
	public void doSendtoBack()
	{
		int shapeIndex = model.getSelectedShapeIndex();
		model.movetoBack(shapeIndex);
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