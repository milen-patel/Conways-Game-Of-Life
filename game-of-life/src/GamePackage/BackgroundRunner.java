package GamePackage;

public class BackgroundRunner extends Thread{
	private boolean isDone;
	private Model model;
	private double pauseDuration; 
	
	public BackgroundRunner(Model model, double pauseDuration) {
		this.isDone = false;
		this.model = model;
		//TODO: Validate input here
		this.pauseDuration = pauseDuration;
	}
	
	public void terminate() {
		isDone = true;
	}
	
	public void run() {
		while (!isDone) {
			try {
				Thread.sleep((long) pauseDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			model.makeNextMove();
		}
	}
	
}
