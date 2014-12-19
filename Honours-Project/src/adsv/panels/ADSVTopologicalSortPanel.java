package adsv.panels;

import adsv.main.ADSVWindow;
import adsv.views.ADSVTopologicalSortView;

import java.awt.*;

public class ADSVTopologicalSortPanel extends ADSVDirectedGraphPanel {

    public ADSVTopologicalSortPanel(ADSVWindow window) {
        super(window);
        this.add(view = graphView = new ADSVTopologicalSortView(this), BorderLayout.CENTER);
        graphView.setDesignToolsPanel(this.designToolFA);
        setUpAnimationPanel(view);
    }

}
