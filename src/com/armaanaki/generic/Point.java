/*
 * 	Class: CCS1302/08
 * 	Term: Spring 2017
 * 	Name: Armaan Esfahani
 *  Instructor: Monisha Verma
 *  Assignment: Maze Project
 */

package com.armaanaki.generic;

public class Point {
	
	private double x; //x coordinate of point
	private double y; //y coordinate of point
	
	//default constructor
	public Point() {
	}
	
	//constructor that allows only a point to be assigned, used for start and end point
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	//return X value of the point
	public double getX() {
		return x;
	}
	
	//return Y value of the point
	public double getY() {
		return y;
	}
	
	//set X value of the point
	public void setX(double x) {
		this.x = x;
	}
	
	//set Y value of the point
	public void setY(double y) {
		this.y = y;
	}
	
	//static method to compare any two given points
	public static boolean comparePosition(Point node1, Point node2) {
		return node1.getX() == node2.getX() && node1.getY() == node2.getY();
	}
	

	//method to compare this points position with another point, calls the static method
	public boolean comparePosition(Point o) {
		return comparePosition(this, o);
	}
	
	//static method to determine the distance between any two points
	public static double distanceBetween(Point node1, Point node2) {
		double diffX = Math.pow(node1.getX() - node2.getX(), 2);
		double diffY = Math.pow(node1.getX() - node2.getY(), 2); 
		return Math.sqrt(diffX + diffY);
	}
	
	//method to determine distance between this point and another
	public double distanceBetween(Point o) {
		return distanceBetween(this, o);
	}
	
	//To string to print in the format of: (x, y)
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
