package cs355.controller;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
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

	public GUIController()
	{
		state = new StateMachine();
		StateMachine.current	= StateMachine.init;

		StateMachine.setCurrentColor(new Color(255, 255, 0));
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
		StateMachine.mouseDraggedFlag = true;
		StateMachine.e = e;
		state.tick();
//		switch (State.current)
//		{
//			case State.selectShape:
//				if(GUIModel.getSelectedShape() != null)
//				{
//					Circle handle = rotationHandleClicked(e);
//					if(handle != null)
//					{
//						handleRotation(e,handle);
//					}
//					else
//					{
//						handleShapeMovement(e);
//					}
//				}
//				break;
//			default:
//				mouseMoved(e);
//				break;
//		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		StateMachine.mouseMovedFlag = true;
//		if(StateMachine.mouseEnteredFlag)
//		{
//			StateMachine.setCurrentMouseLocation(new Point2D.Double(e.getX(), e.getY()));
//		}
		StateMachine.e = e;
		state.tick();
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		StateMachine.mouseClickedFlag = true;
		StateMachine.e = e;
		state.registerClick(new Point2D.Double(e.getX(), e.getY()));
		if(StateMachine.current == StateMachine.init)
		{
			StateMachine.startDrawingFlag = true;
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
		StateMachine.e = e;
		state.tick();
	}
	@Override
	public void mousePressed(MouseEvent e)
	{
		StateMachine.mousePressedFlag = true;
		StateMachine.e = e;
		Point2D.Double clickLocation = new Point2D.Double(e.getX(), e.getY());
		if(StateMachine.current == StateMachine.selectShape)
		{
			Circle c = state.rotationHandleClicked(e);
			if(c != null)
			{
				state.handleRotation(e, c);
			}
			else if(state.shapeClicked(clickLocation))
			{
				StateMachine.clickLocations.clear();
				StateMachine.clickLocations.add(clickLocation);
				state.saveCenter(e);
			}
		}
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
		state.clearDrawingInfo();
		StateMachine.setCurrentColor(c);
		GUIFunctions.changeSelectedColor(StateMachine.getCurrentColor());
		state.tick();
	}
	
	@Override
	public void lineButtonHit()			
	{
		StateMachine.setButtonClicked(lineButton);	 state.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void squareButtonHit()		
	{
		StateMachine.setButtonClicked(squareButton); state.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void rectangleButtonHit()	
	{
		StateMachine.setButtonClicked(rectangleButton); state.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void circleButtonHit()		
	{
		StateMachine.setButtonClicked(circleButton); state.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void ellipseButtonHit()		
	{
		StateMachine.setButtonClicked(ellipseButton); state.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void triangleButtonHit()		
	{
		StateMachine.setButtonClicked(triangleButton); state.clearDrawingInfo();
		state.tick();
	}
	
	@Override
	public void selectButtonHit()		
	{
		StateMachine.setButtonClicked(selectButton);
		state.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void zoomInButtonHit()		
	{
		StateMachine.setButtonClicked(zoomInButton); state.clearDrawingInfo();
		state.tick();
	}

	@Override
	public void zoomOutButtonHit()		
	{
		StateMachine.setButtonClicked(zoomOutButton); state.clearDrawingInfo();
		state.tick();
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
		// TODO: this
	}

	@Override
	public void doSendtoBack()
	{
		// TODO: this
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
}
