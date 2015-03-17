package adv.panels;

import adv.main.Window;
import adv.sortingInputHandler.SortInputDialog;
import adv.views.SortView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class SortPanel extends Panel {

	protected boolean showBars;
	protected boolean showIndices;
	protected SortView sortView;
	protected Window window;
	protected JButton hideBars;
	protected JButton hideIndices;
	protected JButton startAnimation;
	protected JButton newInput;
	protected SortInputDialog dialog;

	public SortPanel(Window window) {
		super(window);
		// this.window = window; unneccssary line!!
		Box box = Box.createHorizontalBox();

		showBars = true;
		showIndices = true;

		hideBars = new JButton("Hide Bars");
		hideBars.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				showBars = !showBars;
				sortView.toggleShowBars();

				if (showBars) {
					hideBars.setText("Hide Bars");
				} else {
					hideBars.setText("View Bars");
				}

				changeDone();
			}
		});
		box.add(hideBars);

		hideIndices = new JButton("Hide Indices");
		hideIndices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				showIndices = !showIndices;
				sortView.toggleShowIndices();
				if (showIndices) {
					hideIndices.setText("Hide Indices");
				} else {
					hideIndices.setText("View Indices");
				}
				changeDone();
			}
		});
		box.add(hideIndices);

		newInput = new JButton("New Input");
		newInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				displayInputDialog();
			}

		});
		box.add(newInput);

		this.add(box, BorderLayout.NORTH);
		dialog = new SortInputDialog(this);

	}

	private void displayInputDialog() {
		dialog.setVisible(true);
		// JOptionPane.showMessageDialog(window.getJFrame(), "Set Input");

	}

	public void disableSpecificButtons() {
		hideBars.setEnabled(false);
		hideIndices.setEnabled(false);
		newInput.setEnabled(false);
	}

	public void enableSpecificButtons() {
		hideBars.setEnabled(true);
		hideIndices.setEnabled(true);
		newInput.setEnabled(true);
	}

	// Persistence
	public void setData(Object data) {
		sortView.setData(data);
	}

	public Object getData() {
		return sortView.getData();
	}

	public SortView getSortView() {
		return sortView;
	}

}
