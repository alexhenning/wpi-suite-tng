package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Color;

/**
 * Custom color for invalid fields
 * @author Christian
 *
 */
public class SparkleRed {

	/**
	 * Constructor of SparkleRed class
	 */
	public SparkleRed(){
		getColor();
	}
	
	/**
	 * 
	 * @return Custom Color
	 */
	public Color getColor(){
		return new Color(250,128,114);
	}
}
