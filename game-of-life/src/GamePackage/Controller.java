package GamePackage;

import javax.swing.JOptionPane;

import SpotPackage.Spot;

public class Controller implements ModelObserver, ViewObserver{
	/* Instance variables */
	private Model model;
	private View view;
	boolean isAutoRunning;
	private BackgroundRunner currentThread;
	
	/* Constructor */
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		model.addModelObserver(this);
		view.addViewObserver(this);
	}

	

	@Override
	public void resetButtonClicked() {
		System.out.println("Observer sees that reset button was clicked");
		System.out.println("Resetting model");
		model.reset();
		
	}

	@Override
	public void spotClicked(int x, int y) {
		System.out.println("View has notified controller that a spot has been clicked");
		System.out.println("Controller notifying the model");
		model.toggleSpot(x, y);
		System.out.println("The clicked spot has # neighbors: " + model.getNumNeighbors(x, y));
	}

	@Override
	public void newBoardSizeRequested(int newXSize, int newYSize) {
		model.changeBoardSize(newXSize, newYSize);
		System.out.println("Controller requesting new board size");
	}

	@Override
	/* Observable method for the Model */
	public void spotChanged() {
		//Model tells us that a spot has changed, we tell view to repaint
		view.updateDisplay(this.model);
	}

	@Override
	/* Observable method for the Model */
	public void newBoardSize() {
		view.regenerateModel(this.model);
	}



	@Override
	public void randomizeBoard() {
		model.randomizeBoard();
	}



	@Override
	public void nextMove() {
		model.makeNextMove();
	}

	@Override
	public void changeThresholdsRequest(int newSurviveMin, int newSurviveMax, int newBirthMin, int newBirthMax) {
		try {
			model.changeThresholds(newSurviveMin, newSurviveMax, newBirthMin, newBirthMax);

		} catch (Exception e) {
			System.out.println(e);
			 JOptionPane.showMessageDialog(null, "Bad threshold dimensions entered. No changes will be made.");
		}
	}



	@Override
	public void showThresholds() {
        JOptionPane.showMessageDialog(null, "Minimum Survival: " + model.getSurviveThresholdLow() + "\nMaximum Survival:" + model.getSurviveThresholdHigh() + "\nMinimum Birth: " + model.getBirthThresholdLow() + "\nMaximum Birth: " + model.getBirthThresholdHigh(), "Current Thresholds", JOptionPane.INFORMATION_MESSAGE);
	}



	@Override
	public void toggleTorus() {
		if (model.toggleTorus())
			JOptionPane.showMessageDialog(null, "Torus mode is now on", "Torus Mode", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "Torus mode is now off", "Torus Mode", JOptionPane.INFORMATION_MESSAGE);
		//TODO: Make the view handle the JOption panes
	}

	@Override
	public void togglePlay() {
		if (isAutoRunning) {
			currentThread.terminate();
			currentThread = null;
			isAutoRunning = false;
			return;
		} else {
			double pauseTime = Double.parseDouble(JOptionPane.showInputDialog(null,"Enter thread duration between 10-1000 ms"));
			if (pauseTime < 10 || pauseTime > 1000) {
				JOptionPane.showMessageDialog(null, "Invalid Input!", "Error", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			isAutoRunning = true;
			currentThread = new BackgroundRunner(this.model, pauseTime);
			currentThread.start();
		}
		
	}
	
	
}
