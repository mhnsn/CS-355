package cs355.solution;

import java.util.logging.Level;

import cs355.controller.CS355LWJGLController;
import cs355.controller.StudentLWJGLController;
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
    catch(Exception ex) 
    {
      ex.printStackTrace();
    }
    finally {
      if(main != null) 
      {
        main.destroy();
      }
    }
  }
    
}
