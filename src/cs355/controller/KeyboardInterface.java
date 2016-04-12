package cs355.controller;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.*;

import cs355.StudentLWJGLController;
import cs355.model.scene.Point3D;

public class KeyboardInterface
{
	private static CurrentCameraState curState;

	/**
	 * a Move left d Move right w Move forward s Move backward q Turn left e
	 * Turn right r Move up f Move down h Return to the original "home" position
	 * and orientation o Switch to orthographic projection p Switch to
	 * perspective projection
	 */
	@SuppressWarnings("serial")
	public static HashMap<Integer, Runnable> keyHandlers = new HashMap<Integer, Runnable>()
	{
		{
			put(new Integer(Keyboard.KEY_A), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("A");
					getCurState().moveLeft(1.0);
				}
			});
			put(new Integer(KeyEvent.VK_A), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("A");
					getCurState().moveLeft(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_D), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("D");
					getCurState().moveRight(1.0);
				}
			});
			put(new Integer(KeyEvent.VK_D), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("D");
					getCurState().moveRight(1.0);
				}
			});
			put(new Integer(Keyboard.KEY_W), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("W");
					getCurState().moveForward(1.0);
				}
			});
			put(new Integer(KeyEvent.VK_W), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("W");
					getCurState().moveForward(1.0);
				}
			});
			
			put(new Integer(Keyboard.KEY_S), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("S");
					getCurState().moveBackward(1.0);
				}
			});
			put(new Integer(KeyEvent.VK_S), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("S");
					getCurState().moveBackward(1.0);
				}
			});
			
			put(new Integer(Keyboard.KEY_Q), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("Q");
					getCurState().turnLeft(1.0);
				}
			});

			put(new Integer(KeyEvent.VK_Q), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("Q");
					getCurState().turnLeft(1.0);
				}
			});
			
			put(new Integer(Keyboard.KEY_E), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("E");
					getCurState().turnRight(1.0);
				}
			});
			put(new Integer(KeyEvent.VK_E), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("E");
					getCurState().turnRight(1.0);
				}
			});
			
			put(new Integer(Keyboard.KEY_R), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("R");
					getCurState().moveUp(1.0);
				}
			});
			put(new Integer(KeyEvent.VK_R), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("R");
					getCurState().moveUp(1.0);
				}
			});
			
			put(new Integer(Keyboard.KEY_F), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("F");
					getCurState().moveDown(1.0);
				}
			});
			put(new Integer(KeyEvent.VK_F), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("F");
					getCurState().moveDown(1.0);
				}
			});
			
			put(new Integer(Keyboard.KEY_H), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("H");
					// getCurState().home();
					StudentLWJGLController.takeMeHome();
				}
			});
			put(new Integer(KeyEvent.VK_H), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("H");
					getCurState().home();
				}
			});
			
			put(new Integer(Keyboard.KEY_O), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("O");
					getCurState().orthographic();
				}
			});
			put(new Integer(Keyboard.KEY_P), new Runnable()
			{
				@Override
				public void run()
				{
					getCurState().setLastKey("P");
					getCurState().perspective();
				}
			});
		}
	};
	
	/**
	 * @return the curState
	 */
	public static CurrentCameraState getCurState()
	{
		return curState;
	}

	public static void handleInput(Iterator<Integer> it)
	{
		int curKey;
		while (it.hasNext())
		{
			curKey = it.next();
			
			for (Map.Entry<Integer, Runnable> k : keyHandlers.entrySet())
			{
				if (k.getKey() == curKey && !curState.getLastKey().equals(k))
				{
					k.getValue().run();
					break;
				}
			}
		}
	}
	
	/**
	 * @param curState
	 *            the curState to set
	 */
	public static void setCurState(CurrentCameraState curState)
	{
		KeyboardInterface.curState = curState;
	}
	
	public KeyboardInterface()
	{
		curState = new CurrentCameraState(new Point3D(0, 0, 0));
	}
	
	/**
	 * This is called every frame, and should be responsible for keyboard //
	 * updates. // An example keyboard event is captured below. // The
	 * "Keyboard" static class should contain everything you need to finish //
	 * this up.
	 *
	 * @param which
	 *            controller to update
	 *
	 */
	
	public void handleInput(StudentLWJGLController lwjgl)
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
}