package adsv.panels;

import adsv.main.Window;
import adsv.views.DirectedGraphView;

import edu.usfca.vas.window.tools.DesignToolsDG;

import java.awt.*;

public abstract class DirectedGraphPanel extends Panel {

	protected DirectedGraphView graphView;
	protected DesignToolsDG designToolFA;

	public DirectedGraphPanel(Window window) {
		super(window);
		this.add(designToolFA = new DesignToolsDG(this.window), BorderLayout.NORTH);
	}

}
