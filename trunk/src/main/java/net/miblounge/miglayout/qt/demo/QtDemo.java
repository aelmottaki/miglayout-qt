package net.miblounge.miglayout.qt.demo;

import java.util.ArrayList;
import java.util.Random;

import net.miblounge.miglayout.qt.MigLayout;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.LayoutUtil;
import net.miginfocom.layout.PlatformDefaults;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QItemSelectionModel.SelectionFlag;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QMoveEvent;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSplitter;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWidgetItem;

public class QtDemo extends QMainWindow {
	public static final int SELECTED_INDEX = 0;

	private static final String[][] panels = new String[][] {
			//			{"BugTestApp", "BugTestApp, Disregard"},
			{"Welcome", "\n\n         \"MigLayout makes complex layouts easy and normal layouts one-liners.\""},
			{
					"Quick Start",
					"This is an example of how to build a common dialog type. Note that there are no special components, nested panels or absolute references to cell positions. If you look at the source code you will see that the layout code is very simple to understand."},
			{
					"Plain",
					"A simple example on how simple it is to create normal forms. No builders needed since the whole layout manager works like a builder."},
			{
					"Alignments",
					"Shows how the alignment of components are specified. At the top/left is the alignment for the column/row. The components have no alignments specified.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6."},
			{
					"Cell Alignments",
					"Shows how components are aligned when both column/row alignments and component constraints are specified. At the top/left are the alignment for the column/row and the text on the buttons is the component constraint that will override the column/row alignment if it is an alignment.\n\nNote that baseline alignment will be interpreted as 'center' before JDK 6."},
			{
					"Basic Sizes",
					"A simple example that shows how to use the column or row min/preferred/max size to set the sizes of the contained components and also an example that shows how to do this directly in the component constraints."},
			{
					"Growing",
					"A simple example that shows how to use the growx and growy constraint to set the sizes and how they should grow to fit the available size. Both the column/row and the component grow/shrink constraints can be set, but the components will always be confined to the space given by its column/row."},
			{
					"Grow Shrink",
					"Demonstrates the very flexible grow and shrink constraints that can be set on a component.\nComponents can be divided into grow/shrink groups and also have grow/shrink weight within each of those groups.\n\nBy default "
						+ "components shrink to their inherent (or specified) minimum size, but they don't grow."},
			{
					"Span",
					"This example shows the powerful spanning and splitting that can be specified in the component constraints. With spanning any number of cells can be merged with the additional option to split that space for more than one component. This makes layouts very flexible and reduces the number of times you will need nested panels to very few."},
			{
					"Flow Direction",
					"Shows the different flow directions. Flow direction for the layout specifies if the next cell will be in the x or y dimension. Note that it can be a different flow direction in the slit cell (the middle cell is slit in two). Wrap is set to 3 for all panels."},
			{
					"Grouping",
					"Sizes for both components and columns/rows can be grouped so they get the same size. For instance buttons in a button bar can be given a size-group so that they will all get "
						+ "the same minimum and preferred size (the largest within the group). Size-groups can be set for the width, height or both."},
			{
					"Units",
					"Demonstrates the basic units that are understood by MigLayout. These units can be extended by the user by adding one or more UnitConverter(s)."},
			{
					"Component Sizes",
					"Minimum, preferred and maximum component sizes can be overridden in the component constraints using any unit type. The format to do this is short and simple to understand. You simply specify the "
						+ "min, preferred and max sizes with a colon between.\n\nAbove are some examples of this. An exclamation mark means that the value will be used for all sizes."},
			{"Bound Sizes", "Shows how to create columns that are stable between tabs using minimum sizes."},
			{
					"Cell Position",
					"Even though MigLayout has automatic grid flow you can still specify the cell position explicitly. You can even combine absolute (x, y) and flow (skip, wrap and newline) constraints to build your layout."},
			{
					"Orientation",
					"MigLayout supports not only right-to-left orientation, but also bottom-to-top. You can even set the flow direction so that the flow is vertical instead of horizontal. It will automatically "
						+ "pick up if right-to-left is to be used depending on the ComponentWrapper, but it can also be manually set for every layout."},
			{
					"Absolute Position",
					"Demonstrates the option to place any number of components using absolute coordinates. This can be just the position (if min/preferred size) using \"x y p p\" format or"
						+ "the bounds using the \"x1 y1 x2 y2\" format. Any unit can be used and percent is relative to the parent.\nAbsolute components will not disturb the flow or occupy cells in the grid. "
						+ "Absolute positioned components will be taken into account when calculating the container's preferred size."},
			{
					"Component Links",
					"Components can be linked to any side of any other component. It can be a forward, backward or cyclic link references, as long as it is stable and won't continue to change value over many iterations."
						+ "Links are referencing the ID of another component. The ID can be overridden by the component's constrains or is provided by the ComponentWrapper. For instance it will use the component's 'name' on Swing.\n"
						+ "Since the links can be combined with any expression (such as 'butt1.x+10' or 'max(button.x, 200)' the links are very customizable."},
			{
					"Docking",
					"Docking components can be added around the grid. The docked component will get the whole width/height on the docked side by default, however this can be overridden. When all docked components are laid out, whatever space "
						+ "is left will be available for the normal grid laid out components. Docked components does not in any way affect the flow in the grid.\n\nSince the docking runs in the same code path "
						+ "as the normal layout code the same properties can be specified for the docking components. You can for instance set the sizes and alignment or link other components to their docked component's bounds."},
			{
					"Button Bars",
					"Button order is very customizable and are by default different on the supported platforms. E.g. Gaps, button order and minimum button size are properties that are 'per platform'. MigLayout picks up the current platform automatically and adjusts the button order and minimum button size accordingly, all without using a button builder or any other special code construct."},
			{
					"Debug",
					"Demonstrates the non-intrusive way to get visual debugging aid. There is no need to use a special DebugPanel or anything that will need code changes. The user can simply turn on debug on the layout manager by using the 'debug' constraint and it will "
						+ "continuously repaint the panel with debug information on top. This means you don't have to change your code to debug!"},
			{
					"Layout Showdown",
					"This is an implementation of the Layout Showdown posted on java.net by John O'Conner. The first tab is a pure implemenetation of the showdown that follows all the rules. The second tab is a slightly fixed version that follows some improved layout guidelines."
						+ "The source code is for bothe the first and for the fixed version. Note the simplification of the code for the fixed version. Writing better layouts with MiG Layout is reasier that writing bad.\n\nReference: http://weblogs.java.net/blog/joconner/archive/2006/10/more_informatio.html"},
			{
					"API Constraints1",
					"This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details."},
			{
					"API Constraints2",
					"This dialog shows the constraint API added to v2.0. It works the same way as the string constraints but with chained method calls. See the source code for details."},};

	private static int DOUBLE_BUFFER = 0;//SWT.DOUBLE_BUFFERED;

	private static int benchRuns = 0;
	private static long startupMillis = 0;
	private static long timeToShowMillis = 0;
	private static long benchRunTime = 0;
	private static String benchOutFileName = null;
	private static boolean append = false;

	private static long lastRunTimeStart = 0;
	private static StringBuffer runTimeSB = null;

	// Helper...
	private QPushButton winButt;
	private QPushButton macButt;
	private QLabel formatLabel;
	private QWidget form;
	private QWidget windowMovedListeningWidget = null;

