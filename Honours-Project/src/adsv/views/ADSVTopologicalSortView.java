package adsv.views;

import adsv.panels.ADSVPanel;

public class ADSVTopologicalSortView extends ADSVGenericDepthFirstSearchView {

    private String topologicalSortOrder = "";

    public ADSVTopologicalSortView(ADSVPanel panel) {
        super(panel);
    }

    @Override
    protected void runSetup() {
        super.runSetup();
        topologicalSortOrder = "";
    }
    @Override
    protected void recordVertexVisit(int vertex){
        getVertex(vertex).setFillColor(processing);
        repaintwait();
    }

    @Override
    protected  void backTrack(int vertex) {
        super.backTrack(vertex);
    }

    @Override
    protected void recordVertexFinish(int vertex) {
        getVertex(vertex).setFillColor(visited);
        repaintwait();
        topologicalSortOrder = vertex + "," + topologicalSortOrder;
    }

    @Override
    protected void showResults() {
        System.out.println(topologicalSortOrder);
    }

    protected void handleVertexProcessed() {
        topologicalSortOrder = "Graph has a cycle. No topological sort exists!";
    }

}
