package adv.views;

import adv.panels.Panel;

import java.awt.*;
import java.util.LinkedList;

public class TopologicalSortView extends DepthFirstSearchView {

    private LinkedList<Integer> topologicalOrder;
    private Color CYCLE_HIGHLIGHT_COLOR = Color.magenta;
    private boolean cycleDetected;
    private int firstVertexInCycle;

    public TopologicalSortView(Panel panel) {
        super(panel);
    }

    @Override
    protected void runSetup() {
        super.runSetup();
        topologicalOrder = new LinkedList<Integer>();
        cycleDetected = false;
    }

    protected void performDepthFirstSearch() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexNotVisited(vertex)) {
                if (dfsFromVertex(vertex)) {
                    break;
                }
                removeHighlightCircle();
            }
        }
    }

    @Override
    protected void backTrackTo(int vertex) {
        super.backTrackTo(vertex);
    }

    @Override
    protected void recordVertexFinish(int vertex) {
        super.recordVertexFinish(vertex);
        topologicalOrder.addFirst(vertex);
        visitPath.remove(new Integer(vertex));
    }

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

    private void buildAndDisplayOrdering() {
        String ordering = "";
        int numElementsInOrder = topologicalOrder.size();
        for (int i = 0 ; i < numElementsInOrder ; i++) {

            if (i == numElementsInOrder - 1) {
                ordering = ordering + topologicalOrder.get(i);
            } else {
                ordering = ordering + topologicalOrder.get(i) + " < ";
            }
        }

        displayMessage("Graph has topological order: " + ordering+".");
    }

    private int firstVertexInCycleIndex() {
        for (int i = 0; i < visitPath.size(); i++) {
            if (visitPath.get(i) == firstVertexInCycle) {
                return i;
            }
        }
        return -1;
    }

    protected boolean indicateCycleDetected(int vertex) {
        cycleDetected = true;
        visitPath.add(vertex);
        firstVertexInCycle = vertex;
        return true;
    }

}
