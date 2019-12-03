package GamePackage;

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
		model.resetBoard();		
	}

	@Override
	public void spotClicked(int x, int y) {
		System.out.println("View has notified controller that a spot has been clicked at (" + x +", " + y + ")");
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
		/* Model tells us that a spot has changed, we tell view to repaint */
		view.updateDisplay(this.model);
	}

	@Override
	/* Observable method for the Model */
	public void newBoardSize() {
		view.updateDisplay(this.model);
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
			this.view.giveBadDimensionError();
		}
	}

	@Override
	public void showThresholds() {
		this.view.ShowThresholdPopup(model.getSurviveThresholdLow(), model.getSurviveThresholdHigh(), model.getBirthThresholdLow(), model.getBirthThresholdHigh());
	}

	@Override
	public void toggleTorus() {
		if (model.toggleTorus())
			view.showTorusModeOn();
		else
			view.showTorusModeOff();
	}

	@Override
	public void togglePlay() {
		if (isAutoRunning) {
			currentThread.terminate();
			currentThread = null;
			isAutoRunning = false;
			return;
		} else {
			double pauseTime = view.getThreadDurationPrompt();
			if (pauseTime < 10 || pauseTime > 1000) {
				this.view.showInvalidInputMessage();
				return;
			}
			isAutoRunning = true;
			currentThread = new BackgroundRunner(this.model, pauseTime);
			currentThread.start();
		}	
	}
}
