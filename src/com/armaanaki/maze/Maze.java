/*
 * 	Class: CCS1302/08
 * 	Term: Spring 2017
 * 	Name: Armaan Esfahani
 *  Instructor: Monisha Verma
 *  Assignment: Maze Project
 */

package com.armaanaki.maze;

import java.util.ArrayList;
import java.util.Arrays;

public class Maze {
	
	
	//								//
	//								//
	//			Class Variables		//
	//								//
	//								//
	
	private MazePoint start; //mazepoint that represents the start of the maze
	private MazePoint end; //mazepoint that represents the end of the maze
	private ArrayList<MazePoint> path = new ArrayList<MazePoint>(); //MazePoints to find the correct Path
	private int[][] maze; //the maze itself
	private int[][] stepMaze; //maze made for taking steps
	private int ratX = -1; //x position for the "takeStep" method
	private int ratY = -1; //y position for the "takeStep" method
	
	
	//								//
	//								//
	//			Constructors		//
	//								//
	//								//
	
	
	//constructor to initialize the maze, if will see if the maze is solvable-- if not it will throw an error
	public Maze(int[][] maze) throws Exception {
		this.maze = maze;
		guessStart();
		guessEnd();
		findPath();
		path.add(0, start); //add the start to the path, as it must be apart of it
		initStepMaze();
	}
	
	//constructor that allows you to set a start and end position with a MazePoint
	public Maze(int[][] maze, MazePoint start, MazePoint end) throws Exception {
		this.maze = maze;
		setStart(start);
		setEnd(end);
		findPath();
		path.add(0, start); //add the end to the path, as it must be apart of it
		initStepMaze();
	}
	
	//constructor that allows you to set a start and end position with integers
	public Maze(int[][] maze, int startX, int startY, int endX, int endY) throws Exception {
		this(maze, new MazePoint(startX, startY), new MazePoint(endX, endY));
	}
	
	
	//								//
	//								//
	//			Get & Set			//
	//								//
	//								//
	
	
	//method to set start based off x and y values
	public void setStart(int x, int y) throws PointNotValidException {
		setStart(new MazePoint(x, y));
	}
	
	//method to set start based off a MazePoint
	public void setStart(MazePoint start) throws PointNotValidException {
		if (maze[(int)start.getY()][(int)start.getX()] == 1)
			this.start = start;
		else
			throw new PointNotValidException("The selected start point is not valid!");
	}
	
	//method to set end based off x and y values
	public void setEnd(int x, int y) throws PointNotValidException {
		setEnd(new MazePoint(x, y));
	}
	
	//method to set end based off a MazePoint
	private void setEnd(MazePoint end) throws PointNotValidException {
		if (maze[(int)end.getY()][(int)end.getX()] == 1)
			this.end = end;
		else
			throw new PointNotValidException("The selected end point is not valid!");
	}
	
	
	//								//	
	//								//
	//		Main Logic Method		//
	//								//		
	//								//
	
	
	//method to find the most efficient path
	private void findPath() throws NoValidPathException {
		ArrayList<MazePoint> open = new ArrayList<MazePoint>(); //array list of possible points
		ArrayList<MazePoint> close = new ArrayList<MazePoint>(); //array list of points that have been completed
		open.add(start); //add intial point so program may generate more
		
		while(!open.isEmpty()) {
			MazePoint mostEfficient = mostEfficientPoint(open); //find the most efficient point in open list
			open.remove(mostEfficient); //remove most efficient point as it is no longer considered "open"
			ArrayList<MazePoint> possibleMoves = possibleMoves(mostEfficient); //generate a list of possible moves from the point

			for (MazePoint possibility : possibleMoves) {
				//check if the point is the end, if it is, add the path to the array list and exit this method
				if (possibility.comparePosition(end)) { 
					addToPath(possibility);
					return;
				}
				possibility.setEfficiency(possibility.getSteps() + MazePoint.distanceBetween(possibility, end)); //generate the efficiency of the point using g + h = f
				
				//make sure a more efficient version of this point wasn't already in use
				if (containsMoreEfficient(possibility, open) || containsMoreEfficient(possibility, close))
					continue;
				else
					open.add(possibility);
			}
			close.add(mostEfficient); //add the current point to closed list
		}
		throw new NoValidPathException("No valid path!"); //if we fail to find the exit after every possibility, tell user the maze is unsolvable and invalid
	}
	
	//								//
	//								//
	//			Display Methods		//
	//								//
	//								//
	
	
	//default display maze method, simply shows the maze
	public void displayMaze() {
		displayMaze(maze);
	}
	
