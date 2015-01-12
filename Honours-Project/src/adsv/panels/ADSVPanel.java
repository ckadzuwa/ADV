package adsv.panels;

import adsv.main.ADSVWindow;
import adsv.views.DSView;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class ADSVPanel extends JPanel {

	protected ADSVWindow window;
	protected JButton restartButton;
	protected JButton stepButton;
	protected JButton goButton;
	protected JButton skipButton;
	protected JComboBox<String> speed;
	protected JLabel runningMsg;
	protected boolean paused;
	protected boolean running;
	protected DSView view;
	protected Vector DirtyDisplay;

	private final static int SLOW_INDEX = 0;
	private final static int MEDIUM_INDEX = 1;
	private final static int FAST_INDEX = 2;
	private final static int VERY_FAST_INDEX = 3;

	private final static int SLOW_VALUE = 75;
	private final static int MEDIUM_VALUE = 50;
	private final static int FAST_VALUE = 15;
	private final static int VERY_FAST_VALUE = 5;

	public ADSVPanel(ADSVWindow window) {
		super(new BorderLayout());
		DirtyDisplay = new Vector();
		this.window = window;
	}

	protected void animate(final int Function, final Object param1, final Object param2) {

		Thread v = new Thread() {
			public void run() {
				startAnimation();
				view.Animate(Function, param1, param2);
				endAnimation();
				repaint();
			}
		};
		v.start();
	}

	protected void beginAnimation(final int Function, final Object param) {

		Thread v = new Thread() {
			public void run() {
				startAnimation();
				view.Animate(Function, param);
				endAnimation();
				repaint();
			}
		};
		v.start();
	}

	protected void beginAnimation() {

		Thread v = new Thread() {
			public void run() {
				startAnimation();
				view.Animate();
				endAnimation();
				repaint();
			}
		};
		v.start();
	}

	protected void setUpAnimationPanel(final DSView view) {
		Box bottomPanel = Box.createHorizontalBox();

		Box buttonsAndLabel = Box.createVerticalBox();
		Box buttonsContainer = Box.createHorizontalBox();

		runningMsg = new JLabel("Animation Completed");

		setUpGoButton(buttonsContainer);
		setUpStepButton(buttonsContainer);
		setUpSkipButton(buttonsContainer);
		setUpRestartButton(buttonsContainer);

		buttonsAndLabel.add(runningMsg);
		buttonsAndLabel.add(buttonsContainer);
		bottomPanel.add(buttonsAndLabel);

		String[] speedStrings = { "Slow", "Medium", "Fast", "Very Fast" };
		JComboBox<String> speed = new JComboBox<String>(speedStrings);
		speed.setMaximumSize(new Dimension(100, 50));
		speed.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<String> speed = (JComboBox<String>) event.getSource();
				switch (speed.getSelectedIndex()) {
				case SLOW_INDEX:
					view.setDelay(SLOW_VALUE);
					break;
				case MEDIUM_INDEX:
					view.setDelay(MEDIUM_VALUE);
					break;
				case FAST_INDEX:
					view.setDelay(FAST_VALUE);
					break;
				case VERY_FAST_INDEX:
					view.setDelay(VERY_FAST_VALUE);
					break;
				}
			}
		}

		);
		speed.setSelectedItem(speedStrings[MEDIUM_INDEX]);
		bottomPanel.add(speed);

		this.add(bottomPanel, BorderLayout.SOUTH);
		disableGoAndSkip();

	}

	protected void setUpGoButton(Box buttonsContainer) {
		goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				if (!running) {
					beginAnimation();
					goButton.setText("Pause");
				} else {

					if (!paused) {
						paused = true;
						view.pause();
						goButton.setText("Go");
						setUpAnimationPaused();
					} else {
						paused = false;
						view.go();
						goButton.setText("Pause");
						setUpAnimationRunning();
					}
				}
			}
		});
		buttonsContainer.add(goButton);
	}

	private void setUpSkipButton(Box buttonsContainer) {
		skipButton = new JButton("Skip");
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!running) {
					beginAnimation();
					view.skip();
				} else {
					if (!paused) {
						view.skip();
					} else {
						view.skip();
						paused = false;
						view.go();
					}

				}
				changeDone();
			}
		});
		buttonsContainer.add(skipButton);
	}

	private void setUpStepButton(Box buttonsContainer) {
		stepButton = new JButton("Step");
		stepButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				view.step();
				changeDone();
			}
		});
		stepButton.setEnabled(false);
		buttonsContainer.add(stepButton);
	}

	private void setUpRestartButton(Box buttonsContainer) {
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				view.go();
				paused = false;
				view.restart();
				restartButton.setEnabled(false);
				beginAnimation();
				goButton.setEnabled(true);
				goButton.setText("Pause");
				changeDone();
			}
		});
		restartButton.setEnabled(false);
		buttonsContainer.add(restartButton);
	}

	public void disableSpecificButtons() {
		/* Override this method to disable specific buttons */
	}

	public void startAnimation() {

		running = true;
		if (paused) {
			setUpAnimationPaused();
		} else {
			setUpAnimationRunning();
		}
		skipButton.setEnabled(true);
		disableSpecificButtons();
	}

	protected void setUpAnimationPaused() {
		runningMsg.setText("Animation Paused");
		runningMsg.setForeground(Color.RED);
		stepButton.setEnabled(true);
	}

	protected void setUpAnimationRunning() {
		runningMsg.setText("Animation Running");
		runningMsg.setForeground(Color.GREEN);
		stepButton.setEnabled(false);
	}

	protected int extractInt(String text, int digits) {
		int extracted;

		try {
			extracted = Integer.parseInt(extractString(text, digits));
		} catch (Exception e) {
			extracted = Integer.MAX_VALUE;
		}
		return extracted;
	}

	protected String extractString(String val, int maxsize) {
		if (val.length() <= maxsize)
			return val;
		return val.substring(0, maxsize);
	}

	public void enableSpecificButtons() {
		/* Override this method to enable specific buttons */
	}

	protected void endAnimation() {
		running = false;
		runningMsg.setText("Animation Completed");
		runningMsg.setForeground(Color.BLACK);
		stepButton.setEnabled(false);
		skipButton.setEnabled(false);
		restartButton.setEnabled(true);
		goButton.setText("Go");
		goButton.setEnabled(false);
		enableSpecificButtons();
	}

	// This method mark the document as dirty (the Save menu item is enabled)
	public void changeDone() {
		window.getDocument().changeDone();
	}

	public BufferedImage getImage() {
		return view.getImage();
	}

	public String getEPS() {
		return view.getEPS();
	}
	
	public void disableGoAndSkip() {
		goButton.setEnabled(false);
		skipButton.setEnabled(false);
	}
	
	public void enableGoAndSkip() {
		goButton.setEnabled(true);
		skipButton.setEnabled(true);
	}

	public void enableRestartButton() {
		restartButton.setEnabled(true);
	}

	public void disableRestartButton(){
		restartButton.setEnabled(false);
	}

	public ADSVWindow getWindow() {
		return window;
	}
}
