package adsv.views;

import adsv.directedGraphModel.EdgePair;
import adsv.directedGraphModel.GElementVertex;
import adsv.panels.ADSVPanel;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;

import java.awt.*;
import java.util.HashMap;
import java.util.TreeSet;

public class ADSVDepthFirstSearchView extends ADSVDirectedGraphView {

    protected GElement highlightCircle;

    protected static final Color UNVISITED = Color.WHITE;
    protected static final Color VISITED_AND_PROCESSING = Color.LIGHT_GRAY;
    protected static final Color VISITED_AND_FINISHED = Color.decode("#424242");

    protected static final Color DEFAULT_EDGE_COLOR = Color.BLACK;
    protected static final Color CONSIDER_EDGE_COLOR = Color.decode("#FF6F00");//Constants.ANDROID_RED;
    protected static final Color TRAVERSED_EDGE_COLOR = Color.BLUE;

    protected HashMap<Integer, TreeSet<Integer>> connectedVertices;

    public ADSVDepthFirstSearchView(ADSVPanel panel) {
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
        initialiseHighlightCircle();
    }

    protected void performDepthFirstSearch() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexNotVisited(vertex)) {
                highlightCircle.setPosition(vertexPosition(vertex).x, vertexPosition(vertex).y);
                dfsFromVertex(vertex);
            }
        }
    }

    private void initialiseHighlightCircle() {
        highlightCircle = createCircle("", 0, 0);
        highlightCircle.setFillColor(null);
        highlightCircle.setOutlineColor(Color.RED);
    }

    protected boolean dfsFromVertex(int vertex) {
        visitVertex(vertex);
        // If vertex has neighbors
        if (connectedVertices.get(vertex) != null) {
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

                    resetEdgeToDefaultColor(vertex, neighbor);
                } else {

                    if (vertexVisited(vertex)) {
                        resetEdgeToDefaultColor(vertex, neighbor);
                    }
                }
            }
        }
        recordVertexFinish(vertex);
        return false; // No cycle reported on this DFS exploration
    }

    private void resetEdgeToDefaultColor(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(defaultEdgeColor);
    }

    private void setEdgeAsTraversed(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(traversedEdgeColor);
    }

    private void considerTraversingEdge(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(considerEdgeColor);
        repaintwait();
    }

    protected boolean indicateCycleDetected(int vertex) {
        return false; // Default behaviour - Don't indicate cycle has been detected
    }


    protected void recordVertexFinish(int vertex) {
        getVertex(vertex).setFillColor(VISITED_AND_FINISHED);
        getVertex(vertex).setLabelColor(Color.WHITE);
        repaintwait();
    }

    protected void backTrackTo(int vertex) {
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        repaintwait();
    }

    private void visitVertex(int vertex) {
        // highlightCircle.setPosition(vertexPosition(vertex).x,vertexPosition(vertex).y);
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        if (skipAnimation) {
            repaintwait();
        } else {
            repaintwait(10);
        }

        recordVertexVisit(vertex);
    }

    protected void showResults() {
        removeAny(highlightCircle);
    }


    protected void recordVertexVisit(int vertex) {
        getVertex(vertex).setFillColor(VISITED_AND_PROCESSING);
        repaintwait();
    }

    protected GLink getEdge(int fromVertex, int toVertex) {
        return directedGraph.getEdge(fromVertex, toVertex);
    }

    protected GElementVertex getVertex(int vertex) {
        return directedGraph.getVertex(vertex);
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
        setDefaultVertexColor();
        setDefaultEdgeColor();
        removeAny(highlightCircle);
        highlightCircle = null;
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

}
