package FIT_8201_Sviridov_Init;

import ru.nsu.cg.MainFrame;

public class InitMainWindow extends MainFrame {
	public InitMainWindow(int x, int y, String title){
		super(x, y, title);
	}
	
	public static void main(String args[]){
		InitMainWindow window = new InitMainWindow(600, 400, "FIT_8201_Sviridov_Init");
		window.setVisible(true);	
	}
}
