package adsv.panels;

import java.awt.BorderLayout;

import adsv.main.Window;
import adsv.views.BubbleSortView;

public class BubbleSortPanel extends SortPanel {

	public BubbleSortPanel(Window window) {
		super(window);
		this.add(view = sortView = new BubbleSortView(), BorderLayout.CENTER);
		setUpAnimationPanel(view);
	}
	
}
