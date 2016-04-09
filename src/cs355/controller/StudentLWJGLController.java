package cs355.controller;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.HashMap;
import java.util.Map;

//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your milage may vary. Don't feel restricted by this list of imports.
import org.lwjgl.input.Keyboard;

import cs355.model.scene.HouseModel;
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
	private WireFrame model = new HouseModel();
	
	// This method is called to "resize" the viewport to match the screen.
	// When you first start, have it be in perspective mode.
	@Override
	public void resizeGL()
	{
		
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
	
	void handleKey(int key)
	{
		switch (key)
		{
			
		}
	}
	
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
	}
	
}
