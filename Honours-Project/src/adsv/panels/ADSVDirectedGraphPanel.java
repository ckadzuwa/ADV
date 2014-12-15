package adsv.panels;

import adsv.main.ADSVWindow;
import adsv.views.ADSVDirectedGraphView;

import edu.usfca.vas.window.tools.DesignToolsDG;

import java.awt.*;

public class ADSVDirectedGraphPanel extends ADSVPanel {

	protected ADSVDirectedGraphView graphView;
	protected DesignToolsDG designToolFA;

	public ADSVDirectedGraphPanel(ADSVWindow window) {
		super(window);
		this.add(designToolFA = new DesignToolsDG(this.window), BorderLayout.NORTH);
	}

}
