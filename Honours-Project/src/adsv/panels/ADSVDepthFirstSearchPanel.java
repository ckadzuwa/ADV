package adsv.panels;

import adsv.main.ADSVWindow;
import adsv.views.ADSVDepthFirstSearchView;

import java.awt.*;

public class ADSVDepthFirstSearchPanel extends ADSVDirectedGraphPanel {

	public ADSVDepthFirstSearchPanel(ADSVWindow window) {
		super(window);
		this.add(view = graphView = new ADSVDepthFirstSearchView(this), BorderLayout.CENTER);
		graphView.setDesignToolsPanel(this.designToolFA);
		setUpAnimationPanel(view);
	}

}
