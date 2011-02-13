package FIT_8201_Sviridov_Lines;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class LinesView extends JPanel {

	private static final long serialVersionUID = -456307332612783435L;

	private LinesFrame _lines_frame;
	public LinesView(LinesFrame lines_frame){
		_lines_frame = lines_frame;
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				int button = event.getButton();
				if(button == MouseEvent.BUTTON1){
					LinesView.this._lines_frame.leftClick(event.getPoint());
				} else if(button == MouseEvent.BUTTON2){
					LinesView.this._lines_frame.rightClick(event.getPoint());
				}
			}
		});
	}
}
