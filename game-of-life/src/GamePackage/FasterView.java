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

public class FasterView extends JPanel implements MouseListener {
	private boolean[][] arr;
	private List<FasterViewObserver> observers;

	Graphics2D g2d;
	public FasterView() {
		observers = new ArrayList<FasterViewObserver>();
		this.addMouseListener(this);
		repaint();		
	}
	
	
	public void repaint(boolean[][] arr) {
		this.arr = arr;
		repaint();
	}
	//TODO Make the board resize itself when the screen is resized

	public void paintComponent(Graphics g) {
		// Super class paintComponent will take care of 
		// painting the background.
		super.paintComponent(g);

		Rectangle r = getBounds();
		//System.out.println("Inner View Width: " + r.getWidth());
		//System.out.println("Inner View Height: " + r.getHeight());
		this.setPreferredSize(new Dimension((int)(r.getWidth()),(int) (r.getHeight())));
		
		if (arr == null) {
			return;
		}
		double eachCellXWidth = (r.getWidth()/arr[0].length);
		double eachCellYWidth = (r.getHeight()/arr.length);
		
		g2d = (Graphics2D) g.create();
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.GREEN);
		g2d.setStroke(new BasicStroke(1));
		
		
		
		
		for (int y=0; y<arr.length; y++) {
			for (int x=0; x<arr[0].length; x++) {
				//Draw the rectangles
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(1));
				g2d.drawRect((int)(x*eachCellXWidth), (int)(y*eachCellYWidth), (int)(eachCellXWidth), (int)(eachCellYWidth));
				//Draw oval when needed
				if (arr[y][x]) {
					g2d.setColor(Color.BLACK);
					g2d.fillOval((int)(x*eachCellXWidth), (int)(y*eachCellYWidth), (int)(eachCellXWidth), (int)(eachCellYWidth));

				}
			}
		}
	}
	
	public Dimension convertPointToCoordinate(int x, int y) {
		Rectangle r = getBounds();
		int eachCellXWidth = (int) (r.getWidth()/arr[0].length);
		int eachCellYWidth = (int) (r.getHeight()/arr.length);
		
		return new Dimension(x/eachCellXWidth, y/eachCellYWidth);
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Dimension temp = convertPointToCoordinate(e.getX(), e.getY());
		System.out.println("Someone clicked the screen, we predict it to be :");
		System.out.println("X: " + temp.width + ", Y: " + temp.height);
		notifyObservers("button_clicked", temp.width, temp.height);
		//TODO dont use dimensions make a new class
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//System.out.println("mouse entered");
		//Dimension temp = convertPointToCoordinate(e.getX(), e.getY());
		//Rectangle r = getBounds();
		//int eachCellXWidth = (int) (r.getWidth()/model.getCurrentXSize()+1);
		//int eachCellYWidth = (int) (r.getHeight()/model.getCurrentYSize()+1);
		//g2d.setColor(Color.YELLOW);
		//g2d.drawRect((int) (temp.getWidth()*eachCellXWidth+30), (int) (temp.getHeight()*eachCellYWidth), eachCellXWidth, eachCellYWidth);
		//repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void addObserver(FasterViewObserver o) { observers.add(o); }
	public void removeObserver(FasterViewObserver o) { observers.remove(o); }
	public void notifyObservers(String action, int x, int y) {
		for (FasterViewObserver o : observers) {
			if (action.contentEquals("button_clicked")) {
				o.buttonClicked(x, y);
			}
		}
	}
	

}
