package cs355.solution;

import cs355.StudentLWJGLController;
import cs355.view.LWJGLSandbox;

/**
 *
 * @author Brennan Smith
 */
public class CS355LWJGL
{
	
	public static void main(String[] args)
	{
		LWJGLSandbox main = null;
		try
		{
			main = new LWJGLSandbox();
			main.create(new StudentLWJGLController());
			main.run();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (main != null)
			{
				main.destroy();
			}
		}
	}
	
}
