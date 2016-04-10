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
	
	void doIt(String x)
	{
		System.out.println("Key is pressed: " + x);
	}
	
	// This is called every frame, and should be responsible for keyboard
	// updates.
	// An example keyboard event is captured below.
	// The "Keyboard" static class should contain everything you need to finish
	// this up.
	@SuppressWarnings("serial")
	Map<Integer, Runnable> keyHandlers = new HashMap<Integer, Runnable>()
	{
		{
			put(new Integer(Keyboard.KEY_A), new Runnable()
			{
				public void run()
				{
					doIt("A");
				}
			});
			put(new Integer(Keyboard.KEY_D), new Runnable()
			{
				public void run()
				{
					doIt("D");
				}
			});
			put(new Integer(Keyboard.KEY_W), new Runnable()
			{
				public void run()
				{
					doIt("W");
				}
			});
			put(new Integer(Keyboard.KEY_S), new Runnable()
			{
				public void run()
				{
					doIt("S");
				}
			});
			put(new Integer(Keyboard.KEY_Q), new Runnable()
			{
				public void run()
				{
					doIt("Q");
				}
			});
			put(new Integer(Keyboard.KEY_E), new Runnable()
			{
				public void run()
				{
					doIt("E");
				}
			});
			put(new Integer(Keyboard.KEY_R), new Runnable()
			{
				public void run()
				{
					doIt("R");
				}
			});
			put(new Integer(Keyboard.KEY_F), new Runnable()
			{
				public void run()
				{
					doIt("F");
				}
			});
			put(new Integer(Keyboard.KEY_H), new Runnable()
			{
				public void run()
				{
					doIt("H");
				}
			});
			put(new Integer(Keyboard.KEY_O), new Runnable()
			{
				public void run()
				{
					doIt("O");
				}
			});
			put(new Integer(Keyboard.KEY_P), new Runnable()
			{
				public void run()
				{
					doIt("P");
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
