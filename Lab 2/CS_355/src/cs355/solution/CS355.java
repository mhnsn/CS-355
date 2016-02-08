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
	public static void main(String[] args) {

		// Fill in the parameters below with your controller and view.
		GUIModel model = new GUIModel();
		
		GUIFunctions.createCS355Frame(new GUIController(), new GUIViewRefresher());

		GUIFunctions.refresh();
	}
}
