package adv.views;

import adv.utility.InputConstraints;
import adv.directedGraphModel.GElementDirectedGraph;
import adv.directedGraphModel.GElementVertex;
import adv.panels.Panel;
import edu.usfca.vas.app.Localized;
import edu.usfca.vas.window.tools.DesignToolsDG;
import edu.usfca.xj.appkit.frame.XJFrame;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;
import edu.usfca.xj.appkit.menu.XJMenu;
import edu.usfca.xj.appkit.menu.XJMenuItem;
import edu.usfca.xj.appkit.utils.XJAlert;

import javax.swing.*;
import java.awt.*;

public abstract class DirectedGraphView extends View {

    // Menu items
    private static final int MI_ADD_VERTEX = 0;
    private static final int MI_REMOVE_VERTEX = 2;
    private static final int MI_EDIT_EDGE = 3;
    private static final int MI_REMOVE_EDGE = 4;
    private static final int MI_CLEAR_ALL = 5;

    protected GElementDirectedGraph directedGraph;

    protected DesignToolsDG designToolFA = null;
    protected XJFrame parent;
    protected Panel panel;

    private boolean edgeModificationAllowed = false;
    private boolean automaticVertexCreation = true;
    protected boolean canvasLocked = false;

    public DirectedGraphView(Panel panel) {
        this.panel = panel;
        this.parent = panel.getWindow();
        setDirectedGraph(new GElementDirectedGraph(edgeModificationAllowed));
        setUpExplainationText("Message Text");
    }


    public int defaultLinkShape() {
        return GLink.SHAPE_ARC;
    }

    public void setDesignToolsPanel(DesignToolsDG designToolFA) {
        this.designToolFA = designToolFA;
    }

    public void setDirectedGraph(GElementDirectedGraph graph) {
        setRootElement(graph);
        directedGraph = graph;
    }

    public GElementDirectedGraph getDirectedGraph() {
        return directedGraph;
    }


    public JPopupMenu getContextualMenu(GElement element) {
        if (!canvasLocked) {

            boolean vertexSelected = false;
            boolean linkSelected = false;

            if (element != null) {
                vertexSelected = element.getClass().equals(GElementVertex.class);
                linkSelected = element.getClass().equals(GLink.class);
            }

            JPopupMenu menu = new JPopupMenu();
            menu.addPopupMenuListener(new MyContextualMenuListener());

            if (vertexSelected) {
                int N = directedGraph.getNumberVertices();
                GElementVertex selectedVertex = (GElementVertex) element;
                int vertexKey = Integer.parseInt(selectedVertex.getLabel());

                if (vertexKey == N - 1) {
                    addMenuItem(menu, Localized.getString("dgMIDelete"), MI_REMOVE_VERTEX, element);
                }

            } else if (linkSelected) {

                if (edgeModificationAllowed) {
                    addMenuItem(menu, Localized.getString("dgMIEdit"), MI_EDIT_EDGE, element);
                }

                addMenuItem(menu, Localized.getString("dgMIDelete"), MI_REMOVE_EDGE, element);
            } else {
                addMenuItem(menu, Localized.getString("dgMIAddVertex"), MI_ADD_VERTEX, null);
                menu.addSeparator();
                addMenuItem(menu, Localized.getString("dgMIDeleteAll"), MI_CLEAR_ALL, null);
            }

            return menu;
        }

        return null;
    }

