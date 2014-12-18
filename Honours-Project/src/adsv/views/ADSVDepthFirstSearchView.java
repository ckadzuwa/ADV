package adsv.views;

import adsv.globals.Constants;
import adsv.graphs.dg.GElementDirectedGraph;
import adsv.panels.ADSVPanel;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;

import java.awt.Color;

public class ADSVDepthFirstSearchView extends ADSVDirectedGraphView {

    protected GElementDirectedGraph directedGraph;
    protected GElement highlightCircle;

    protected Color unvisited = Color.WHITE;
    protected Color visited = Color.BLACK;

    protected boolean[][] connectedVertices;


    public ADSVDepthFirstSearchView(ADSVPanel panel) {
        super(panel);
        directedGraph = getDirectedGraph();
    }

    public void callFunction() {
        dfs();
    }

    public void dfs() {
        runSetup();
        performDepthFirstSearch();
    }

    public void runSetup() {
        directedGraph = getDirectedGraph();
        connectedVertices = directedGraph.getConnectedMatrix();
    }

    private void performDepthFirstSearch() {
        highlightFirstVertex();

        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexNotVisited(vertex)) {
                dfsFromVertexFromVertex(vertex);
            }
        }
    }

    private void highlightFirstVertex() {
        int firstVertex = directedGraph.getFirstVertex();
        Vector2D firstVertexPosition = vertexPosition(firstVertex);
        highlightCircle = createCircle("", firstVertexPosition.x, firstVertexPosition.y);
        highlightCircle.setFillColor(null);
        highlightCircle.setOutlineColor(Constants.ANDROID_RED);
    }

    private void dfsFromVertexFromVertex(int vertexValue) {
        visitVertex(vertexValue);
        int N = connectedVertices[vertexValue].length;
        //If i is a neighbour of the vertex
        for (int i = 0; i < N; i++) {
            if (connectedVertices[vertexValue][i] == true) {
                if (vertexNotVisited(i)) {
                    dfsFromVertexFromVertex(i);
                    backTrack(vertexValue);
                }
            }
        }

    }

    private void backTrack(int vertexValue) {
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertexValue), 20);
        repaintwait();
    }

    private void visitVertex(int vertexValue) {
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertexValue), 20);
        directedGraph.getVertex(vertexValue).setFillColor(visited);
        repaintwait();
    }

    private Vector2D vertexPosition(int vertexValue) {
        return directedGraph.vertexPosition(vertexValue);
    }

    private boolean vertexNotVisited(int vertexValue) {
        return directedGraph.getVertex(vertexValue).getFillColor() == unvisited;
    }

}
