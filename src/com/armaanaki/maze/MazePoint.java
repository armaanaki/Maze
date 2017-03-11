/*
 * 	Class: CCS1302/08
 * 	Term: Spring 2017
 * 	Name: Armaan Esfahani
 *  Instructor: Monisha Verma
 *  Assignment: Maze Project
 */

package com.armaanaki.maze;

import com.armaanaki.generic.Point;

//this method extends Point as Point is a generic point that may be used in future projects
public class MazePoint extends Point {
	
	private double g; // distance moved from start
	private double f; // total cost (g + distance to end)
	private MazePoint parent; //the point that led to this one, allows us to chain back to start
	
	//set a MazePoint with no parent
	public MazePoint(int x, int y) {
		super(x, y);
	}
	
	//constructor to instantiate a point and set it's parent point
	public MazePoint(int x, int y, MazePoint parent) {
		super(x, y);
		this.parent = parent;
		this.g = parent.getSteps() + 1; //set steps to be one more than the parents
	}
	
	//return the parent of the point
	public MazePoint getParent() {
		return parent;
	}
	
	//method to return the total steps taken to reach this current node
	public double getSteps() {
		return g;
	}
	
	//return the f (efficiency) of this point
	public double getEfficiency() {
		return f;
	}
	
	//method used to manually set the efficiency
	public void setEfficiency(double f) {
		this.f = f;
	}
}
