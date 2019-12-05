package a8;

public interface ViewObserver {

	public enum ViewEvent {
		resetButtonClicked, spotClicked, newBoardSizeRequestClicked, randomizeBoardClicked, nextMoveClicked,
		changeThresholdClicked, showThresholdsClicked, toggleTorusClicked, togglePlayClicked
	};

	public void handleViewEvent(ViewEvent e);
	// For spotClicked, newBoardSizeRequested
	public void handleViewEvent(ViewEvent e, int x, int y);
	// For changeThresholdRequest
	public void handleViewEvent(ViewEvent e, int newSurviveMin, int newSurviveMax, int newBirthMin, int newBirthMax);
}
