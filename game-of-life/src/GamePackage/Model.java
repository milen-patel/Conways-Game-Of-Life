package GamePackage;
// Questions:
// Does the grid always have to be a square
// Can i model the grid from the top left or does it have to be the middle
// TODO: Make the board be one color
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
	int currentXSize;
	int currentYSize;
	private List<ModelObserver> observers;
	
	/* Create a constructor */
	public Model() {
		/* Instantiate 2d-array */
		gridModel = new boolean[500][500];
		currentXSize = 10;
		currentYSize = 10;
		observers = new ArrayList<ModelObserver>();
	}
	
	/* Getters */
	public int getCurrentXSize() { return this.currentXSize; }
	public int getCurrentYSize() { return this.currentYSize; }
	
	/* Returns if the spot is alive
	 * (x,y) is a spot in the array itself
	 * (0,0) is the top left spot
	 * (250,250) is the bottom right spot
	 */
	public boolean getIsSpotAlive(int x, int y) { return this.gridModel[y][x]; }
	
	
	/* Setters */
	public void changeXSize(int newVal) {
		if (newVal < 10 || newVal > 500) {
			throw new RuntimeException("Invalid board dimensions");
		}
		this.currentXSize = newVal;
		notifyObservers("newBoardSize");
	}
	
	public void changeYSize(int newVal) {
		if (newVal < 10 || newVal > 500) {
			throw new RuntimeException("Invalid board dimensions");
		}
		this.currentYSize = newVal;
		notifyObservers("newBoardSize");
	}
	
	public void toggleSpot(int x, int y) {
		gridModel[y][x] = !gridModel[y][x];
		//TODO: Notify view observers
		notifyObservers("spot_changed");
	}
	
	/* Helper methods */
	public int normalizeXValue(int x) { return 250+x; }
	public int normalizeYValue(int y) { return 250+y; }
	
	
	/* Observable Methods */
	public void addModelObserver(ModelObserver o) { this.observers.add(o); }
	public void removeModelObserver(ModelObserver o) { this.observers.remove(o); }
	public void notifyObservers(String event) {
		for (ModelObserver o : observers) {
			o.notify(event);
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
		
		//Check directly above, if possible
		if (y != 0 && getIsSpotAlive(x,y-1))
			currentNeighbors++;
		//Check directly below, if possible
		if (y != getCurrentYSize()-1 && getIsSpotAlive(x,y+1))
			currentNeighbors++;
		//Check directly to the right, if possible
		if (x != getCurrentXSize()-1 && getIsSpotAlive(x+1,y))
			currentNeighbors++;
		//Check directly to the left, if possible
		if (x != 0 && getIsSpotAlive(x-1,y))
			currentNeighbors++;
		//Check the diagonal Top Right spot, if possible
		if (x != getCurrentXSize()-1 && y != 0 && getIsSpotAlive(x+1,y-1))
			currentNeighbors++;
		//Check the diagonal Top Left spot, if possible
		if (x != 0 && y != 0 && getIsSpotAlive(x-1,y-1))
			currentNeighbors++;
		//Check the diagonal Bottom Right, if possible
		if (x != getCurrentXSize()-1 && y != getCurrentYSize() && getIsSpotAlive(x+1,y+1))
			currentNeighbors++;
		//Check the diagonal Bottom Left, if possible
		if (x != 0 && y != getCurrentYSize()-1 && getIsSpotAlive(x-1,y+1))
			currentNeighbors++;
		
		return currentNeighbors;
	}
}
