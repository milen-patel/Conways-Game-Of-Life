package GamePackage;

import SpotPackage.Spot;

public interface ViewObserver {
	public void resetButtonClicked();

	public void spotClicked(Spot s);

	public void newBoardSizeRequested(int newXSize, int newYSize);

	public void randomizeBoard();

	public void nextMove();

	public void changeThresholdsRequest(int newSurviveMin, int newSurviveMax, int newBirthMin, int newBirthMax);

	public void showThresholds();
}
