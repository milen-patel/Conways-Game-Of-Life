package GamePackage;


public interface ViewObserver {
	public void resetButtonClicked();

	public void spotClicked(int x, int y);

	public void newBoardSizeRequested(int newXSize, int newYSize);

	public void randomizeBoard();

	public void nextMove();

	public void changeThresholdsRequest(int newSurviveMin, int newSurviveMax, int newBirthMin, int newBirthMax);

	public void showThresholds();

	public void toggleTorus();

	public void togglePlay();
}