package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.controller.KeyboardInterface;
import cs355.controller.StateMachine;
import cs355.model.GUIModel;
import cs355.model.LineVector3D;
import cs355.model.Transform3D;
import cs355.model.drawing.Line;
import cs355.model.scene.Instance;
import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;
import cs355.model.scene.WireFrame;

public class GUIViewRefresherTest
{
	private static final Point3D viewSpaceOrigin = new Point3D(0, 0, 0);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		new KeyboardInterface();
		new GUIModel();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	public LineVector3D				IDENTITY_V	= new LineVector3D(
			new Line3D(new Point3D(0, 0, 0), new Point3D(1, 1, 1)));

	private ArrayList<LineVector3D>	al			= new ArrayList<LineVector3D>();
	private ArrayList<LineVector3D>	bl			= new ArrayList<LineVector3D>();
	
	LineVector3D					a;
	LineVector3D					b;
	LineVector3D					c;
	LineVector3D					d;
	LineVector3D					e;
	LineVector3D					f;
	LineVector3D					g;
	LineVector3D					h;
	LineVector3D					i;
	LineVector3D					j;
	LineVector3D					k;
	LineVector3D					l;
	LineVector3D					m;
	LineVector3D					n;
	LineVector3D					o;
	LineVector3D					p;
	LineVector3D					q;
	LineVector3D					r;
	LineVector3D					s;
	LineVector3D					t;
	LineVector3D					u;
	LineVector3D					v;
	LineVector3D					w;
	LineVector3D					x;
	LineVector3D					y;
	LineVector3D					z;
	LineVector3D					aa;
	LineVector3D					ab;
	LineVector3D					ac;
	LineVector3D					ad;
	LineVector3D					ae;
	LineVector3D					af;

	private final int				dimensions	= 2048;
	private double					tolerance	= .01;

