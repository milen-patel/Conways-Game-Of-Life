package GamePackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GridVisualizerWidget extends JPanel implements MouseListener {
	private boolean[][] arr;
	private List<GridVisualizerWidgetObserver> observers;

	Graphics2D g2d;
	public GridVisualizerWidget() {
		observers = new ArrayList<GridVisualizerWidgetObserver>();
		this.addMouseListener(this);
		repaint();		
	}
	
	
	public void repaint(boolean[][] arr) {
		this.arr = arr;
		repaint();
	}
	//TODO Make the board resize itself when the screen is resized

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Rectangle r = getBounds();
		//this.setPreferredSize(new Dimension((int)(r.getWidth()),(int) (r.getHeight())));
		
		/* If we have nothing to visualize, then move on */
		if (arr == null) {
			return;
		}
		//WORKS BETTER WHEN THESE ARE INTS
		double eachCellXWidth =  (r.getWidth()/arr[0].length);
		double eachCellYWidth =  (r.getHeight()/arr.length);
		System.out.println("Widget will use x width: " + eachCellXWidth * arr[0].length);
		System.out.println("Widget will use y height: " + eachCellYWidth * arr.length);
		System.out.println("Widget has potential to use width: " + r.getWidth() + ", height: " + r.getHeight());
		g2d = (Graphics2D) g.create();
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));
		
		for (int y=0; y<arr.length; y++) {
			for (int x=0; x<arr[0].length; x++) {
				//Draw the rectangles when we need to
				if (arr[0].length < 350 && arr.length < 350) {
					g2d.drawRect((int)(x*eachCellXWidth), (int)(y*eachCellYWidth), (int)(eachCellXWidth), (int)(eachCellYWidth));
				}
				//Draw oval when needed
				if (arr[y][x]) {
					g2d.fillOval((int)(x*eachCellXWidth), (int)(y*eachCellYWidth), (int)(eachCellXWidth), (int)(eachCellYWidth));

				}
			}
		}
	}
	
	public Dimension convertPointToCoordinate(int x, int y) {
		Rectangle r = getBounds();
		double eachCellXWidth =  (r.getWidth()/arr[0].length);
		double eachCellYWidth =  (r.getHeight()/arr.length);
		
		return new Dimension((int) (x/eachCellXWidth), (int) (y/eachCellYWidth));
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		Dimension temp = convertPointToCoordinate(e.getX(), e.getY());
		if (temp.width < 0 || temp.height < 0 || temp.width >= arr[0].length || temp.height >= arr.length)
			return; //Temp solution
		notifyObservers("button_clicked", temp.width, temp.height);
		//TODO dont use dimensions make a new class
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println("mouse entered" + e.getX());
		//Dimension temp = convertPointToCoordinate(e.getX(), e.getY());
		//Rectangle r = getBounds();
		//int eachCellXWidth = (int) (r.getWidth()/arr[0].length);
		//int eachCellYWidth = (int) (r.getHeight()/arr.length);
		//g2d.setColor(Color.YELLOW);
		//g2d.fillRect((int) (temp.getWidth()*eachCellXWidth+30), (int) (temp.getHeight()*eachCellYWidth), eachCellXWidth, eachCellYWidth);
		//repaint();
		//TODO needs to highlight spot
		
	}

	public void addObserver(GridVisualizerWidgetObserver o) { observers.add(o); }
	public void removeObserver(GridVisualizerWidgetObserver o) { observers.remove(o); }
	public void notifyObservers(String action, int x, int y) {
		for (GridVisualizerWidgetObserver o : observers) {
			if (action.contentEquals("button_clicked")) {
				o.buttonClicked(x, y);
			}
		}
	}
	

}
