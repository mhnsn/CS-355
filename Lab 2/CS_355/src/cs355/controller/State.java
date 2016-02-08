package cs355.controller;

public class State
{
	// create a current state object
	static String current;
	
	// enumerate states
	final static String init 				= "init";
	final static String drawing 			= "drawing";
	final static String submitShapeToModel 	= "submitShapeToModel";

	//create initializer
	State()
	{
		current = init;
	}
}
