package FIT_8201_Sviridov_Flt;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import ru.nsu.cg.MainFrame;

/**
 * Frame - Application main frame
 * 
 * @author alstein
 */
public class FltFrame extends MainFrame implements FltFrameService {

	private static final long serialVersionUID = -5961249108882455789L;
	private ImageNavigationViewerPanel _zone_b = new ImageNavigationViewerPanel(
			"Zone B");
	private ImageNavigationPanel _zone_a = new ImageNavigationPanel("Zone A",
			_zone_b);
	private ImagePanel _zone_c = new ImageResultPanel("Zone C");
	private ImagePanel _zones[] = new ImagePanel[] { _zone_a, _zone_b, _zone_c };

	/**
	 * Sets application title to "<code>name</code> - App name"
	 * 
	 * @param name
	 *            first part of application title
	 */
	private void setDocumentName(String name) {
		setTitle(name + " - " + FltSettings.FLT_NAME);
	}

	/**
	 * Constructs application frame with given width and height
	 * 
	 * @param width
	 *            width of frame
	 * @param height
	 *            height of frame
	 */
	public FltFrame(int width, int height) {
		super(width, height, "");
		try {
			// constructing Menu
			addSubMenu("File", KeyEvent.VK_F);
			addMenuItem("File/New", "New document", KeyEvent.VK_N, "new.gif",
					"onNew");
			addMenuItem("File/Load", "Load document", KeyEvent.VK_L,
					"load.gif", "onLoad");
			addMenuItem("File/Save as...", "Save document", KeyEvent.VK_S,
					"save.gif", "onSave");
			addMenuItem("File/Exit", "Exit application", KeyEvent.VK_X,
					"exit.gif", "onExit");
			addSubMenu("Edit", KeyEvent.VK_E);
			addMenuItem("Edit/Select", "Select region", KeyEvent.VK_S,
					"select.gif", "onSelect");
			addMenuItem("Edit/From C to B", "Copy image fron zone C to zone B",
					KeyEvent.VK_C, "back.gif", "onFromCtoB");

			addMenuItem("Edit/Grayscale",
					"Apply black&white (grayscale) filter", KeyEvent.VK_G,
					"grayscale.gif", "onGrayscale");

			addMenuItem("Edit/Negative", "Apply negative filter",
					KeyEvent.VK_N, "negative.gif", "onNegative");

			addMenuItem("Edit/Floyd-Steinberg dithering",
					"Apply Floyd-Steinberg dithering filter", KeyEvent.VK_F,
					"fs_dithering.gif", "onFloydSteinbergDithering");

			addMenuItem("Edit/Orderted dithering",
					"Apply ordered dithering filter", KeyEvent.VK_O,
					"o_dithering.gif", "onOrderedDithering");

			addMenuItem("Edit/Double scale", "Apply double scale filter",
					KeyEvent.VK_D, "scale.gif", "onDoubleScale");

			addMenuItem("Edit/Roberts edge detecting",
					"Apply Roberts operator edge detection filter",
					KeyEvent.VK_R, "roberts.gif", "onRoberts");

			addMenuItem("Edit/Sobel edge detecting",
					"Apply Sobel operator edge detection filter",
					KeyEvent.VK_L, "sobel.gif", "onSobel");

			addMenuItem("Edit/Blur", "Apply blur filter", KeyEvent.VK_B,
					"blur.gif", "onBlur");

			addMenuItem("Edit/Sharpen", "Apply sharpen filter", KeyEvent.VK_S,
					"sharpen.gif", "onSharpen");

			addMenuItem("Edit/Emboss", "Apply emboss filter", KeyEvent.VK_E,
					"emboss.gif", "onEmboss");

			addMenuItem("Edit/Aquarelle", "Apply aquarelle filter",
					KeyEvent.VK_A, "aquarelle.gif", "onAquarelle");

			addMenuItem("Edit/Gamma", "Apply gamma correction", KeyEvent.VK_G,
					"gamma.gif", "onGamma");

			addMenuItem("Edit/Brightness", "Apply brightness correction",
					KeyEvent.VK_B, "brightness.gif", "onBrightness");

			addMenuItem("Edit/Contrast", "Apply contrast correction",
					KeyEvent.VK_A, "contrast.gif", "onContrast");

			addSubMenu("Help", KeyEvent.VK_H);
			addMenuItem("Help/About",
					"View application version and author information",
					KeyEvent.VK_A, "about.gif", "onAbout");
			// constructing Toolbar
			addToolBarButton("File/New");
			addToolBarButton("File/Load");
			addToolBarButton("File/Save as...");
			addToolBarSeparator();
			addToolBarButton("Edit/Select");
			addToolBarButton("Edit/From C to B");
			addToolBarSeparator();

			addToolBarButton("Edit/Grayscale");
			addToolBarButton("Edit/Negative");
			addToolBarSeparator();
			addToolBarButton("Edit/Floyd-Steinberg dithering");
			addToolBarButton("Edit/Orderted dithering");
			addToolBarSeparator();
			addToolBarButton("Edit/Double scale");
			addToolBarSeparator();
			addToolBarButton("Edit/Roberts edge detecting");
			addToolBarButton("Edit/Sobel edge detecting");
			addToolBarSeparator();
			addToolBarButton("Edit/Blur");
			addToolBarButton("Edit/Sharpen");
			addToolBarSeparator();
			addToolBarButton("Edit/Emboss");
			addToolBarButton("Edit/Aquarelle");
			addToolBarSeparator();

			addToolBarButton("Edit/Gamma");
			addToolBarButton("Edit/Brightness");
			addToolBarButton("Edit/Contrast");

			addToolBarSeparator();
			addToolBarButton("Help/About");
			addToolBarSeparator();
			addToolBarButton("File/Exit");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		toolBar.setFloatable(false);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panel.setBorder(new EmptyBorder(FltSettings.PANEL_PADDING, 0,
				FltSettings.PANEL_PADDING, 0));
		for (JPanel p : _zones) {
			JPanel outer = new JPanel();
			outer.setBorder(new MatteBorder(new Insets(1, 1, 1, 1), Color.black));
			p.setBorder(new EmptyBorder(new Insets(1, 1, 1, 1)));
			outer.add(p);
			panel.add(outer);
		}
		JScrollPane scroll_pane = new JScrollPane(panel);
		add(scroll_pane);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				onExit();
			}
		});

		this._zone_a.setFrameService(this);
		this._zone_b.setFrameService(this);
		this._zone_c.setFrameService(this);

		pack();
		reset();
	}

	/**
	 * Resets application to state with no images loaded and panels blocked
	 */
	public void reset() {
		setDocumentName(FltSettings.UNTITLED_DOCUMENT);
		setSelectBlocked(true);
		setSaveBlocked(true);
		setFromCtoBBlocked(true);
		setFiltersBlocked(true);
		for (ImagePanel p : _zones) {
			p.setImage(null);
		}
		setSelectBlocked(true);
		setDocumentName(FltSettings.UNTITLED_DOCUMENT);
	}

	/**
	 * Class represents dialog for Floyd-Steinberg dithering
	 */
	class FloydSteinbergDitheringDialog extends JDialog {

		private static final long serialVersionUID = 4639058986330908356L;
		private JSpinner r = new JSpinner(new SpinnerNumberModel(8, 1, 255, 1));
		private JSpinner g = new JSpinner(new SpinnerNumberModel(8, 1, 255, 1));
		private JSpinner b = new JSpinner(new SpinnerNumberModel(4, 1, 255, 1));
		private JButton ok = new JButton("Apply");

		/**
		 * Calls process and makes dialog visible
		 */
		public void showDialog() {
			process();
			setVisible(true);
		}

		/**
		 * Applies filter to image
		 */
		public void process() {
			BufferedImage o = _zone_b.getImage();
			_zone_c.setImage(Filters.getFloydSteinbergDitheredImage(o,
					(Integer) r.getValue(), (Integer) g.getValue(),
					(Integer) b.getValue()));
		}

		/**
		 * Default constructor
		 */
		public FloydSteinbergDitheringDialog() {
			super(FltFrame.this, "Floyd-Steinberg dithering dialog", true);
			setLocationRelativeTo(FltFrame.this);
			setResizable(false);
			setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

			add(new JLabel("Red:"));
			add(r);
			add(new JLabel("Green:"));
			add(g);
			add(new JLabel("Blue:"));
			add(b);
			add(ok);

			ChangeListener cl = new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					process();
				}
			};

			for (JSpinner s : new JSpinner[] { r, g, b }) {
				JComponent editor = s.getEditor();
				JFormattedTextField textfield = ((JSpinner.DefaultEditor) editor)
						.getTextField();
				NumberFormatter n_format = (NumberFormatter) textfield
						.getFormatter();
				n_format.setCommitsOnValidEdit(true);
				n_format.setAllowsInvalid(false);

				s.addChangeListener(cl);
			}

			ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});

			pack();
		}
	}

	FloydSteinbergDitheringDialog _floyd_steinberg_dialog = new FloydSteinbergDitheringDialog();

	/**
	 * Method is called when user chooses Floyd-Steinberg dithering menu item or
	 * corresponding button on toolbar
	 */
	public void onFloydSteinbergDithering() {
		_floyd_steinberg_dialog.showDialog();
	}

	/**
	 * Method is called when user chooses double scale menu item or
	 * corresponding button on toolbar
	 */
	public void onDoubleScale() {
		BufferedImage o = _zone_b.getImage();
		_zone_c.setImage(Filters.getDoubleScaleImage(o));
	}

	/**
	 * Method is called when user chooses ordered dithering menu item or
	 * corresponding button on toolbar
	 */
	public void onOrderedDithering() {
		BufferedImage o = _zone_b.getImage();
		_zone_c.setImage(Filters.getOrderedDitherImage(o));
	}

	/**
	 * Method is called when user chooses aquarelle menu item or corresponding
	 * button on toolbar
	 */
	public void onAquarelle() {
		BufferedImage o = _zone_b.getImage();
		BufferedImage csi = Filters.getColorSmoothedImage(o, 5);
		BufferedImage si = Filters.getSharpen5Image(csi);
		_zone_c.setImage(si);
	}

	/**
	 * Base class for dialogs with slider, spinner and button
	 */
	class SliderSpinnerDialog extends JDialog {

		private static final long serialVersionUID = -5016733013958923881L;
		protected int value;
		protected int max_value;
		protected JSlider slider;
		protected JSpinner spinner;
		protected JButton ok = new JButton("Apply");

		/**
		 * Calls process and makes dialog visible
		 */
		public void showDialog() {
			process();
			setVisible(true);
		}

		/**
		 * Method to be overriden with actual action. Does nothing.
		 */
		public void process() {
		}

		/**
		 * Inits dialog components
		 */
		private void init() {
			setLocationRelativeTo(FltFrame.this);

			this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
			this.setResizable(false);

			slider.setPaintTicks(true);
			slider.setPaintLabels(true);

			JComponent editor = spinner.getEditor();
			JFormattedTextField textfield = ((JSpinner.DefaultEditor) editor)
					.getTextField();
			NumberFormatter n_format = (NumberFormatter) textfield
					.getFormatter();
			n_format.setCommitsOnValidEdit(true);
			n_format.setAllowsInvalid(false);

			add(slider);
			add(spinner);
			add(ok);

			ok.addActionListener(new AbstractAction() {

				private static final long serialVersionUID = -3355926542129119774L;

				@Override
				public void actionPerformed(ActionEvent e) {
					SliderSpinnerDialog.this.setVisible(false);
				}
			});
			pack();
		}

		/**
		 * Constructor
		 * 
		 * @param title
		 *            dialog title
		 * @param init
		 *            init value
		 * @param max
		 *            max value
		 */
		public SliderSpinnerDialog(String title, int init, int min, int max) {
			super(FltFrame.this, title, true);

			value = init;
			max_value = max;

			slider = new JSlider(min, max_value, value);
			spinner = new JSpinner(new SpinnerNumberModel(value, 0, max_value,
					5));
			slider.setMajorTickSpacing(max_value / 5);
			spinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					slider.setValue((Integer) ((JSpinner) e.getSource())
							.getValue());
					value = slider.getValue();
					process();
				}
			});

			slider.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					spinner.setValue((Integer) ((JSlider) e.getSource())
							.getValue());
					value = (Integer) spinner.getValue();
				}
			});

			init();
		}
	}

	/**
	 * Class represents sobel edge detection filter dialog
	 */
	class SobelDialog extends SliderSpinnerDialog {

		private static final long serialVersionUID = -1560130858483233109L;

		/**
		 * Applies filter
		 */
		@Override
		public void process() {
			BufferedImage o = _zone_b.getImage();
			_zone_c.setImage(Filters.getSobelImage(o, value));
		}

		/**
		 * Default constructor
		 */
		public SobelDialog() {
			super("Sobel threshold dialog", 60, 0, 1020);
		}
	}

	private SobelDialog _sobel_dialog = new SobelDialog();

	/**
	 * Method is called when user chooses Sobel edge detection menu item or
	 * corresponding button on toolbar
	 */
	public void onSobel() {
		_sobel_dialog.showDialog();
	}

	/**
	 * Class represents Sobel edge detection filter dialog
	 */
	class RobertsDialog extends SliderSpinnerDialog {

		private static final long serialVersionUID = 8165566545358931694L;

		@Override
		public void process() {
			BufferedImage o = _zone_b.getImage();
			_zone_c.setImage(Filters.getRobertsImage(o, value));
		}

		/**
		 * Default constructor
		 */
		public RobertsDialog() {
			super("Roberts threshold dialog", 60, 0, 510);
		}
	}

	private RobertsDialog _roberts_dialog = new RobertsDialog();

	/**
	 * Method is called when user chooses Roberts edge detection menu item or
	 * corresponding button on toolbar
	 */
	public void onRoberts() {
		_roberts_dialog.showDialog();
	}

	/**
	 * Method is called when user chooses blur filter menu item or corresponding
	 * button on toolbar
	 */
	public void onBlur() {
		BufferedImage o = _zone_b.getImage();
		_zone_c.setImage(Filters.getBlurImage(o));
	}

	/**
	 * Method is called when user chooses emboss filter menu item or
	 * corresponding button on toolbar
	 */
	public void onEmboss() {
		BufferedImage o = _zone_b.getImage();
		_zone_c.setImage(Filters.getEmbossImage(o));
	}

	/**
	 * Method is called when user chooses sharpen filter menu item or
	 * corresponding button on toolbar
	 */
	public void onSharpen() {
		BufferedImage o = _zone_b.getImage();
		_zone_c.setImage(Filters.getSharpen3Image(o));
	}

	/**
	 * Method is called when user chooses negative filter menu item or
	 * corresponding button on toolbar
	 */
	public void onNegative() {
		BufferedImage o = _zone_b.getImage();
		_zone_c.setImage(Filters.getNegativeImage(o));
	}

	/**
	 * Method is called when user chooses grayscale filter menu item or
	 * corresponding button on toolbar
	 */
	public void onGrayscale() {
		BufferedImage o = _zone_b.getImage();
		_zone_c.setImage(Filters.getGreyscaleImage(o));
	}

	/**
	 * Class represents gamma filter dialog
	 */
	class GammaDialog extends SliderSpinnerDialog {

		private static final long serialVersionUID = -4718682390193192233L;

		@Override
		public void process() {
			BufferedImage o = _zone_b.getImage();
			_zone_c.setImage(Filters.getGammaCorrectedImage(o,
					(double) value / 100));
		}

		/**
		 * Default constructor
		 */
		public GammaDialog() {
			super("Gamma correction dialog", 100, 0, 1000);
		}
	}

	private GammaDialog _gamma_dialog = new GammaDialog();

	/**
	 * Method is called when user chooses grayscale filter menu item or
	 * corresponding button on toolbar
	 */
	public void onGamma() {
		_gamma_dialog.showDialog();
	}

	/**
	 * Class represents brightness filter dialog
	 */
	class BrightnessDialog extends SliderSpinnerDialog {
		private static final long serialVersionUID = -626750246915866360L;

		@Override
		public void process() {
			BufferedImage o = _zone_b.getImage();
			_zone_c.setImage(Filters.getBrightnessCorrectedImage(o,
					(double) value / 100));
		}

		/**
		 * Default constructor
		 */
		public BrightnessDialog() {
			super("Brightness correction dialog", 0, -100, 100);
		}
	}

	private BrightnessDialog _brightness_dialog = new BrightnessDialog();

	/**
	 * Method is called when user chooses grayscale filter menu item or
	 * corresponding button on toolbar
	 */
	public void onBrightness() {
		_brightness_dialog.showDialog();
	}

	/**
	 * Class represents contrast filter dialog
	 */
	class ContrastDialog extends SliderSpinnerDialog {

		private static final long serialVersionUID = 2805392208112947969L;

		@Override
		public void process() {
			BufferedImage o = _zone_b.getImage();
			_zone_c.setImage(Filters.getContrastCorrectedImage(o,
					(double) value / 100));
		}

		/**
		 * Default constructor
		 */
		public ContrastDialog() {
			super("Contrast correction dialog", 100, 0, 1000);
		}
	}

	private ContrastDialog _contrast_dialog = new ContrastDialog();

	/**
	 * Method is called when user chooses grayscale filter menu item or
	 * corresponding button on toolbar
	 */
	public void onContrast() {
		_contrast_dialog.showDialog();

	}

	/**
	 * Method is invoked after user presses Back toolbar buttons or chooses Edit
	 * -> From C to B
	 */
	public void onFromCtoB() {
		BufferedImage img = _zone_c.getImage();
		_zone_b.setImage(img.getSubimage(0, 0, img.getWidth(), img.getHeight()));
	}

	/**
	 * Method is called when user chooses select menu item or corresponding
	 * button on toolbar
	 */
	public void onSelect() {
		setSelectBlocked(true);
		_zone_a.startSelecting();
	}

	/**
	 * Method called when user chooses "New" in menu or on toolbar. Asks user to
	 * save current document (if needed) and sets application to new document
	 * state
	 */
	public void onNew() {
		reset();
	}

	/**
	 * Method called when user chooses "Exit" in menu or on toolbar. Asks user
	 * to save current document (if needed) and terminates application
	 */
	public void onExit() {
		System.exit(0);
	}

	/**
	 * Method called when user chooses "About" in menu or on toolbar. Loads text
	 * editor with <tt>FIT_8201_Sviridov_About.txt</tt> open
	 */
	public void onAbout() {
		String path = FltSettings.ABOUT_FILE;
		File f = new File(path);
		if (f.exists() == false) {
			path = ".." + System.getProperties().getProperty("file.separator")
					+ FltSettings.ABOUT_FILE;
			f = new File(path);
			if (f.exists() == false) {
				JOptionPane
						.showMessageDialog(
								this,
								"File "
										+ FltSettings.ABOUT_FILE
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
	 * Method called when user chooses "Load" in menu or on toolbar. Asks user
	 * to save current document (if needed), shows dialog to choose file and
	 * loads document from it
	 */
	public void onLoad() {
		try {
			File file = getOpenFileName("bmp", "24-bit color bitmap pictures");
			if (file == null) {
				return;
			}
			_zone_a.setImage(BmpImage.readBmpImage(file));
			_zone_b.setImage(null);
			_zone_c.setImage(null);
			setSelectBlocked(false);
			setDocumentName(file.getName());
		} catch (IllegalArgumentException ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(this,
					"Document is of unknown format", "Loading document",
					JOptionPane.ERROR_MESSAGE);
			setDocumentName(FltSettings.UNTITLED_DOCUMENT);
		} catch (Exception ex) {
			System.out.println(ex);
			JOptionPane.showMessageDialog(this,
					"Error loading file: \n" + ex.getLocalizedMessage(),
					"Loading document", JOptionPane.ERROR_MESSAGE);
			setDocumentName(FltSettings.UNTITLED_DOCUMENT);
		}
	}

	/**
	 * Method called when user chooses "Save" in menu or on toolbar. Shows
	 * dialog to choose/create file and saves document to it
	 */
	public void onSave() {
		try {
			File file = getSaveFileName("bmp", "24-bit color bitmap pictures");
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
			BmpImage.writeBmpImage(_zone_c.getImage(), file);
			setDocumentName(file.getName());
		} catch (Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(this,
					"Error saving file: \n" + e.getLocalizedMessage(),
					"Saving document", JOptionPane.ERROR_MESSAGE);
		}
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
				FltFrame frame = new FltFrame(FltSettings.FRAME_WIDTH,
						FltSettings.FRAME_HEIGHT);
				frame.setVisible(true);
			}
		});
	}

	@Override
	public void setSelectBlocked(boolean value) {
		JMenuBar menu_bar = getJMenuBar();
		JMenu edit = (JMenu) menu_bar.getComponent(1);
		edit.getMenuComponent(0).setEnabled(!value);
		toolBar.getComponent(4).setEnabled(!value);
	}

	@Override
	public void setFiltersBlocked(boolean value) {
		JMenuBar menu_bar = getJMenuBar();
		JMenu edit = (JMenu) menu_bar.getComponent(1);
		for (int i = 2; i < 16; ++i) {
			edit.getMenuComponent(i).setEnabled(!value);
		}

		for (int i = 7; i < 27; ++i) {
			if (i != 9 && i != 12 && i != 14 && i != 17 && i != 20 && i != 23) {
				toolBar.getComponent(i).setEnabled(!value);
			}
		}

	}

	@Override
	public void setSaveBlocked(boolean value) {
		JMenuBar menu_bar = getJMenuBar();
		JMenu file = (JMenu) menu_bar.getComponent(0);
		file.getMenuComponent(2).setEnabled(!value);
		toolBar.getComponent(2).setEnabled(!value);
	}

	@Override
	public void setFromCtoBBlocked(boolean value) {
		JMenuBar menu_bar = getJMenuBar();
		JMenu edit = (JMenu) menu_bar.getComponent(1);
		edit.getMenuComponent(1).setEnabled(!value);
		toolBar.getComponent(5).setEnabled(!value);
	}
}
