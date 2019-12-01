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
	private int surviveThresholdLow;
	private int surviveThresholdHigh;
	private int birthThresholdLow;
	private int birthThresholdHigh;
	private boolean isTorus;
	
	/* Create a constructor */
	public Model() {
		/* Instantiate 2d-array */
		gridModel = new boolean[10][10];
		observers = new ArrayList<ModelObserver>();
		surviveThresholdLow = 2;
		surviveThresholdHigh = 3;
		birthThresholdLow = 3;
		birthThresholdHigh = 3;
		isTorus = false;
	}
	
	/* Getters */
	public int getCurrentXSize() { return gridModel[0].length-1; }
	public int getCurrentYSize() { return gridModel.length-1; }
	public int getSurviveThresholdLow() { return surviveThresholdLow; }
	public int getSurviveThresholdHigh() { return surviveThresholdHigh; }
	public int getBirthThresholdLow() { return birthThresholdLow; }
	public int getBirthThresholdHigh() { return birthThresholdHigh; }
	public boolean getIsTorus() { return isTorus; }
	/* Returns if the spot is alive
	 * (x,y) is a spot in the array itself
	 * (0,0) is the top left spot
	 * (250,250) is the bottom right spot
	 */
	public boolean getIsSpotAlive(int x, int y, boolean[][] arr) { return arr[y][x]; }
	public boolean getIsSpotAlive(int x, int y) { return getIsSpotAlive(x,y,this.gridModel); }
	
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
				if (getIsSpotAlive(x,y,gridModel) && getNumNeighbors(x,y)<this.surviveThresholdLow) {
					arr[y][x] = false;
					continue;
				}
				//2. Any live cell with two or three live neighbours lives on to the next generation.
				if (getIsSpotAlive(x,y,gridModel) && (getNumNeighbors(x,y)<=this.surviveThresholdHigh && getNumNeighbors(x,y)>=this.surviveThresholdLow)) {
					arr[y][x] = true;
					continue;
				}
				//3. Any live cell with more than three live neighbours dies, as if by overpopulation.
				if (getIsSpotAlive(x,y,gridModel) && getNumNeighbors(x,y)>this.surviveThresholdHigh) {
					arr[y][x] = false;
					continue;
				}
				//4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
				if (!getIsSpotAlive(x,y,gridModel) && getNumNeighbors(x,y)>=this.birthThresholdLow && getNumNeighbors(x,y)<=this.birthThresholdHigh) {
					arr[y][x] = true;
					continue;
				}				
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
		/* Handle Torus Mode implementation */
		//if (isTorus) {
		//	// If it is Torus then there are two conditions
		//	/* Case 1: X is on the left side of the board */
		//	if (x == this.gridModel[0].length-1) {
		//		x = 0;
		//	} else if (x == 0) {
		//		/* Case 2: X is on the right side of the board */
		//		x = this.gridModel[0].length-1;
		//	}
		//}
		
		/* A less effective way of handling torus mode 
		 * For this method, paste 3 of the boards next to each other to mimick wrapping
		 * And then in the method, only refer to this temporary board
		 */
		// Step 1: Shift our x parameter so we are looking at the middle of the board
		x += this.gridModel[0].length;
		// Step 2: Create new boolean[][] with correct dimensions
		boolean[][] tempArr = new boolean[this.gridModel.length][this.gridModel[0].length*3];
		// Step 3: Population boolean[][]
		for (int tempy=0; tempy<gridModel.length; tempy++) {
			for (int tempx=0; tempx<gridModel[0].length; tempx++) {
				tempArr[tempy][tempx] = this.gridModel[tempy][tempx];
			}
		}
		for (int tempy=0; tempy<gridModel.length; tempy++) {
			for (int tempx=this.gridModel[0].length; tempx<2*gridModel[0].length; tempx++) {
				tempArr[tempy][tempx] = this.gridModel[tempy][tempx-this.gridModel[0].length];
			}
		}
		for (int tempy=0; tempy<gridModel.length; tempy++) {
			for (int tempx=2*this.gridModel[0].length; tempx<3*gridModel[0].length; tempx++) {
				tempArr[tempy][tempx] = this.gridModel[tempy][tempx-2*this.gridModel[0].length];
			}
		}
		int currentNeighbors = 0;
		//Check directly above, if possible
		if (y != 0 && getIsSpotAlive(x,y-1,tempArr))
			currentNeighbors++;

		//Check directly below, if possible
		if (y != getCurrentYSize() && getIsSpotAlive(x,y+1,tempArr))
			currentNeighbors++;

		//Check directly to the right, if possible
		System.out.println("Curr x vs size:"+  x + " : " + getCurrentXSize());
		if (x != getCurrentXSize() && getIsSpotAlive(x+1,y,tempArr))
			currentNeighbors++;

		//Check directly to the left, if possible
		if (x != 0 && getIsSpotAlive(x-1,y,tempArr))
			currentNeighbors++;

		//Check the diagonal Top Right spot, if possible
		if (x != getCurrentXSize() && y != 0 && getIsSpotAlive(x+1,y-1,tempArr))
			currentNeighbors++;

		//Check the diagonal Top Left spot, if possible
		if (x != 0 && y != 0 && getIsSpotAlive(x-1,y-1,tempArr))
			currentNeighbors++;

		//Check the diagonal Bottom Right, if possible
		if (x != getCurrentXSize() && y != getCurrentYSize() && getIsSpotAlive(x+1,y+1,tempArr))
			currentNeighbors++;

		//Check the diagonal Bottom Left, if possible
		if (x != 0 && y != getCurrentYSize() && getIsSpotAlive(x-1,y+1,tempArr))
			currentNeighbors++;

		return currentNeighbors;
	}


	public void changeThresholds(int newSurviveMin, int newSurviveMax, int newBirthMin, int newBirthMax) {
		if (newSurviveMin < 0 || newSurviveMax < 0 || newBirthMin < 0 || newBirthMax < 0)
			throw new RuntimeException("Bad threshold parameters passed");
		if (newSurviveMin > newSurviveMax)
			throw new RuntimeException("Bad threshold parameters passed");
		if (newBirthMin > newBirthMax)
			throw new RuntimeException("Bad threshold parameters passed");
		this.birthThresholdLow = newBirthMin;
		this.birthThresholdHigh = newBirthMax;
		this.surviveThresholdLow = newSurviveMin;
		this.surviveThresholdHigh = newSurviveMax;
	}
	/* Toggles Torus Mode
	 * Returns boolean representing if Torus is on or off
	 */
	public boolean toggleTorus() {
		this.isTorus = !this.isTorus;
		return this.isTorus;
	}
}
