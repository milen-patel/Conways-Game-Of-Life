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
	public void handleViewEvent(ViewEvent e) {
		if (e.equals(ViewEvent.togglePlayClicked)) {
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
		} else if (e.equals(ViewEvent.toggleTorusClicked)) {
			if (model.toggleTorus())
				view.showTorusModeOn();
			else
				view.showTorusModeOff();
		} else if (e.equals(ViewEvent.showThresholdsClicked)) {
			this.view.ShowThresholdPopup(model.getSurviveThresholdLow(), model.getSurviveThresholdHigh(), model.getBirthThresholdLow(), model.getBirthThresholdHigh());
		} else if (e.equals(ViewEvent.nextMoveClicked)) {
			model.makeNextMove();
		} else if (e.equals(ViewEvent.resetButtonClicked)) {
			model.resetBoard();
		} else if (e == (ViewEvent.randomizeBoardClicked)) {
			model.randomizeBoard();
		}
		
	}

	@Override
	public void handleViewEvent(ViewEvent e, int x, int y) {
		if (e.equals(ViewObserver.ViewEvent.spotClicked)) {
			model.toggleSpot(x, y);
		} else if (e.equals(ViewObserver.ViewEvent.newBoardSizeRequestClicked)) {
			/* Stop a thread from running if it is */
			if (isAutoRunning) {
				view.showCannotResizeBoardWhilePlayingError();
				return;
			}
			model.changeBoardSize(x, y);
		} 
		System.out.println(e);
		System.out.println(ViewEvent.randomizeBoardClicked);
	}

	@Override
	public void handleViewEvent(ViewEvent e, int newSurviveMin, int newSurviveMax, int newBirthMin, int newBirthMax) {
		if (e.equals(ViewEvent.changeThresholdClicked)) {
			try {
				model.changeThresholds(newSurviveMin, newSurviveMax, newBirthMin, newBirthMax);
			} catch (Exception excep) {
				System.out.println(excep);
				this.view.giveBadDimensionError();
			}
		}
	}
}
