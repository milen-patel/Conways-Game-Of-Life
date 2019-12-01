package GamePackage;
// Questions:
// Does the grid always have to be a square
// Can i model the grid from the top left or does it have to be the middle
// TODO: Why does repainting print "Repainting Display" twice
// TODO: Make console print more detailed information
// TODO: Make 'Event' based classes instead of just passing strings
// TODO: Change layout to gridbag layout

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Model {
	/* Define instance variables */
	/* Create a 2d-array to model the maximum sized board (500x500)
	 * true --> cell is alive
	 * false --> cell is dead
	 */
	boolean[][] gridModel;
	private List<ModelObserver> observers;
	
	/* Create a constructor */
	public Model() {
		/* Instantiate 2d-array */
		gridModel = new boolean[10][10];
		observers = new ArrayList<ModelObserver>();

	}
	
	/* Getters */
	public int getCurrentXSize() { return gridModel[0].length-1; }
	public int getCurrentYSize() { return gridModel.length-1; }
	
	/* Returns if the spot is alive
	 * (x,y) is a spot in the array itself
	 * (0,0) is the top left spot
	 * (250,250) is the bottom right spot
	 */
	public boolean getIsSpotAlive(int x, int y) { return this.gridModel[y][x]; }
	
	
	/* Setters */
	public void changeBoardSize(int newX, int newY) {
		if (newX < 10 || newX > 500 || newY < 10 || newY > 500) {
			throw new RuntimeException("Invalid board dimensions");
		}
		
		/* Reset all the pieces on the board */
		gridModel = new boolean[newY][newX];
		/* Notify observers */
		notifyObservers("newBoardSize");
	}
	
	public void toggleSpot(int x, int y) {
		gridModel[y][x] = !gridModel[y][x];
		/* Notify Observesrs */
		notifyObservers("spot_changed");
	}
	
	public void makeNextMove() {
		/* Create a new grid model that will replace the old grid model */
		boolean arr[][] = new boolean[this.gridModel.length][this.gridModel[0].length];
		/* Loop over the entire board */
		for (int y=0; y<this.gridModel.length; y++) {
			for (int x=0; x<this.gridModel[0].length; x++) {
				System.out.println("(" + x + ", " + y + ")");
				//1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
				System.out.println("A");
				if (getIsSpotAlive(x,y) && getNumNeighbors(x,y)<2) {
					arr[y][x] = false;
					continue;
				}
				System.out.println("B");

				//2. Any live cell with two or three live neighbours lives on to the next generation.
				if (getIsSpotAlive(x,y) && (getNumNeighbors(x,y)==2 || getNumNeighbors(x,y)==3)) {
					arr[y][x] = true;
					continue;
				}
				System.out.println("C");

				//3. Any live cell with more than three live neighbours dies, as if by overpopulation.
				if (getIsSpotAlive(x,y) && getNumNeighbors(x,y)>3) {
					arr[y][x] = false;
					continue;
				}
				System.out.println("D");

				//4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
				if (!getIsSpotAlive(x,y) && getNumNeighbors(x,y)==3) {
					arr[y][x] = true;
					continue;
				}
				
				System.out.println("E");
			}
		}
		
		/* Change instance variable */
		this.gridModel = arr;
		/* Notify Observers */
		//TODO Test options of notification
		notifyObservers("newBoardSize");
	}
	
	
	public void randomizeBoard() {
		/* Start by resetting the board */
		this.gridModel = new boolean[this.gridModel.length][this.gridModel[0].length];
		/* Now loop over the board and randomize it */
		for (int y=0; y < this.gridModel.length; y++) {
			for (int x=0; x < this.gridModel[y].length; x++) {
				if (Math.random() >= 0.50) {
					this.gridModel[y][x] = true;
				}
			}
		}
		/* Now notify observers */
		notifyObservers("spot_changed");
	}

	
	/* Observable Methods */
	public void addModelObserver(ModelObserver o) { this.observers.add(o); }
	public void removeModelObserver(ModelObserver o) { this.observers.remove(o); }
	public void notifyObservers(String event) {
		for (ModelObserver o : observers) {
			if (event.contentEquals("newBoardSize")) {
				o.newBoardSize();
			} else if (event.contentEquals("spot_changed")) {
				o.spotChanged();
			}
		}
	}

	public void reset() {
		for(int x=0; x<gridModel.length;x++) {
			for(int y=0; y<gridModel[x].length; y++) {
				gridModel[x][y] = false;
			}
		}
		//System.out.println(Arrays.deepToString(gridModel));
		notifyObservers("spot_changed");
	}
	
	public int getNumNeighbors(int x, int y) {
		int currentNeighbors = 0;
		//System.out.println("Case 1");
		//Check directly above, if possible
		if (y != 0 && getIsSpotAlive(x,y-1))
			currentNeighbors++;
		//System.out.println("Case 2");

		//Check directly below, if possible
		if (y != getCurrentYSize() && getIsSpotAlive(x,y+1))
			currentNeighbors++;
		//System.out.println("Case 3");

		//Check directly to the right, if possible
		System.out.println("Curr x vs size:"+  x + " : " + getCurrentXSize());
		if (x != getCurrentXSize() && getIsSpotAlive(x+1,y))
			currentNeighbors++;
		//System.out.println("Case 4");

		//Check directly to the left, if possible
		if (x != 0 && getIsSpotAlive(x-1,y))
			currentNeighbors++;
		//System.out.println("Case 5");

		//Check the diagonal Top Right spot, if possible
		if (x != getCurrentXSize() && y != 0 && getIsSpotAlive(x+1,y-1))
			currentNeighbors++;
		//System.out.println("Case 6");

		//Check the diagonal Top Left spot, if possible
		if (x != 0 && y != 0 && getIsSpotAlive(x-1,y-1))
			currentNeighbors++;
		//System.out.println("Case 7");

		//Check the diagonal Bottom Right, if possible
		if (x != getCurrentXSize() && y != getCurrentYSize() && getIsSpotAlive(x+1,y+1))
			currentNeighbors++;
		//System.out.println("Case 8");

		//Check the diagonal Bottom Left, if possible
		if (x != 0 && y != getCurrentYSize() && getIsSpotAlive(x-1,y+1))
			currentNeighbors++;
		//System.out.println("Case 9");

		return currentNeighbors;
	}
}
