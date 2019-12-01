package GamePackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import SpotPackage.JSpotBoard;
import SpotPackage.Spot;
import SpotPackage.SpotListener;

public class View extends JPanel implements ActionListener, SpotListener{
	/* Define instance variables */
	private List<ViewObserver> observers;
	private JButton resetButton;
	private JButton resizeBoardButton;
	private JButton randomizeBoardButton;
	private JButton nextMoveButton;
	private JSpotBoard cellBoard;
	
	/* Define Constructor */
	public View () {
		/* Handle observer */
		observers = new ArrayList<ViewObserver>();
		/* Set the layout */
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new GridLayout(5, 1, 10, 10));
		/* Add reset button */
		resetButton = new JButton("Reset");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		this.add(resetButton);
		/* Add Resize Button */
		resizeBoardButton = new JButton("Resize Board");
		resizeBoardButton.setActionCommand("resize");
		resizeBoardButton.addActionListener(this);
		this.add(resizeBoardButton);
		/* Add randomize board button */
		randomizeBoardButton = new JButton("Randomize Board");
		randomizeBoardButton.setActionCommand("randomize");
		randomizeBoardButton.addActionListener(this);
		this.add(randomizeBoardButton);
		/* Add next move button */
		nextMoveButton = new JButton("Next Move");
		nextMoveButton.setActionCommand("nextMove");
		nextMoveButton.addActionListener(this);
		this.add(nextMoveButton);
		/* Add Spot Board */
		cellBoard = new JSpotBoard(10,10);
		this.add(cellBoard);
		cellBoard.addSpotListener(this);
		cellBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
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
			}
		}
	}
	
	public void notifyObservers(String action, Spot s) {
		for (ViewObserver o : observers) {
			if (action.equals("spot clicked")) {
				o.spotClicked(s);
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
		}
		
	}

	@Override
	public void spotClicked(Spot spot) {
		// TODO Auto-generated method stub
		notifyObservers("spot clicked", spot);

	}

	@Override
	public void spotEntered(Spot spot) {
		spot.highlightSpot();	
	}

	@Override
	public void spotExited(Spot spot) {
		spot.unhighlightSpot();
		// TODO Auto-generated method stub
		
	}
	
	public void updateDisplay(Model model) {
		System.out.println("Repainting display");
		//Loop over the entire JSpotBoard
		for (Spot s: cellBoard) {
			if (model.getIsSpotAlive(s.getSpotX(), s.getSpotY())) {
				cellBoard.getSpotAt(s.getSpotX(), s.getSpotY()).setSpot();
			} else {
				cellBoard.getSpotAt(s.getSpotX(), s.getSpotY()).clearSpot();
			}
		}
		this.revalidate();
		this.repaint();
	}

	public void regenerateModel(Model model) {
		this.remove(cellBoard);
		cellBoard = new JSpotBoard(model.getCurrentXSize()+1,model.getCurrentYSize()+1);
		this.add(cellBoard);
		cellBoard.addSpotListener(this);
		cellBoard.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		updateDisplay(model);
	}
	
	
}
