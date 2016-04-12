package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cs355.model.LineVector3D;
import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;

public class LineVector3DTest
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	private Point3D			origin		= new Point3D(0, 0, 0);
	private Point3D			allAxes		= new Point3D(1, 1, 1);
	private LineVector3D	l			= new LineVector3D(new Line3D(origin, allAxes));
	private double			tolerance	= .01;
	
	@Before
	public void setUp() throws Exception
	{
		l = new LineVector3D(new Line3D(origin, allAxes));
	}
	
	@After
	public void tearDown() throws Exception
	{
	}
	
	@Test
	public final void testGetEnd()
	{
		Point3D p = l.getEnd();

		assertEquals(1, p.x, tolerance);
		assertEquals(1, p.y, tolerance);
		assertEquals(1, p.z, tolerance);
	}

	@Test
	public final void testGetOrigin()
	{
		Point3D p = l.getOrigin();

		assertEquals(0, p.x, tolerance);
		assertEquals(0, p.y, tolerance);
		assertEquals(0, p.z, tolerance);
	}
	
	@Test
	public final void testLineVector3D()
	{
	}
	
	@Test
	public final void testLineVector3DLine3D()
	{
		assertEquals(1, l.x, tolerance);
		assertEquals(1, l.y, tolerance);
		assertEquals(1, l.z, tolerance);
		assertEquals(1, l.w, tolerance);

		assertFalse(l.isVector());
	}
	
	@Test
	public final void testNormalize()
	{
		l.normalize();
		
		assertEquals(1, l.x, tolerance);
		assertEquals(1, l.y, tolerance);
		assertEquals(1, l.z, tolerance);
		assertEquals(1, l.w, tolerance);
	}

	@Test
	public final void testSetEnd()
	{
		l.setEnd(origin);

		assertEquals(0, l.x, tolerance);
		assertEquals(0, l.y, tolerance);
		assertEquals(0, l.z, tolerance);
		assertEquals(1, l.w, tolerance);
	}
	
	@Test
	public final void testToArray()
	{
		double[] a = l.toArray();

		assertEquals(1, a[0], tolerance);
		assertEquals(1, a[1], tolerance);
		assertEquals(1, a[2], tolerance);
		assertEquals(1, a[3], tolerance);
	}
	
	@Test
	public final void testToString()
	{
		String s = l.toString();
		String t = l.toString();
		
		assertEquals(s, t);
	}
}
