package adv.views;

import adv.panels.Panel;

import java.awt.*;
import java.util.LinkedList;
import java.util.TreeSet;

public class TopologicalSortView extends DepthFirstSearchView {

    private LinkedList<Integer> topologicalOrder;
    private Color CYCLE_HIGHLIGHT_COLOR = Color.magenta;
    private boolean cycleDetected;
    private int firstVertexInCycle;

    public TopologicalSortView(Panel panel) {
        super(panel);
    }

    public void runAlgorithm() {
        performTopologicalSort();
    }

    public void performTopologicalSort() {
        lockCanvas();
        runSetup();
        topSort();
        showResults();
        unlockCanvas();
    }

    @Override
    protected void runSetup() {
        super.runSetup();
        topologicalOrder = new LinkedList<Integer>();
        cycleDetected = false;
    }

    // Topological sort algorithm discussed within INF2B
    protected void topSort() {
        int N = directedGraph.getNumberVertices();

        for (int i = 0; i < N; i++) {
            if (vertexUnvisited(i)) {
                if (sortFromVertex(i)) {
                    break;
                }
                removeHighlightCircle();
            }
        }
    }

    // Recursive DFS used by topological sort
    private boolean sortFromVertex(int vertex) {
        visitVertex(vertex);
        TreeSet<Integer> vertexNeighbours = connectedVertices.get(vertex);

        if (vertexNeighbours != null) {

            displayMessage("Process vertex " + vertex + "'s neighbours.");
            repaintwait();

            for (Integer neighbor : vertexNeighbours) {

                considerTraversingEdge(vertex, neighbor);

                if (vertexUnvisited(neighbor)) {
                    setEdgeAsTraversed(vertex, neighbor);

                    if (sortFromVertex(neighbor)) {
                        return true; //If a cycle is detected further down in DFS - Halt exploration
                    }

                    backTrackTo(vertex);
                } else if (vertexBeingProcessed(neighbor)) {
                    recordCycleDetected(neighbor);
                    return true; // Indicate a cycle has been detected
                } else {
                    abortEdgeTraversal(vertex, neighbor);
                }
            }
        } else {
            displayMessage("Vertex " + vertex + " has no neighbours to be processed.");
            repaintwait();
        }

        recordVertexFinish(vertex);
        return false; // No cycle reported on this DFS exploration
    }

    @Override
    protected void backTrackTo(int vertex) {
        super.backTrackTo(vertex);
    }


    // Record that a vertex has turned black
    @Override
    protected void recordVertexFinish(int vertex) {
        super.recordVertexFinish(vertex);
        topologicalOrder.addFirst(vertex);
        visitPath.remove(new Integer(vertex));
    }

    // Show topological order if one exists or display that
    // a cycle exists and highlight the edges that form a
    // a cycle
    @Override
    protected void showResults() {
        removeHighlightCircle();

        if (cycleDetected) {
            int firstVertexInCycleIndex = firstVertexInCycleIndex();

            for (int i = firstVertexInCycleIndex; i < visitPath.size() - 1; i++) {
                int fromVertex = visitPath.get(i);
                int toVertex = visitPath.get(i + 1);
                getEdge(fromVertex, toVertex).setOutlineColor(CYCLE_HIGHLIGHT_COLOR);
            }

            repaint();
            displayMessage("No topological order exists as this graph has a cycle.");
        } else {
            buildAndDisplayOrdering();
        }

    }

    // Display topological order
    private void buildAndDisplayOrdering() {
        String ordering = "";
        int numElementsInOrder = topologicalOrder.size();
        for (int i = 0; i < numElementsInOrder; i++) {

            if (i == numElementsInOrder - 1) {
                ordering = ordering + topologicalOrder.get(i);
            } else {
                ordering = ordering + topologicalOrder.get(i) + " < ";
            }
        }

        displayMessage("Graph has topological order: " + ordering + ".");
    }

    private int firstVertexInCycleIndex() {
        for (int i = 0; i < visitPath.size(); i++) {
            if (visitPath.get(i) == firstVertexInCycle) {
                return i;
            }
        }
        return -1;
    }

    private void recordCycleDetected(int vertex) {
        cycleDetected = true;
        visitPath.add(vertex);
        firstVertexInCycle = vertex;
    }

}
