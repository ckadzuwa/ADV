package adsv.main;

import adsv.globals.Constants;
import adsv.panels.AVLTreePanel;
import adsv.panels.BubbleSortPanel;
import adsv.panels.DepthFirstSearchPanel;
import adsv.panels.TopologicalSortPanel;
import edu.usfca.xj.appkit.frame.XJWindow;
import edu.usfca.xj.appkit.menu.XJMainMenuBar;
import edu.usfca.xj.appkit.menu.XJMenu;
import edu.usfca.xj.appkit.menu.XJMenuItem;
import edu.usfca.xj.appkit.menu.XJMenuItemDelegate;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Window extends XJWindow implements XJMenuItemDelegate {

    private static final int MENUITEM_SORTING_ALGORITHMS = 201;
    private static final int MENUITEM_GRAPH_ALGORITHMS = 202;
    private static final int MENUITEM_TREE_ALGORITHMS = 203;

    protected JTabbedPane viewTabbedPane;
    protected BubbleSortPanel bubbleSortPanel;
    protected DepthFirstSearchPanel depthFirstSearchPanel;
    protected TopologicalSortPanel topologicalSortPanel;
    protected AVLTreePanel avlTreePanel;

    public Window() {

        setWindowSize();
        // setLookAndFeel();

        viewTabbedPane = new JTabbedPane();
        viewTabbedPane.setTabPlacement(JTabbedPane.LEFT);

        bubbleSortPanel = new BubbleSortPanel(this);
        depthFirstSearchPanel = new DepthFirstSearchPanel(this);
        topologicalSortPanel = new TopologicalSortPanel(this);
        avlTreePanel = new AVLTreePanel(this);

        viewTabbedPane.add("AVL Tree",avlTreePanel);
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

    }

    ;

    @Override
    public void customizeEditMenu(XJMenu menu) {
        removeRedundantDeafults(menu);

        menu.setTitle("Algorithms");
        menu.addItem(new XJMenuItem("Sorting Algortihms", KeyEvent.VK_1, MENUITEM_SORTING_ALGORITHMS, this));
        menu.addItem(new XJMenuItem("Graph Algorithms", KeyEvent.VK_2, MENUITEM_GRAPH_ALGORITHMS, this));
        menu.addItem(new XJMenuItem("Tree Algorithms", KeyEvent.VK_3, MENUITEM_TREE_ALGORITHMS, this));

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
                viewTabbedPane.add("Topological Sort", topologicalSortPanel);
                break;
            case MENUITEM_TREE_ALGORITHMS:
                viewTabbedPane.removeAll();
                viewTabbedPane.add("AVL Tree",avlTreePanel);
                break;

        }

    }
}
