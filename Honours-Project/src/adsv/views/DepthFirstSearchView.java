package adsv.views;

import adsv.directedGraphModel.EdgePair;
import adsv.directedGraphModel.GElementVertex;
import adsv.panels.Panel;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class DepthFirstSearchView extends DirectedGraphView {

    protected static final Color UNVISITED = Color.WHITE;
    protected static final Color VISITED_AND_PROCESSING = Color.LIGHT_GRAY;
    protected static final Color VISITED_AND_FINISHED = Color.decode("#424242");
    protected static final Color DEFAULT_EDGE_COLOR = Color.BLACK;
    protected static final Color CONSIDER_EDGE_COLOR = Color.decode("#FF6F00");//Constants.ANDROID_RED;
    protected static final Color TRAVERSED_EDGE_COLOR = Color.BLUE;
    protected GElement highlightCircle;
    protected HashMap<Integer, TreeSet<Integer>> connectedVertices;
    protected ArrayList<Integer> visitPath;

    public DepthFirstSearchView(Panel panel) {
        super(panel);
    }

    public void runAlgorithm() {
        dfs();
    }

    public void dfs() {
        lockCanvas();
        runSetup();
        performDepthFirstSearch();
        showResults();
        unlockCanvas();
    }

    private void lockCanvas() {
        canvasLocked = true;
    }

    private void unlockCanvas() {
        canvasLocked = false;
    }

    protected void runSetup() {
        connectedVertices = directedGraph.getConnectedMatrix();
        visitPath = new ArrayList<Integer>();
    }

    protected void performDepthFirstSearch() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexNotVisited(vertex)) {
                dfsFromVertex(vertex);
            }
            removeHighlightCircle();
        }
    }

    protected void removeHighlightCircle() {
        removeAny(highlightCircle);
        highlightCircle = null;
    }

    protected boolean dfsFromVertex(int vertex) {
        visitVertex(vertex);
        // If vertex has neighbors
        if (connectedVertices.get(vertex) != null) {

            displayMessage("Process vertex "+vertex+"'s neighbours.");
            repaintwait();

            for (Integer neighbor : connectedVertices.get(vertex)) {

                considerTraversingEdge(vertex, neighbor);

                if (vertexNotVisited(neighbor)) {
                    setEdgeAsTraversed(vertex, neighbor);

                    if (dfsFromVertex(neighbor)) {
                        return true; //If a cycle is detected further down in DFS - Halt exploration
                    }

                    backTrackTo(vertex);
                } else if (vertexBeingProcessed(neighbor)) {

                    if (indicateCycleDetected(neighbor)) {
                        return true; // Indicate a cycle has been detected
                    }

                    abortEdgeTraversal(vertex, neighbor);
                } else {

                    if (vertexVisited(neighbor)) {
                        abortEdgeTraversal(vertex, neighbor);
                    }
                }
            }
        } else {
            displayMessage("Vertex "+vertex+" has no neighbours to be processed.");
            repaintwait();
        }

        recordVertexFinish(vertex);
        return false; // No cycle reported on this DFS exploration
    }

    private void setEdgeAsTraversed(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(TRAVERSED_EDGE_COLOR);
    }

    private void considerTraversingEdge(Integer fromVertex, Integer toVertex) {
        displayMessage("Consider traversing edge from vertex " + fromVertex + " to vertex " + toVertex+".");
        getEdge(fromVertex, toVertex).setOutlineColor(CONSIDER_EDGE_COLOR);
        repaintwait();
    }

    protected boolean indicateCycleDetected(int vertex) {
        return false; // Default behaviour - Don't indicate cycle has been detected
    }

    protected void recordVertexFinish(int vertex) {
        displayMessage("Finished processing vertex " + vertex+".");
        getVertex(vertex).setFillColor(VISITED_AND_FINISHED);
        getVertex(vertex).setLabelColor(Color.WHITE);
        repaintwait();
    }

    protected void backTrackTo(int vertex) {
        displayMessage("Backtrack to vertex " + vertex+".");
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        repaintwait();
    }

    private void visitVertex(int vertex) {

        displayMessage("Visit vertex " + vertex+".");

        if (highlightCircle == null) {
            setHighlightCircleAtVertex(vertex);
            repaintwait();
        } else {
            AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
            repaintwait();
        }


        if (skipAnimation) {
            repaintwaitmin();
        } else {
            repaintwaitmin(10);
        }

        recordVertexVisit(vertex);
    }

    private void setHighlightCircleAtVertex(int vertex) {
        highlightCircle = createCircle("", vertexPosition(vertex).getX(), vertexPosition(vertex).getY());
        highlightCircle.setOutlineColor(Color.RED);
    }

    protected void showResults() {
        removeHighlightCircle();
        String dfsTraversalOrder = "";
        for (int i = 0 ; i < visitPath.size() ; i++) {
            dfsTraversalOrder = dfsTraversalOrder + visitPath.get(i);

            if (i != visitPath.size() - 1) {
                dfsTraversalOrder = dfsTraversalOrder + " , ";
            }

        }

        displayMessage("Finished traversal,"+" vertices visited in the order: "+dfsTraversalOrder);
    }

    protected void recordVertexVisit(int vertex) {
        visitPath.add(vertex);
        displayMessage("Vertex " + vertex + " has been visited.");
        getVertex(vertex).setFillColor(VISITED_AND_PROCESSING);
        repaintwait();
    }

    protected Vector2D vertexPosition(int vertex) {
        return directedGraph.vertexPosition(vertex);
    }

    protected boolean vertexNotVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == UNVISITED;
    }

    private boolean vertexBeingProcessed(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == VISITED_AND_PROCESSING;
    }

    private boolean vertexVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == VISITED_AND_FINISHED;
    }

    @Override
    public void restart() {
        setDefaultGraphColours();
    }

    @Override
    protected void setDefaultGraphColours() {
        displayMessage("Explaination Text");
        setDefaultVertexColor();
        setDefaultEdgeColor();
        removeHighlightCircle();
        repaint();
    }

    private void setDefaultVertexColor() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            getVertex(vertex).setFillColor(UNVISITED);
            getVertex(vertex).setLabelColor(Color.BLACK);
        }
    }

    private void setDefaultEdgeColor() {
        for (EdgePair pair : directedGraph.getEdgeSet()) {
            int fromVertex = pair.getFromValue();
            int toVertex = pair.getToValue();
            resetEdgeToDefaultColor(fromVertex, toVertex);
        }
    }

    protected GElementVertex getVertex(int vertex) {
        return directedGraph.getVertex(vertex);
    }

    private void abortEdgeTraversal(Integer fromVertex, Integer toVertex) {
        displayMessage("Vertex "+toVertex+ " already visited, abort visit.");
        getEdge(fromVertex, toVertex).setOutlineColor(DEFAULT_EDGE_COLOR);
        repaintwait();
    }

    private void resetEdgeToDefaultColor(Integer fromVertex, Integer toVertex ) {
        getEdge(fromVertex, toVertex).setOutlineColor(DEFAULT_EDGE_COLOR);
    }

    protected GLink getEdge(int fromVertex, int toVertex) {
        return directedGraph.getEdge(fromVertex, toVertex);
    }


}
