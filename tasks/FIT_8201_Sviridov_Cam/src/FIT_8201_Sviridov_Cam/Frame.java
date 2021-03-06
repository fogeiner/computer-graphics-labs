package FIT_8201_Sviridov_Cam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import ru.nsu.cg.MainFrame;

/**
 * Frame - Application main frame
 * 
 * @author alstein
 */
public final class Frame extends MainFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 474140705036147248L;
	public static final String NAME = "FIT_8201_Sviridov_Cam";
	public static final String UNTITLED_DOCUMENT = "Untitled";
	public static final String ABOUT_FILE = "FIT_8201_Sviridov_Cam_About.txt";
	private Color pressedColor = Color.lightGray;
	private Scene scene = new Scene();
	private boolean modified;
	private SettingsDialog settingsDialog = new SettingsDialog(this);

	/**
	 * Class for settings dialog
	 */
	class SettingsDialog extends JDialog {

		private static final long serialVersionUID = -2287400008055325290L;
		private JSlider znSlider = new JSlider(0, 4000);
		private JSpinner znSpinner = new JSpinner(new SpinnerNumberModel(0, 0,
				4000, 10));
		private JSlider zfSlider = new JSlider(0, 4000);
		private JSpinner zfSpinner = new JSpinner(new SpinnerNumberModel(0, 0,
				4000, 10));
		private JSlider rSlider = new JSlider(0, 100);
		private JSpinner rSpinner = new JSpinner(new SpinnerNumberModel(0.01,
				0.0, 1.0, 0.01));
		private JSlider dSlider = new JSlider(0, 100);
		private JSpinner dSpinner = new JSpinner(new SpinnerNumberModel(0, 0,
				100, 1));
		private JButton okButton = new JButton("OK");
		private JButton cancelButton = new JButton("Cancel");

		{
			setupSpinner(rSpinner, 5);
			rSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					rSpinner.setValue(rSlider.getValue() / 100.0);
				}
			});
			rSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					rSlider.setValue((int) ((Double) (rSpinner.getValue()) * 100 + 0.5));
					setModified(true);
				}
			});

			setupSpinner(dSpinner, 5);
			dSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					dSpinner.setValue(dSlider.getValue());
				}
			});
			dSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					dSlider.setValue((Integer) dSpinner.getValue());
					setModified(true);
				}
			});

			setupSpinner(znSpinner, 5);
			znSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					znSpinner.setValue(znSlider.getValue());
				}
			});
			znSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					znSlider.setValue((Integer) znSpinner.getValue());
				}
			});

			setupSpinner(zfSpinner, 5);
			zfSlider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					zfSpinner.setValue(zfSlider.getValue());
				}
			});
			zfSpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					zfSlider.setValue((Integer) zfSpinner.getValue());
				}
			});

			okButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					confirm();
				}
			});
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					cancel();
				}
			});
		}

		/**
		 * Sets up given spinner
		 * 
		 * @param spinner
		 *            spinner
		 * @param columns
		 *            number of columns
		 */
		private void setupSpinner(JSpinner spinner, int columns) {
			JComponent editor = spinner.getEditor();
			JFormattedTextField textfield = ((JSpinner.DefaultEditor) editor)
					.getTextField();
			NumberFormatter nFormat = (NumberFormatter) textfield
					.getFormatter();
			nFormat.setCommitsOnValidEdit(true);
			nFormat.setAllowsInvalid(false);

			((JSpinner.DefaultEditor) rSpinner.getEditor()).getTextField()
					.setColumns(columns);
		}

		/**
		 * Reads out values and makes dialog visible
		 */
		public void showDialog() {
			// read out values
			znSpinner.setValue((int) (scene.getZnear() + 0.5));
			zfSpinner.setValue((int) (scene.getZfar() + 0.5));

			rSpinner.setValue(scene.getRotateCoef());
			dSpinner.setValue((int) (scene.getRollCoef() + 0.5));

			setVisible(true);
		}

		/**
		 * Hides dialog
		 */
		public void cancel() {
			setVisible(false);
		}

		/**
		 * Sets parameters and make dialog invisible
		 */
		public void confirm() {
			int znear = (Integer) znSpinner.getValue(), zfar = (Integer) zfSpinner
					.getValue();
			if (zfar <= znear) {
				JOptionPane.showMessageDialog(this,
						"zFar must be more than zNear", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			scene.setRollCoef((Integer) dSpinner.getValue());
			scene.setRotateCoef((Double) rSpinner.getValue());
			scene.setZnear(znear);
			scene.setZfar(zfar);
			setVisible(false);
		}

		/**
		 * Creates subpanel
		 * 
		 * @param title
		 *            border title
		 * @param slider
		 *            slider
		 * @param spinner
		 *            spinner
		 * @return new subpanel
		 */
		private JPanel makeSubPanel(String title, JSlider slider,
				JSpinner spinner) {
			JPanel p = new JPanel(new BorderLayout(5, 5));
			p.setBorder(BorderFactory.createTitledBorder(title));
			p.add(slider, BorderLayout.CENTER);
			p.add(spinner, BorderLayout.LINE_END);
			return p;
		}

		/**
		 * Makes panel with buttons
		 * 
		 * @return panel with buttons
		 */
		private JPanel makeButtonsPanel() {
			JPanel p = new JPanel(new GridLayout(1, 2, 5, 5));
			p.add(okButton);
			p.add(cancelButton);
			JPanel outer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			outer.add(p);
			return outer;
		}

		/**
		 * Ctor
		 * 
		 * @param owner
		 *            frame
		 */
		public SettingsDialog(JFrame owner) {
			super(owner, "Settings dialog");
			JPanel mainPanel = new JPanel();
			mainPanel.add(makeSubPanel("zNear", znSlider, znSpinner));
			mainPanel.add(makeSubPanel("zFar", zfSlider, zfSpinner));
			mainPanel.add(makeSubPanel("r", rSlider, rSpinner));
			mainPanel.add(makeSubPanel("d", dSlider, dSpinner));
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			setLayout(new BorderLayout(5, 5));
			add(mainPanel, BorderLayout.CENTER);
			add(makeButtonsPanel(), BorderLayout.SOUTH);
			pack();
		}
	}

	/**
	 * Sets application title to "<code>name</code> - App name"
	 * 
	 * @param name
	 *            first part of application title
	 */
	@Override
	public void setTitle(String name) {
		super.setTitle(name + " - " + NAME);
	}

	/**
	 * Constructs application frame with given width and height
	 * 
	 * @param width
	 *            width of frame
	 * @param height
	 *            height of frame
	 */
	public Frame(int width, int height) {
		super(width, height, "");
		try {
			// constructing Menu
			addSubMenu("File", KeyEvent.VK_F);
			addMenuItem("File/Load", "Load document", KeyEvent.VK_L,
					"load.gif", "onLoad");
			addMenuItem("File/Save as...", "Save document", KeyEvent.VK_S,
					"save.gif", "onSave");
			addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X,
					"exit.gif", "onExit");
			addSubMenu("Edit", KeyEvent.VK_E);

			addMenuItem("Edit/Init", "Init scene", KeyEvent.VK_I, "init.gif",
					"onInit");
			addMenuItem("Edit/Show or hide orts", "Show/hide orts",
					KeyEvent.VK_O, "orts.gif", "onOrts");
			addMenuItem("Edit/Show or hide box", "Show/hide box",
					KeyEvent.VK_B, "box.gif", "onBox");
			addMenuItem("Edit/Show or hide objects", "Show/hide objects",
					KeyEvent.VK_S, "obj.gif", "onObjects");
			addMenuItem("Edit/Settings", "Show settings dialog", KeyEvent.VK_S,
					"settings.gif", "onSettings");

			addSubMenu("Help", KeyEvent.VK_H);

			addMenuItem("Help/About",
					"View application version and author information",
					KeyEvent.VK_A, "about.gif", "onAbout");
			// constructing Toolbar
			addToolBarButton("File/Load");
			addToolBarButton("File/Save as...");
			addToolBarSeparator();
			addToolBarButton("Edit/Init");
			addToolBarButton("Edit/Show or hide orts");
			addToolBarButton("Edit/Show or hide box");
			addToolBarButton("Edit/Show or hide objects");
			addToolBarButton("Edit/Settings");
			addToolBarSeparator();
			addToolBarButton("Help/About");
			addToolBarSeparator();
			addToolBarButton("File/Exit");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		toolBar.setFloatable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				onExit();
			}
		});

		JPanel outer1 = new JPanel(new BorderLayout());
		outer1.setBackground(Color.white);
		outer1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		scene.setBorder(new LineBorder(Color.black));
		outer1.add(scene, BorderLayout.CENTER);
		add(outer1);

		JButton but1 = (JButton) toolBar.getComponent(5);
		JButton but2 = (JButton) toolBar.getComponent(4);
		JButton but3 = (JButton) toolBar.getComponent(6);
		but1.setBackground(pressedColor);
		but2.setBackground(pressedColor);
		but3.setBackground(pressedColor);

		setTitle(UNTITLED_DOCUMENT);
		setModified(false);
	}

	/**
	 * Method called when user chooses "Init" in menu or on toolbar
	 */

	public void onInit() {
		scene.init();
	}

	/**
	 * Method called when user chooses "Box" in menu or on toolbar
	 */
	public void onBox() {
		JButton but = (JButton) toolBar.getComponent(5);
		Color butColor = but.getBackground();
		if (butColor == pressedColor) {
			but.setBackground(null);
			scene.setBoxVisible(false);
		} else {
			but.setBackground(pressedColor);
			scene.setBoxVisible(true);
		}
	}

	/**
	 * Method called when user chooses "Orts" in menu or on toolbar
	 */
	public void onOrts() {
		JButton but = (JButton) toolBar.getComponent(4);

		Color butColor = but.getBackground();
		if (butColor == pressedColor) {
			but.setBackground(null);
			scene.setOrtsVisible(false);
		} else {
			but.setBackground(pressedColor);
			scene.setOrtsVisible(true);
		}
	}

	/**
	 * Method called when user chooses "Objects" in menu or on toolbar
	 */
	public void onObjects() {
		JButton but = (JButton) toolBar.getComponent(6);
		Color butColor = but.getBackground();
		if (butColor == pressedColor) {
			but.setBackground(null);
			scene.setObjectsVisible(false);
		} else {
			but.setBackground(pressedColor);
			scene.setObjectsVisible(true);
		}
	}

	/**
	 * Method called when user chooses "Load" in menu or on toolbar. Asks user
	 * to save current document (if needed), shows dialog to choose file and
	 * loads document from it
	 */
	public void onLoad() {

		if (isModified() == true) {
			switch (showSaveMessage()) {
			case JOptionPane.OK_OPTION:
				onSave();

				// still not saved
				if (isModified()) {
					return;
				}
				break;
			case JOptionPane.CLOSED_OPTION:
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.NO_OPTION:
			}
		}

		try {
			File file = getOpenFileName("txt", "plain text file");
			if (file == null) {
				return;
			}

			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String str = LineParseUtils.nextNormalizedLine(br);
			String strs[] = str.split(" ");
			Double r = Double.parseDouble(strs[0]), d = Double
					.parseDouble(strs[1]);

			scene.setRotateCoef(r);
			scene.setRollCoef(d);

			br.close();
			setTitle(file.getName());
			setModified(false);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this,
					"Document error: " + ex.getMessage(), "Loading document",
					JOptionPane.ERROR_MESSAGE);
			setTitle(UNTITLED_DOCUMENT);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Error loading file: \n" + ex.getLocalizedMessage(),
					"Loading document", JOptionPane.ERROR_MESSAGE);
			setTitle(UNTITLED_DOCUMENT);
		}
	}

	/**
	 * Method called when user chooses "Settings" in menu or on toolbar.
	 */
	public void onSettings() {
		settingsDialog.showDialog();
	}

	/**
	 * Shows message to find out if user wills to save current document
	 * 
	 * @return <code>true</code> if user confirms, <code>false</code> otherwise
	 */
	private int showSaveMessage() {
		int answer = JOptionPane.showConfirmDialog(this,
				"The document was modified. Save?");
		return answer;
	}

	/**
	 * Method called when user chooses "Exit" in menu or on toolbar. Asks user
	 * to save current document (if needed) and terminates application
	 */
	public void onExit() {

		if (isModified() == true) {
			switch (showSaveMessage()) {
			case JOptionPane.OK_OPTION:
				onSave();

				// still not saved
				if (isModified()) {
					return;
				}
				break;
			case JOptionPane.CLOSED_OPTION:
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.NO_OPTION:
			}
		}

		System.exit(0);
	}

	/**
	 * Method called when user chooses "About" in menu or on toolbar. Loads text
	 * editor with <tt>FIT_8201_Sviridov_About.txt</tt> open
	 */
	public void onAbout() {
		String path = ABOUT_FILE;
		File f = new File(path);
		if (f.exists() == false) {
			path = ".." + System.getProperties().getProperty("file.separator")
					+ ABOUT_FILE;
			f = new File(path);
			if (f.exists() == false) {
				JOptionPane
						.showMessageDialog(
								this,
								"File "
										+ ABOUT_FILE
										+ " could not be found neither in the application directory nor in the preceding directory.",
								"Opening about file", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		try {
			String editor;
			if (System.getProperty("os.name").startsWith("Windows")) {
				editor = "notepad.exe";
			} else {
				editor = "gedit";
			}
			Runtime.getRuntime().exec(editor + " " + path);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error staring text editor: \n"
					+ e.getLocalizedMessage(), "Exec error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Method called when user chooses "Save" in menu or on toolbar. Shows
	 * dialog to choose/create file and saves document to it
	 */
	public void onSave() {

		try {
			File file = getSaveFileName("txt", "plain text file");
			if (file == null) {
				return;
			}
			if (file.exists()) {
				int answer = JOptionPane
						.showConfirmDialog(
								this,
								"File already exists. Are you sure you want to overwrite it?",
								"Select an Option",
								JOptionPane.YES_NO_CANCEL_OPTION);
				if (answer == JOptionPane.NO_OPTION) {
					onSave();
					return;
				}
				if (answer != JOptionPane.OK_OPTION) {
					return;
				}
			}
			FileWriter fw = new FileWriter(file);
			fw.append(new Double(scene.getRotateCoef()).toString());
			fw.append(" ");
			fw.append(new Double(scene.getRollCoef()).toString());
			fw.append("\r\n");
			fw.close();
			setTitle(file.getName());
			setModified(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Error saving file: \n" + e.getLocalizedMessage(),
					"Saving document", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Returns true if file needs saving after being modified, false otherwise
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Method to set/unset modified flag
	 */
	public void setModified(boolean value) {
		modified = value;
	}

	/**
	 * Application main entry point
	 * 
	 * @param args
	 *            command line arguments (unused)
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				Frame frame = new Frame(900, 700);
				frame.setVisible(true);
			}
		});
	}
}
