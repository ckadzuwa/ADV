package adsv.views;

import adsv.graphs.dg.GElementDirectedGraph;
import adsv.panels.ADSVPanel;

public class ADSVDepthFirstSearchView extends ADSVDirectedGraphView {

	protected GElementDirectedGraph directedGraph;

	public ADSVDepthFirstSearchView(ADSVPanel panel) {
		super(panel);
		directedGraph = getDirectedGraph();
	}

	public void callFunction() {
		dfs();
	}

	public void dfs() {

	}
}
