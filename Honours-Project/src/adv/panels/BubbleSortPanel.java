package adv.panels;

import java.awt.BorderLayout;

import adv.main.Window;
import adv.views.BubbleSortView;

public class BubbleSortPanel extends SortPanel {

	public BubbleSortPanel(Window window) {
		super(window);
		this.add(view = sortView = new BubbleSortView(), BorderLayout.CENTER);
		setUpAnimationPanel(view);
	}
	
}
