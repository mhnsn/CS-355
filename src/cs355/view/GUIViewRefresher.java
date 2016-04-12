package cs355.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.GUIController;
import cs355.controller.KeyboardInterface;
import cs355.controller.ShapeBound;
import cs355.controller.StateMachine;
import cs355.model.GUIModel;
import cs355.model.LineVector3D;
import cs355.model.Transform3D;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;
import cs355.model.scene.Instance;
import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;
import cs355.model.scene.WireFrame;

public class GUIViewRefresher implements ViewRefresher
{
	static List<Shape>		modelShapes;
	public static final int	dimensions	= 2048;
	
	public static void initScrollBars()
	{
		GUIFunctions.setHScrollBarPosit(0);
		GUIFunctions.setVScrollBarPosit(0);
		
		GUIFunctions.setHScrollBarKnob((int) Math.pow(2, GUIController.getZoomLevel()));
		GUIFunctions.setVScrollBarKnob((int) Math.pow(2, GUIController.getZoomLevel()));
		
		GUIFunctions.setHScrollBarMin(0);
		GUIFunctions.setVScrollBarMin(0);
		
		GUIFunctions.setHScrollBarMax(2048);
		GUIFunctions.setVScrollBarMax(2048);
	}
	
	public static void setScrollBars(Point2D.Double newOrigin)
	{
		boolean zooming = false;
		int zoomLevel = GUIController.getZoomLevel();
		
		if (StateMachine.getZoomFlag())
		{
			zooming = true;
			
			if (zoomLevel == 11)
			{
				StateMachine.setViewOrigin(new Point2D.Double(0, 0));
				
				GUIFunctions.setHScrollBarPosit(0);
				GUIFunctions.setVScrollBarPosit(0);
				
				GUIFunctions.setHScrollBarKnob((int) Math.pow(2, zoomLevel));
				GUIFunctions.setVScrollBarKnob((int) Math.pow(2, zoomLevel));
				
				GUIFunctions.refresh();
				return;
			}
			else if (zoomLevel == 10)
			{
				GUIFunctions.setHScrollBarKnob((int) Math.pow(2, zoomLevel));
				GUIFunctions.setVScrollBarKnob((int) Math.pow(2, zoomLevel));
			}
		}
		
		StateMachine.setViewOrigin(newOrigin);
		
		GUIFunctions.setHScrollBarPosit((int) -newOrigin.getX());
		
		GUIFunctions.setVScrollBarPosit((int) -newOrigin.getY());
		
		GUIFunctions.setHScrollBarKnob((int) Math.pow(2, zoomLevel));
		
		GUIFunctions.setVScrollBarKnob((int) Math.pow(2, zoomLevel));
		
		if (zooming)
		{
			StateMachine.lowerZoomFlag();
		}
	}
	
	public GUIViewRefresher()
	{
		modelShapes = new ArrayList<Shape>();
	}
	
