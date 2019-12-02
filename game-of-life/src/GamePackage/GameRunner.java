package GamePackage;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GameRunner {

	public static void main(String[] args) {
		/* Create all components of MVC Architecture */
		Model model = new Model();
		View view = new View();
		Controller controller = new Controller(model, view);
		
		/* Create a frame and add the view to it */
		JFrame main_frame = new JFrame();
		main_frame.setTitle("Conway's Game of Life");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame.setContentPane(view);
		
		/* Lock in dimensions */
		main_frame.setPreferredSize(new Dimension(1000, 900));
		main_frame.setResizable(false);

		/* Make the frame visible */
		main_frame.pack();
		main_frame.setVisible(true);
		
	}

}

