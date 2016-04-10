package cs355.controller;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

//import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
//import static org.lwjgl.opengl.GL11.GL_LINES;
//import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
//import static org.lwjgl.opengl.GL11.GL_PROJECTION;
//import static org.lwjgl.opengl.GL11.glBegin;
//import static org.lwjgl.opengl.GL11.glClear;
//import static org.lwjgl.opengl.GL11.glColor3f;
//import static org.lwjgl.opengl.GL11.glEnd;
//import static org.lwjgl.opengl.GL11.glLoadIdentity;
//import static org.lwjgl.opengl.GL11.glMatrixMode;
//import static org.lwjgl.opengl.GL11.glPushMatrix;
//import static org.lwjgl.opengl.GL11.glRotatef;
//import static org.lwjgl.opengl.GL11.glTranslatef;
//import static org.lwjgl.opengl.GL11.glVertex3d;
//import static org.lwjgl.opengl.GL11.glViewport;
//import static org.lwjgl.opengl.GL11.glOrtho;
//import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import cs355.model.scene.CS355Scene;
import cs355.model.scene.HouseModel;
import cs355.model.scene.Point3D;
import cs355.model.scene.WireFrame;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController
{

	// This is a model of a house.
	// It has a single method that returns an iterator full of Line3Ds.
	// A "Line3D" is a wrapper class around two Point2Ds.
	// It should all be fairly intuitive if you look at those classes.
	// If not, I apologize.
	private WireFrame				model			= new HouseModel();
	private static final int		DISPLAY_WIDTH	= 640;
	private static final int		DISPLAY_HEIGHT	= 480;
	private static CS355Scene		scene			= new CS355Scene();
	
	private static final Point3D	WHERE_HOME_IS	= new Point3D(0, 0, 0);
	private static final double		NO_ROTATION		= 0;
	
	private static final int		PERSPECTIVE		= 0;
	private static final int		ORTHOGRAPHIC	= 1;
	
	// This method is called to "resize" the viewport to match the screen.
	// When you first start, have it be in perspective mode.
	@Override
	public void resizeGL()
	{
		scene.setCameraPosition(new Point3D(0, 0, 0));
		scene.setCameraRotation(0);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		GL11.glLoadIdentity();
		
		runHome();

	  if (curState == null)
	  {
		  curState = new CurrentState(new Point3D(0,0,0), 0, 0);
	  }
	}
	
	private void runHome()
	{
		scene.setCameraPosition(WHERE_HOME_IS);
		scene.setCameraRotation(NO_ROTATION);
	}
	
	@Override
	public void update()
	{
		
	}
	
    class CurrentState
    {
    	public CurrentState(Point3D loc)
    	{
    		this(loc, 0.0, 0.0);
    	}
    	public CurrentState(Point3D loc, double dir)
    	{
    		this(loc, dir, 0.0);
    	}
    	public CurrentState(Point3D loc, double dir, double el)
    	{
    		this.lastKey   = new String("None");
    		this.location  = new Point3D(0, 0, 0);
    		this.direction = 45.0;
    		this.elevation =  0.0;
    		updateTravelVector();
    		dump();
    	}

    	public double getDirection() {
			return direction;
		}
		public void setDirection(double direction) {
			this.direction = ((direction % 360.0) + 360.0) % 360.0;
			updateTravelVector();
			dump();
		}
		public void updateDirection(double angle)
		{
			setDirection(direction + angle);
		}
		public void turnLeft(double angle)
		{
			updateDirection(-angle);
		}
		public void turnRight(double angle)
		{
			updateDirection(angle);
		}

		public String getLastKey() {
			return lastKey;
		}

		public void setLastKey(String lastKey) {
			this.lastKey = lastKey;
			dump();
		}

		public Point3D getLocation() {
			return location;
		}

		public void setLocation(Point3D location) {
			this.location = location;
			dump();
		}
		public void updateLocation(Point3D deltas)
		{
			setLocation(new Point3D(location.x + deltas.x,
									location.y + deltas.y,
									location.z + deltas.z));
		}
		
		public double getElevation()
		{
			return elevation;
		}
		public void setElevation(double newElevation)
		{
			elevation = newElevation % 90.0;
			updateTravelVector();
		}
		public void updateElevation(double angle)
		{
			setElevation(elevation + angle);
		}
		public void lookUp(double angle)
		{
			updateElevation(angle);
		}
		public void lookDown(double angle)
		{
			updateElevation(-angle);
		}
		public void updateTravelVector()
		{
			/*
			 * Per http://gamedev.stackexchange.com/questions/17005/solving-for-velocity-in-the-x-y-z-axes
			 * 
			 * x = cos(theta) * cos(phi);
			 * y = sin(theta) * cos(phi);
			 * z = sin(phi);
			 */
			double radDir = Math.toRadians(direction);
			double radEl  = Math.toRadians(elevation);

			travelVec = new Point3D(Math.cos(radDir) * Math.cos(radEl),
									Math.sin(radEl),
									Math.sin(radDir) * Math.cos(radEl)
									);
			perpVec   = new Point3D(-Math.sin(radDir),
									0,
									Math.cos(radDir)
									);
		}

		public void moveUp(double distance)
		{
			updateLocation(new Point3D(0, distance, 0));
		}
		public void moveDown(double distance)
		{
			moveUp(-distance);
		}
		public void moveRight(double distance)
		{
			updateLocation(new Point3D(distance * perpVec.x,
										distance * perpVec.y,
										distance * perpVec.z
										));
		}
		public void moveLeft(double distance)
		{
			moveRight(-distance);
		}
		public void moveForward(double distance)
		{
			updateLocation(new Point3D(distance * travelVec.x,
										distance * travelVec.y,
										distance * travelVec.z
										));
		}
		public void moveBackward(double distance)
		{
			moveForward(-distance);
		}
		public void move(double distance)
		{
			updateLocation(new Point3D(distance * travelVec.x,
										distance * travelVec.y,
										distance * travelVec.z
										));
		}
		public void home()
		{
			setLocation(this.home);
			dump();
		}
		
		public void orthographic()
		{
			dump();
		}
		public void perspective()
		{
			dump();
		}

		public double  direction = 0.0; // degrees   0...360
		public double  elevation = 0.0; // degrees -90...90
    	public String  lastKey   = "<none>";
    	public Point3D home      = new Point3D(0,0,0);
    	public Point3D location  = home;
    	public Point3D travelVec = new Point3D(1,0,0);
    	public Point3D perpVec   = new Point3D(0,0,1);

    	public void dump()
    	{
    		System.out.println("Key pressed:   " + lastKey);
    		System.out.println("Location:      " + location.toString());
    		System.out.println("Direction:     " + direction);
    		System.out.println("Travel Vector: " + travelVec.toString());
    		System.out.println("Perp Vector:   " + perpVec  .toString());
    		System.out.println("====================");
    	}
    };

    private CurrentState curState;

    //This is called every frame, and should be responsible for keyboard updates.
    //An example keyboard event is captured below.
    //The "Keyboard" static class should contain everything you need to finish
    // this up.
    @SuppressWarnings("serial")
	Map<Integer, Runnable> keyHandlers = new HashMap<Integer, Runnable>() {{
			/*
			 * a Move left
			 * d Move right
			 * w Move forward
			 * s Move backward
			 * q Turn left
			 * e Turn right
			 * r Move up
			 * f Move down
			 * h Return to the original “home” position and orientation
			 * o Switch to orthographic projection
			 * p Switch to perspective projection
			 */
    		put(new Integer(Keyboard.KEY_A), new Runnable()
    			{
    			public void run()
    				{
    				curState.setLastKey("A");
    				curState.moveLeft(1.0);
    				}
    			});
    		put(new Integer(Keyboard.KEY_D), new Runnable()
				{
				public void run()
					{
					curState.setLastKey("D");
					curState.moveRight(1.0);
					}
				});
    		put(new Integer(Keyboard.KEY_W), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("W");
					curState.moveForward(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_S), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("S");
					curState.moveBackward(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_Q), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("Q");
					curState.turnLeft(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_E), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("E");
					curState.turnRight(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_R), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("R");
					curState.moveUp(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_F), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("F");
					curState.moveDown(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_H), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("H");
					curState.home();
				}
			});
			put(new Integer(Keyboard.KEY_O), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("O");
					curState.orthographic();
				}
			});
			put(new Integer(Keyboard.KEY_P), new Runnable()
			{
				public void run()
					{
					curState.setLastKey("P");
					curState.perspective();
					}
			});
		}
	};
	
	@Override
	public void updateKeyboard()
	{
		for (Map.Entry<Integer, Runnable> k : keyHandlers.entrySet())
		{
			if (Keyboard.isKeyDown(k.getKey()))
			{
				k.getValue().run();
				break;
			}
		}
	}
	
	// This method is the one that actually draws to the screen.
	@Override
	public void render()
	{
		// This clears the screen.
		glClear(GL_COLOR_BUFFER_BIT);
		
		// Do your drawing here.
		
		Display.update();
		
	}
}
