package cs355.controller;

//import static org.lwjgl.opengl.GL11.*;

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
	
	// This is a model of a house.
	// It has a single method that returns an iterator full of Line3Ds.
	// A "Line3D" is a wrapper class around two Point2Ds.
	// It should all be fairly intuitive if you look at those classes.
	// If not, I apologize.
	private WireFrame				model			= new HouseModel();
	private Instance				home			= new Instance(Color.white, WHERE_HOME_IS, 0, 1, model);
	
	private static final int		DISPLAY_WIDTH	= 640;
	private static final int		DISPLAY_HEIGHT	= 480;
	private static CS355Scene		scene			= new CS355Scene();
	
	private static final Point3D	WHERE_HOME_IS	= new Point3D(0, 0, 0);
	private static final Point3D	BY_MY_MAILBOX	= new Point3D(0, -5, -30);
	private static final Point3D	UP				= new Point3D(0, 1, 0);
	
	private static final double		NO_ROTATION		= 0;
	
	private static final int		PERSPECTIVE		= 0;
	private static final int		ORTHOGRAPHIC	= 1;
	private int						mode			= PERSPECTIVE;
	
	// This method is called to "resize" the viewport to match the screen.
	// When you first start, have it be in perspective mode.
	@Override
	public void resizeGL()
	{
		scene.setCameraPosition(new Point3D(0, 0, 0));
		scene.setCameraRotation(0);
		scene.addInstance(home);
		
		glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		takeMeHome();
	}
	
	private void takeMeHome()
	{
		scene.setCameraPosition(BY_MY_MAILBOX); // Phil Collins would be proud.
		scene.setCameraRotation(NO_ROTATION); // also, you are outside a white
												// house.
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glMatrixMode(GL_MODELVIEW);
		gluLookAt((float) BY_MY_MAILBOX.x, (float) BY_MY_MAILBOX.y, (float) BY_MY_MAILBOX.z, (float) WHERE_HOME_IS.x,
				(float) WHERE_HOME_IS.y, (float) WHERE_HOME_IS.z, (float) UP.x, (float) UP.y, (float) UP.z); // I
		// recommend
		// you
		// NOT
		// float
		// up.
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
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		// handle different viewing modes here.
		switch (mode)
		{
			case PERSPECTIVE:
				gluPerspective(45f, 1f, 0.01f, 50f);
				break;
			case ORTHOGRAPHIC:
				// glOrtho();
				break;
		}
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		Point3D cameraLoc = scene.getCameraPosition();
		glRotatef((float) scene.getCameraRotation(), 0f, 0f, 0f);
		glTranslatef((float) cameraLoc.x, (float) cameraLoc.y, (float) cameraLoc.z);
		
		drawStreet();
	}
	
	private void drawStreet()
	{
		for (Instance i : scene.instances())
		{
			glPushMatrix();
			drawInstance(i);
			glPopMatrix();
		}
	}
	
	private void drawInstance(Instance i)
	{
		/**
		 * this may not be necessary, but is useful for pipelining.
		 * 
		 * push the instance transformation
		 * 
		 * draw instance
		 * 
		 * pop the instance transformation
		 */
		glBegin(GL_LINES);
		
		Iterator<Line3D> it = i.getModel().getLines();
		Color c = i.getColor();
		Point3D p = i.getPosition();
		float r = (float) i.getRotAngle();
		double s = i.getScale();
		
		glColor3d(c.getRed(), c.getBlue(), c.getGreen());
		glTranslatef((float) -p.x, (float) -p.y, (float) -p.z);
		glRotatef(r, -0f, -0f, -0f);
		glScaled(s, s, s);
		
		while (it.hasNext())
		{
			Line3D cur = it.next();
			glVertex3d(cur.start.x, cur.start.y, cur.start.z);
			glVertex3d(cur.end.x, cur.end.y, cur.end.z);
		}
		
		glEnd();
	}
}