	public static void main(final String[] args) {
		startupMillis = System.currentTimeMillis();

		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				final String arg = args[i].trim();
				if (arg.startsWith("-bench")) {
					benchRuns = 10;
					try {
						if (arg.length() > 6) {
							benchRuns = Integer.parseInt(arg.substring(6));
						}
					} catch (final Exception ex) {
					}
				} else if (arg.startsWith("-bout")) {
					benchOutFileName = arg.substring(5);
				} else if (arg.startsWith("-append")) {
					append = true;
				} else if (arg.startsWith("-verbose")) {
					runTimeSB = new StringBuffer(256);
				} else {
					System.out.println("Usage: [-bench[#_of_runs]] [-bout[benchmark_results_filename]] [-append]\n"
						+ " -bench Run demo as benchmark. Run count can be appended. 10 is default.\n"
						+ " -bout  Benchmark results output filename.\n"
						+ " -append Appends the result to the \"-bout\" file.\n"
						+ " -verbose Print the times of every run.\n"
						+ "\nExamples:\n"
						+ " java -jar swtdemoapp.jar -bench -boutC:/bench.txt -append\n"
						+ " java -jar swtdemoapp.jar -bench20\n"
						+ "NOTE! swt-win32-3232.dll must be in the current directory!");
					System.exit(0);
				}
			}
		}

		if (benchRuns == 0) {
			LayoutUtil.setDesignTime(null, true);
		}

		QApplication.initialize(args);

		final QtDemo gui = new QtDemo();
		gui.show();

		QApplication.exec();
	}

	final QListWidget pickerList;
	final QWidget layoutDisplayPanel;
	final MigLayout layoutDisplayPanelLayout;
	final QTextEdit descrTextArea;
	final QWidget shell;

	public QtDemo() {
		setWindowTitle("MigLayout QT Demo v2.5 - Mig Layout v" + LayoutUtil.getVersion());
		resize(new QSize(900, 600));

		shell = new QWidget(this);
		final MigLayout layout = new MigLayout("wrap", "[]u[grow,fill]", "[grow,fill][pref!]");
		shell.setLayout(layout);

		final QTabWidget layoutPickerTabPane = new QTabWidget(shell);
		layout.addItem(new QWidgetItem(layoutPickerTabPane), "spany,grow");

		pickerList = new QListWidget();
		pickerList.setBackgroundRole(layoutPickerTabPane.backgroundRole());
		// font bold...
		layoutPickerTabPane.addTab(pickerList, "Example Browser");
		for (int i = 0; i < panels.length; i++) {
			pickerList.addItem(panels[i][0]);
		}

		layoutDisplayPanel = new QWidget(shell);
		layoutDisplayPanelLayout = new MigLayout("fill, insets 0");
		layoutDisplayPanel.setLayout(layoutDisplayPanelLayout);
		layout.addItem(new QWidgetItem(layoutDisplayPanel));

		final QTabWidget descriptionTabPane = new QTabWidget(shell);
		layout.addItem(new QWidgetItem(descriptionTabPane), "growx,hmin 120,w 500:500");
		descrTextArea = createTextArea(descriptionTabPane, "", "");
		descrTextArea.setBackgroundRole(descriptionTabPane.backgroundRole());
		descriptionTabPane.addTab(descrTextArea, "Description");

		pickerList.itemSelectionChanged.connect(this, "dispatchSelection()");

		pickerList.selectionModel().setCurrentIndex(pickerList.model().index(SELECTED_INDEX, 0), SelectionFlag.SelectCurrent);
		dispatchSelection();

		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * if (benchRuns > 0) {
		 * doBenchmark();
		 * } else {
		 * pickerList.select(SELECTED_INDEX);
		 * dispatchSelection();
		 * 
		 * display.addFilter(SWT.KeyDown, new Listener() {
		 * 
		 * public void handleEvent(Event e)
		 * {
		 * if (e.character == 'b') {
		 * startupMillis = System.currentTimeMillis();
		 * timeToShowMillis = System.currentTimeMillis() - startupMillis;
		 * benchRuns = 1;
		 * doBenchmark();
		 * }
		 * }
		 * });
		 * }
		 */

		setCentralWidget(shell);
	}

	public QWidget createWelcome(final QWidget parent) {

		final QTabWidget tabbedPane = new QTabWidget(parent);

		final QWidget panel = new QWidget(tabbedPane);
		final MigLayout layout = new MigLayout("ins 20, fill");
		panel.setLayout(layout);
		tabbedPane.addTab(panel, "Welcome");

		final String s = "MigLayout's main purpose is to make layouts for SWT and Swing, and possibly other frameworks, much more powerful and a lot easier to create, especially for manual coding.\n\n"
			+ "The motto is: \"MigLayout makes complex layouts easy and normal layouts one-liners.\"\n\n"
			+ "The layout engine is very flexible and advanced, something that is needed to make it simple to use yet handle almost all layout use-cases.\n\n"
			+ "MigLayout can handle all layouts that the commonly used Swing Layout Managers can handle and this with a lot of extra features. "
			+ "It also incorporates most, if not all, of the open source alternatives FormLayout's and TableLayout's functionality."
			+ "\n\n\nThanks to Karsten Lentzsch from JGoodies.com for allowing the reuse of the main demo application layout and for his inspiring talks that led to this layout Manager."
			+ "\n\n\nMikael Grev\n"
			+ "MiG InfoCom AB\n"
			+ "miglayout@miginfocom.com";

		// One needs to set both min and pref for SWT wrapping text areas since it always returns the unwrapped size otherwise.
		final QTextEdit textArea = createTextArea(panel, s, "w 500:500, ay top, grow, push");
		textArea.setFrameShape(Shape.NoFrame);
		//textArea.setBackground(panel.getBackground());
		//textArea.setBackgroundMode(SWT.INHERIT_NONE);

		return tabbedPane;
	}

	public QWidget createQuick_Start(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		final MigLayout lm = new MigLayout("wrap", "[right][fill,sizegroup]unrel[right][fill,sizegroup]", "");
		final QWidget panel = createTabPanel(tabbedPane, "Quick Start", lm);

		addSeparator(panel, "General");
		createLabel(panel, "Company", "gap indent");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Contact", "gap indent");
		createTextField(panel, "", "span,growx");

		addSeparator(panel, "Propeller");
		createLabel(panel, "PTI/kW", "gap indent");
		createTextField(panel, "", "wmin 130");
		createLabel(panel, "Power/kW", "gap indent");
		createTextField(panel, "", "wmin 130");
		createLabel(panel, "R/mm", "gap indent");
		createTextField(panel, "", "wmin 130");
		createLabel(panel, "D/mm", "gap indent");
		createTextField(panel, "", "wmin 130");

		return tabbedPane;
	}

	public QWidget createPlain(final QWidget parent) {
		return createPlainImpl(parent, false);
	}

	private QWidget createPlainImpl(final QWidget parent, final boolean debug) {
		final QTabWidget tabbedPane = new QTabWidget(parent);
		final MigLayout lm = new MigLayout((debug && benchRuns == 0 ? "debug" : ""), "[r][100lp, fill][60lp][95lp, fill]", "");

		final QWidget panel = createTabPanel(tabbedPane, "Plain", lm);

		addSeparator(panel, "Manufacturer");
		createLabel(panel, "Company", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Contact", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Order No", "");
		createTextField(panel, "", "wmin 15*6,wrap");

		addSeparator(panel, "Inspector");
		createLabel(panel, "Name", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Reference No", "");
		createTextField(panel, "", "wrap");
		createLabel(panel, "Status", "");
		createCombo(panel, new String[] {"In Progress", "Finnished", "Released"}, "wrap");

		addSeparator(panel, "Ship");
		createLabel(panel, "Shipyard", "");
		createTextField(panel, "", "span,growx");
		createLabel(panel, "Register No", "");
		createTextField(panel, "", "");
		createLabel(panel, "Hull No", "right");
		createTextField(panel, "", "wmin 15*6,wrap");
		createLabel(panel, "Project StructureType", "");
		createCombo(panel, new String[] {"New Building", "Convention", "Repair"}, "wrap");

		if (debug) {
			createLabel(
					panel,
					"Blue is component bounds. Cell bounds (red) can not be shown in SWT",
					"newline,ax left,span,gaptop 40");
		}

		return tabbedPane;
	}

	public QWidget createAlignments(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// Horizontal tab

		final MigLayout horLM = new MigLayout("wrap", "[label]15[left]15[center]15[right]15[fill]15[]", "[]15[]");

		final QWidget horTab = createTabPanel(tabbedPane, "Horizontal", horLM);

		final String[] horLabels = new String[] {"[label]", "[left]", "[center]", "[right]", "[fill]", "[] (Default)"};
		final String[] horNames = new String[] {"First Name", "Phone Number", "Facsmile", "Email", "Address", "Other"};
		for (int c = 0; c < horLabels.length; c++) {
			createLabel(horTab, horLabels[c], "");
		}

		for (int r = 0; r < horLabels.length; r++) {
			for (int c = 0; c < horNames.length; c++) {
				if (c == 0) {
					createLabel(horTab, horNames[r] + ":", "");
				} else {
					createButton(horTab, horNames[r], "");
				}
			}
		}

		// Vertical tab
		final MigLayout verLM = new MigLayout(
			"wrap,flowy",
			"[]unrel[]rel[]",
			"[top]15[center]15[bottom]15[fill]15[fill,baseline]15[baseline]15[]");
		final QWidget verTab = createTabPanel(tabbedPane, "Vertical", verLM);

		final String[] verLabels = new String[] {
				"[top]", "[center]", "[bottom]", "[fill]", "[fill,baseline]", "[baseline]", "[] (Default)"};

		for (int c = 0; c < verLabels.length; c++) {
			createLabel(verTab, verLabels[c], "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createButton(verTab, "A Button", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextField(verTab, "JTextFied", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextArea(verTab, "Text    ", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextArea(verTab, "Text\nwith two lines", "");
		}

		for (int c = 0; c < verLabels.length; c++) {
			createTextArea(verTab, "Scrolling Text\nwith two lines", "");
		}

		return tabbedPane;
	}

	public QWidget createCell_Alignments(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// Horizontal
		final MigLayout hLM = new MigLayout("wrap", "[grow,left][grow,center][grow,right][grow,fill,center]", "[]unrel[][]");
		final QWidget hPanel = createTabPanel(tabbedPane, "Horizontal", hLM);

		final String[] sizes = new String[] {"", "growx", "growx 0", "left", "center", "right", "leading", "trailing"};
		createLabel(hPanel, "[left]", "c");
		createLabel(hPanel, "[center]", "c");
		createLabel(hPanel, "[right]", "c");
		createLabel(hPanel, "[fill,center]", "c, growx 0");

		for (int r = 0; r < sizes.length; r++) {
			for (int c = 0; c < 4; c++) {
				final String text = sizes[r].length() > 0 ? sizes[r] : "default";
				createButton(hPanel, text, sizes[r]);
			}
		}

		// Vertical
		final MigLayout vLM = new MigLayout(
			"wrap,flowy",
			"[right][]",
			"[grow,top][grow,center][grow,bottom][grow,fill,bottom][grow,fill,baseline]");
		final QWidget vPanel = createTabPanel(tabbedPane, "Vertical", vLM);

		final String[] vSizes = new String[] {"", "growy", "growy 0", "top", "center", "bottom"};
		createLabel(vPanel, "[top]", "center");
		createLabel(vPanel, "[center]", "center");
		createLabel(vPanel, "[bottom]", "center");
		createLabel(vPanel, "[fill, bottom]", "center, growy 0");
		createLabel(vPanel, "[fill, baseline]", "center");

		for (int c = 0; c < vSizes.length; c++) {
			for (int r = 0; r < 5; r++) {
				final String text = vSizes[c].length() > 0 ? vSizes[c] : "default";
				createButton(vPanel, text, vSizes[c]);
			}
		}

		return tabbedPane;
	}

	public QWidget createBasic_Sizes(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// Horizontal
		final MigLayout horLM = new MigLayout("", "[]15[75px]25[min]25[]", "[]15");
		final QWidget horTab = createTabPanel(tabbedPane, "Horizontal - Column size set", horLM);

		// Horizontal tab
		createLabel(horTab, "75px", "skip");
		createLabel(horTab, "Min", "");
		createLabel(horTab, "Pref", "wrap");

		createLabel(horTab, "new Text(15)", "");
		createTextField(horTab, "               ", "wmin 10");
		createTextField(horTab, "               ", "wmin 10");
		createTextField(horTab, "               ", "wmin 10");

		// Vertical tab 1
		final MigLayout verLM = new MigLayout("flowy,wrap", "[]15[]", "[]15[c,45:45]15[c,min]15[c,pref]");
		final QWidget verTab = createTabPanel(tabbedPane, "Vertical - Row sized", verLM);

		createLabel(verTab, "45px", "skip");
		createLabel(verTab, "Min", "");
		createLabel(verTab, "Pref", "");

		createLabel(verTab, "new Text(SWT.MULTI)", "");
		createTextArea(verTab, "", "");
		createTextArea(verTab, "", "");
		createTextArea(verTab, "", "");

		// Componentsized/Baseline 2
		final MigLayout verLM2 = new MigLayout("flowy,wrap", "[]15[]", "[]15[baseline]15[baseline]15[baseline]");
		final QWidget verTab2 = createTabPanel(tabbedPane, "Vertical - Component sized + Baseline", verLM2);

		createLabel(verTab2, "45px", "skip");
		createLabel(verTab2, "Min", "");
		createLabel(verTab2, "Pref", "");

		createLabel(verTab2, "new Text(SWT.MULTI)", "");
		createTextArea(verTab2, "", "height 45");
		createTextArea(verTab2, "", "height min");
		createTextArea(verTab2, "", "height pref");

		return tabbedPane;
	}

	public QWidget createGrowing(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// All tab
		final MigLayout allLM = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
		final QWidget allTab = createTabPanel(tabbedPane, "All", allLM);

		createLabel(allTab, "Fixed", "");
		createLabel(allTab, "Gets all extra space", "wrap");
		createTextField(allTab, "     ", "");
		createTextField(allTab, "     ", "");

		// Half tab
		final MigLayout halfLM = new MigLayout("", "[pref!][grow,fill]", "[]15[]");
		final QWidget halfTab = createTabPanel(tabbedPane, "Half", halfLM);

		createLabel(halfTab, "Fixed", "");
		createLabel(halfTab, "Gets half of extra space", "");
		createLabel(halfTab, "Gets half of extra space", "wrap");
		createTextField(halfTab, "     ", "");
		createTextField(halfTab, "     ", "");
		createTextField(halfTab, "     ", "");

		// Percent 1 tab
		final MigLayout p1LM = new MigLayout("", "[pref!][0:0,grow 25,fill][0:0,grow 75,fill]", "[]15[]");
		final QWidget p1Tab = createTabPanel(tabbedPane, "Percent 1", p1LM);

		createLabel(p1Tab, "Fixed", "");
		createLabel(p1Tab, "Gets 25% of extra space", "");
		createLabel(p1Tab, "Gets 75% of extra space", "wrap");
		createTextField(p1Tab, "     ", "");
		createTextField(p1Tab, "     ", "");
		createTextField(p1Tab, "     ", "");

		// Percent 2 tab
		final MigLayout p2LM = new MigLayout("", "[0:0,grow 33,fill][0:0,grow 67,fill]", "[]15[]");
		final QWidget p2Tab = createTabPanel(tabbedPane, "Percent 2", p2LM);

		createLabel(p2Tab, "Gets 33% of extra space", "");
		createLabel(p2Tab, "Gets 67% of extra space", "wrap");
		createTextField(p2Tab, "     ", "");
		createTextField(p2Tab, "     ", "");

		// Vertical 1 tab
		final MigLayout v1LM = new MigLayout("flowy", "[]15[]", "[][c,pref!][c,grow 25,fill][c,grow 75,fill]");
		final QWidget v1Tab = createTabPanel(tabbedPane, "Vertical 1", v1LM);

		createLabel(v1Tab, "Fixed", "skip");
		createLabel(v1Tab, "Gets 25% of extra space", "");
		createLabel(v1Tab, "Gets 75% of extra space", "wrap");
		createLabel(v1Tab, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
		createTextArea(v1Tab, "", "hmin 4*13");
		createTextArea(v1Tab, "", "hmin 4*13");
		createTextArea(v1Tab, "", "hmin 4*13");

		// Vertical 2 tab
		final MigLayout v2LM = new MigLayout("flowy", "[]15[]", "[][c,grow 33,fill][c,grow 67,fill]");
		final QWidget v2Tab = createTabPanel(tabbedPane, "Vertical 2", v2LM);

		createLabel(v2Tab, "Gets 33% of extra space", "skip");
		createLabel(v2Tab, "Gets 67% of extra space", "wrap");
		createLabel(v2Tab, "new Text(SWT.MULTI | SWT.WRAP | SWT.BORDER)", "");
		createTextArea(v2Tab, "", "hmin 4*13");
		createTextArea(v2Tab, "", "hmin 4*13");

		return tabbedPane;
	}

	public QWidget createGrow_Shrink(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// shrink tab
		final QGridLayout shrinkPanelLayout = new QGridLayout();
		final QWidget shrinkPanel = createTabPanel(tabbedPane, "Shrink", shrinkPanelLayout);

		final QSplitter sSplitPane = new QSplitter(shrinkPanel);
		sSplitPane.setOrientation(Orientation.Horizontal);
		shrinkPanelLayout.addWidget(sSplitPane);

		final QWidget sPanel = new QWidget();
		final MigLayout slm = new MigLayout("nogrid");
		sPanel.setLayout(slm);

		createTextField(sPanel, "shp 110", "shp 110,w 10:130");
		createTextField(sPanel, "Default (100)", "w 10:130");
		createTextField(sPanel, "shp 90", "shp 90,w 10:130");

		createTextField(sPanel, "shrink 25", "newline,shrink 25,w 10:130");
		createTextField(sPanel, "shrink 75", "shrink 75,w 10:130");

		createTextField(sPanel, "Default", "newline, w 10:130");
		createTextField(sPanel, "Default", "w 10:130");

		createTextField(sPanel, "shrink 0", "newline,shrink 0,w 10:130");

		createTextField(sPanel, "shp 110", "newline,shp 110,w 10:130");
		createTextField(sPanel, "shp 100,shrink 25", "shp 100,shrink 25,w 10:130");
		createTextField(sPanel, "shp 100,shrink 75", "shp 100,shrink 75,w 10:130");

		sSplitPane.addWidget(sPanel);

		final QTextEdit sDescText = createTextArea(
				sSplitPane,
				"Use the slider to see how the components shrink depending on the constraints set on them.\n\n'shp' means Shrink Priority. "
					+ "Lower values will be shrunk before higer ones and the default value is 100.\n\n'shrink' means Shrink Weight. "
					+ "Lower values relative to other's means they will shrink less when space is scarse. "
					+ "Shrink Weight is only relative to components with the same Shrink Priority. Default Shrink Weight is 100.\n\n"
					+ "The component's minimum size will always be honored.\n\nFor SWT, which doesn't have a component notion of minimum, "
					+ "preferred or maximum size, those sizes are set explicitly to minimum 10 and preferred 130 pixels.",
				"");
		sDescText.setFrameShape(Shape.NoFrame);

		// Grow tab
		final QGridLayout growPanelLayout = new QGridLayout();
		final QWidget growPanel = createTabPanel(tabbedPane, "Grow", growPanelLayout);

		final QSplitter gSplitPane = new QSplitter(growPanel);
		gSplitPane.setOrientation(Orientation.Horizontal);
		growPanelLayout.addWidget(gSplitPane);

		final QWidget gPanel = new QWidget();
		final MigLayout glm = new MigLayout("nogrid", "[grow]", "");
		gPanel.setLayout(glm);

		createButton(gPanel, "gp 110, grow", "gp 110, grow, wmax 170");
		createButton(gPanel, "Default (100), grow", "grow, wmax 170");
		createButton(gPanel, "gp 90, grow", "gp 90, grow, wmax 170");

		createButton(gPanel, "Default Button", "newline");

		createButton(gPanel, "growx", "newline,growx,wrap");

		createButton(gPanel, "gp 110, grow", "gp 110, grow, wmax 170");
		createButton(gPanel, "gp 100, grow 25", "gp 100, grow 25, wmax 170");
		createButton(gPanel, "gp 100, grow 75", "gp 100, grow 75, wmax 170");

		gSplitPane.addWidget(gPanel);
		final QTextEdit gDescText = createTextArea(
				gSplitPane,
				"'gp' means Grow Priority. "
					+ "Higher values will be grown before lower ones and the default value is 100.\n\n'grow' means Grow Weight. "
					+ "Higher values relative to other's means they will grow more when space is up for takes. "
					+ "Grow Weight is only relative to components with the same Grow Priority. Default Grow Weight is 0 which means "
					+ "components will normally not grow. \n\nNote that the buttons in the first and last row have max width set to 170 to "
					+ "emphasize Grow Priority.\n\nThe component's maximum size will always be honored.",
				"");
		gDescText.setFrameShape(Shape.NoFrame);

		return tabbedPane;
	}

	public QWidget createSpan(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// Horizontal span
		final MigLayout colLM = new MigLayout("", "[fill][25%,fill][105lp!,fill][150px!,fill]", "[]15[][]");
		final QWidget colPanel = createTabPanel(tabbedPane, "Column Span/Split", colLM);

		createTextField(colPanel, "Col1 [ ]", "");
		createTextField(colPanel, "Col2 [25%]", "");
		createTextField(colPanel, "Col3 [105lp!]", "");
		createTextField(colPanel, "Col4 [150px!]", "wrap");

		createLabel(colPanel, "Full Name:", "");
		createTextField(colPanel, "span, growx                              ", "span,growx");

		createLabel(colPanel, "Phone:", "");
		createTextField(colPanel, "   ", "span 3, split 5");
		createTextField(colPanel, "     ", null);
		createTextField(colPanel, "     ", null);
		createTextField(colPanel, "       ", null);
		createLabel(colPanel, "(span 3, split 4)", "wrap");

		createLabel(colPanel, "Zip/City:", "");
		createTextField(colPanel, "     ", "");
		createTextField(colPanel, "span 2, growx", null);

		// Vertical span
		final MigLayout rowLM = new MigLayout("wrap", "[225lp]para[225lp]", "[]3[]unrel[]3[]unrel[]3[]");
		final QWidget rowPanel = createTabPanel(tabbedPane, "Row Span", rowLM);

		createLabel(rowPanel, "Name", "");
		createLabel(rowPanel, "Notes", "");
		createTextField(rowPanel, "growx", null);
		createTextArea(rowPanel, "spany,grow          ", "spany,grow,hmin 13*5");
		createLabel(rowPanel, "Phone", "");
		createTextField(rowPanel, "growx", null);
		createLabel(rowPanel, "Fax", "");
		createTextField(rowPanel, "growx", null);

		return tabbedPane;
	}

	public QWidget createFlow_Direction(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		createFlowPanel(tabbedPane, "Layout: flowx, Cell: flowx", "", "flowx");
		createFlowPanel(tabbedPane, "Layout: flowx, Cell: flowy", "", "flowy");
		createFlowPanel(tabbedPane, "Layout: flowy, Cell: flowx", "flowy", "flowx");
		createFlowPanel(tabbedPane, "Layout: flowy, Cell: flowy", "flowy", "flowy");

		return tabbedPane;
	}

	private QWidget createFlowPanel(final QTabWidget parent, final String text, final String gridFlow, final String cellFlow) {
		final MigLayout lm = new MigLayout("center, wrap 3," + gridFlow, "[110,fill]", "[110,fill]");
		final QWidget panel = createTabPanel(parent, text, lm);

		for (int i = 0; i < 9; i++) {
			final QWidget b = createPanel(panel, "" + (i + 1), cellFlow);

			// TODO: Font
			// Font f = deriveFont(b, SWT.DEFAULT, 20);
			// b.getChildren()[0].setFont(f);
		}

		final QWidget b = createPanel(panel, "5:2", cellFlow + ",cell 1 1");
		// TODO: Font
		// Font f = deriveFont(b, SWT.DEFAULT, 20);
		// b.getChildren()[0].setFont(f);

		return panel;
	}

	public QWidget createGrouping(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// Ungrouped
		final MigLayout ugM = new MigLayout("", "[]push[][][]", "");
		final QWidget ugPanel = createTabPanel(tabbedPane, "Ungrouped", ugM);

		createButton(ugPanel, "Help", "");
		createButton(ugPanel, "< Back", "gap push");
		createButton(ugPanel, "Forward >", "");
		createButton(ugPanel, "Apply", "gap unrel");
		createButton(ugPanel, "Cancel", "gap unrel");

		// Grouped Components
		final MigLayout gM = new MigLayout("nogrid, fillx");
		final QWidget gPanel = createTabPanel(tabbedPane, "Grouped (Components)", gM);

		createButton(gPanel, "Help", "sg");
		createButton(gPanel, "< Back", "sg, gap push");
		createButton(gPanel, "Forward >", "sg");
		createButton(gPanel, "Apply", "sg, gap unrel");
		createButton(gPanel, "Cancel", "sg, gap unrel");

		// Grouped Columns
		final MigLayout gcM = new MigLayout("", "[sg,fill]push[sg,fill][sg,fill]unrel[sg,fill]unrel[sg,fill]", "");
		final QWidget gcPanel = createTabPanel(tabbedPane, "Grouped (Columns)", gcM);

		createButton(gcPanel, "Help", "");
		createButton(gcPanel, "< Back", "");
		createButton(gcPanel, "Forward >", "");
		createButton(gcPanel, "Apply", "");
		createButton(gcPanel, "Cancel", "");

		// Ungrouped Rows
		final MigLayout ugrM = new MigLayout();
		final QWidget ugrPanel = createTabPanel(tabbedPane, "Ungrouped Rows", ugrM);

		createLabel(ugrPanel, "File Number:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "BL/MBL number:", "");
		createTextField(ugrPanel, "7      ", "split 2");
		createTextField(ugrPanel, "7      ", "wrap");
		createLabel(ugrPanel, "Entry Date:", "");
		createTextField(ugrPanel, "7      ", "wrap");
		createLabel(ugrPanel, "RFQ Number:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "Goods:", "");
		createCheck(ugrPanel, "Dangerous", "wrap");
		createLabel(ugrPanel, "Shipper:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "Customer:", "");
		createTextField(ugrPanel, "", "split 2,growx");
		createButton(ugrPanel, "...", "width 60px:pref,wrap");
		createLabel(ugrPanel, "Port of Loading:", "");
		createTextField(ugrPanel, "30                            ", "wrap");
		createLabel(ugrPanel, "Destination:", "");
		createTextField(ugrPanel, "30                            ", "wrap");

		// Grouped Rows
		final MigLayout grM = new MigLayout("", "[]", "[sg]"); // "sg" is the only difference to previous panel
		final QWidget grPanel = createTabPanel(tabbedPane, "Grouped Rows", grM);

		createLabel(grPanel, "File Number:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "BL/MBL number:", "");
		createTextField(grPanel, "7      ", "split 2");
		createTextField(grPanel, "7      ", "wrap");
		createLabel(grPanel, "Entry Date:", "");
		createTextField(grPanel, "7      ", "wrap");
		createLabel(grPanel, "RFQ Number:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "Goods:", "");
		createCheck(grPanel, "Dangerous", "wrap");
		createLabel(grPanel, "Shipper:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "Customer:", "");
		createTextField(grPanel, "", "split 2,growx");
		createButton(grPanel, "...", "width 50px:pref,wrap");
		createLabel(grPanel, "Port of Loading:", "");
		createTextField(grPanel, "30                            ", "wrap");
		createLabel(grPanel, "Destination:", "");
		createTextField(grPanel, "30                            ", "wrap");

		return tabbedPane;
	}

	public QWidget createUnits(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		// Horizontal
		final MigLayout hLM = new MigLayout("wrap", "[right][]", "");
		final QWidget hPanel = createTabPanel(tabbedPane, "Horizontal", hLM);

		final String[] sizes = new String[] {"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "30sp"};
		for (int i = 0; i < sizes.length; i++) {
			createLabel(hPanel, sizes[i], "");
			createTextField(hPanel, "", "width " + sizes[i] + "");
		}

		// Horizontal lp
		final MigLayout hlpLM = new MigLayout("", "[right][][]", "");
		final QWidget hlpPanel = createTabPanel(tabbedPane, "Horizontal LP", hlpLM);

		createLabel(hlpPanel, "9 cols", "");
		createTextField(hlpPanel, "", "wmin 9*6");
		final String[] lpSizes = new String[] {"75lp", "75px", "88px", "100px"};
		createLabel(hlpPanel, "", "wrap");
		for (int i = 0; i < lpSizes.length; i++) {
			createLabel(hlpPanel, lpSizes[i], "");
			createTextField(hlpPanel, "", "width " + lpSizes[i] + ", wrap");
		}

		// Vertical
		final MigLayout vLM = new MigLayout("wrap,flowy", "[c]", "[top][top]");
		final QWidget vPanel = createTabPanel(tabbedPane, "Vertical", vLM);

		final String[] vSizes = new String[] {"72pt", "25.4mm", "2.54cm", "1in", "72px", "96px", "120px", "25%", "30sp"};
		for (int i = 0; i < sizes.length; i++) {
			createLabel(vPanel, vSizes[i], "");
			createTextArea(vPanel, "", "width 50!, height " + vSizes[i] + "");
		}

		// Vertical lp
		final MigLayout vlpLM = new MigLayout("wrap,flowy", "[c]", "[top][top]40px[top][top]");
		final QWidget vlpPanel = createTabPanel(tabbedPane, "Vertical LP", vlpLM);

		createLabel(vlpPanel, "4 rows", "");
		createTextArea(vlpPanel, "\n\n\n\n", "width 50!");
		createLabel(vlpPanel, "field", "");
		createTextField(vlpPanel, "", "wmin 5*9");

		final String[] vlpSizes1 = new String[] {"63lp", "57px", "63px", "68px", "25%"};
		final String[] vlpSizes2 = new String[] {"21lp", "21px", "23px", "24px", "10%"};
		for (int i = 0; i < vlpSizes1.length; i++) {
			createLabel(vlpPanel, vlpSizes1[i], "");
			createTextArea(vlpPanel, "", "width 50!, height " + vlpSizes1[i] + "");
			createLabel(vlpPanel, vlpSizes2[i], "");
			createTextField(vlpPanel, "", "height " + vlpSizes2[i] + "!,wmin 5*6");
		}

		createLabel(vlpPanel, "button", "skip 2");
		createButton(vlpPanel, "...", "");

		return tabbedPane;
	}

	public QWidget createComponent_Sizes(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget();

		final QGridLayout tabPanelLayout = new QGridLayout();
		final QWidget tabPanel = createTabPanel(tabbedPane, "Component Sizes", tabPanelLayout);

		final QSplitter splitPane = new QSplitter(tabPanel);
		splitPane.setOrientation(Orientation.Horizontal);
		tabPanelLayout.addWidget(splitPane);

		final QWidget panel = new QWidget();
		final MigLayout lm = new MigLayout("wrap", "[right][0:pref,grow]", "");
		panel.setLayout(lm);

		createLabel(panel, "", "");
		createTextField(panel, "8       ", "");
		createLabel(panel, "width min!", null);
		createTextField(panel, "3  ", "width min!");
		createLabel(panel, "width pref!", "");
		createTextField(panel, "3  ", "width pref!");
		createLabel(panel, "width min:pref", null);
		createTextField(panel, "8       ", "width min:pref");
		createLabel(panel, "width min:100:150", null);
		createTextField(panel, "8       ", "width min:100:150");
		createLabel(panel, "width min:100:150, growx", null);
		createTextField(panel, "8       ", "width min:100:150, growx");
		createLabel(panel, "width min:100, growx", null);
		createTextField(panel, "8       ", "width min:100, growx");
		createLabel(panel, "width 40!", null);
		createTextField(panel, "8       ", "width 40!");
		createLabel(panel, "width 40:40:40", null);
		createTextField(panel, "8       ", "width 40:40:40");

		splitPane.addWidget(panel);

		final QTextEdit descrText = createTextArea(
				splitPane,
				"Use slider to see how the components grow and shrink depending on the constraints set on them.",
				"");
		descrText.setFrameShape(Shape.NoFrame);

		return tabbedPane;
	}

	public QWidget createBound_Sizes(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget();

		for (int i = 0; i < 2; i++) { // Jumping for 0 and Stable for 1
			final String colConstr = i == 0 ? "[right][300]" : "[right, 150lp:pref][300]";

			final MigLayout lm1 = new MigLayout("wrap", colConstr, "");
			final QWidget panel1 = createTabPanel(tabbedPane, i == 0 ? "Jumping 1" : "Stable 1", lm1);

			createLabel(panel1, "File Number:", "");
			createTextField(panel1, "", "growx");
			createLabel(panel1, "RFQ Number:", "");
			createTextField(panel1, "", "growx");
			createLabel(panel1, "Entry Date:", "");
			createTextField(panel1, "        ", "wmin 6*6");
			createLabel(panel1, "Sales Person:", "");
			createTextField(panel1, "", "growx");

			final MigLayout lm2 = new MigLayout("wrap", colConstr, "");
			final QWidget panel2 = createTabPanel(tabbedPane, i == 0 ? "Jumping 2" : "Stable 2", lm2);

			createLabel(panel2, "Shipper:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
			createLabel(panel2, "Consignee:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
			createLabel(panel2, "Departure:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
			createLabel(panel2, "Destination:", "");
			createTextField(panel2, "        ", "split 2");
			createTextField(panel2, "", "growx");
		}
		return tabbedPane;
	}

	public QWidget createCell_Position(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget();

		// Absolute grid position
		final MigLayout absLM = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
		final QWidget absPanel = createTabPanel(tabbedPane, "Absolute", absLM);

		createPanel(absPanel, "cell 0 0", null);
		createPanel(absPanel, "cell 2 0", null);
		createPanel(absPanel, "cell 3 0", null);
		createPanel(absPanel, "cell 1 1", null);
		createPanel(absPanel, "cell 0 2", null);
		createPanel(absPanel, "cell 2 2", null);
		createPanel(absPanel, "cell 2 2", null);

		// Relative grid position with wrap
		final MigLayout relAwLM = new MigLayout(
			"wrap",
			"[100:pref,fill][100:pref,fill][100:pref,fill][100:pref,fill]",
			"[100:pref,fill]");
		final QWidget relAwPanel = createTabPanel(tabbedPane, "Relative + Wrap", relAwLM);

		createPanel(relAwPanel, "", null);
		createPanel(relAwPanel, "skip", null);
		createPanel(relAwPanel, "", null);
		createPanel(relAwPanel, "skip,wrap", null);
		createPanel(relAwPanel, "", null);
		createPanel(relAwPanel, "skip,split", null);
		createPanel(relAwPanel, "", null);

		// Relative grid position with manual wrap
		final MigLayout relWLM = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
		final QWidget relWPanel = createTabPanel(tabbedPane, "Relative", relWLM);

		createPanel(relWPanel, "", null);
		createPanel(relWPanel, "skip", null);
		createPanel(relWPanel, "wrap", null);
		createPanel(relWPanel, "skip,wrap", null);
		createPanel(relWPanel, "", null);
		createPanel(relWPanel, "skip,split", null);
		createPanel(relWPanel, "", null);

		// Mixed relative and absolute grid position
		final MigLayout mixLM = new MigLayout("", "[100:pref,fill]", "[100:pref,fill]");
		final QWidget mixPanel = createTabPanel(tabbedPane, "Mixed", mixLM);

		createPanel(mixPanel, "", null);
		createPanel(mixPanel, "cell 2 0", null);
		createPanel(mixPanel, "", null);
		createPanel(mixPanel, "cell 1 1,wrap", null);
		createPanel(mixPanel, "", null);
		createPanel(mixPanel, "cell 2 2,split", null);
		createPanel(mixPanel, "", null);

		return tabbedPane;
	}

	public QWidget createOrientation(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget();

		final MigLayout lm = new MigLayout("flowy", "[grow,fill]", "[]0[]15lp[]0[]");
		final QWidget mainPanel = createTabPanel(tabbedPane, "Orientation", lm);

		// Default orientation
		final QWidget defPanel = createPanel(mainPanel, new MigLayout("", "[][grow,fill]", ""));

		addSeparator(defPanel, "Default Orientation");
		createLabel(defPanel, "Level", "");
		createTextField(defPanel, "", "span,growx");
		createLabel(defPanel, "Radar", "");
		createTextField(defPanel, "", "");
		createTextField(defPanel, "", "");

		// Right-to-left, Top-to-bottom
		final QWidget rtlPanel = createPanel(mainPanel, new MigLayout("rtl,ttb", "[][grow,fill]", ""));
		addSeparator(rtlPanel, "Right to Left");
		createLabel(rtlPanel, "Level", "");
		createTextField(rtlPanel, "", "span,growx");
		createLabel(rtlPanel, "Radar", "");
		createTextField(rtlPanel, "", "");
		createTextField(rtlPanel, "", "");

		// Right-to-left, Bottom-to-top
		final QWidget rtlbPanel = createPanel(mainPanel, new MigLayout("rtl,btt", "[][grow,fill]", ""));
		addSeparator(rtlbPanel, "Right to Left, Bottom to Top");
		createLabel(rtlbPanel, "Level", "");
		createTextField(rtlbPanel, "", "span,growx");
		createLabel(rtlbPanel, "Radar", "");
		createTextField(rtlbPanel, "", "");
		createTextField(rtlbPanel, "", "");

		// Left-to-right, Bottom-to-top
		final QWidget ltrbPanel = createPanel(mainPanel, new MigLayout("ltr,btt", "[][grow,fill]", ""));
		addSeparator(ltrbPanel, "Left to Right, Bottom to Top");
		createLabel(ltrbPanel, "Level", "");
		createTextField(ltrbPanel, "", "span,growx");
		createLabel(ltrbPanel, "Radar", "");
		createTextField(ltrbPanel, "", "");
		createTextField(ltrbPanel, "", "");

		return tabbedPane;
	}

	public QWidget createAbsolute_Position(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget();

		// Pos tab
		final QGridLayout posTabLayout = new QGridLayout(); // filllayout
		final QWidget posTabPanel = createTabPanel(tabbedPane, "X Y Positions", posTabLayout);

		final QWidget posPanel = new QWidget(posTabPanel);
		final MigLayout posPanelLayout = new MigLayout();
		posPanel.setLayout(posPanelLayout);
		posTabLayout.addWidget(posPanel);

		createButton(posPanel, "pos 0.5al 0al", null);
		createButton(posPanel, "pos 1al 0al", null);
		createButton(posPanel, "pos 0.5al 0.5al", null);
		createButton(posPanel, "pos 5in 45lp", null);
		createButton(posPanel, "pos 0.5al 0.5al", null);
		createButton(posPanel, "pos 0.5al 1al", null);
		createButton(posPanel, "pos 1al .25al", null);
		createButton(posPanel, "pos visual.x2-pref visual.y2-pref", null);
		createButton(posPanel, "pos 1al -1in", null);
		createButton(posPanel, "pos 100 100", null);
		createButton(posPanel, "pos (10+(20*3lp)) 200", null);
		createButton(
				posPanel,
				"Drag Window! (pos 500-container.xpos 500-container.ypos)",
				"pos 500-container.xpos 500-container.ypos");

		// Bounds tab
		final QWidget boundsTabPanel = new QWidget(tabbedPane);
		final QGridLayout boundsTabLayout = new QGridLayout(); // filllayout
		boundsTabPanel.setLayout(boundsTabLayout);
		tabbedPane.addTab(boundsTabPanel, "X1 Y1 X2 Y2 Bounds");

		final QWidget boundsPanel = new QWidget();
		final MigLayout boundsPanelLayout = new MigLayout();
		boundsPanel.setLayout(boundsPanelLayout);
		boundsTabLayout.addWidget(boundsPanel);

		createButton(boundsPanel, "pos n 50% 50% n", null);
		createButton(boundsPanel, "pos 50% n n 50%", null);
		createButton(boundsPanel, "pos 50% 50% n n", null);
		createButton(boundsPanel, "pos n n 50% 50%", null);
		createButton(boundsPanel, "pos 0.5al 0.5al, pad 3 0 -3 0", null);
		createButton(boundsPanel, "pos 0.9al 0.4al n visual.y2-10", null);
		createButton(boundsPanel, "pos 0.1al 0.4al n visual.y2-10", null);
		createButton(boundsPanel, "pos visual.x 100 visual.x2 p", null);
		createButton(boundsPanel, "pos visual.x 40 visual.x2 70", null);
		createButton(boundsPanel, "pos 0 0 container.x2 n", null);

		final QLabel southLabel = createLabel(
				boundsPanel,
				"pos (visual.x+visual.w*0.1) visual.y2-40 (visual.x2-visual.w*0.1) visual.y2",
				null);
		final QPalette plt = new QPalette();
		// set Background
		plt.setBrush(ColorRole.Window, new QBrush(new QColor(200, 200, 255)));
		southLabel.setPalette(plt);
		southLabel.setAutoFillBackground(true);
		//deriveFont(southLabel, SWT.BOLD, 10);

		windowMovedListeningWidget = posPanel;

		return tabbedPane;
	}

	@Override
	protected void moveEvent(final QMoveEvent event) {
		if (windowMovedListeningWidget != null) {
			windowMovedListeningWidget.layout().invalidate();
			//windowMovedListeningWidget.layout();
		}
		super.moveEvent(event);
	}

	public QWidget createComponent_Links(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget();

		final QWidget linksPanel = createTabPanel(tabbedPane, "Component Links", new MigLayout());

		// Links tab
		createButton(linksPanel, "Mini", "pos null ta.y ta.x2 null, pad 3 0 -3 0");
		createTextArea(linksPanel, "Components, Please Link to Me!\nMy ID is: 'ta'", "id ta, pos 0.5al 0.5al, w 300");
		createButton(linksPanel, "id b1,pos ta.x2 ta.y2", null);
		createButton(linksPanel, "pos b1.x2+rel b1.y visual.x2 null", null);
		createCheck(linksPanel, "pos (ta.x+indent) (ta.y2+rel)", null);
		createButton(linksPanel, "pos ta.x2+rel ta.y visual.x2 null", null);
		createButton(linksPanel, "pos null ta.y+(ta.h-pref)/2 ta.x-rel null", null);
		createButton(linksPanel, "pos ta.x ta.y2+100 ta.x2 null", null);

		// External tab
		final QWidget externalPanel = createTabPanel(tabbedPane, "External Components", new MigLayout());

		final QPushButton extButt = createButton(externalPanel, "Bounds Externally Set!", "id ext, external");
		extButt.setGeometry(250, 130, 200, 40);
		createButton(externalPanel, "pos ext.x2 ext.y2", "pos ext.x2 ext.y2");
		createButton(externalPanel, "pos null null ext.x ext.y", "pos null null ext.x ext.y");

		// End grouping
		final QWidget egTabPanel = createTabPanel(tabbedPane, "End Grouping", new QGridLayout());
		final QWidget egPanel = createPanel(egTabPanel, new MigLayout());
		egTabPanel.layout().addWidget(egPanel);

		createButton(egPanel, "id b1, endgroupx g1, pos 200 200", null);
		createButton(egPanel, "id b2, endgroupx g1, pos (b1.x+2ind) (b1.y2+rel)", null);
		createButton(egPanel, "id b3, endgroupx g1, pos (b1.x+4ind) (b2.y2+rel)", null);
		createButton(egPanel, "id b4, endgroupx g1, pos (b1.x+6ind) (b3.y2+rel)", null);

		// Group Bounds tab
		final QWidget gpTabPanel = createTabPanel(tabbedPane, "Group Bounds", new QGridLayout());
		final MigLayout gpLayout = new MigLayout();
		final QWidget gpPanel = createPanel(gpTabPanel, gpLayout);
		gpTabPanel.layout().addWidget(gpPanel);

		final QWidget boundsPanel = createPanel(gpPanel, (QLayout) null);
		final QPalette plt = new QPalette();
		// set Background
		plt.setBrush(ColorRole.Window, new QBrush(new QColor(200, 200, 255)));
		boundsPanel.setPalette(plt);
		boundsPanel.setAutoFillBackground(true);

		gpLayout.addItem(new QWidgetItem(boundsPanel), "pos grp1.x grp1.y grp1.x2 grp1.y2");

		createButton(gpPanel, "id grp1.b1, pos n 0.5al 50% n", null);
		createButton(gpPanel, "id grp1.b2, pos 50% 0.5al n n", null);
		createButton(gpPanel, "id grp1.b3, pos 0.5al n n b1.y", null);
		createButton(gpPanel, "id grp1.b4, pos 0.5al b1.y2 n n", null);

		createButton(gpPanel, "pos n grp1.y2 grp1.x n", null);
		createButton(gpPanel, "pos n n grp1.x grp1.y", null);
		createButton(gpPanel, "pos grp1.x2 n n grp1.y", null);
		createButton(gpPanel, "pos grp1.x2 grp1.y2", null);

		return tabbedPane;
	}

	public QWidget createDocking(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);
		//tabbedPane.setLayoutData("grow");

		final QWidget p1 = createTabPanel(tabbedPane, "Docking 1", new MigLayout("fill"));

		createPanel(p1, "1. North", "north");
		createPanel(p1, "2. West", "west");
		createPanel(p1, "3. East", "east");
		createPanel(p1, "4. South", "south");

		final QTableWidget table = new QTableWidget(p1);
		table.setColumnCount(5);
		table.setRowCount(15);
		final ArrayList<String> columnNames = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {

			columnNames.add("Column " + (i + 1));
			table.setColumnWidth(i, 100);
		}
		table.setHorizontalHeaderLabels(columnNames);

		for (int r = 0; r < table.rowCount(); r++) {
			for (int c = 0; c < table.columnCount(); c++) {
				final String cell = "Cell " + (r + 1) + ", " + (c + 1);
				final QTableWidgetItem newItem = new QTableWidgetItem(cell);
				table.setItem(r, c, newItem);
			}
		}
		//table.setHeaderVisible(true);
		//table.setLinesVisible(true);
		((MigLayout) p1.layout()).addItem(new QWidgetItem(table), "grow");

		final QWidget p2 = createTabPanel(tabbedPane, "Docking 2 (fill)", new MigLayout("fill", "[c]", ""));

		createPanel(p2, "1. North", "north");
		createPanel(p2, "2. North", "north");
		createPanel(p2, "3. West", "west");
		createPanel(p2, "4. West", "west");
		createPanel(p2, "5. South", "south");
		createPanel(p2, "6. East", "east");
		createButton(p2, "7. Normal", "");
		createButton(p2, "8. Normal", "");
		createButton(p2, "9. Normal", "");

		final QWidget p3 = createTabPanel(tabbedPane, "Docking 3", new MigLayout());

		createPanel(p3, "1. North", "north");
		createPanel(p3, "2. South", "south");
		createPanel(p3, "3. West", "west");
		createPanel(p3, "4. East", "east");
		createButton(p3, "5. Normal", "");

		final QWidget p4 = createTabPanel(tabbedPane, "Docking 4", new MigLayout());

		createPanel(p4, "1. North", "north");
		createPanel(p4, "2. North", "north");
		createPanel(p4, "3. West", "west");
		createPanel(p4, "4. West", "west");
		createPanel(p4, "5. South", "south");
		createPanel(p4, "6. East", "east");
		createButton(p4, "7. Normal", "");
		createButton(p4, "8. Normal", "");
		createButton(p4, "9. Normal", "");

		final QWidget p5 = createTabPanel(tabbedPane, "Docking 5 (fillx)", new MigLayout("fillx", "[c]", ""));

		createPanel(p5, "1. North", "north");
		createPanel(p5, "2. North", "north");
		createPanel(p5, "3. West", "west");
		createPanel(p5, "4. West", "west");
		createPanel(p5, "5. South", "south");
		createPanel(p5, "6. East", "east");
		createButton(p5, "7. Normal", "");
		createButton(p5, "8. Normal", "");
		createButton(p5, "9. Normal", "");

		final QWidget p6 = createTabPanel(tabbedPane, "Random Docking", new MigLayout("fill"));

		final String[] sides = {"north", "east", "south", "west"};
		final Random rand = new Random();
		for (int i = 0; i < 20; i++) {
			final int side = rand.nextInt(4);
			createPanel(p6, ((i + 1) + " " + sides[side]), sides[side]);
		}
		createPanel(p6, "I'm in the Center!", "grow");

		return tabbedPane;
	}

	public QWidget createButton_Bars(final QWidget parent) {
		final MigLayout lm = new MigLayout("ins 0 0 15lp 0", "[grow]", "[grow]u[baseline,nogrid]");

		final QWidget mainPanel = new QWidget(parent);
		mainPanel.setLayout(lm);
		form = mainPanel;

		final QTabWidget tabbedPane = new QTabWidget(mainPanel);
		lm.addItem(new QWidgetItem(tabbedPane), "grow, wrap");

		createButtonBarsPanel(tabbedPane, "Buttons", "help", false);
		createButtonBarsPanel(tabbedPane, "Buttons with Help2", "help2", false);
		createButtonBarsPanel(tabbedPane, "Buttons (Same width)", "help", true);
		// TODO: check if it is necessary to add the panels...

		createLabel(mainPanel, "Button Order:", "");
		formatLabel = createLabel(mainPanel, "", "growx");
		// TODO: Font
		//deriveFont(formatLabel, SWT.BOLD , -1);

		winButt = createToggleButton(mainPanel, "Windows", "wmin button");
		macButt = createToggleButton(mainPanel, "Mac OS X", "wmin button");

		winButt.clicked.connect(this, "clicked_winbutt()");
		macButt.clicked.connect(this, "clicked_macbutt()");

		final QPushButton helpButt = createButton(mainPanel, "Help", "gap unrel,wmin button");
		helpButt.clicked.connect(this, "clicked_help()");

		(PlatformDefaults.getPlatform() == PlatformDefaults.WINDOWS_XP ? winButt : macButt).setChecked(true);

		return mainPanel;
	}

	protected void clicked_winbutt() {
		PlatformDefaults.setPlatform(PlatformDefaults.WINDOWS_XP);
		formatLabel.setText("'" + PlatformDefaults.getButtonOrder() + "'");
		winButt.setChecked(true);
		macButt.setChecked(false);
		form.layout();
	}

	protected void clicked_macbutt() {
		PlatformDefaults.setPlatform(PlatformDefaults.MAC_OSX);
		formatLabel.setText("'" + PlatformDefaults.getButtonOrder() + "'");
		macButt.setChecked(true);
		winButt.setChecked(false);
		form.layout();
	}

	protected void clicked_help() {
		QMessageBox.information(
				this,
				"Help",
				"See JavaDoc for PlatformConverter.getButtonBarOrder(..) for details on the format string.");
	}

	private QWidget createButtonBarsPanel(
		final QTabWidget parent,
		final String text,
		final String helpTag,
		final boolean sizeLocked) {
		final MigLayout lm = new MigLayout("nogrid, fillx, aligny 100%, gapy unrel");
		final QWidget panel = createTabPanel(parent, text, lm);

		// Notice that the order in the rows below does not matter...
		final String[][] buttons = new String[][] {
				{"No", "Yes"}, {"Help", "Close"}, {"OK", "Help"}, {"OK", "Cancel", "Help"}, {"OK", "Cancel", "Apply", "Help"},
				{"No", "Yes", "Cancel"}, {"Help", "< Move Back", "Move Forward >", "Cancel"}, {"Print...", "Cancel", "Help"},};

		for (int r = 0; r < buttons.length; r++) {
			for (int i = 0; i < buttons[r].length; i++) {
				final String txt = buttons[r][i];
				String tag = txt;

				if (txt.equals("Help")) {
					tag = helpTag;
				} else if (txt.equals("< Move Back")) {
					tag = "back";
				} else if (txt.equals("Close")) {
					tag = "cancel";
				} else if (txt.equals("Move Forward >")) {
					tag = "next";
				} else if (txt.equals("Print...")) {
					tag = "other";
				}
				final String wrap = (i == buttons[r].length - 1) ? ",wrap" : "";
				final String sizeGroup = sizeLocked ? ("sgx " + r + ",") : "";
				createButton(panel, txt, sizeGroup + "tag " + tag + wrap);
			}
		}

		return panel;
	}

	public QWidget createLayout_Showdown(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		final QWidget p1 = createTabPanel(tabbedPane, "Layout Showdown (pure)", new MigLayout("", "[]15[][grow,fill]15[grow]"));

		// References to text fields not stored to reduce code clutter.

		createList(p1, "Mouse, Mickey", "spany, growy, wmin 150");
		createLabel(p1, "Last Name", "");
		createTextField(p1, "", "");
		createLabel(p1, "First Name", "split"); // split divides the cell
		createTextField(p1, "", "growx, wrap");
		createLabel(p1, "Phone", "");
		createTextField(p1, "", "");
		createLabel(p1, "Email", "split");
		createTextField(p1, "", "growx, wrap");
		createLabel(p1, "Address 1", "");
		createTextField(p1, "", "span, growx"); // span merges cells
		createLabel(p1, "Address 2", "");
		createTextField(p1, "", "span, growx");
		createLabel(p1, "City", "");
		createTextField(p1, "", "wrap"); // wrap continues on next line
		createLabel(p1, "State", "");
		createTextField(p1, "", "");
		createLabel(p1, "Postal Code", "split");
		createTextField(p1, "", "growx, wrap");
		createLabel(p1, "Country", "");
		createTextField(p1, "", "wrap 15");

		createButton(p1, "New", "span, split, align right");
		createButton(p1, "Delete", "");
		createButton(p1, "Edit", "");
		createButton(p1, "Save", "");
		createButton(p1, "Cancel", "wrap push");

		// Fixed version *******************************************

		final QWidget p2 = createTabPanel(tabbedPane, "Layout Showdown (improved)", new MigLayout(
			"",
			"[]15[][grow,fill]15[][grow,fill]"));

		// References to text fields not stored to reduce code clutter.

		createList(p2, "Mouse, Mickey", "spany, growy, wmin 150");
		createLabel(p2, "Last Name", "");
		createTextField(p2, "", "");
		createLabel(p2, "First Name", "");
		createTextField(p2, "", "wrap");
		createLabel(p2, "Phone", "");
		createTextField(p2, "", "");
		createLabel(p2, "Email", "");
		createTextField(p2, "", "wrap");
		createLabel(p2, "Address 1", "");
		createTextField(p2, "", "span");
		createLabel(p2, "Address 2", "");
		createTextField(p2, "", "span");
		createLabel(p2, "City", "");
		createTextField(p2, "", "wrap");
		createLabel(p2, "State", "");
		createTextField(p2, "", "");
		createLabel(p2, "Postal Code", "");
		createTextField(p2, "", "width 50, grow 0, wrap");
		createLabel(p2, "Country", "");
		createTextField(p2, "", "wrap 15");

		createButton(p2, "New", "tag other, span, split");
		createButton(p2, "Delete", "tag other");
		createButton(p2, "Edit", "tag other");
		createButton(p2, "Save", "tag other");
		createButton(p2, "Cancel", "tag cancel, wrap push");

		return tabbedPane;
	}

	public QWidget createAPI_Constraints1(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		final LC layC = new LC().fill().wrap();
		final AC colC = new AC().align("right", 0).fill(1, 3).grow(100, 1, 3).align("right", 2).gap("15", 1);
		final AC rowC = new AC().align("top", 7).gap("15!", 6).grow(100, 8);

		final QWidget p1 = createTabPanel(tabbedPane, "Layout Showdown (improved)", new MigLayout(layC, colC, rowC));

		// References to text fields not stored to reduce code clutter.

		createList(p1, "Mouse, Mickey", new CC().dockWest().minWidth("150").gapX(null, "10"));
		createLabel(p1, "Last Name", "");
		createTextField(p1, "", "");
		createLabel(p1, "First Name", "");
		createTextField(p1, "", new CC().wrap());
		createLabel(p1, "Phone", "");
		createTextField(p1, "", "");
		createLabel(p1, "Email", "");
		createTextField(p1, "", "");
		createLabel(p1, "Address 1", "");
		createTextField(p1, "", new CC().spanX().growX());
		createLabel(p1, "Address 2", "");
		createTextField(p1, "", new CC().spanX().growX());
		createLabel(p1, "City", "");
		createTextField(p1, "", new CC().wrap());
		createLabel(p1, "State", "");
		createTextField(p1, "", "");
		createLabel(p1, "Postal Code", "");
		createTextField(p1, "", new CC().spanX(2).growX(0));
		createLabel(p1, "Country", "");
		createTextField(p1, "", new CC().wrap());

		createButton(p1, "New", new CC().spanX(5).split(5).tag("other"));
		createButton(p1, "Delete", new CC().tag("other"));
		createButton(p1, "Edit", new CC().tag("other"));
		createButton(p1, "Save", new CC().tag("other"));
		createButton(p1, "Cancel", new CC().tag("cancel"));

		return tabbedPane;
	}

	public QWidget createAPI_Constraints2(final QWidget parent) {
		final QTabWidget tabbedPane = new QTabWidget(parent);

		final LC layC = new LC().fill().wrap();
		final AC colC = new AC().align("right", 0).fill(1, 3).grow(100, 1, 3).align("right", 2).gap("15", 1);
		final AC rowC = new AC().index(6).gap("15!").align("top").grow(100, 8);

		final QWidget p1 = createTabPanel(tabbedPane, "Layout Showdown (improved)", new MigLayout(layC, colC, rowC));

		// References to text fields not stored to reduce code clutter.

		createLabel(p1, "Last Name", "");
		createTextField(p1, "", "");
		createLabel(p1, "First Name", "");
		createTextField(p1, "", new CC().wrap());
		createLabel(p1, "Phone", "");
		createTextField(p1, "", "");
		createLabel(p1, "Email", "");
		createTextField(p1, "", "");
		createLabel(p1, "Address 1", "");
		createTextField(p1, "", new CC().spanX().growX());
		createLabel(p1, "Address 2", "");
		createTextField(p1, "", new CC().spanX().growX());
		createLabel(p1, "City", "");
		createTextField(p1, "", new CC().wrap());
		createLabel(p1, "State", "");
		createTextField(p1, "", "");
		createLabel(p1, "Postal Code", "");
		createTextField(p1, "", new CC().spanX(2).growX(0));
		createLabel(p1, "Country", "");
		createTextField(p1, "", new CC().wrap());

		createButton(p1, "New", new CC().spanX(5).split(5).tag("other"));
		createButton(p1, "Delete", new CC().tag("other"));
		createButton(p1, "Edit", new CC().tag("other"));
		createButton(p1, "Save", new CC().tag("other"));
		createButton(p1, "Cancel", new CC().tag("cancel"));

		return tabbedPane;
	}

	protected void dispatchSelection() {
		final int ix = pickerList.selectionModel().currentIndex().row();
		if (ix == -1) {
			return;
		}

		windowMovedListeningWidget = null;

		final String methodName = "create" + panels[ix][0].replace(' ', '_');

		// Clear layoutDisplayPanel
		while (layoutDisplayPanelLayout.count() > 0) {
			final QWidgetItem item = ((QWidgetItem) layoutDisplayPanelLayout.takeAt(0));
			item.invalidate();
			item.widget().dispose();
			item.dispose();
		}

		try {
			final QWidget child = (QWidget) QtDemo.class.getMethod(methodName, new Class[] {QWidget.class}).invoke(
					QtDemo.this,
					new Object[] {layoutDisplayPanel});
			//child.setLayoutData("grow, wmin 500");
			layoutDisplayPanelLayout.addItem(new QWidgetItem(child), "grow, wmin 500");

			layoutDisplayPanelLayout.invalidate();
			descrTextArea.setText(panels[ix][1]);
		} catch (final Exception e1) {
			e1.printStackTrace(); // Should never happpen...
		}
	}

	/*
	 * private static Control[] comps = null; // thread hack...
	 * private static Control[] tabs = null; // thread hack...
	 * 
	 * private void doBenchmark()
	 * {
	 * final int pCnt = pickerList.getItemCount();
	 * Thread benchThread = new Thread() {
	 * public void run()
	 * {
	 * for (int j = 0; j < benchRuns; j++) {
	 * lastRunTimeStart = System.currentTimeMillis();
	 * final int jj = j;
	 * for (int i = 0; i < pCnt; i++) {
	 * final int ii = i;
	 * try {
	 * display.syncExec(new Runnable() {
	 * public void run () {
	 * pickerList.setSelection(ii);
	 * dispatchSelection();
	 * }
	 * });
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * 
	 * display.syncExec(new Runnable() {
	 * public void run() {
	 * comps = layoutDisplayPanel.getChildren();
	 * }
	 * });
	 * 
	 * for (int cIx = 0; cIx < comps.length; cIx++) {
	 * 
	 * if (comps[cIx] instanceof TabFolder) {
	 * final TabFolder tp = (TabFolder) comps[cIx];
	 * 
	 * display.syncExec(new Runnable() {
	 * public void run() {
	 * tabs = tp.getTabList();
	 * }
	 * });
	 * 
	 * for (int k = 0; k < tabs.length; k++) {
	 * final int kk = k;
	 * try {
	 * display.syncExec(new Runnable() {
	 * public void run() {
	 * tp.setSelection(kk);
	 * 
	 * if (timeToShowMillis == 0)
	 * timeToShowMillis = System.currentTimeMillis() - startupMillis;
	 * }
	 * });
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * };
	 * }
	 * }
	 * }
	 * }
	 * if (runTimeSB != null) {
	 * runTimeSB.append("Run ").append(jj).append(": ");
	 * runTimeSB.append(System.currentTimeMillis() - lastRunTimeStart).append(" millis.\n");
	 * }
	 * }
	 * 
	 * benchRunTime = System.currentTimeMillis() - startupMillis - timeToShowMillis;
	 * 
	 * final String message = "Java Version:       " + System.getProperty("java.version") + "\n" +
	 * "Time to Show:       " + timeToShowMillis + " millis.\n" +
	 * (runTimeSB != null ? runTimeSB.toString() : "") +
	 * "Benchmark Run Time: " + benchRunTime + " millis.\n" +
	 * "Average Run Time:   " + (benchRunTime / benchRuns) + " millis (" + benchRuns + " runs).\n\n";
	 * 
	 * display.syncExec(new Runnable() {
	 * public void run() {
	 * if (benchOutFileName == null) {
	 * MessageBox messageBox = new MessageBox(display.getActiveShell(), SWT.OK | SWT.ICON_INFORMATION);
	 * messageBox.setText("Results");
	 * messageBox.setMessage(message);
	 * messageBox.open();
	 * } else {
	 * FileWriter fw = null;
	 * try {
	 * fw = new FileWriter(benchOutFileName, append);
	 * fw.write(message);
	 * } catch(IOException ex) {
	 * ex.printStackTrace();
	 * } finally {
	 * if (fw != null)
	 * try {fw.close();} catch(IOException ex) {}
	 * }
	 * }
	 * }
	 * });
	 * 
	 * System.out.println(message);
	 * 
	 * if (benchOutFileName != null)
	 * System.exit(0);
	 * }
	 * };
	 * benchThread.start();
	 * }
	 * 
	 * 
	 * public Control createTest(Composite parent)
	 * {
	 * // TabFolder tabFolder = new TabFolder(parent2, DOUBLE_BUFFER);
	 * 
	 * Button button;
	 * 
	 * Composite composite = new Composite(parent, SWT.NONE);
	 * composite.setLayout(new MigLayout("debug", "[right][grow]", ""));
	 * 
	 * button = new Button(composite, SWT.PUSH);
	 * button.setText("New");
	 * button.setLayoutData("span 2, align left, split, sgx button");
	 * button = new Button(composite, SWT.PUSH);
	 * button.setText("Edit");
	 * button.setLayoutData("sgx button");
	 * button = new Button(composite, SWT.PUSH);
	 * button.setText("Cancel");
	 * button.setLayoutData("sgx button");
	 * button = new Button(composite, SWT.PUSH);
	 * button.setText("Save");
	 * button.setLayoutData("sgx button, wrap");
	 * 
	 * new Label(composite, SWT.NONE).setText("Name");
	 * Text text = new Text(composite, SWT.BORDER);
	 * text.setLayoutData("sgy control, pushx, growx, wrap");
	 * 
	 * new Label(composite, SWT.NONE).setText("Sex");
	 * Combo combo = new Combo(composite, SWT.DROP_DOWN);
	 * combo.setLayoutData("sgy control, w 50!, wrap");
	 * combo.setItems(new String[]
	 * { "M", "F", "-" });
	 * 
	 * 
	 * return composite;
	 * }
	 * 
	 * 
	 * 
	 * public Control createDebug(Composite parent)
	 * {
	 * return createPlainImpl(parent, true);
	 * }
	 * 
	 * // **********************************************************
	 * // * Helper Methods
	 * // **********************************************************
	 * 
	 * 
	 * private Font deriveFont(Control cont, int style, int height)
	 * {
	 * Font f = cont.getFont();
	 * FontData fd = f.getFontData()[0];
	 * if (style != SWT.DEFAULT)
	 * fd.setStyle(style);
	 * 
	 * if (height != -1)
	 * fd.setHeight(height);
	 * 
	 * Font font = new Font(display, fd);
	 * cont.setFont(font);
	 * return font;
	 * }
	 */

	private QListWidget createList(final QWidget parent, final String text, final Object layoutdata) {
		final QListWidget list = new QListWidget(parent);
		list.addItem(text);

		final MigLayout layout = (MigLayout) parent.layout();
		if (layout != null) {
			layout.addItem(new QWidgetItem(list), (layoutdata != null) ? layoutdata : text);
		}
		return list;
	}

	private QTextEdit createTextArea(final QWidget parent, final String text, final String layoutdata) {
		final QTextEdit result = new QTextEdit(parent);
		result.setText(text);

		final MigLayout layout = (MigLayout) parent.layout();
		if (layout != null) {
			layout.addItem(new QWidgetItem(result), (layoutdata != null) ? layoutdata : text);
		}
		return result;
	}

	private QWidget createPanel(final QWidget parent, String text, final Object layout) {
		//QColor bg = new Color(display.getActiveShell().getDisplay(), 255, 255, 255);
		//QWidget panel = new QWidget(parent);
		final QFrame panel = new QFrame(parent);
		panel.setFrameShape(Shape.Panel);
		final MigLayout panelLayout = new MigLayout("fill");
		panel.setLayout(panelLayout);
		final MigLayout parentLayout = (MigLayout) (parent.layout());
		//panel.setBackground(bg);
		parentLayout.addItem(new QWidgetItem(panel), layout != null ? layout : text);
		text = text.length() == 0 ? "\"\"" : text;

		final QLabel label = createLabel(panel, text, "grow");
		label.setAlignment(Qt.AlignmentFlag.AlignCenter);
		//label.setBackground(bg);

		//		configureActiveComponet(panel);

		return panel;
	}

	private QWidget createPanel(final QWidget parent, final QLayout layout) {
		final QWidget panel = new QWidget(parent);
		if (layout != null) {
			panel.setLayout(layout);
		}
		if (parent.layout() instanceof MigLayout) {
			((MigLayout) parent.layout()).addItem(new QWidgetItem(panel));
		}
		return panel;
	}

	private QLabel createLabel(final QWidget parent, final String text, final Object layoutdata) {
		final QLabel b = new QLabel(parent);
		final MigLayout layout = (MigLayout) parent.layout();
		b.setText(text);
		layout.addItem(new QWidgetItem(b), layoutdata != null ? layoutdata : text);

		return b;
	}

	private void addSeparator(final QWidget panel, final String text) {
		final QLabel l = createLabel(panel, text, "gaptop para, span, split 2");
		//l.setForeground(new QColor(display, 0, 70, 213));

		final QPalette plt = new QPalette();
		plt.setColor(ColorRole.WindowText, new QColor(0, 70, 213));
		l.setPalette(plt);

		final QFrame f = new QFrame(panel);
		f.setFrameShape(QFrame.Shape.HLine);
		f.setFrameShadow(QFrame.Shadow.Sunken);
		final MigLayout layout = (MigLayout) panel.layout();
		layout.addItem(new QWidgetItem(f), "gapleft rel, gaptop para, growx");
	}

	private QLineEdit createTextField(final QWidget parent, final String text, final Object layoutdata) {
		final QLineEdit b = new QLineEdit(parent);
		b.setText(text);
		// TODO: set length
		final MigLayout layout = (MigLayout) parent.layout();
		layout.addItem(new QWidgetItem(b), layoutdata != null ? layoutdata : text);

		return b;
	}

	private QComboBox createCombo(final QWidget parent, final String[] texts, final Object layoutdata) {
		final QComboBox b = new QComboBox(parent);
		for (int i = 0; i < texts.length; i++) {
			b.addItem(texts[i]);
		}

		b.setEditable(true); // discuss
		b.setCurrentIndex(-1); // clear the text

		final MigLayout layout = (MigLayout) parent.layout();
		layout.addItem(new QWidgetItem(b), layoutdata);

		return b;
	}

	private QPushButton createButton(final QWidget parent, final String text, final Object layoutdata) {
		return createButton(parent, text, layoutdata, false);
	}

	private QPushButton createButton(final QWidget parent, final String text, final Object layoutdata, final boolean bold) {
		final QPushButton b = new QPushButton(parent);
		b.setText(text.length() == 0 ? "\"\"" : text);
		// TODO: set bold ?
		final MigLayout layout = (MigLayout) parent.layout();
		layout.addItem(new QWidgetItem(b), layoutdata != null ? layoutdata : text);

		//		configureActiveComponet(b);

		return b;
	}

	private QPushButton createToggleButton(final QWidget parent, final String text, final Object layoutdata) {
		final QPushButton b = new QPushButton(parent); // SWT.TOGGLE
		b.setCheckable(true);
		b.setText(text.length() == 0 ? "\"\"" : text);

		final MigLayout layout = (MigLayout) parent.layout();
		layout.addItem(new QWidgetItem(b), layoutdata != null ? layoutdata : text);

		return b;
	}

	private QCheckBox createCheck(final QWidget parent, final String text, final Object layoutdata) {
		final QCheckBox b = new QCheckBox(parent);
		b.setText(text);

		final MigLayout layout = (MigLayout) parent.layout();
		layout.addItem(new QWidgetItem(b), layoutdata != null ? layoutdata : text);

		return b;
	}

	private QWidget createTabPanel(final QTabWidget parent, final String title, final QLayout layout) {
		final QWidget result = new QWidget(parent);
		parent.addTab(result, title);
		result.setLayout(layout);

		return result;
	}

}
