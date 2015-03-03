package adsv.panels;

import adsv.globals.Constants;
import adsv.globals.GenericFunctions;
import adsv.main.Window;
import adsv.views.AVLTreeView;
import edu.usfca.xj.appkit.utils.XJAlert;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AVLTreePanel extends Panel implements DocumentListener, ActionListener {

    private AVLTreeView AVLTreeView;
    private JLabel insertLabel;
    private JTextField insertField;
    private Document insertFieldDocument;
    private JLabel deleteLabel;
    private JTextField deleteField;
    private Document deleteFieldDocument;
    private JLabel findLabel;
    private JTextField findField;
    private Document findFieldDocument;
    private JLabel userInputMessage;

    private int nonEmptyField;
    private String nonEmptyFieldInput;

    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int FIND = 3;

    public AVLTreePanel(Window window) {
        super(window);

        Box box = Box.createHorizontalBox();
        Dimension labelSize = new Dimension(50, 50);
        Dimension fieldSize = new Dimension(100, 50);

        insertLabel = new JLabel("Insert: ");
        insertField = new JTextField("");
        insertField.addActionListener(this);
        insertFieldDocument = insertField.getDocument();
        insertFieldDocument.addDocumentListener(this);
        insertField.setMaximumSize(fieldSize);
        box.add(insertLabel);
        box.add(insertField);

        deleteLabel = new JLabel("Delete: ");
        deleteField = new JTextField("");
        deleteField.addActionListener(this);
        deleteFieldDocument = deleteField.getDocument();
        deleteFieldDocument.addDocumentListener(this);
        deleteField.setMaximumSize(fieldSize);
        box.add(deleteLabel);
        box.add(deleteField);

        findLabel = new JLabel("Find: ");
        findField = new JTextField("");
        findField.addActionListener(this);
        findFieldDocument = findField.getDocument();
        findFieldDocument.addDocumentListener(this);
        findField.setMaximumSize(fieldSize);
        box.add(findLabel);
        box.add(findField);

        userInputMessage = new JLabel("");
        userInputMessage.setForeground(Color.RED);

        this.add(box, BorderLayout.NORTH);
        this.add(view = AVLTreeView = new AVLTreeView());
        setUpAnimationPanel(AVLTreeView);
    }

    // Persistence

    public void setData(Object data) {
        AVLTreeView.setData(data);
    }

    public Object getData() {
        return AVLTreeView.getData();
    }

    @Override
    public void insertUpdate(DocumentEvent docEvent) {
        Document doc = docEvent.getDocument();
        disableOtherFields(doc);
        setNonEmptyField(doc);
        try {
            String currentFieldInput = doc.getText(0, doc.getLength());
            nonEmptyFieldInput = currentFieldInput;
            if (currentFieldInput.length() > 0) {
                enableGoAndSkip();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void removeUpdate(DocumentEvent docEvent) {

        Document doc = docEvent.getDocument();

        try {
            String currentFieldInput = doc.getText(0, doc.getLength());
            nonEmptyFieldInput = currentFieldInput;
            if (currentFieldInput.length() == 0) {
                enableSpecificButtons();
                nonEmptyField = -1;
                disableGoAndSkip();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //Does nor fire any of these events
    }

    // Disable the textfield other than the
    // one whose document we are working with
    private void disableOtherFields(Document doc) {
        if (!doc.equals(insertFieldDocument)) {
            insertField.setEnabled(false);
        }

        if (!doc.equals(deleteFieldDocument)) {
            deleteField.setEnabled(false);
        }

        if (!doc.equals(findFieldDocument)) {
            findField.setEnabled(false);
        }
    }

    private void clearAllFields() {
        insertField.setText("");
        deleteField.setText("");
        findField.setText("");
    }

    private void setNonEmptyField(Document doc) {

        if (doc.equals(insertFieldDocument)) {
            nonEmptyField = INSERT;
        } else if (doc.equals(deleteFieldDocument)) {
            nonEmptyField = DELETE;
        } else {
            nonEmptyField = FIND;
        }

    }

    @Override
    public void enableSpecificButtons() {
        insertField.setEnabled(true);
        deleteField.setEnabled(true);
        findField.setEnabled(true);
    }

    @Override
    public void disableSpecificButtons() {
        insertField.setEnabled(false);
        deleteField.setEnabled(false);
        findField.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextField inputField = ((JTextField) e.getSource());
        if (userInputValid(nonEmptyFieldInput) && nonEmptyField > 0) {
            goButton.requestFocusInWindow();
            goButton.doClick();
        } else {
            showInvalidInputDialog();
        }
    }

    @Override
    protected void setUpGoButton(Box buttonsContainer) {
        goButton = new JButton("Go");
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!userInputValid(nonEmptyFieldInput) || nonEmptyField < 0) {
                    showInvalidInputDialog();
                    return;
                }

                if (!running) {
                    beginAnimation(nonEmptyField, nonEmptyFieldInput);
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

    @Override
    protected void setUpSkipButton(Box buttonsContainer) {
        skipButton = new JButton("Skip");
        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (!running) {
                    beginAnimation(nonEmptyField,nonEmptyFieldInput);
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

    @Override
    protected void setUpRestartButton(Box buttonsContainer) {
        super.setUpRestartButton(buttonsContainer);
        buttonsContainer.remove(restartButton);
    }

    private void showInvalidInputDialog() {
        XJAlert.display(window.getJavaContainer(), "Invalid Input", "Please enter a number or a list of comma separated numbers\n\n" + "Note: Number(s) can't be be greater than " + Constants.MAX_VALUE);
    }

    private boolean userInputValid(String nonEmptyFieldInput) {
        return GenericFunctions.isValidNumber(nonEmptyFieldInput) || GenericFunctions.isValidNumberList(nonEmptyFieldInput);
    }

    protected void endAnimation(){
        super.endAnimation();
        clearAllFields();
    }


}

