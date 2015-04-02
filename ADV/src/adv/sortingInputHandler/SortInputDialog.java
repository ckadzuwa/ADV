package adv.sortingInputHandler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import adv.utility.InputConstraints;
import adv.panels.SortPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class SortInputDialog extends JDialog implements ItemListener, ActionListener, DocumentListener {
    private JPanel inputMethodsContainer;
    private JPanel presetsInputPanel;
    private JPanel userInputPanel;
    private JPanel buttonPanel;
    private JButton okButton;
    private JButton cancelButton;
    private Box horizontalBox;
    private JTextField[] arrayLocations;
    private JLabel lblListSize;
    private JButton clearButton;
    private JPanel inputChoicePanel;
    private JSpinner arraySizeSpinner;
    private JRadioButton presetInputChoice;
    private JRadioButton userInputChoice;
    private JPanel presetChoicesContainer;
    private JRadioButton randomPresetChoice;
    private JRadioButton decreasing;
    private JRadioButton increasing;
    private SortPanel sortPanel;

    private JLabel userInputMessage;
    private boolean visitedUserInputPanel;
    private JFormattedTextField arraySizeTextField;
    private Document arraySizeSpinnerDoc;
    private JLabel presetInputMessage;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            SortInputDialog dialog = new SortInputDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public SortInputDialog(SortPanel sortPanel) {
        this();
        this.sortPanel = sortPanel;
    }

    public SortInputDialog() {
        setModalityType(ModalityType.APPLICATION_MODAL);
        setModal(true);
        setAlwaysOnTop(true);
        setBounds(100, 100, 801, 613);
        getContentPane().setLayout(new BorderLayout(0, 0));
        {
            inputMethodsContainer = new JPanel();
            getContentPane().add(inputMethodsContainer, BorderLayout.NORTH);
            inputMethodsContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
            inputMethodsContainer.setLayout(new BorderLayout(0, 30));
            {
                presetsInputPanel = new JPanel();
                inputMethodsContainer.add(presetsInputPanel, BorderLayout.NORTH);
                presetsInputPanel.setBorder(new BubbleBorder(Color.BLACK, 2, 16, 0));
                GridBagLayout gbl_presetsInputPanel = new GridBagLayout();
                gbl_presetsInputPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
                gbl_presetsInputPanel.columnWidths = new int[]{100, 100, 100, 100, 100};
                gbl_presetsInputPanel.rowHeights = new int[]{50, 50, 50};
                presetsInputPanel.setLayout(gbl_presetsInputPanel);
                {
                    lblListSize = new JLabel("Array Size:");
                    lblListSize.setHorizontalAlignment(SwingConstants.CENTER);
                    lblListSize.setHorizontalTextPosition(SwingConstants.CENTER);
                    lblListSize.setAlignmentX(Component.CENTER_ALIGNMENT);
                    GridBagConstraints gbc_lblListSize = new GridBagConstraints();
                    gbc_lblListSize.insets = new Insets(0, 0, 5, 5);
                    gbc_lblListSize.gridx = 2;
                    gbc_lblListSize.gridy = 0;
                    presetsInputPanel.add(lblListSize, gbc_lblListSize);
                }
                {
                    arraySizeSpinner = new JSpinner();
                    arraySizeSpinner.setModel(new SpinnerNumberModel((InputConstraints.MAX_NUM_ELEMENTS / 2), 1,
                            InputConstraints.MAX_NUM_ELEMENTS, 1));

                    JComponent editor = arraySizeSpinner.getEditor();
                    JTextField arraySizeTextField = ((DefaultEditor) editor).getTextField();
                    arraySizeSpinnerDoc = arraySizeTextField.getDocument();
                    arraySizeSpinnerDoc.addDocumentListener(this);
                    GridBagConstraints gbc_arraySizeSpinner = new GridBagConstraints();
                    gbc_arraySizeSpinner.anchor = GridBagConstraints.WEST;
                    gbc_arraySizeSpinner.insets = new Insets(0, 0, 5, 5);
                    gbc_arraySizeSpinner.gridx = 3;
                    gbc_arraySizeSpinner.gridy = 0;
                    presetsInputPanel.add(arraySizeSpinner, gbc_arraySizeSpinner);
                }
                {
                    presetChoicesContainer = new JPanel();
                    FlowLayout flowLayout = (FlowLayout) presetChoicesContainer.getLayout();
                    GridBagConstraints gbc_presetChoicesContainer = new GridBagConstraints();
                    gbc_presetChoicesContainer.gridwidth = 2;
                    gbc_presetChoicesContainer.insets = new Insets(0, 0, 5, 5);
                    gbc_presetChoicesContainer.gridx = 2;
                    gbc_presetChoicesContainer.gridy = 1;
                    presetsInputPanel.add(presetChoicesContainer, gbc_presetChoicesContainer);

                    ButtonGroup presetChoices = new ButtonGroup();
                    {
                        {
                            randomPresetChoice = new JRadioButton("Random");
                            randomPresetChoice.setHorizontalAlignment(SwingConstants.CENTER);
                            randomPresetChoice.setSelected(true);
                            randomPresetChoice.addItemListener(this);
                            presetChoices.add(randomPresetChoice);
                            presetChoicesContainer.add(randomPresetChoice);
                        }

                        {
                            decreasing = new JRadioButton("<html><br>Random Decreasing<br>Sequence</html>");
                            decreasing.setHorizontalAlignment(SwingConstants.CENTER);
                            decreasing.addItemListener(this);
                            presetChoices.add(decreasing);
                            presetChoicesContainer.add(decreasing);
                        }
                        {
                            increasing = new JRadioButton("<html><br>Random Increasing<br>Sequence</html>");
                            increasing.setHorizontalAlignment(SwingConstants.CENTER);
                            presetChoices.add(increasing);
                            presetChoicesContainer.add(increasing);
                        }
                    }
                }
                {
                    presetInputMessage = new JLabel("");
                    presetInputMessage.setHorizontalAlignment(SwingConstants.CENTER);
                    GridBagConstraints gbc_presetInputMessage = new GridBagConstraints();
                    gbc_presetInputMessage.anchor = GridBagConstraints.WEST;
                    gbc_presetInputMessage.gridwidth = 2;
                    gbc_presetInputMessage.insets = new Insets(0, 0, 0, 5);
                    gbc_presetInputMessage.gridx = 2;
                    gbc_presetInputMessage.gridy = 2;
                    presetsInputPanel.add(presetInputMessage, gbc_presetInputMessage);
                }
            }
            {
                userInputPanel = new JPanel();
                userInputPanel.setBorder(new BubbleBorder(Color.BLACK, 2, 16, 0));
                inputMethodsContainer.add(userInputPanel);
                GridBagLayout gbl_userInputPanel = new GridBagLayout();
                gbl_userInputPanel.columnWidths = new int[]{100, 100, 100, 100, 100};
                gbl_userInputPanel.rowHeights = new int[]{50, 50, 50, 50};
                userInputPanel.setLayout(gbl_userInputPanel);
                {
                    horizontalBox = Box.createHorizontalBox();
                    GridBagConstraints gbc_horizontalBox = new GridBagConstraints();
                    gbc_horizontalBox.insets = new Insets(0, 0, 5, 5);
                    gbc_horizontalBox.gridx = 2;
                    gbc_horizontalBox.gridy = 1;
                    userInputPanel.add(horizontalBox, gbc_horizontalBox);
                    {
                        clearButton = new JButton("Clear");
                        clearButton.addActionListener(this);
                        GridBagConstraints gbc_btnClear = new GridBagConstraints();
                        gbc_btnClear.insets = new Insets(0, 0, 5, 5);
                        gbc_btnClear.gridx = 2;
                        gbc_btnClear.gridy = 2;
                        userInputPanel.add(clearButton, gbc_btnClear);
                    }
                    {
                        userInputMessage = new JLabel("");
                        userInputMessage.setHorizontalTextPosition(SwingConstants.CENTER);
                        userInputMessage.setHorizontalAlignment(SwingConstants.CENTER);
                        GridBagConstraints gbc_userInputMessage = new GridBagConstraints();
                        gbc_userInputMessage.insets = new Insets(0, 0, 0, 5);
                        gbc_userInputMessage.gridx = 2;
                        gbc_userInputMessage.gridy = 3;
                        userInputPanel.add(userInputMessage, gbc_userInputMessage);
                    }
                    intialiseArrayLocations();
                }
            }
        }
        {
            buttonPanel = new JPanel();
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
            FlowLayout fl_buttonPanel = new FlowLayout(FlowLayout.CENTER, 50, 5);
            buttonPanel.setLayout(fl_buttonPanel);
            {
                okButton = new JButton("OK");
                okButton.addActionListener(this);
                okButton.setActionCommand("OK");
                buttonPanel.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(this);
                cancelButton.setActionCommand("Cancel");
                buttonPanel.add(cancelButton);
            }
        }
        {
            inputChoicePanel = new JPanel();
            inputChoicePanel.setBorder(new EmptyBorder(0, 20, 0, 20));
            getContentPane().add(inputChoicePanel);
            ButtonGroup inputChoices = new ButtonGroup();
            {
                {
                    presetInputChoice = new JRadioButton("Use Preset Input");
                    presetInputChoice.addItemListener(this);
                    inputChoices.add(presetInputChoice);
                    inputChoicePanel.add(presetInputChoice);
                }

                {
                    userInputChoice = new JRadioButton("Use My Input");
                    userInputChoice.addItemListener(this);
                    inputChoices.add(userInputChoice);
                    inputChoicePanel.add(userInputChoice);
                }
            }
        }
        presetInputChoice.setSelected(true);

    }

    private void intialiseArrayLocations() {

        arrayLocations = new JTextField[InputConstraints.MAX_NUM_ELEMENTS];

        for (int i = 0; i < InputConstraints.MAX_NUM_ELEMENTS; i++) {
            Box verticalBox = Box.createVerticalBox();
            JLabel arrayIndice = new JLabel(i + "");
            JTextField arrayLocation = new JTextField();
            Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
            arrayLocation.setBorder(border);
            arrayLocation.setMinimumSize(new Dimension(30, 30));
            arrayLocation.setColumns(10);
            arrayLocation.setHorizontalAlignment(JTextField.CENTER);
            arrayLocations[i] = arrayLocation;
            arrayLocations[i].getDocument().addDocumentListener(this);

            arrayIndice.setAlignmentX(Component.CENTER_ALIGNMENT);
            verticalBox.add(arrayLocation);
            verticalBox.add(arrayIndice);
            horizontalBox.add(verticalBox);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent event) {

        JRadioButton choiceSelected = (JRadioButton) event.getItem();

        if (event.getStateChange() == ItemEvent.SELECTED) {

            if (choiceSelected.equals(presetInputChoice)) {
                System.out.println("PRESET-INPUT!");
                setEnabledAll(userInputPanel, false);
                setEnabledAll(presetsInputPanel, true);
                userInputPanel.setBackground(null);
                presetChoicesContainer.setBackground(Color.LIGHT_GRAY);
                presetsInputPanel.setBackground(Color.LIGHT_GRAY);
                userInputMessage.setText("");
            }

            if (choiceSelected.equals(userInputChoice)) {
                System.out.println("USER-INPUT!");
                setEnabledAll(presetsInputPanel, false);
                setEnabledAll(userInputPanel, true);
                presetsInputPanel.setBackground(null);
                presetChoicesContainer.setBackground(null);
                userInputPanel.setBackground(Color.LIGHT_GRAY);

                if (visitedUserInputPanel) {
                    userInputValid();
                } else {
                    visitedUserInputPanel = true;
                }
            }
        }
    }

    public void setEnabledAll(Object object, boolean state) {
        if (object instanceof Container) {
            Container c = (Container) object;
            Component[] components = c.getComponents();
            for (Component component : components) {
                setEnabledAll(component, state);
                component.setEnabled(state);
            }
        } else {
            if (object instanceof Component) {
                Component component = (Component) object;
                component.setEnabled(state);
            }
        }
    }

    private void checkOptionSelected() {
        if (presetInputChoice.isSelected()) {

            int arraySize = ((Integer) arraySizeSpinner.getValue()).intValue();

            if (randomPresetChoice.isSelected()) {
                generateRandomInput(arraySize);
            } else if (decreasing.isSelected()) {
                generateDecreasingInput(arraySize);
            } else {
                generateIncreasingInput(arraySize);
            }

            sortPanel.enableGoAndSkip();
            this.dispose();

        } else {

            if (userInputValid()) {
                generateUserInput();
            } else {
                System.out.println("INVALID");
            }

        }
    }

    private void generateUserInput() {

        int limit = 0;
        for (int i = InputConstraints.MAX_NUM_ELEMENTS - 1; i >= 0; i--) {

            if (!arrayLocations[i].getText().isEmpty()) {
                limit = i;
                break;
            }
        }

        int[] numArray = new int[limit + 1];
        for (int i = 0; i <= limit; i++) {
            numArray[i] = Integer.parseInt(arrayLocations[i].getText());
        }

        sortPanel.getSortView().setNewInput(numArray);
        sortPanel.enableGoAndSkip();
        this.dispose();

    }

    private void generateIncreasingInput(int arraySize) {

        int[] numArray = generateRandomIntegerArray(arraySize);

        // Sorts in ascending order
        Arrays.sort(numArray);
        sortPanel.getSortView().setNewInput(numArray);
    }

    private void generateDecreasingInput(int arraySize) {

        int[] numArray = generateRandomIntegerArray(arraySize);

        // Sort in ascending order
        Arrays.sort(numArray);

        // Reverse array
        for (int i = 0; i < arraySize/2; i++) {
            int tmp = numArray[i];
            numArray[i] = numArray[arraySize-i-1];
            numArray[arraySize-i-1] = tmp;
        }

        sortPanel.getSortView().setNewInput(numArray);
    }

    private void generateRandomInput(int arraySize) {

        int[] numArray = generateRandomIntegerArray(arraySize);
        sortPanel.getSortView().setNewInput(numArray);

    }

    private int[] generateRandomIntegerArray(int arraySize) {

        int[] numArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            numArray[i] = (int) (Math.random() * (InputConstraints.MAX_INPUT_VALUE + 1));
        }

       return numArray;

    }

    private boolean userInputValid() {

        for (int i = 0; i < InputConstraints.MAX_NUM_ELEMENTS; i++) {

            String currentLocationText = arrayLocations[i].getText().trim();

            if (!InputConstraints.isValidNumber(currentLocationText)) {

                if (i > 0 && currentLocationText.isEmpty()) {
                    return ensureEmpty(i);
                } else {

                    if (i == 0 && currentLocationText.isEmpty()) {
                        if (ensureEmpty(i)) {
                            setUserInputEmptyWarning();
                            return false;
                        }
                    }

                    setUserInputWarning(i);
                    return false;

                }
            }
        }

        clearUserInputWarning();
        return true;

    }

    private void setUserInputEmptyWarning() {
        userInputMessage.setForeground(Color.RED);
        userInputMessage.setText("The array is empty.");

    }

    private void clearUserInputWarning() {
        // warningMessage.setForeground(Color.BLACK);
        // warningMessage.setText("Input OK.");
        userInputMessage.setText("");
    }

    private void setUserInputWarning(int i) {
        userInputMessage.setForeground(Color.RED);
        userInputMessage.setText("Invald entry at index " + i + ". Please enter a number between "+
                InputConstraints.MIN_INPUT_VALUE+" and "+InputConstraints.MAX_INPUT_VALUE+".");
    }

    private void setPresetInputWarning() {
        presetInputMessage.setForeground(Color.RED);
        presetInputMessage.setText("Array size must be a number between " + 1 + " and " + InputConstraints.MAX_NUM_ELEMENTS);
    }

    private boolean ensureEmpty(int startingPosition) {

        for (int j = startingPosition; j < InputConstraints.MAX_NUM_ELEMENTS; j++) {

            if (!arrayLocations[j].getText().isEmpty()) {
                setUserInputWarning(startingPosition);

                return false;
            }

        }

        clearUserInputWarning();
        return true;
    }

    private void clearArrayLocations() {

        for (JTextField arrayLocation : arrayLocations) {
            arrayLocation.setText("");
        }

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        Object cause = event.getSource();

        if (cause instanceof JButton) {

            JButton buttonPressed = (JButton) event.getSource();

            if (buttonPressed.equals(clearButton)) {
                System.out.println("Clear Button");
                clearArrayLocations();
            } else if (buttonPressed.equals(okButton)) {
                System.out.println("OK Button");
                checkOptionSelected();
            } else {
                System.out.println("Cancel Button");
                this.dispose();
            }
        }

        if (cause instanceof JTextField) {
            System.out.println(arraySizeTextField.getText());
        }

    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
        // Does not fire these events
    }

    @Override
    public void insertUpdate(DocumentEvent docEvent) {

        Document doc = docEvent.getDocument();

        if (doc.equals(arraySizeSpinnerDoc)) {
            arraySizeValid(doc);
        } else {
            userInputValid();
        }
    }

    private void arraySizeValid(Document doc) {

        try {
            String content = doc.getText(0, doc.getLength());
            if (InputConstraints.isValidNumber(content)) {
                int number = Integer.parseInt(content);

                if (number < 1 || number > InputConstraints.MAX_NUM_ELEMENTS) {
                    setPresetInputWarning();
                } else {
                    clearPresetInputWarning();
                }
            } else {
                setPresetInputWarning();
            }

        } catch (BadLocationException e) {

        }

    }

    private void clearPresetInputWarning() {
        presetInputMessage.setText("");

    }

    @Override
    public void removeUpdate(DocumentEvent docEvent) {

        Document doc = docEvent.getDocument();

        if (doc.equals(arraySizeSpinnerDoc)) {
            arraySizeValid(doc);
        } else {
            userInputValid();
        }
    }

}
