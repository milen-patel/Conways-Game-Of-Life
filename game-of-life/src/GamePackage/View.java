package GamePackage;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import GamePackage.ViewObserver.ViewEvent;


public class View extends JPanel implements ActionListener, GridVisualizerWidgetObserver {
	/* Define instance variables */
	private List<ViewObserver> observers;
	private JButton resetButton;
	private JButton resizeBoardButton;
	private JButton randomizeBoardButton;
	private JButton nextMoveButton;
	private JButton changeThresholdsButton;
	private JButton getThresholdsButton;
	private JButton toggleTorusButton;
	private JButton togglePlayButton;
	private GridVisualizerWidget displayWidget;
	private JLabel populationLabel;
	private JLabel stepNumberLabel;
	
	GridBagConstraints c;
	
	/* Define Constructor */
	public View () {
		/* Handle observer */
		observers = new ArrayList<ViewObserver>();
		/* Set the layout */
		this.setLayout(new GridBagLayout());
	    c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		this.setPreferredSize(new Dimension(1000, 1000));
		
		/* Add reset button */
		resetButton = new JButton("Reset");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		c.gridx = 0;
		c.gridy = 0;
		this.add(resetButton, c);
		
		/* Add Resize Button */
		resizeBoardButton = new JButton("Resize Board");
		resizeBoardButton.setActionCommand("resize");
		resizeBoardButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 0;
		this.add(resizeBoardButton, c);
		
		/* Add randomize board button */
		randomizeBoardButton = new JButton("Randomize Board");
		randomizeBoardButton.setActionCommand("randomize");
		randomizeBoardButton.addActionListener(this);
		c.gridx = 2;
		c.gridy = 0;
		this.add(randomizeBoardButton, c);
		
		/* Add next move button */
		nextMoveButton = new JButton("Next Move");
		nextMoveButton.setActionCommand("nextMove");
		nextMoveButton.addActionListener(this);
		c.gridx = 3;
		c.gridy = 0;
		this.add(nextMoveButton, c);
		
		/* Add get threshold button */
		getThresholdsButton = new JButton("See Thresholds");
		getThresholdsButton.setActionCommand("getThresholds");
		getThresholdsButton.addActionListener(this);
		c.gridx=0;
		c.gridy=1;
		this.add(getThresholdsButton, c);
		
		/* Add change threshold button */
		changeThresholdsButton = new JButton("Change Thresholds");
		changeThresholdsButton.setActionCommand("changeThresholds");
		changeThresholdsButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 1;
		this.add(changeThresholdsButton, c);
		
		/* Add torus button */
		toggleTorusButton = new JButton("Torus On/Off");
		toggleTorusButton.setActionCommand("toggleTorus");
		toggleTorusButton.addActionListener(this);
		c.gridx = 2;
		c.gridy = 1;
		this.add(toggleTorusButton, c);
		
		/* Add pause/play button */
		togglePlayButton = new JButton("Play/Pause");
		togglePlayButton.setActionCommand("togglePlay");
		togglePlayButton.addActionListener(this);
		c.gridx = 3;
		c.gridy = 1;
		this.add(togglePlayButton, c);
		
		/* Add Board Visualizer Widget */
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		displayWidget = new GridVisualizerWidget();
		displayWidget.setPreferredSize(new Dimension(1000,800));
		displayWidget.repaint(new boolean[10][10]);
		displayWidget.addObserver(this);
		this.add(displayWidget, c);
		
		/* Add population label */
		c.anchor = GridBagConstraints.PAGE_END; 
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		populationLabel = new JLabel("Population: 0");
		this.add(populationLabel, c);
		
		/* Add stepNumberLabel */
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		stepNumberLabel = new JLabel("Step: 0");
		this.add(stepNumberLabel, c);
	}
	
