package cs355;

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
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.awt.Color;
import java.util.Iterator;

import cs355.controller.CS355LWJGLController;
import cs355.controller.CurrentCameraState;
import cs355.controller.GUIController;
import cs355.controller.KeyboardInterface;
import cs355.controller.StateMachine;
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
	private static final Point3D	WHERE_HOME_IS	= new Point3D(0, 0, 0);

	private static final int		DISPLAY_WIDTH	= 640;
	private static final int		DISPLAY_HEIGHT	= 480;
	public static CS355Scene		scene			= new CS355Scene();
	private static final Point3D	BY_MY_MAILBOX	= new Point3D(0, 2, 30);
	private static final Point3D	UP				= new Point3D(0, 1, 0);

	private static final double		NO_ROTATION		= 0;
	private static final int		PERSPECTIVE		= 0;
	private static final int		ORTHOGRAPHIC	= 1;
	public static Point3D			where_i_am;
	
	public static void takeMeHome()
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
		KeyboardInterface.getCurState().setLocation(where_i_am);
		KeyboardInterface.getCurState().setDirection(0);
	}

	/**
	 * This is a model of a house. It has a single method that returns an
	 * iterator full of Line3Ds. A "Line3D" is a wrapper class around two
	 * Point2Ds. It should all be fairly intuitive if you look at those classes.
	 * If not, I apologize.
	 */
	private WireFrame	model					= new HouseModel();
	private Instance	home					= new Instance(Color.white, WHERE_HOME_IS, 0, 1, model);

	Point3D				jonesHome				= new Point3D(-15, 0, 0);
	Instance			theJones				= new Instance(Color.yellow, jonesHome, 0, 1, model);
	Point3D				thePigsty				= new Point3D(15, 0, 0);
	Instance			timonAndPumbaa			= new Instance(Color.green, thePigsty, 0, 1, model);
	Point3D				smithHome				= new Point3D(0, 0, -100);
	Instance			theSmiths				= new Instance(Color.CYAN, smithHome, 180, 1, model);
	Point3D				theMayorsHouse			= new Point3D(-15, 0, -100);
	Instance			mayorMayor				= new Instance(Color.orange, theMayorsHouse, 180, 1, model);
	Point3D				doNotVisit				= new Point3D(15, 0, -100);

	Instance			crazyOldManMarley		= new Instance(Color.gray, doNotVisit, 180, 1, model);
	Point3D				highSecurityLocation	= new Point3D(-50, 5, -50);

	Instance			misterPresident			= new Instance(Color.white, highSecurityLocation, 90, 5, model);
	
	KeyboardInterface	keyboardInterface		= new KeyboardInterface();

	private Object		curState;

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

	private void drawStreet()
	{
		for (Instance i : scene.instances())
		{
			drawInstance(i);
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
		switch (KeyboardInterface.getCurState().getMode())
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
			curState = new CurrentCameraState(BY_MY_MAILBOX, 0, 0);
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

	@Override
	public void update()
	{
		
	};
	
	/**
	 * This is called every frame, and should be responsible for keyboard //
	 * updates. // An example keyboard event is captured below. // The
	 * "Keyboard" static class should contain everything you need to finish //
	 * this up.
	 *
	 * @deprecated Use
	 *             {@link cs355.controller.KeyboardInterface#handleInput(cs355.StudentLWJGLController)}
	 *             instead
	 *
	 */
	@Deprecated
	@Override
	public void updateKeyboard()
	{
		/**
		 * IMPORTANT NOTE: THIS CODE ONLY SHARES A KEYBOARD INTERFACE WITH THE
		 * REST OF THE MAIL SHELL FOR FUNCTIONALITY OF LAB 5. THE RENDERING IN
		 * THE MAIN SHELL IS DONE ACCORDING TO SPEC FOR LAB 5.
		 *
		 * The keyboard parsing has been offloaded to an external class to make
		 * this point crystal clear. The updateKeyboard method here defers to
		 * the external KeyboardInterface class for processing.
		 */

		keyboardInterface.handleInput(this);
	}
}