	@Before
	public void setUp() throws Exception
	{
		a = new LineVector3D(new double[] { -1.000002, 0, 5, 1 });
		b = new LineVector3D(new double[] { 0.999999, 0, 5, 1 });
		c = new LineVector3D(new double[] { 0.999999, 3, 5, 1 });
		d = new LineVector3D(new double[] { 0.999999, 0, 5, 1 });
		e = new LineVector3D(new double[] { -1.000002, 0, 5, 1 });
		f = new LineVector3D(new double[] { -1.000002, 3, 5, 1 });
		g = new LineVector3D(new double[] { 2.00E-06, 8, -5, 1 });
		h = new LineVector3D(new double[] { 5.000001, 5, -4.999999, 1 });
		i = new LineVector3D(new double[] { -4.999999, 5, -5.000001, 1 });
		j = new LineVector3D(new double[] { -4.999999, 0, -5.000001, 1 });
		k = new LineVector3D(new double[] { -4.999999, 5, -5.000001, 1 });
		l = new LineVector3D(new double[] { 5.000001, 5, -4.999999, 1 });
		m = new LineVector3D(new double[] { 5.000001, 0, -4.999999, 1 });
		n = new LineVector3D(new double[] { 5.000001, 0, -4.999999, 1 });
		o = new LineVector3D(new double[] { 5.000001, 5, -4.999999, 1 });
		p = new LineVector3D(new double[] { 4.999999, 5, 5.000001, 1 });
		q = new LineVector3D(new double[] { 4.999999, 0, 5.000001, 1 });
		r = new LineVector3D(new double[] { -2.00E-06, 8, 5, 1 });
		s = new LineVector3D(new double[] { -5.000001, 5, 4.999999, 1 });
		t = new LineVector3D(new double[] { 4.999999, 5, 5.000001, 1 });
		u = new LineVector3D(new double[] { -5.000001, 0, 4.999999, 1 });
		v = new LineVector3D(new double[] { -5.000001, 5, 4.999999, 1 });
		w = new LineVector3D(new double[] { -4.999999, 5, -5.000001, 1 });
		x = new LineVector3D(new double[] { -4.999999, 0, -5.000001, 1 });
		y = new LineVector3D(new double[] { 2.00E-06, 8, -5, 1 });
		z = new LineVector3D(new double[] { -2.00E-06, 8, 5, 1 });
		aa = new LineVector3D(new double[] { 4.999999, 5, 5.000001, 1 });
		ab = new LineVector3D(new double[] { 5.000001, 5, -4.999999, 1 });
		ac = new LineVector3D(new double[] { -5.000001, 5, 4.999999, 1 });
		ad = new LineVector3D(new double[] { -2.00E-06, 8, 5, 1 });
		ae = new LineVector3D(new double[] { 2.00E-06, 8, -5, 1 });
		af = new LineVector3D(new double[] { -4.999999, 5, -5.000001, 1 });

		IDENTITY_V = new LineVector3D(new Line3D(new Point3D(0, 0, 0), new Point3D(1, 1, 1)));

		al.add(a);
		al.add(b);
		al.add(c);
		al.add(d);
		al.add(e);
		al.add(f);
		al.add(g);
		al.add(h);
		al.add(i);
		al.add(j);
		al.add(k);
		al.add(l);
		al.add(m);
		al.add(n);
		al.add(o);
		al.add(p);
		al.add(q);
		al.add(r);
		al.add(s);
		al.add(t);
		al.add(u);
		al.add(v);
		al.add(w);
		al.add(x);
		al.add(y);
		al.add(z);
		al.add(aa);
		al.add(ab);
		al.add(ac);
		al.add(ad);
		al.add(ae);
		al.add(af);

		bl.add(IDENTITY_V);

		GUIModel.setCameraLocation(new LineVector3D(0, 2, 30, viewSpaceOrigin, false));
		GUIModel.setCameraOrientation(new double[] { 0, 0, 0 });
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testRender()
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
		
		/*
		 * for (Instance i : GUIModel.getForegroundScene().instances()) { wf =
		 * i.getModel(); it = wf.getLines();
		 *
		 * while (it.hasNext()) { lv = new LineVector3D(it.next());
		 * lines.add(lv); } }
		 */

		// System.out.println(lines.size() + " initial lines.");
		System.out.println(al.size() + " initial lines.");
		
		assertEquals(32, al.size());
		
		/***********************************************************************
		 * generate world-to-camera transform (result of concatenating a
		 * translation matrix and a rotation matrix).
		 **********************************************************************/
		
		LineVector3D cameraLoc = GUIModel.getCameraLocation();
		double[] cameraOr = GUIModel.getCameraOrientation();
		
		// Transform3D.rotate(Transform3D.IDENTITY_V, new Point3D(0, 0, 0),
		// cameraOr[0], 0, 0, true);
		Transform3D.rotate(Transform3D.IDENTITY_V, new Point3D(0, 0, 0), 0, -cameraOr[1], 0, false);
		// Transform3D.rotate(Transform3D.IDENTITY_V, new Point3D(0, 0, 0), 0,
		// 0, cameraOr[2], true);
		
		Transform3D.translate(Transform3D.IDENTITY_V, cameraLoc.getOrigin());
		Transform3D.setWorldToCamera();
		
		/***********************************************************************
		 * Apply this matrix to the 3D homogeneous world-space point to get a 3D
		 * homogeneous camera-space point.
		 **********************************************************************/
		ArrayList<LineVector3D> homogeneousLines = new ArrayList<LineVector3D>();
		
		// for (LineVector3D l : lines)
		for (LineVector3D l : al)
		{
			Transform3D.transform(l);
			
			// to make clear what is held, they're moved.
			// I know this isn't optimum, but I don't have to optimize this. =)
			homogeneousLines.add(l);
		}
		
		assertEquals(32, homogeneousLines.size());

		Transform3D.transform(IDENTITY_V);

		assertEquals(1, IDENTITY_V.x, tolerance);
		assertEquals(1, IDENTITY_V.y, tolerance);
		assertEquals(1, IDENTITY_V.z, tolerance);
		assertEquals(1, IDENTITY_V.w, tolerance);

		IDENTITY_V.normalize();
		
		assertEquals(1, IDENTITY_V.x, tolerance);
		assertEquals(1, IDENTITY_V.y, tolerance);
		assertEquals(1, IDENTITY_V.z, tolerance);
		assertEquals(1, IDENTITY_V.w, tolerance);
		
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

		assertTrue(Transform3D.passesClipTests(IDENTITY_V.toArray()));
		assertEquals(homogeneousLines.size(), clippedLines.size());
		
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
			// REMOVED - JUST TEST OTHER VALUES.
			// g2d.setTransform(worldToView);
			// Point2D.Double e2D = l.getEndV();
			// g2d.drawLine(0, 0, (int) e2D.getX(), (int) e2D.getY());
			
		}
	}

}
