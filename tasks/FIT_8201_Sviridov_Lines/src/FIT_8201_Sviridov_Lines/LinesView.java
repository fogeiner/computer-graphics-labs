package FIT_8201_Sviridov_Lines;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class LinesView extends JPanel {

	private static final long serialVersionUID = -456307332612783435L;

	private LinesFrame _lines_frame;
	private boolean _rubber_line = false;

	private Timer _timer;
	private TimerTask _timer_task;
	private int _refresh_period = 100;
	
	public LinesView(LinesFrame lines_frame) {
		_lines_frame = lines_frame;

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				int button = event.getButton();
				if (button == MouseEvent.BUTTON1) {
					LinesView.this._lines_frame.leftClick(event.getPoint());
				} else if (button == MouseEvent.BUTTON3) {
					LinesView.this._lines_frame.rightClick(event.getPoint());
				}
			}
		});
		
		_timer = new Timer();
		_timer_task = new TimerTask() {
			
			@Override
			public void run() {
				LinesView.this.repaint();
			}
		};
		
		_timer.scheduleAtFixedRate(_timer_task, new Date(), _refresh_period);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		List<Polyline> polylines = _lines_frame.getPolylines();

		for (Polyline polyline : polylines) {
			List<Point> points = polyline.getPoints();
			for (int i = 1; i < points.size(); ++i) {
				Point p1 = points.get(i - 1);
				Point p2 = points.get(i);

				int x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;

				// System.out.println("("+x1+","+y1+")" + "->" + "(" + x2
				// +","+y2+")");
				g.drawLine(x1, y1, x2, y2);
			}
		}

		if (_rubber_line) {
			Polyline last_polyline = polylines.get(polylines.size() - 1);
			List<Point> last_points = last_polyline.getPoints();
			Point last_point = last_points.get(last_points.size() - 1);

			Point pointer_location = MouseInfo.getPointerInfo().getLocation();

			int x1 = last_point.x, y1 = last_point.y, x2 = pointer_location.x
					- this.getLocationOnScreen().x, y2 = pointer_location.y
					- this.getLocationOnScreen().y;

			g.drawLine(x1, y1, x2, y2);
		}
	}

	public void enableRubberLine() {
		_rubber_line = true;
	}

	public void disableRubberLine() {
		_rubber_line = false;
	}

}
