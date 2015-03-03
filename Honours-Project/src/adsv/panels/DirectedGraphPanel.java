package adsv.panels;

import adsv.main.Window;
import adsv.views.DirectedGraphView;

import edu.usfca.vas.window.tools.DesignToolsDG;

import java.awt.*;

public class DirectedGraphPanel extends GenericPanel {

	protected DirectedGraphView graphView;
	protected DesignToolsDG designToolFA;

	public DirectedGraphPanel(Window window) {
		super(window);
		this.add(designToolFA = new DesignToolsDG(this.window), BorderLayout.NORTH);
	}

}
