package adv.views;

import adv.directedGraphModel.Edge;
import adv.directedGraphModel.GElementVertex;
import adv.panels.Panel;
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
    protected static final Color CONSIDER_EDGE_COLOR = Color.decode("#FF6F00"); //AMBER
    protected static final Color TRAVERSED_EDGE_COLOR = Color.BLUE;
    protected GElement highlightCircle;
    protected HashMap<Integer, TreeSet<Integer>> connectedVertices;
    protected ArrayList<Integer> visitPath;

    public DepthFirstSearchView(Panel panel) {
        super(panel);
    }

    public void runAlgorithm() {
        performDepthFirstSearch();
    }

    public void performDepthFirstSearch() {
        lockCanvas();
        runSetup();
        dfs();
        showResults();
        unlockCanvas();
    }

    protected void lockCanvas() {
        canvasLocked = true;
    }

    protected void unlockCanvas() {
        canvasLocked = false;
    }

    protected void runSetup() {
        connectedVertices = directedGraph.adjacencyList();
        visitPath = new ArrayList<Integer>();
    }

    protected void dfs() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexUnvisited(vertex)) {
                dfsFromVertex(vertex);
            }
            removeHighlightCircle();
        }
    }

    protected void dfsFromVertex(int vertex) {
        visitVertex(vertex);
        TreeSet<Integer> vertexNeighbours = connectedVertices.get(vertex);

        if (vertexNeighbours != null) {

            displayMessage("Process vertex " + vertex + "'s neighbours.");
            repaintwait();

            for (Integer neighbor : vertexNeighbours) {

                considerTraversingEdge(vertex, neighbor);

                if (vertexUnvisited(neighbor)) {
                    setEdgeAsTraversed(vertex, neighbor);
                    dfsFromVertex(neighbor);
                    backTrackTo(vertex);
                } else {
                    abortEdgeTraversal(vertex, neighbor);
                }

            }
        } else {
            displayMessage("Vertex " + vertex + " has no neighbours to be processed.");
            repaintwait();
        }

        recordVertexFinish(vertex);
    }

    protected void setEdgeAsTraversed(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(TRAVERSED_EDGE_COLOR);
    }

    protected void considerTraversingEdge(Integer fromVertex, Integer toVertex) {
        displayMessage("Consider traversing edge from vertex " + fromVertex + " to vertex " + toVertex + ".");
        getEdge(fromVertex, toVertex).setOutlineColor(CONSIDER_EDGE_COLOR);
        repaintwait();
    }

    protected void recordVertexFinish(int vertex) {
        displayMessage("Finished processing vertex " + vertex + ".");
        getVertex(vertex).setFillColor(VISITED_AND_FINISHED);
        getVertex(vertex).setLabelColor(Color.WHITE);
        repaintwait();
    }

    protected void backTrackTo(int vertex) {
        displayMessage("Backtrack to vertex " + vertex + ".");
        AnimatePath(highlightCircle, highlightCircle.getPosition(), vertexPosition(vertex), 40);
        repaintwait();
    }

    protected void visitVertex(int vertex) {

        displayMessage("Visit vertex " + vertex + ".");

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
        for (int i = 0; i < visitPath.size(); i++) {
            dfsTraversalOrder = dfsTraversalOrder + visitPath.get(i);

            if (i != visitPath.size() - 1) {
                dfsTraversalOrder = dfsTraversalOrder + " , ";
            }

        }

        displayMessage("Finished traversal," + " vertices visited in the order: " + dfsTraversalOrder);
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

    protected boolean vertexUnvisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == UNVISITED;
    }

    protected boolean vertexBeingProcessed(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == VISITED_AND_PROCESSING;
    }

    protected boolean vertexVisited(int vertex) {
        return directedGraph.getVertex(vertex).getFillColor() == VISITED_AND_FINISHED;
    }

    @Override
    public void restart() {
        setDefaultGraphColours();
    }

    @Override
    protected void setDefaultGraphColours() {
        displayMessage("Message Text");
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
        for (Edge edge : directedGraph.getEdgeSet()) {
            int fromVertex = edge.getFromVertex();
            int toVertex = edge.getToVertex();
            resetEdgeToDefaultColor(fromVertex, toVertex);
        }
    }

    protected void removeHighlightCircle() {
        removeAny(highlightCircle);
        highlightCircle = null;
    }

    protected GElementVertex getVertex(int vertex) {
        return directedGraph.getVertex(vertex);
    }

    private void resetEdgeToDefaultColor(Integer fromVertex, Integer toVertex) {
        getEdge(fromVertex, toVertex).setOutlineColor(DEFAULT_EDGE_COLOR);
    }

    protected GLink getEdge(int fromVertex, int toVertex) {
        return directedGraph.getEdge(fromVertex, toVertex);
    }

    protected void abortEdgeTraversal(Integer fromVertex, Integer toVertex) {
        displayMessage("Vertex " + toVertex + " already visited, abort visit.");
        getEdge(fromVertex, toVertex).setOutlineColor(DEFAULT_EDGE_COLOR);
        repaintwait();
    }


}
