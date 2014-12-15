package adsv.panels;

import java.awt.BorderLayout;

import adsv.main.ADSVWindow;
import adsv.views.ADSVDepthFirstSearchView;

public class ADSVDepthFirstSearchPanel extends ADSVDirectedGraphPanel {

	public ADSVDepthFirstSearchPanel(ADSVWindow window) {
		super(window);
		this.add(view = graphView = new ADSVDepthFirstSearchView(this.window), BorderLayout.CENTER);
		graphView.setDesignToolsPanel(this.designToolFA);
		setUpAnimationPanel(view);
	}

}
