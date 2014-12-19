package adsv.panels;

import adsv.main.ADSVWindow;
import adsv.views.ADSVGenericDepthFirstSearchView;

import java.awt.*;

public class ADSVDepthFirstSearchPanel extends ADSVDirectedGraphPanel {

	public ADSVDepthFirstSearchPanel(ADSVWindow window) {
		super(window);
		this.add(view = graphView = new ADSVGenericDepthFirstSearchView(this), BorderLayout.CENTER);
		graphView.setDesignToolsPanel(this.designToolFA);
		setUpAnimationPanel(view);
	}

}
