package GamePackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
	private JSpotBoard cellBoard;
	
	/* Define Constructor */
	public View () {
		/* Handle observer */
		observers = new ArrayList<ViewObserver>();
		/* Set the layout */
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		/* Add reset button */
		resetButton = new JButton("Reset");
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(this);
		resetButton.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(resetButton);
		/* Add Resize Button */
		resizeBoardButton = new JButton("Resize Board");
		resizeBoardButton.setActionCommand("resize");
		resizeBoardButton.addActionListener(this);
		this.add(resizeBoardButton);
		/* Add Spot Board */
		cellBoard = new JSpotBoard(25,25);
		this.add(cellBoard);
		cellBoard.addSpotListener(this);
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
		cellBoard = new JSpotBoard(model.getCurrentXSize(),model.getCurrentYSize());
		this.add(cellBoard);
		cellBoard.addSpotListener(this);	
		updateDisplay(model);
	}
	
	
}
