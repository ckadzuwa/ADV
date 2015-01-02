package adsv.views;

import adsv.panels.ADSVPanel;

import java.awt.*;
import java.util.ArrayList;

public class ADSVTopologicalSortView extends ADSVDepthFirstSearchView {

    private String topologicalSortOrder;
    private ArrayList<Integer> visitPath;
    private Color cycleHighlight = Color.RED;
    private boolean cycleDetected;
    private int firstCycleElement;

    public ADSVTopologicalSortView(ADSVPanel panel) {
        super(panel);
    }

    @Override
    protected void runSetup() {
        super.runSetup();
        topologicalSortOrder = "";
        cycleDetected = false;
        visitPath = new ArrayList<Integer>();
    }


    protected void performDepthFirstSearch() {
        for (Integer vertex : directedGraph.getVertexSet()) {
            if (vertexNotVisited(vertex)) {
                highlightCircle.setPosition(vertexPosition(vertex).x, vertexPosition(vertex).y);
                if (dfsFromVertex(vertex)) {
                    cycleDetected = true;
                    break;
                }
            }
        }
    }

    @Override
    protected void backTrack(int vertex) {
        super.backTrack(vertex);
    }

    @Override
    protected void recordVertexVisit(int vertex) {
        super.recordVertexVisit(vertex);
        visitPath.add(vertex);
    }

    @Override
    protected void recordVertexFinish(int vertex) {
        super.recordVertexFinish(vertex);
        topologicalSortOrder = vertex + " < " + topologicalSortOrder;
        visitPath.remove(new Integer(vertex));
    }

    @Override
    protected void showResults() {
        super.showResults();
        System.out.println(topologicalSortOrder);
        if (cycleDetected) {
            int firstCycleElementIndex = firstCycleElementIndex();
            for (int i = firstCycleElementIndex; i < visitPath.size() - 1; i++) {
                int fromVertex = visitPath.get(i);
                int toVertex = visitPath.get(i + 1);
                getEdge(fromVertex, toVertex).setOutlineColor(cycleHighlight);
            }
            repaint();
        }
    }

    private int firstCycleElementIndex() {
        for (int i = 0; i < visitPath.size(); i++) {
            if (visitPath.get(i) == firstCycleElement) {
                return i;
            }
        }
        return -1;
    }

    protected boolean handleVertexProcessed(int vertex) {
        topologicalSortOrder = topologicalSortOrder + " - Graph has a cycle. No topological sort exists!";
        visitPath.add(vertex);
        firstCycleElement = vertex;
        return true;
    }

}