	@Override
	public void refreshView(Graphics2D g2d)
	{
		boolean usingControllerShape = false;
		if (StateMachine.getCurrentShape() != null)
		{
			usingControllerShape = true;
			modelShapes.add(StateMachine.getCurrentShape());
		}
		
		AffineTransform objToView;
		
		if (GUIModel.getBackgroundImage() != null)
		{
			BufferedImage bi = GUIModel.getBackgroundImage().getImage();
			if (bi != null)
			{
				// calculate the x and y offset to use
				// in order to center the image
				int offsetX = bi.getWidth() / 2;
				int offsetY = bi.getHeight() / 2;
				
				// set the image's x and y values
				// to 1024-offsetX
				// and 1024-offsetY
				int imageTopLeftX = 1024 - offsetX;
				int imageTopLeftY = 1024 - offsetY;
				
				objToView = StateMachine.objectToView(new Rectangle(Color.WHITE,
						new Point2D.Double(imageTopLeftX, imageTopLeftY), bi.getWidth(), bi.getHeight()));
				
				g2d.setTransform(objToView);
				
				g2d.drawImage(bi, 0, 0, null);
			}
		}
		
		for (Shape s : modelShapes)
		{
			objToView = StateMachine.objectToView(s);
			g2d.setColor(s.getColor());
			g2d.setTransform(objToView);
			
			if (s.getClass().equals(cs355.model.drawing.Line.class))
			{
				Line line = (Line) s;
				
				// g2d.setTransform(objToWorld);
				
				g2d.drawLine(0, 0, (int) line.getEndV().getX(), (int) line.getEndV().getY());
			}
			else if (s.getClass().equals(cs355.model.drawing.Square.class))
			{
				Square sq = (Square) s;
				
				g2d.fillRect((int) -(sq.getSize() / 2), (int) -(sq.getSize() / 2), (int) sq.getSize(),
						(int) sq.getSize());
			}
			else if (s.getClass().equals(cs355.model.drawing.Rectangle.class))
			{
				Rectangle r = (Rectangle) s;
				
				g2d.fillRect((int) -(r.getWidth() / 2), (int) -(r.getHeight() / 2), (int) r.getWidth(),
						(int) r.getHeight());
			}
			else if (s.getClass().equals(cs355.model.drawing.Circle.class))
			{
				Circle c = (Circle) s;
				
				g2d.fillOval((int) -c.getRadius(), (int) -c.getRadius(), (int) c.getRadius() * 2,
						(int) c.getRadius() * 2);
			}
			else if (s.getClass().equals(cs355.model.drawing.Ellipse.class))
			{
				Ellipse e = (Ellipse) s;
				
				g2d.fillOval((int) -e.getWidth(), (int) -e.getHeight(), (int) e.getWidth() * 2,
						(int) e.getHeight() * 2);
			}
			else if (s.getClass().equals(cs355.model.drawing.Triangle.class))
			{
				Triangle t = (Triangle) s;
				
				t.cleanup();
				
				int xCoords[] = { (int) t.getA().getX(), (int) t.getB().getX(), (int) t.getC().getX() };
				int yCoords[] = { (int) t.getA().getY(), (int) t.getB().getY(), (int) t.getC().getY() };
				
				g2d.fillPolygon(xCoords, yCoords, 3);
			}
		}
		
		ShapeBound sb = null;
		
		if (GUIModel.getSelectedShape() != null)
		{
			sb = new ShapeBound(GUIModel.getSelectedShape());
		}
		
		if (sb != null)
		{
			Rectangle c = sb.getBoundingBox();
			g2d.setTransform(sb.getBoundingBoxTransform());
			
			double scaleFactor = Math.pow(2, GUIController.getZoomLevel() - 9);
			
			g2d.setStroke(new BasicStroke((float) (1 * scaleFactor)));
			
			g2d.setColor(c.getColor());
			g2d.drawRect((int) -(c.getWidth() / 2), (int) -(c.getHeight() / 2), (int) c.getWidth(),
					(int) c.getHeight());
			if (sb.getShape().getClass().equals(cs355.model.drawing.Circle.class))
			{
				// don't draw the handle for circles - rotation is useless.
			}
			else
			{
				if (StateMachine.doRotation())
				{
					g2d.fillOval((int) (sb.getHandleCenter().getX() - 5 * scaleFactor),
							(int) (sb.getHandleCenter().getY() - 5 * scaleFactor), (int) (10 * scaleFactor),
							(int) (10 * scaleFactor));
				}
				else
				{
					g2d.drawOval((int) (sb.getHandleCenter().getX() - 5 * scaleFactor),
							(int) (sb.getHandleCenter().getY() - 5 * scaleFactor), (int) (10 * scaleFactor),
							(int) (10 * scaleFactor));
				}
			}
		}
		
		if (usingControllerShape)
		{
			modelShapes.remove(modelShapes.size() - 1); // pop off data from the
														// controller if
														// necessary
		}
		
		if (GUIModel.draw3D())
		{
			render(g2d);
		}
	}
	
