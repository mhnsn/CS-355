package cs355.controller;

import static org.lwjgl.opengl.GL11.*;

//import static org.lwjgl.opengl.GL11.*;

//import static org.lwjgl.opengl.GL11.glViewport;
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
//import static org.lwjgl.opengl.GL11.glOrtho;
//import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
//import static org.lwjgl.opengl.GL11.GL_LINES;
//import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
//import static org.lwjgl.util.glu.GLU.gluPerspective;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.util.glu.GLU.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import cs355.model.scene.CS355Scene;
import cs355.model.scene.HouseModel;
import cs355.model.scene.Instance;
import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;
import cs355.model.scene.WireFrame;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController
{
	/**
	 * This is a model of a house. It has a single method that returns an
	 * iterator full of Line3Ds. A "Line3D" is a wrapper class around two
	 * Point2Ds. It should all be fairly intuitive if you look at those classes.
	 * If not, I apologize.
	 */
	private WireFrame				model					= new HouseModel();
	private Instance				home					= new Instance(Color.white, WHERE_HOME_IS, 0, 1, model);
	
	private static final Point3D	WHERE_HOME_IS			= new Point3D(0, 0, 0);
	Point3D							jonesHome				= new Point3D(-15, 0, 0);
	Instance						theJones				= new Instance(Color.yellow, jonesHome, 0, 1, model);
	Point3D							thePigsty				= new Point3D(15, 0, 0);
	Instance						timonAndPumbaa			= new Instance(Color.green, thePigsty, 0, 1, model);
	
	Point3D							smithHome				= new Point3D(0, 0, -100);
	Instance						theSmiths				= new Instance(Color.CYAN, smithHome, 180, 1, model);
	Point3D							theMayorsHouse			= new Point3D(-15, 0, -100);
	Instance						mayorMayor				= new Instance(Color.orange, theMayorsHouse, 180, 1, model);
	Point3D							doNotVisit				= new Point3D(15, 0, -100);
	Instance						crazyOldManMarley		= new Instance(Color.gray, doNotVisit, 180, 1, model);
	
	Point3D							highSecurityLocation	= new Point3D(-50, 5, -50);
	Instance						misterPresident			= new Instance(Color.white, highSecurityLocation, 90, 5,
			model);
	
	private static final int		DISPLAY_WIDTH			= 640;
	private static final int		DISPLAY_HEIGHT			= 480;
	private static CS355Scene		scene					= new CS355Scene();
	
	private static final Point3D	BY_MY_MAILBOX			= new Point3D(0, 2, 30);
	private static final Point3D	UP						= new Point3D(0, 1, 0);
	
	private static final double		NO_ROTATION				= 0;
	private static final int		PERSPECTIVE				= 0;
	private static final int		ORTHOGRAPHIC			= 1;
	private int						mode					= PERSPECTIVE;
	
	private static Point3D			where_i_am;
	private CurrentState			curState;
	
	/**
	 * This method is called to "resize" the viewport to match the screen. //
	 * When you first start, have it be in perspective mode.
	 */
	@Override
	public void resizeGL()
	{
		scene.setCameraPosition(new Point3D(0, 0, 0));
		scene.setCameraRotation(0);
		
		scene.addInstance(home);
		thereGoesTheNeighborhood();
		
		// System.out.println("There are " + scene.instances().size()
		// + " people in this house, and you're the only one who has to make
		// trouble.");
		
		glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		if (curState == null)
		{
			curState = new CurrentState(BY_MY_MAILBOX, 0, 0);
		}
		
		takeMeHome();
	}
	
	private void thereGoesTheNeighborhood()
	{
		scene.addInstance(theJones);
		scene.addInstance(theSmiths);
		scene.addInstance(timonAndPumbaa);
		scene.addInstance(mayorMayor);
		scene.addInstance(crazyOldManMarley);
		scene.addInstance(misterPresident);
	}
	
	private void takeMeHome()
	{
		scene.setCameraPosition(BY_MY_MAILBOX); // Phil Collins would be proud.
		scene.setCameraRotation(NO_ROTATION); // also, you are outside a white
												// house.
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		where_i_am = BY_MY_MAILBOX;
		
		glMatrixMode(GL_MODELVIEW);
		gluLookAt((float) where_i_am.x, (float) where_i_am.y, (float) where_i_am.z, (float) WHERE_HOME_IS.x,
				(float) WHERE_HOME_IS.y, (float) WHERE_HOME_IS.z, (float) UP.x, (float) UP.y, (float) UP.z);
		/**
		 * I // recommend // you // NOT // float // up.
		 */
		curState.setLocation(where_i_am);
		curState.setDirection(0);
	}
	
	@Override
	public void update()
	{
		
	}
	
	// private int tally = 0;
	
	/**
	 * This is called every frame, and should be responsible for keyboard //
	 * updates. // An example keyboard event is captured below. // The
	 * "Keyboard" static class should contain everything you need to finish //
	 * this up.
	 * 
	 */
	@Override
	public void updateKeyboard()
	{
		for (Map.Entry<Integer, Runnable> k : keyHandlers.entrySet())
		{
			// if (tally++ % 30 == 0)
			// {
			// curState.turnLeft(1);
			// }
			
			if (Keyboard.isKeyDown(k.getKey()))
			{
				scene.setCameraPosition(where_i_am);
				k.getValue().run();
				break;
			}
		}
	}
	
	/**
	 * This method is the one that actually draws to the screen.
	 */
	@Override
	public void render()
	{
		// This clears the screen.
		glClear(GL_COLOR_BUFFER_BIT);
		
		// Do your drawing here.
		glMatrixMode(GL_PROJECTION);
		
		glLoadIdentity();
		
		// handle different viewing modes here.
		switch (mode)
		{
			case PERSPECTIVE:
				gluPerspective(45f, 1f, 0.01f, 1000f);
				break;
			case ORTHOGRAPHIC:
				glOrtho(-25f, 25f, -25f, 25f, 0.01f, 1000f);
				break;
		}
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		Point3D cameraLoc = scene.getCameraPosition();
		glRotatef((float) scene.getCameraRotation(), 0f, 1f, 0f);
		
		glTranslatef((float) -cameraLoc.x, (float) -cameraLoc.y, (float) -cameraLoc.z);
		
		drawStreet();
	}
	
	private void drawStreet()
	{
		for (Instance i : scene.instances())
		{
			drawInstance(i);
		}
	}
	
	private void drawInstance(Instance i)
	{
		/**
		 * push the instance transformation
		 * 
		 * draw instance
		 * 
		 * pop the instance transformation
		 */
		Iterator<Line3D> it = i.getModel().getLines();
		Color c = i.getColor();
		Point3D p = i.getPosition();
		float r = (float) i.getRotAngle();
		double s = i.getScale();
		
		glPushMatrix();
		
		glColor3d(c.getRed(), c.getBlue(), c.getGreen());
		glTranslatef((float) -p.x, (float) -p.y, (float) -p.z);
		glRotatef(-r, 0f, 1f, 0f);
		glScaled(s, s, s);
		
		glBegin(GL_LINES);
		while (it.hasNext())
		{
			Line3D cur = it.next();
			glVertex3d(cur.start.x, cur.start.y, cur.start.z);
			glVertex3d(cur.end.x, cur.end.y, cur.end.z);
		}
		glEnd();
		
		glPopMatrix();
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
			this.lastKey = new String("None");
			this.location = new Point3D(0, 0, 0);
			this.direction = 45.0;
			this.elevation = 0.0;
			updateTravelVector();
			dump();
		}
		
		public double getDirection()
		{
			return direction;
		}
		
		public void setDirection(double dir)
		{
			direction = ((dir % 360.0) + 360.0) % 360.0;
			updateTravelVector();
			scene.setCameraRotation(direction);
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
		
		public String getLastKey()
		{
			return lastKey;
		}
		
		public void setLastKey(String lastKey)
		{
			this.lastKey = lastKey;
			dump();
		}
		
		public Point3D getLocation()
		{
			return location;
		}
		
		public void setLocation(Point3D location)
		{
			this.location = location;
			where_i_am = location;
			dump();
		}
		
		public void updateLocation(Point3D deltas)
		{
			setLocation(new Point3D(location.x + deltas.x, location.y + deltas.y, location.z + deltas.z));
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
			 * Per http://gamedev.stackexchange.com/questions/17005/solving-for-
			 * velocity-in-the-x-y-z-axes
			 * 
			 * x = cos(theta) * cos(phi); y = sin(theta) * cos(phi); z =
			 * sin(phi);
			 */
			double radDir = Math.toRadians(direction);
			double radEl = Math.toRadians(elevation);
			
			perpVec = new Point3D(Math.cos(radDir) * Math.cos(radEl), Math.sin(radEl),
					Math.sin(radDir) * Math.cos(radEl));
			travelVec = new Point3D(Math.sin(radDir), 0, -Math.cos(radDir));
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
			updateLocation(new Point3D(distance * perpVec.x, distance * perpVec.y, distance * perpVec.z));
		}
		
		public void moveLeft(double distance)
		{
			moveRight(-distance);
		}
		
		public void moveForward(double distance)
		{
			updateLocation(new Point3D(distance * travelVec.x, distance * travelVec.y, distance * travelVec.z));
		}
		
		public void moveBackward(double distance)
		{
			moveForward(-distance);
		}
		
		public void move(double distance)
		{
			updateLocation(new Point3D(distance * travelVec.x, distance * travelVec.y, distance * travelVec.z));
		}
		
		public void home()
		{
			setLocation(this.home);
			setDirection(0);
			dump();
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
		
		public double	direction	= 0.0;					// degrees 0...360
		public double	elevation	= 0.0;					// degrees -90...90
		public String	lastKey		= "<none>";
		public Point3D	home		= BY_MY_MAILBOX;
		public Point3D	location	= BY_MY_MAILBOX;
		public Point3D	travelVec	= new Point3D(1, 0, 0);
		
		public Point3D getTravelVec()
		{
			return travelVec;
		}
		
		public void setTravelVec(Point3D travelVec)
		{
			this.travelVec = travelVec;
		}
		
		public Point3D perpVec = new Point3D(0, 0, 1);
		
		public void dump()
		{
			System.out.println("Key pressed: " + lastKey);
			System.out.println("Location: " + location.toString());
			System.out.println("Direction: " + direction);
			System.out.println("Travel Vector: " + travelVec.toString());
			System.out.println("Perp Vector: " + perpVec.toString());
			System.out.println("====================");
		}
	};
	
	@SuppressWarnings("serial")
	
	Map<Integer, Runnable> keyHandlers = new HashMap<Integer, Runnable>()
	{
		{
			/*
			 * a Move left d Move right w Move forward s Move backward q Turn
			 * left e Turn right r Move up f Move down h Return to the original
			 * “home” position and orientation o Switch to orthographic
			 * projection p Switch to perspective projection
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
}