    public void createVertexAtXY(double x, double y) {

        String s;
        if (automaticVertexCreation) {
            s = lowestNumberedUnusedVertex();
        } else {
            s = (String) JOptionPane.showInputDialog(parent.getJavaContainer(),
                    Localized.getString("dgNewVertexMessage"), Localized.getString("dgNewVertexTitle"),
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            s = s.trim();
        }

        if (s != null) {
            validateAndAddVertex(s, (int) x, (int) y);
        }
    }


    public void handleMenuEvent(XJMenu menu, XJMenuItem item) {
        switch (item.getTag()) {
            case MI_ADD_VERTEX:
                if (spaceExistsForVertex()) {
                    createVertexAtXY(getLastMousePosition().getX(), getLastMousePosition().getY());
                }
                break;
            case MI_REMOVE_VERTEX:
                getDirectedGraph().removeVertex((GElementVertex) item.getObject());
                checkGraphHasVertices();
                changeDone();
                setDefaultGraphColours();
                break;
            case MI_CLEAR_ALL:
                getDirectedGraph().clear();
                setUpExplainationText("Message Text");
                checkGraphHasVertices();
                changeDone();
                break;
            case MI_EDIT_EDGE:
                getDirectedGraph().editEdge((GLink) item.getObject());
                changeDone();
                setDefaultGraphColours();
                break;
            case MI_REMOVE_EDGE:
                getDirectedGraph().removeEdge((GLink) item.getObject());
                changeDone();
                setDefaultGraphColours();
                checkGraphHasVertices();
                break;
        }
    }

    private boolean spaceExistsForVertex() {
        if (getDirectedGraph().getNumberVertices() < InputConstraints.MAX_NUM_ELEMENTS) {
            return true;
        } else {
            XJAlert.display(parent.getJavaContainer(), "Too many vertices",
                    "A graph can have at most " + InputConstraints.MAX_NUM_ELEMENTS + " vertices!");
            return false;
        }
    }

    public void eventCreateElement(Point p, boolean doubleclick) {
        if (!canvasLocked) {

            if (doubleclick) {
                if (spaceExistsForVertex()) {
                    createVertexAtXY(p.x, p.y);
                }
            }

            if (designToolFA.getSelectedTool() == DesignToolsDG.TOOL_VERTEX) {
                if (spaceExistsForVertex()) {
                    String vertexValue;
                    if (automaticVertexCreation) {
                        vertexValue = lowestNumberedUnusedVertex();
                        designToolFA.consumeSelectedState();
                    } else {
                        vertexValue = designToolFA.retrieveVertexValue();
                    }
                    validateAndAddVertex(vertexValue, p.x, p.y);
                }
            }
        }
    }

    private String lowestNumberedUnusedVertex() {
        return getDirectedGraph().lowestNumberedUnusedVertex();
    }

    private void validateAndAddVertex(String vertexValue, int x, int y) {
        if (vertexValue != null) {
            if (!InputConstraints.isValidNumber(vertexValue)) {
                XJAlert.display(parent.getJavaContainer(), Localized.getString("dgInvalidInputTitle"),
                        "Please enter a valid number between "+InputConstraints.MIN_INPUT_VALUE+
                                "and "+InputConstraints.MAX_INPUT_VALUE+".");
            } else if (getDirectedGraph().containsVertex(vertexValue))
                XJAlert.display(parent.getJavaContainer(), Localized.getString("dgNewVertexTitle"),
                        Localized.getString("dgNewVertexAlreadyExists"));
            else {
                getDirectedGraph().addVertexAtXY(vertexValue, x, y);
                checkGraphHasVertices();
                changeDone();
                // Ensure graph elements appear in their default manner (i.e. with no colour)
                setDefaultGraphColours();
            }
        }
    }

    public boolean eventCanCreateLink() {
        int tool = designToolFA.getSelectedTool();
        if (tool == DesignToolsDG.TOOL_EDGE) {
            designToolFA.consumeSelectedState();
            return true;
        } else
            return false;
    }

    public void eventCreateLink(GElement source, String sourceAnchorKey, GElement target, String targetAnchorKey,
                                int shape, Point p) {
        if (!canvasLocked) {
            getDirectedGraph().createEdge((GElementVertex) source, sourceAnchorKey, (GElementVertex) target, targetAnchorKey,
                    shape, p);
            changeDone();
            setDefaultGraphColours();
            checkGraphHasVertices();

        }
    }

    public void eventEditElement(GElement e) {
        if (!canvasLocked) {
            if (edgeModificationAllowed) {
                if (e instanceof GLink) {
                    if (getDirectedGraph().editEdge((GLink) e)) {
                        changeDone();
                        setDefaultGraphColours();
                        checkGraphHasVertices();
                    }
                }
            }

        }
    }

    private void checkGraphHasVertices() {
        int N = getDirectedGraph().getNumberVertices();

        if (N == 0) {
            panel.disableGoAndSkip();
            panel.disableRestartButton();
        }

        if (N > 0) {
            panel.enableGoAndSkip();
            panel.disableRestartButton();
        }
    }

    protected abstract void setDefaultGraphColours();
}