	public void render(Graphics2D g2d)
	{
		/***********************************************************************
		 * get all lines and convert to homogeneous coordinates
		 **********************************************************************/
		
		ArrayList<LineVector3D> lines = new ArrayList<LineVector3D>();
		LineVector3D lv;
		WireFrame wf;
		Iterator<Line3D> it;
		
		Transform3D.clearPipeline();
		
		// TODO: verify that this render process isn't destructive to original
		// data
		for (Instance i : GUIModel.getForegroundScene().instances())
		{
			wf = i.getModel();
			it = wf.getLines();
			
			while (it.hasNext())
			{
				lv = new LineVector3D(it.next());
				lines.add(lv);
			}
		}

		System.out.println(lines.size() + " initial lines.");
		
		/***********************************************************************
		 * generate world-to-camera transform (result of concatenating a
		 * translation matrix and a rotation matrix).
		 **********************************************************************/
		
		LineVector3D cameraLoc = GUIModel.getCameraLocation();
		double[] cameraOr = GUIModel.getCameraOrientation();
		
		Transform3D.rotate(Transform3D.IDENTITY_V, new Point3D(0, 0, 0), cameraOr[0], cameraOr[1], cameraOr[2], false);
		Transform3D.translate(Transform3D.IDENTITY_V, cameraLoc.getOrigin());

		Transform3D.setWorldToCamera();
		
		/***********************************************************************
		 * Apply this matrix to the 3D homogeneous world-space point to get a 3D
		 * homogeneous camera-space point.
		 **********************************************************************/
		ArrayList<LineVector3D> homogeneousLines = new ArrayList<LineVector3D>();
		
		for (LineVector3D l : lines)
		{
			Transform3D.transform(l);
			
			// to make clear what is held, they're moved.
			// I know this isn't optimum, but I don't have to optimize this. =)
			homogeneousLines.add(l);
		}
		
		/***********************************************************************
		 * Build a clip matrix as discussed in class and in your textbook. Pick
		 * appropriate parameters for the zoom x , zoom y , near plane distance
		 * n, and far-plane distance f.
		 **********************************************************************/
		
		ArrayList<LineVector3D> clippedLines = new ArrayList<LineVector3D>();
		
		double zX = 50, zY = 50; // 50 degrees in each direction
		double n = .05; // please don't show me anything less than 5cm from my
						// face.
		double f = 100; // I can see about 100m away.
		
		double[][] frustum = Transform3D.generateClipMatrix(zX, zY, n, f);
		
		/*******************************************************************
		 * Apply this clip matrix to the 3D homogeneous camera-space point to
		 * get 3D homogeneous points in clip space.
		 *******************************************************************
		 * Apply the clipping tests described in class and in your textbook.
		 * Reject a line if both points fail the same view frustum test OR if
		 * either endpoint fails the near-plane test. For this lab, we'll let
		 * Java's 2D line-drawing handing any other clipping.
		 *******************************************************************
		 * n.b. The clip tests and the result of clip application are all
		 * handled within Transform3D.clip(), which runs through clip tests and
		 * immediately returns false if the line doesn't pass. It then
		 * transforms the line, storing the values in the LineVector3D arg
		 ******************************************************************/
		
		clippedLines = Transform3D.clip(homogeneousLines, frustum);

		System.out.println(clippedLines.size() + " lines after clipping.");
		
		for (LineVector3D vt : clippedLines)
		{
			/*******************************************************************
			 * Apply perspective by normalizing the 3D homogeneous clip-space
			 * coordinate to get the (x/w,y/w) location of the point in
			 * canonical screen space.
			 ******************************************************************/
			vt.normalize();
			
			/*******************************************************************
			 * Apply a viewport transformation to map the canonical screen space
			 * to the actual drawing space (2048*2048, with the origin in the
			 * upper left).
			 *******************************************************************/
			
			Transform3D.mapToDrawingSpace(vt, dimensions);
			
			/*******************************************************************
			 * Apply the same viewing transformation you use to implement
			 * zooming and scrolling of the 2D graphic objects to map from a
			 * portion of the 2048*2048 to the 512*512 screen area.
			 ******************************************************************/
			
			Point3D o = vt.getOrigin();
			Point3D e3D = vt.getEnd();
			
			Line l = new Line(Color.WHITE, new Point2D.Double(o.x, o.y), new Point2D.Double(e3D.x, e3D.y));
			
			AffineTransform worldToView = StateMachine.worldToView(StateMachine.getViewOrigin());
			
			/*******************************************************************
			 * Draw the line on the screen. (This is where rasterization of the
			 * primitive would take place, but we'll just use the familiar 2D
			 * drawing commands to do this.)
			 ******************************************************************/
			g2d.setTransform(worldToView);
			
			Point2D.Double e2D = l.getEndV();
			
			g2d.drawLine(0, 0, (int) e2D.getX(), (int) e2D.getY());
			
		}
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		GUIModel m = (GUIModel) arg0;
		modelShapes = m.getShapesReversed();
		GUIFunctions.refresh();
	}
}
