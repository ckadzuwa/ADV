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
    protected Color visited = Color.decode("#424242");

    protected boolean[][] connectedVertices;


    public ADSVDepthFirstSearchView(ADSVPanel panel) {
        super(panel);
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
        initialiseHighlightCircle();
    }



    private void performDepthFirstSearch() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexNotVisited(vertex)) {
                highlightCircle.setPosition(vertexPosition(vertex).x, vertexPosition(vertex).y);
                dfsFromVertexFromVertex(vertex);
            }
        }
    }

    private void initialiseHighlightCircle() {
        highlightCircle = createCircle("", 0, 0);
        highlightCircle.setFillColor(null);
        highlightCircle.setOutlineColor(Constants.ANDROID_RED);
    }

    private void dfsFromVertexFromVertex(int vertex) {
        visitVertex(vertex);
        int N = connectedVertices[vertex].length;
        for (int i = 0; i < N; i++) {
            //If i is a neighbour of the vertex
            if (connectedVertices[vertex][i] == true) {
                if (vertexNotVisited(i)) {
                    dfsFromVertexFromVertex(i);
                    backTrack(vertex);
                }
            }
        }


    }

    private void backTrack(int vertex) {
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        repaintwait();
    }

    private void visitVertex(int vertex) {
        // highlightCircle.setPosition(vertexPosition(vertex).x,vertexPosition(vertex).y);
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        repaintwait(10);
        directedGraph.getVertex(vertex).setFillColor(visited);
        repaintwait();
    }

    private Vector2D vertexPosition(int vertex) {
        return directedGraph.vertexPosition(vertex);
    }

    private boolean vertexNotVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == unvisited;
    }

    @Override
    public void restart() {
       resetDirectedGraph();
        dfs();
    }

    private void resetDirectedGraph() {
        // TODO
    }

}
