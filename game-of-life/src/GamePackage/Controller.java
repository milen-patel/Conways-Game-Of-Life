package GamePackage;

import javax.swing.JOptionPane;

import SpotPackage.Spot;

public class Controller implements ModelObserver, ViewObserver{
	/* Instance variables */
	private Model model;
	private View view;
	
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
	public void spotClicked(Spot s) {
		System.out.println("View has notified controller that a spot has been clicked");
		System.out.println("Controller notifying the model");
		model.toggleSpot(s.getSpotX(), s.getSpotY());
		System.out.println("The clicked spot has # neighbors: " + model.getNumNeighbors(s.getSpotX(), s.getSpotY()));
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
		System.out.println("TEst");
		// TODO Auto-generated method stub
        JOptionPane.showMessageDialog(null, "Minimum Survival: " + model.getSurviveThresholdLow() + "\nMaximum Survival:" + model.getSurviveThresholdHigh() + "\nMinimum Birth: " + model.getBirthThresholdLow() + "\nMaximum Birth: " + model.getBirthThresholdHigh(), "Current Thresholds", JOptionPane.INFORMATION_MESSAGE);

	}
	
}