	/* Observer methods */
	public void addViewObserver(ViewObserver o) { this.observers.add(o); }
	public void removeViewObserver(ViewObserver o) { this.observers.remove(o); }
	public void notifyObservers(ViewObserver.ViewEvent e) {
		for (ViewObserver o: observers) {
			o.handleViewEvent(e);
		}
	}
	public void notifyObservers(ViewObserver.ViewEvent e, int x, int y) {
		for (ViewObserver o : observers) {
			o.handleViewEvent(e, x, y);
		}
	}
	public void notifyObservers(ViewEvent e, int newSurviveMin, int newSurviveMax, int newBirthMin, int newBirthMax) {
		for (ViewObserver o : observers) {
			o.handleViewEvent(e, newSurviveMin, newSurviveMax, newBirthMin, newBirthMax);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals("reset")) {
			notifyObservers(ViewEvent.resetButtonClicked);
		} else if (e.getActionCommand().equals("resize")) {
			try {
				int newXSize = Integer.parseInt(JOptionPane.showInputDialog(null, "X Value"));
				int newYSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Y Value"));
				if (newXSize < 10 || newYSize < 10 || newXSize > 500 || newYSize > 500) {
					throw new RuntimeException();
				}
				notifyObservers(ViewEvent.newBoardSizeRequestClicked, newXSize, newYSize);
			} catch (Exception excep) {
				JOptionPane.showMessageDialog(null, "Bad resizable board dimensions entered. No changes will be made.");
				return;
			}
		} else if (e.getActionCommand().contentEquals("randomize")) {
			notifyObservers(ViewEvent.randomizeBoardClicked);
		} else if (e.getActionCommand().contentEquals("nextMove")) {
			notifyObservers(ViewEvent.nextMoveClicked);
		} else if (e.getActionCommand().contentEquals("changeThresholds")) {
			try {
				int newSurviveMin = Integer.parseInt(JOptionPane.showInputDialog(null, "Survival Minimum"));
				int newSurviveMax = Integer.parseInt(JOptionPane.showInputDialog(null, "Survival Maximum"));
				int newBirthMin = Integer.parseInt(JOptionPane.showInputDialog(null, "Birth Minimum"));
				int newBirthMax = Integer.parseInt(JOptionPane.showInputDialog(null, "Birth Maximum"));
				notifyObservers(ViewEvent.changeThresholdClicked, newSurviveMin, newSurviveMax, newBirthMin,
						newBirthMax);
			} catch (Exception excep) {
				JOptionPane.showMessageDialog(null, "Bad threshold dimensions entered. No changes will be made.");
				return;
			}
		} else if (e.getActionCommand().contentEquals("getThresholds")) {
			notifyObservers(ViewEvent.showThresholdsClicked);
		} else if (e.getActionCommand().contentEquals("toggleTorus")) {
			notifyObservers(ViewEvent.toggleTorusClicked);
		} else if (e.getActionCommand().contentEquals("togglePlay")) {
			notifyObservers(ViewEvent.togglePlayClicked);
		}
	}

	/* Repaints the display by using the displayWidget method */
	public void updateDisplay(Model model) {
		populationLabel.setText("Population: " + model.getPopulation());
		stepNumberLabel.setText("Step: " + model.getStepNumber());
		displayWidget.repaint(model.getBoard());
		
	}

	/* Implementation of GridVisualizerWidgetObserver */
	@Override
	public void buttonClicked(int x, int y) {
		notifyObservers(ViewEvent.spotClicked, x, y);
	}

	public void giveBadDimensionError() {
		 JOptionPane.showMessageDialog(null, "Bad threshold dimensions entered. No changes will be made.");		
	}

	public void showTorusModeOn() {
		JOptionPane.showMessageDialog(null, "Torus mode is now on", "Torus Mode", JOptionPane.INFORMATION_MESSAGE);		
	}

	public void showTorusModeOff() {
		JOptionPane.showMessageDialog(null, "Torus mode is now off", "Torus Mode", JOptionPane.INFORMATION_MESSAGE);		
	}

	public void ShowThresholdPopup(int surviveThresholdLow, int surviveThresholdHigh, int birthThresholdLow,
			int birthThresholdHigh) {
        JOptionPane.showMessageDialog(null, "Minimum Survival: " + surviveThresholdLow + "\nMaximum Survival:" + surviveThresholdHigh + "\nMinimum Birth: " + birthThresholdLow + "\nMaximum Birth: " + birthThresholdHigh, "Current Thresholds", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showInvalidInputMessage() {
		JOptionPane.showMessageDialog(null, "Invalid Input!", "Error", JOptionPane.INFORMATION_MESSAGE);		
	}

	public double getThreadDurationPrompt() {
		return Double.parseDouble(JOptionPane.showInputDialog(null,"Enter thread duration between 10-1000 ms"));
	}

	public void showCannotResizeBoardWhilePlayingError() {
		JOptionPane.showMessageDialog(null, "You cannot resize the board while the simulation is playing!", "Error", JOptionPane.INFORMATION_MESSAGE);		
	}
	
}