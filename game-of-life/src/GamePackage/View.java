package GamePackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


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
		c.anchor = GridBagConstraints.PAGE_END; 
		displayWidget = new GridVisualizerWidget();
		displayWidget.setPreferredSize(new Dimension(1000,800));
		displayWidget.repaint(new boolean[10][10]);
		displayWidget.addObserver(this);
		this.add(displayWidget, c);
		
	}
	
	/* Observer methods */
	public void addViewObserver(ViewObserver o) { this.observers.add(o); }
	public void removeViewObserver(ViewObserver o) { this.observers.remove(o); }
	public void notifyObservers(String action) {
		for (ViewObserver o : observers) {
			if (action.equals("ResetButton")) {
				o.resetButtonClicked();
			} else if (action.contentEquals("ResizeBoard")) {
				System.out.println("Board Resize Requested");
				 try {
				 int newXSize = Integer.parseInt(JOptionPane.showInputDialog(null, "X Value"));
				 int newYSize = Integer.parseInt(JOptionPane.showInputDialog(null, "Y Value"));
				 if (newXSize < 10 || newYSize < 10 || newXSize > 500 || newYSize > 500) {throw new RuntimeException();}
				 o.newBoardSizeRequested(newXSize, newYSize);
				 } catch (Exception e) {
					 JOptionPane.showMessageDialog(null, "Bad resizable board dimensions entered. No changes will be made.");
					 return;
				 }
				 
			} else if (action.contentEquals("RandomizeBoard")) {
				o.randomizeBoard();
			} else if (action.contentEquals("NextMove")) {
				o.nextMove();
			} else if (action.contentEquals("changeThresholds")) {
				try {
					int newSurviveMin = Integer.parseInt(JOptionPane.showInputDialog(null, "Survival Minimum"));
					int newSurviveMax = Integer.parseInt(JOptionPane.showInputDialog(null, "Survival Maximum"));
					int newBirthMin = Integer.parseInt(JOptionPane.showInputDialog(null, "Birth Minimum"));
					int newBirthMax = Integer.parseInt(JOptionPane.showInputDialog(null, "Birth Maximum"));
					o.changeThresholdsRequest(newSurviveMin, newSurviveMax, newBirthMin, newBirthMax);
				} catch (Exception e) {
					System.out.println(e);
					 JOptionPane.showMessageDialog(null, "Bad threshold dimensions entered. No changes will be made.");
					 return;
				}
			} else if (action.contentEquals("showThresholds")) {
				o.showThresholds();
			} else if (action.contentEquals("toggleTorus")) {
				o.toggleTorus();
			} else if (action.contentEquals("togglePlay")) {
				o.togglePlay();
			}
		}
	}
	
	public void notifyObservers(String action, int x, int y) {
		for (ViewObserver o : observers) {
			if (action.equals("spot clicked")) {
				o.spotClicked(x, y);
			} 
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals("reset")) {
			notifyObservers("ResetButton");
		} else if (e.getActionCommand().equals("resize")) {
			notifyObservers("ResizeBoard");
		} else if (e.getActionCommand().contentEquals("randomize")) {
			notifyObservers("RandomizeBoard");
		} else if (e.getActionCommand().contentEquals("nextMove")) {
			notifyObservers("NextMove");
		} else if (e.getActionCommand().contentEquals("changeThresholds")) {
			notifyObservers("changeThresholds");
		} else if (e.getActionCommand().contentEquals("getThresholds")) {
			notifyObservers("showThresholds");
		} else if (e.getActionCommand().contentEquals("toggleTorus")) {
			notifyObservers("toggleTorus");
		} else if (e.getActionCommand().contentEquals("togglePlay")) {
			notifyObservers("togglePlay");
		}
	}

	/* Repaints the display by using the displayWidget method */
	public void updateDisplay(Model model) {
		displayWidget.repaint(model.getBoard());
	}

	/* Implementation of GridVisualizerWidgetObserver */
	@Override
	public void buttonClicked(int x, int y) {
		notifyObservers("spot clicked", x, y);
	}
	
}