package adv.panels;

import java.awt.BorderLayout;

import adv.main.Window;
import adv.views.BubbleSortView;

public class BubbleSortPanel extends SortPanel {

	protected final static int SLOW_VALUE = 75;
	protected final static int MEDIUM_VALUE = 50;
	protected final static int FAST_VALUE = 15;
	protected final static int VERY_FAST_VALUE = 5;

	public BubbleSortPanel(Window window) {
		super(window);
		this.add(view = sortView = new BubbleSortView(), BorderLayout.CENTER);
		setUpAnimationPanel(view);
	}

	protected void setAnimationSpeeds() {
		slowValue = 200;
		mediumValue = 150;
		fastValue = 75;
		veryFastValue = 15;
	}
	
}
