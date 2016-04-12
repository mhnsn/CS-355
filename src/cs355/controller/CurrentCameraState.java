package cs355.controller;

import cs355.StudentLWJGLController;
import cs355.model.scene.Point3D;

public class CurrentCameraState
{
	static final int	PERSPECTIVE		= 0;
	static final int	ORTHOGRAPHIC	= 1;
	private int			mode			= PERSPECTIVE;

	/**
	 *
	 */
	public double		direction		= 0.0;							// degrees
																		// 0...360

	public double		elevation		= 0.0;							// degrees
																		// -90...90

	public String		lastKey			= "<none>";
	public Point3D		home			= StateMachine.BY_MY_MAILBOX;
	public Point3D		location		= StateMachine.BY_MY_MAILBOX;
	public Point3D		travelVec		= new Point3D(1, 0, 0);
	public Point3D		perpVec			= new Point3D(0, 0, 1);
	
	public CurrentCameraState(Point3D loc)
	{
		this(loc, 0.0, 0.0);
	}

	public CurrentCameraState(Point3D loc, double dir)
	{
		this(loc, dir, 0.0);
	}

	public CurrentCameraState(Point3D loc, double dir, double el)
	{
		lastKey = new String("None");
		location = new Point3D(0, 0, 0);
		direction = 0.0;
		elevation = 0.0;
		updateTravelVector();
		dump();
	}

	public void dump()
	{
		// System.out.println("Key pressed: " + lastKey);
		// System.out.println("Location: " + location.toString());
		// System.out.println("Direction: " + direction);
		// System.out.println("Travel Vector: " + travelVec.toString());
		// System.out.println("Perp Vector: " + perpVec.toString());
		// System.out.println("====================");
	}

	public double getDirection()
	{
		return direction;
	}

	public double getElevation()
	{
		return elevation;
	}

	public String getLastKey()
	{
		return lastKey;
	}

	public Point3D getLocation()
	{
		return location;
	}

	public int getMode()
	{
		return mode;
	}

	public Point3D getTravelVec()
	{
		return travelVec;
	}

	public void home()
	{
		setLocation(home);
		setDirection(0);
		dump();
	}

	public void lookDown(double angle)
	{
		updateElevation(-angle);
	}

	public void lookUp(double angle)
	{
		updateElevation(angle);
	}

	public void move(double distance)
	{
		updateLocation(new Point3D(distance * travelVec.x, distance * travelVec.y, distance * travelVec.z));
	}

	public void moveBackward(double distance)
	{
		moveForward(-distance);
	}

	public void moveDown(double distance)
	{
		moveUp(-distance);
	}

	public void moveForward(double distance)
	{
		updateLocation(new Point3D(distance * travelVec.x, distance * travelVec.y, distance * travelVec.z));
	}

	public void moveLeft(double distance)
	{
		moveRight(-distance);
	}

	public void moveRight(double distance)
	{
		updateLocation(new Point3D(distance * perpVec.x, distance * perpVec.y, distance * perpVec.z));
	}

	public void moveUp(double distance)
	{
		updateLocation(new Point3D(0, distance, 0));
	}

	public void orthographic()
	{
		mode = ORTHOGRAPHIC;
		dump();
	}

	public void perspective()
	{
		mode = PERSPECTIVE;
		dump();
	}

	public void setDirection(double dir)
	{
		direction = (dir % 360.0 + 360.0) % 360.0;
		updateTravelVector();
		StateMachine.scene.setCameraRotation(direction);
		dump();
	}

	public void setElevation(double newElevation)
	{
		elevation = newElevation % 90.0;
		updateTravelVector();
	}

	public void setLastKey(String lastKey)
	{
		this.lastKey = lastKey;
		dump();
	}

	public void setLocation(Point3D l)
	{
		location = l;
		StateMachine.where_i_am = l;
		dump();
	}

	public void setTravelVec(Point3D travelVec)
	{
		this.travelVec = travelVec;
	}

	public void turnLeft(double angle)
	{
		updateDirection(-angle);
	}

	public void turnRight(double angle)
	{
		updateDirection(angle);
	}

	public void updateDirection(double angle)
	{
		setDirection(direction + angle);
		StudentLWJGLController.scene.setCameraRotation(direction);
	}

	public void updateElevation(double angle)
	{
		setElevation(elevation + angle);
	}

	public void updateLocation(Point3D deltas)
	{
		setLocation(new Point3D(location.x + deltas.x, location.y + deltas.y, location.z + deltas.z));
		StudentLWJGLController.scene.setCameraPosition(location);
	}
	
	public void updateTravelVector()
	{
		/*
		 * Per http://gamedev.stackexchange.com/questions/17005/solving-for-
		 * velocity-in-the-x-y-z-axes
		 *
		 * x = cos(theta) * cos(phi); y = sin(theta) * cos(phi); z = sin(phi);
		 */
		double radDir = Math.toRadians(direction);
		double radEl = Math.toRadians(elevation);

		perpVec = new Point3D(Math.cos(radDir) * Math.cos(radEl), Math.sin(radEl), Math.sin(radDir) * Math.cos(radEl));
		travelVec = new Point3D(Math.sin(radDir), 0, -Math.cos(radDir));
	}
}