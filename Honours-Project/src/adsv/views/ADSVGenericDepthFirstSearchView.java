package adsv.views;

import adsv.globals.Constants;
import adsv.graphs.dg.EdgePair;
import adsv.graphs.dg.GElementDirectedGraph;
import adsv.graphs.dg.GElementVertex;
import adsv.panels.ADSVPanel;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;

import java.awt.Color;

public class ADSVGenericDepthFirstSearchView extends ADSVDirectedGraphView {

    protected GElementDirectedGraph directedGraph;
    protected GElement highlightCircle;

    protected Color unvisited = Color.WHITE;
    protected Color processing = Color.LIGHT_GRAY;
    protected Color visited = Color.decode("#424242");


    protected Color defaultEdgeColor = Color.BLACK;
    protected Color considerEdgeColor = Constants.ANDROID_RED;
    protected Color traversedEdgeColor = Color.BLUE;

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


    private void performDepthFirstSearch() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexNotVisited(vertex)) {
                highlightCircle.setPosition(vertexPosition(vertex).x, vertexPosition(vertex).y);
                if (dfsFromVertex(vertex)) {
                    break;
                }
            }
        }
    }

    private void initialiseHighlightCircle() {
        highlightCircle = createCircle("", 0, 0);
        highlightCircle.setFillColor(null);
        highlightCircle.setOutlineColor(Constants.ANDROID_RED);
    }

    private boolean dfsFromVertex(int vertex) {
        animateVertexVisit(vertex);
        int N = connectedVertices[vertex].length;
        for (int i = 0; i < N; i++) {
            //If i is a neighbour of the vertex
            if (connectedVertices[vertex][i]) {
                getEdge(vertex, i).setOutlineColor(considerEdgeColor);
                repaintwait();
                if (vertexNotVisited(i)) {
                    getEdge(vertex, i).setOutlineColor(traversedEdgeColor);
                    //If a cycle is detected - Halt operations
                    if (dfsFromVertex(i)) {
                        return true;
                    }
                    backTrack(vertex);
                } else if (vertexBeingProcessed(i)) {
                    handleVertexProcessed();
                    return true;
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

    protected void handleVertexProcessed() {
    }


    protected void recordVertexFinish(int vertex) {
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
    }


    protected void recordVertexVisit(int vertex) {
        getVertex(vertex).setFillColor(visited);
        repaintwait();
    }

    private GLink getEdge(int fromVertex, int toVertex) {
        return directedGraph.getEdge(fromVertex, toVertex);
    }

    protected GElementVertex getVertex(int vertex) {
        return directedGraph.getVertex(vertex);
    }

    private Vector2D vertexPosition(int vertex) {
        return directedGraph.vertexPosition(vertex);
    }

    private boolean vertexNotVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == unvisited;
    }

    private boolean vertexBeingProcessed(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == processing;
    }

    private boolean vertexVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == visited;
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
