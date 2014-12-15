package adsv.panels;

import java.awt.BorderLayout;

import adsv.main.ADSVWindow;
import adsv.views.ADSVBubbleSortView;

public class ADSVBubbleSortPanel extends ADSVSortPanel {

	public ADSVBubbleSortPanel(ADSVWindow window) {
		super(window);
		this.add(view = sortView = new ADSVBubbleSortView(), BorderLayout.CENTER);
		setUpAnimationPanel(view);
	}
	
}
