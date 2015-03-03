package adsv.panels;

import adsv.main.Window;
import adsv.views.TopologicalSortView;

import java.awt.*;

public class TopologicalSortPanel extends DirectedGraphPanel {

    public TopologicalSortPanel(Window window) {
        super(window);
        this.add(view = graphView = new TopologicalSortView(this), BorderLayout.CENTER);
        graphView.setDesignToolsPanel(this.designToolFA);
        setUpAnimationPanel(view);
    }

}
