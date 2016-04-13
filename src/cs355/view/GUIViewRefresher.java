package cs355.view;

import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
	static List<Shape>				modelShapes;
	public static final int			dimensions		= 2048;
	static public ArrayList<Line3D>	clippedLines	= new ArrayList<Line3D>();

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

	private void dumpData(String string, ArrayList<LineVector3D> lines)
	{
		// System.out.println("=================================================");
		// System.out.println("\t" + string);

		for (LineVector3D l : lines)
		{
			l.toString();
		}
	}

	private void dumpVector(LineVector3D vt, Double e2d)
	{
		// System.out.println("=================================================");
		// System.out.println("\t Mapping to screen");
		// System.out.println("Vector: " + vt.toString());
		// System.out.println("Mapped: " + e2d.toString());
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

	// public void render(Graphics2D g2d)
	// {
	// /***********************************************************************
	// * get all lines and convert to homogeneous coordinates
	// **********************************************************************/
	//
	// ArrayList<Line3D> lines = new ArrayList<Line3D>();
	// Line3D lv;
	// WireFrame wf;
	// Iterator<Line3D> it;
	// Line3D l;
	// Transform3D.clearPipeline();
	//
	// for (Instance i : GUIModel.getForegroundScene().instances())
	// {
	// wf = i.getModel();
	// it = wf.getLines();
	//
	// while (it.hasNext())
	// {
	// l = it.next();
	// // this constructor was only doing shallow copy! verify that
	// // subsequent instantiations don't have this issue
	// lv = new Line3D(new Point3D(l.start.x, l.start.y, l.start.z), new
	// Point3D(l.end.x, l.end.y, l.end.z));
	// lines.add(lv);
	// }
	// }
	//
	// if (lines.size() > 0)
	// {
	//// dumpData("Initial lines", lines);
	// }
	//
	// // System.out.println(lines.size() + " initial lines.");
	//
	// /***********************************************************************
	// * generate world-to-camera transform (result of concatenating a
	// * translation matrix and a rotation matrix).
	// **********************************************************************/
	//
	// Point3D cameraLoc = StateMachine.where_i_am;
	// double[] cameraDir = GUIModel.getCameraOrientation();
	//
	// Transform3D.rotate(Transform3D.IDENTITY_V, cameraDir[0], cameraDir[1],
	// cameraDir[2], false);
	// Transform3D.translate(cameraLoc);
	//
	// Transform3D.setWorldToCamera();
	//
	// /***********************************************************************
	// * Apply this matrix to the 3D homogeneous world-space point to get a 3D
	// * homogeneous camera-space point.
	// **********************************************************************/
	// ArrayList<LineVector3D> homogeneousLines = new ArrayList<LineVector3D>();
	//
	// if (homogeneousLines.size() > 0)
	// {
	// dumpData("Homogeneous lines", homogeneousLines);
	// }
	//
	// for (LineVector3D l : lines)
	// {
	// // even though this does in-place modification, it should be fine,
	// // since this is a copy of the data, not the original
	// Transform3D.transform(l);
	//
	// // to make clear what is held, they're moved.
	// // I know this isn't optimum, but I don't have to optimize this. =)
	// homogeneousLines.add(l);
	// }
	//
	// /***********************************************************************
	// * Build a clip matrix as discussed in class and in your textbook. Pick
	// * appropriate parameters for the zoom x , zoom y , near plane distance
	// * n, and far-plane distance f.
	// **********************************************************************/
	//
	// ArrayList<LineVector3D> clippedLines = new ArrayList<LineVector3D>();
	//
	// double zX = 50, zY = 50; // 50 degrees in each direction
	// double n = .05; // please don't show me anything less than 5cm from my
	// // face.
	// double f = 100; // I can see about 100m away.
	//
	// double[][] frustum = Transform3D.generateClipMatrix(zX, zY, n, f);
	//
	// /*******************************************************************
	// * Apply this clip matrix to the 3D homogeneous camera-space point to
	// * get 3D homogeneous points in clip space.
	// *******************************************************************
	// * Apply the clipping tests described in class and in your textbook.
	// * Reject a line if both points fail the same view frustum test OR if
	// * either endpoint fails the near-plane test. For this lab, we'll let
	// * Java's 2D line-drawing handing any other clipping.
	// *******************************************************************
	// * n.b. The clip tests and the result of clip application are all
	// * handled within Transform3D.clip(), which runs through clip tests and
	// * immediately returns false if the line doesn't pass. It then
	// * transforms the line, storing the values in the LineVector3D arg
	// ******************************************************************/
	//
	// clippedLines = Transform3D.clip(homogeneousLines, frustum);
	//
	// if (clippedLines.size() > 0)
	// {
	// dumpData("Initial lines", lines);
	// }
	// else
	// {
	// // System.out.println("No lines after clipping.");
	// }
	//
	// for (LineVector3D vt : clippedLines)
	// {
	// /*******************************************************************
	// * Apply perspective by normalizing the 3D homogeneous clip-space
	// * coordinate to get the (x/w,y/w) location of the point in
	// * canonical screen space.
	// ******************************************************************/
	// vt.normalize();
	//
	// /*******************************************************************
	// * Apply a viewport transformation to map the canonical screen space
	// * to the actual drawing space (2048*2048, with the origin in the
	// * upper left).
	// *******************************************************************/
	//
	// Transform3D.mapToDrawingSpace(vt, dimensions);
	//
	// /*******************************************************************
	// * Apply the same viewing transformation you use to implement
	// * zooming and scrolling of the 2D graphic objects to map from a
	// * portion of the 2048*2048 to the 512*512 screen area.
	// ******************************************************************/
	//
	// Point3D o = vt.getOrigin();
	// Point3D e3D = vt.getEnd();
	//
	// Line l = new Line(Color.WHITE, new Point2D.Double(o.x, o.y), new
	// Point2D.Double(e3D.x, e3D.y));
	//
	// AffineTransform worldToView =
	// StateMachine.worldToView(StateMachine.getViewOrigin());
	//
	// /*******************************************************************
	// * Draw the line on the screen. (This is where rasterization of the
	// * primitive would take place, but we'll just use the familiar 2D
	// * drawing commands to do this.)
	// ******************************************************************/
	// g2d.setTransform(worldToView);
	//
	// Point2D.Double e2D = l.getEndV();
	//
	// dumpVector(vt, e2D);
	//
	// g2d.drawLine(0, 0, (int) e2D.getX(), (int) e2D.getY());
	// }
	//
	// }

	public synchronized void render(Graphics2D g2d)
	{
		/***********************************************************************
		 * get all lines and convert to homogeneous coordinates
		 **********************************************************************/

		ArrayList<Line3D> lines = new ArrayList<Line3D>();
		WireFrame wf;
		Iterator<Line3D> it;
		Line3D l;
		Transform3D.clearPipeline();

		for (Instance i : GUIModel.getForegroundScene().instances())
		{
			Line3D vt;
			wf = i.getModel();
			it = wf.getLines();

			while (it.hasNext())
			{
				vt = it.next();
				// this constructor was only doing shallow copy! verify that
				Transform3D.clearPipeline();
				// double[][] rMatrix =
				Transform3D.scale(vt, i.getScale());
				Transform3D.rotate(Transform3D.IDENTITY_V, 0, i.getRotAngle(), 0, false);
				Transform3D.translate(i.getPosition());
				
				// subsequent instantiations don't have this issue
				l = new Line3D(new Point3D(vt.start.x, vt.start.y, vt.start.z),
						new Point3D(vt.end.x, vt.end.y, vt.end.z));
				lines.add(l);
			}
		}

		// System.out.println(al.size() + " initial lines.");
		Transform3D.clearPipeline();
		
		/***********************************************************************
		 * generate world-to-camera transform (result of concatenating a
		 * translation matrix and a rotation matrix).
		 **********************************************************************/
		
		Point3D cameraLoc = StateMachine.where_i_am;
		Point3D invCameraLoc = new Point3D(-cameraLoc.x, -cameraLoc.y, -cameraLoc.z);
		double[] cameraDir = GUIModel.getCameraOrientation();
		
		Transform3D.translate(invCameraLoc);
		Transform3D.rotate(Transform3D.IDENTITY_V, cameraDir[0], -cameraDir[1], cameraDir[2], false);
		
		Transform3D.setWorldToCamera();
		
		/***********************************************************************
		 * Apply this matrix to the 3D homogeneous world-space point to get a 3D
		 * homogeneous camera-space point.
		 **********************************************************************/
		ArrayList<Line3D> homogeneousLines = new ArrayList<Line3D>();
		
		for (Line3D vt : lines)
		{
			// Transform3D.transform(theLine);
			Transform3D.worldToCamera(vt);
			
			// to make clear what is held, they're moved.
			// I know this isn't optimum, but I don't have to optimize this. =)
			homogeneousLines.add(vt);
		}

		/***********************************************************************
		 * Build a clip matrix as discussed in class and in your textbook. Pick
		 * appropriate parameters for the zoom x , zoom y , near plane distance
		 * n, and far-plane distance f.
		 **********************************************************************/
		
		clippedLines = new ArrayList<Line3D>();

		// 45f, 1f, 0.01f, 1000f
		double fovX = 45, fovY = 45; // 50 degrees in each direction
		double n = .05; // please don't show me anything less than 10m from my
						// face.
		double f = 100; // I can see about 100m away. As they say in
						// Chile, tengo ojo de águila
		
		double[][] frustum = Transform3D.generateClipMatrix(fovX, fovY, n, f);
		
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

		// clippedLines = Transform3D.clip(homogeneousLines, frustum);
		double[] cameraSpaceV, cameraSpaceP, clipSpaceV, clipSpaceP;
		int i, j;

		for (Line3D v : homogeneousLines)
		{
			cameraSpaceV = new double[] { v.end.x, v.end.y, v.end.z, 1 };// v.getVArray();
			cameraSpaceP = new double[] { v.start.x, v.start.y, v.start.z, 1 };// =
																				// v.getPArray();
			
			clipSpaceV = Transform3D.leftMultiply(frustum, cameraSpaceV);
			clipSpaceP = Transform3D.leftMultiply(frustum, cameraSpaceP);

			i = Transform3D.passesClipTests(clipSpaceV);
			j = Transform3D.passesClipTests(clipSpaceP);
			// i = Transform3D.passesClipTests(cameraSpaceV);
			// j = Transform3D.passesClipTests(cameraSpaceP);
			
			// TODO: make sure that operator precedence holds as expected
			if (i == j && i != 0 || j == -2 || i == -2)
			{
				// System.out.println("====================================\r\n"
				// + "Clipping\r\n\t" + v.toString());
				continue;
			}

			// normalize that thing!
			v.start.x = clipSpaceP[0] / clipSpaceP[3];
			v.start.y = clipSpaceP[1] / clipSpaceP[3];
			v.start.z = clipSpaceP[2] / clipSpaceP[3];

			v.end.x = clipSpaceV[0] / clipSpaceV[3];
			v.end.y = clipSpaceV[1] / clipSpaceV[3];
			v.end.z = clipSpaceV[2] / clipSpaceV[3];

			// clippedLines.add(v);
			// }
			//
			// // System.out.println(clippedLines.size() + " lines after
			// clipping.");
			//
			// for (Line3D vt : clippedLines)
			// {
			/*******************************************************************
			 * Apply perspective by normalizing the 3D homogeneous clip-space
			 * coordinate to get the (x/w,y/w) location of the point in
			 * canonical screen space.
			 ******************************************************************/
			Line3D vt = v;
			// normalize(vt, clipSpaceP[3], clipSpaceV[3]);
			
			/*******************************************************************
			 * Apply a viewport transformation to map the canonical screen space
			 * to the actual drawing space (2048*2048, with the origin in the
			 * upper left).
			 *******************************************************************/
			
			Transform3D.mapToDrawingSpace(vt, dimensions, clipSpaceP[3], clipSpaceV[3]);
			
			/*******************************************************************
			 * Apply the same viewing transformation you use to implement
			 * zooming and scrolling of the 2D graphic objects to map from a
			 * portion of the 2048*2048 to the 512*512 screen area.
			 ******************************************************************/
			
			Point3D o = vt.start;
			Point3D e3D = vt.end;
			
			Line line2D = new Line(Color.WHITE, new Point2D.Double(o.x, o.y), new Point2D.Double(e3D.x, e3D.y));
			
			// AffineTransform worldToView =
			// StateMachine.worldToView(StateMachine.getViewOrigin());
			
			/*******************************************************************
			 * Draw the line on the screen. (This is where rasterization of the
			 * primitive would take place, but we'll just use the familiar 2D
			 * drawing commands to do this.)
			 ******************************************************************/
			// REMOVED - JUST TEST OTHER VALUES.
			// g2d.setTransform(worldToView);
			g2d.setColor(Color.WHITE);
			Point2D.Double e2D = line2D.getEndV();
			Point2D.Double o2D = line2D.getCenter();
			g2d.drawLine((int) o2D.x, (int) o2D.y, (int) e2D.getX(), (int) e2D.getY());
		}
	}

	// private void normalize(Line3D vt, double d, double e)
	// {
	// vt.end.x
	// }

	@Override
	public void update(Observable arg0, Object arg1)
	{
		GUIModel m = (GUIModel) arg0;
		modelShapes = m.getShapesReversed();
		GUIFunctions.refresh();
	}
}
