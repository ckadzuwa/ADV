package adsv.views;

import adsv.graphs.dg.EdgePair;
import adsv.graphs.dg.GElementDirectedGraph;
import adsv.graphs.dg.GElementVertex;
import adsv.panels.ADSVPanel;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;

import java.awt.*;

public class ADSVGenericDepthFirstSearchView extends ADSVDirectedGraphView {

    protected GElementDirectedGraph directedGraph;
    protected GElement highlightCircle;

    protected final Color unvisited = Color.WHITE;
    protected final Color visitedAndProcessing = Color.LIGHT_GRAY;
    protected final Color visitedAndFinished = Color.decode("#424242");

    protected final Color defaultEdgeColor = Color.BLACK;
    protected final Color considerEdgeColor = Color.decode("#FF6F00");//Constants.ANDROID_RED;
    protected final Color traversedEdgeColor = Color.BLUE;

    protected boolean[][] connectedVertices;

    public ADSVGenericDepthFirstSearchView(ADSVPanel panel) {
        super(panel);
    }

    public void callFunction() {
        dfs();
    }

    public void dfs() {
        runSetup();
        performDepthFirstSearch();
        showResults();
    }

    protected void runSetup() {
        directedGraph = getDirectedGraph();
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
        animateVertexVisit(vertex);
        int N = connectedVertices[vertex].length;
        for (int i = 0; i < N; i++) {
            //If i is a neighbour of the vertex
            if (connectedVertices[vertex][i]) {
                getEdge(vertex, i).setOutlineColor(considerEdgeColor);
                repaintwait();
                if (vertexNotVisited(i)) {
                    getEdge(vertex, i).setOutlineColor(traversedEdgeColor);
                    //If a cycle is detected further down in exploration - Halt operations
                    if (dfsFromVertex(i)) {
                        return true;
                    }
                    backTrack(vertex);
                } else if (vertexBeingProcessed(i)) {
                    if (handleVertexProcessed(i)) {
                        return true; // To indicate a cycle has been detected
                    } else {
                        getEdge(vertex, i).setOutlineColor(defaultEdgeColor);
                    }
                } else {
                    if (vertexVisited(i)) {
                        getEdge(vertex, i).setOutlineColor(defaultEdgeColor);
                    }
                }
            }
        }
        recordVertexFinish(vertex);
        return false;
    }

    protected boolean handleVertexProcessed(int vertex) {
        return false; // Default behaviour - Don't indicate cycle has been detected
    }


    protected void recordVertexFinish(int vertex) {
        getVertex(vertex).setFillColor(visitedAndFinished);
        repaintwait();
    }

    protected void backTrack(int vertex) {
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        repaintwait();
    }

    private void animateVertexVisit(int vertex) {
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
        getVertex(vertex).setFillColor(visitedAndProcessing);
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
        return directedGraph.getVertex(vertex).getFillColor() == unvisited;
    }

    private boolean vertexBeingProcessed(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == visitedAndProcessing;
    }

    private boolean vertexVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == visitedAndFinished;
    }

    @Override
    public void restart() {
        resetDirectedGraph();
    }

    private void resetDirectedGraph() {
        resetVertices();
        resetEdges();
        removeAny(highlightCircle);
        highlightCircle = null;
        repaint();
    }

    private void resetVertices() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            getVertex(vertex).setFillColor(unvisited);
        }
    }

    private void resetEdges() {
        for (EdgePair pair : directedGraph.getEdgeSet()) {
            int fromVertex = Integer.parseInt(pair.getFirstValue());
            int toVertex = Integer.parseInt(pair.getSecondValue());
            getEdge(fromVertex, toVertex).setOutlineColor(defaultEdgeColor);
        }
    }

}
