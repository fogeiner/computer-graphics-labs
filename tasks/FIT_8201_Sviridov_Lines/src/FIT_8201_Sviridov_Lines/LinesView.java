package FIT_8201_Sviridov_Lines;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;

public class LinesView extends JPanel {

	private static final long serialVersionUID = -456307332612783435L;

	private LinesFrame _lines_frame;
	private boolean _rubber_line = false;
	private final Object _monitor = new Object();

	private int _refresh_period = 40;

	public LinesView(LinesFrame lines_frame) {
		_lines_frame = lines_frame;

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				int button = event.getButton();
				if (button == MouseEvent.BUTTON1) {
					LinesView.this._lines_frame.leftClick(event.getPoint());
				} else if (button == MouseEvent.BUTTON3) {
					LinesView.this._lines_frame.rightClick(event.getPoint());
				}
			}
		});

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						synchronized (LinesView.this._monitor) {
							if (LinesView.this._rubber_line == false) {
								_monitor.wait();
								continue;
							}
						}
						LinesView.this.repaint();
						Thread.sleep(LinesView.this._refresh_period);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(_lines_frame.getBackgroundColor());
		g2.fillRect(0, 0, getWidth(), getHeight());

		List<Polyline> polylines = _lines_frame.getPolylines();
		int radius = _lines_frame.getCircleRadius();

		for (Polyline polyline : polylines) {
			polyline.draw(g2, radius);
		}

		if (_rubber_line) {
			Polyline last_polyline = polylines.get(polylines.size() - 1);
			List<Point> last_points = last_polyline.getPoints();
			Point last_point = last_points.get(last_points.size() - 1);

			Point pointer_location = MouseInfo.getPointerInfo().getLocation();

			int x1 = last_point.x, y1 = last_point.y, x2 = pointer_location.x
					- this.getLocationOnScreen().x, y2 = pointer_location.y
					- this.getLocationOnScreen().y;

			g2.drawLine(x1, y1, x2, y2);
		}
	}

	public void enableRubberLine() {
		synchronized (_monitor) {
			_rubber_line = true;
			_monitor.notifyAll();
		}
		repaint();
	}

	public void disableRubberLine() {
		synchronized (_monitor) {
			_rubber_line = false;
			_monitor.notifyAll();
		}
		repaint();
	}

}
