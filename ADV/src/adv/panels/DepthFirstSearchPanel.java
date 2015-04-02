package adv.panels;

import adv.main.Window;
import adv.views.DepthFirstSearchView;

import java.awt.*;

public class DepthFirstSearchPanel extends DirectedGraphPanel {

	public DepthFirstSearchPanel(Window window) {
		super(window);
		this.add(view = graphView = new DepthFirstSearchView(this), BorderLayout.CENTER);
		graphView.setDesignToolsPanel(this.designToolFA);
		setUpAnimationPanel(view);
	}

}
