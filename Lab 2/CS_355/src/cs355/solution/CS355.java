package cs355.solution;

import cs355.GUIFunctions;
import cs355.controller.GUIController;
import cs355.model.drawing.GUIModel;
import cs355.view.GUIViewRefresher;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {

	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	static GUIModel 		model;
	static GUIViewRefresher vr;
	static GUIController 	c;
	
	public static void main(String[] args) {

		// Fill in the parameters below with your controller and view.
		model 	= new GUIModel();
		vr		= new GUIViewRefresher();
		c		= new GUIController();
		
		model.addObserver(vr);
		c.setModelView(model, vr);
		
		GUIFunctions.createCS355Frame(c, vr);
		GUIFunctions.changeSelectedColor(GUIController.getCurrentColor());
		GUIFunctions.refresh();	}
}
