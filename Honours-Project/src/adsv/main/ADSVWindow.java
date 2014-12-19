package adsv.main;

import adsv.globals.Constants;
import adsv.panels.ADSVBubbleSortPanel;
import adsv.panels.ADSVDepthFirstSearchPanel;
import adsv.panels.ADSVTopologicalSortPanel;
import edu.usfca.xj.appkit.frame.XJWindow;
import edu.usfca.xj.appkit.menu.XJMainMenuBar;
import edu.usfca.xj.appkit.menu.XJMenu;
import edu.usfca.xj.appkit.menu.XJMenuItem;
import edu.usfca.xj.appkit.menu.XJMenuItemDelegate;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ADSVWindow extends XJWindow implements XJMenuItemDelegate {

	private static final int MENUITEM_SORTING_ALGORITHMS = 201;
	private static final int MENUITEM_GRAPH_ALGORITHMS = 202;

	protected JTabbedPane viewTabbedPane;
	protected ADSVBubbleSortPanel bubbleSortPanel;
	protected ADSVDepthFirstSearchPanel depthFirstSearchPanel;
	protected ADSVTopologicalSortPanel topologicalSortPanel;

	public ADSVWindow() {

		setWindowSize();
		// setLookAndFeel();

		viewTabbedPane = new JTabbedPane();
		viewTabbedPane.setTabPlacement(JTabbedPane.LEFT);

		bubbleSortPanel = new ADSVBubbleSortPanel(this);
		depthFirstSearchPanel = new ADSVDepthFirstSearchPanel(this);
		topologicalSortPanel = new ADSVTopologicalSortPanel(this);

		viewTabbedPane.add("Depth First Search", depthFirstSearchPanel);
		viewTabbedPane.add("Topological Sort",topologicalSortPanel);
		getContentPane().add(viewTabbedPane);
		pack();
	}

	private void setLookAndFeel() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				try {
					UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
				} catch (UnsupportedLookAndFeelException e) {

				}
				UIManager.getLookAndFeelDefaults().put("ClassLoader",
						SubstanceBusinessBlackSteelLookAndFeel.class.getClassLoader());
			}

		});
	}

	private void setWindowSize() {

		Rectangle r = new Rectangle(Constants.CANVAS_SIZE);
		r.width *= 1.065f;
		r.height *= 1.125f;
		getRootPane().setPreferredSize(r.getSize());
	}

	public void setData(Object dataForKey) {

	}

	public Object getData() {
		return null;
	}

	// Adding additional menu between Algorithms and Window Menu
	@Override
	public void customizeMenuBar(XJMainMenuBar menubar) {

		XJMenu menu = new XJMenu();
		menu.setTitle("Data Structures");

		menubar.addCustomMenu(menu);

	};

	@Override
	public void customizeEditMenu(XJMenu menu) {
		removeRedundantDeafults(menu);

		menu.setTitle("Algorithms");
		menu.addItem(new XJMenuItem("Sorting Algortihms", KeyEvent.VK_1, MENUITEM_SORTING_ALGORITHMS, this));
		menu.addItem(new XJMenuItem("Graph Algorithms", KeyEvent.VK_2, MENUITEM_GRAPH_ALGORITHMS, this));

	}

	// Remove the default contents of Edit Menu
	public void removeRedundantDeafults(XJMenu menu) {
		menu.removeAllItems();
	}

	public void handleMenuEvent(XJMenu menu, XJMenuItem item) {
		super.handleMenuEvent(menu, item);

		switch (item.getTag()) {

		case MENUITEM_SORTING_ALGORITHMS:
			viewTabbedPane.removeAll();
			viewTabbedPane.add("Bubble Sort", bubbleSortPanel);
			break;
		case MENUITEM_GRAPH_ALGORITHMS:
			viewTabbedPane.removeAll();
			viewTabbedPane.add("Depth First Search", depthFirstSearchPanel);
			viewTabbedPane.add("Topological Sort",topologicalSortPanel);
			break;
			
		}

	}
}