	//method to change the path to '2' in a new array and have it printed
	public void findExit() {
		ratX = -1; //avoid printing an @ sign if they had partially taken steps
		ratY= -1; 
		int[][] pathedMaze = mazeCopy();
		for (MazePoint point : path)
			pathedMaze[(int) point.getY()][(int) point.getX()] = 2;
		displayMaze(pathedMaze);
	}
	
	//method to show the path taken up till now
	public void displayPath() {
		displayMaze(stepMaze);
	}
	
	//method to move the mouse one step in the path then print it's position
	public boolean takeStep() {
		for (MazePoint testPoint : path) {
			if (stepMaze[(int) testPoint.getY()][(int) testPoint.getX()] == 3) {
				stepMaze[(int) testPoint.getY()][(int) testPoint.getX()] = 2;
				ratX = (int) testPoint.getX();
				ratY = (int) testPoint.getY();
				displayPath();
				break;
			}
		}
		return isExit();
	}
	
	
	//								//
	//								//
	//		Pure Helper Methods		//
	//								//
	//								//
	
	
	//helper method to print out a maze, depending on the type of array given
	private void displayMaze (int[][] maze) {
		for (int y = 0; y < maze.length; y++) {
			for (int x = 0; x < maze[y].length; x++) {
				int mazeValue = maze[y][x];
				if (x == ratX && y == ratY) {
					System.out.print("@");
				} else if (mazeValue == 0) {
					System.out.print("█");
				} else if (mazeValue == 2) {
					System.out.print("~");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	//method to find the most efficient point in a given list
	private MazePoint mostEfficientPoint(ArrayList<MazePoint> array) {
		MazePoint mostEfficient = array.get(0);
		double lowestEfficiency = mostEfficient.getEfficiency();
		
		for (MazePoint possibility : array) {
			if (possibility.getEfficiency() < lowestEfficiency) {
				mostEfficient = possibility;
				lowestEfficiency = possibility.getEfficiency();
			}
		}
		
		return mostEfficient;
	}
	
	//method to return an arraylist of valid and possible moves
	private ArrayList<MazePoint> possibleMoves(MazePoint testPoint) {
		ArrayList<MazePoint> possibleMoves = new ArrayList<MazePoint>();
		ArrayList<MazePoint> validMoves = new ArrayList<MazePoint>();
		
		possibleMoves.add(new MazePoint((int) testPoint.getX(), (int) testPoint.getY() + 1, testPoint));
		possibleMoves.add(new MazePoint((int) testPoint.getX(), (int) testPoint.getY() - 1, testPoint));
		possibleMoves.add(new MazePoint((int) testPoint.getX() + 1, (int) testPoint.getY(), testPoint));
		possibleMoves.add(new MazePoint((int) testPoint.getX() - 1, (int) testPoint.getY(), testPoint));
		
		for(MazePoint currentTest : possibleMoves) {
			try {
				if (maze[(int) currentTest.getY()][(int) currentTest.getX()] != 0) {
					validMoves.add(currentTest);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		
		return validMoves;
	}
	
	
	//method to check if an arraylist contains a more efficient point
	private boolean containsMoreEfficient(MazePoint point1, ArrayList<MazePoint> array) {
		for (MazePoint point2 : array) {
			if (MazePoint.comparePosition(point1, point2) && point2.getEfficiency() < point1.getEfficiency()){
				return true;
			}
		}
		return false;
	}
	
	
	//method to add to the path based on the parents of the last point
	private void addToPath(MazePoint finalPoint) {
		while (finalPoint.getParent() != null) {
			path.add(0, finalPoint);
			finalPoint = finalPoint.getParent();
		}
	}
	
	//method to give copy of maze array
	private int[][] mazeCopy() {
		int[][] copy = new int[maze.length][];
		for (int i = 0; i < maze.length; i++) {
			copy[i] = Arrays.copyOf(maze[i], maze[i].length);
		}
		
		return copy;
	}
	
	//method used to guess the start based off the current maze (bottom left square)
	private void guessStart() throws PointNotValidException{
		setStart(0, maze.length - 1);
	}
	
	//method used to guess end based off current maze (only open space on right wall)
	private void guessEnd() throws PointNotValidException {
		for (int y = 0; y < maze.length; y++) {
			if (maze[y][maze[0].length - 1] == 1) {
				setEnd(maze[0].length - 1, y);
				return;
			}
		}
		throw new PointNotValidException("End point not found!");
	}
	
	//method to init takeStep maze
	private void initStepMaze() {
		stepMaze = mazeCopy();
		for (MazePoint point : path)
			stepMaze[(int) point.getY()][(int) point.getX()] = 3;
	}
	
	//method to check if rat is at exit
	private boolean isExit() {
		return ratX == end.getX() && ratY == end.getY();
	}
}
