package GamePackage;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GameRunner {

	public static void main(String[] args) {
		Model model = new Model();
		View view = new View();
		Controller controller = new Controller(model, view);
		
		JFrame main_frame = new JFrame();
		main_frame.setTitle("Game of Life!");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame.setContentPane(view);
		main_frame.setPreferredSize(new Dimension(1000, 900));
		main_frame.setResizable(false);

		main_frame.pack();
		main_frame.setVisible(true);
		
	}

}

