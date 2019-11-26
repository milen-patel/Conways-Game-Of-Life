package GamePackage;

import SpotPackage.Spot;

public interface ViewObserver {
	public void resetButtonClicked();

	public void spotClicked(Spot s);

	public void newBoardSizeRequested(int newXSize, int newYSize);
}
