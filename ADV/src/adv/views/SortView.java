package adv.views;

import adv.utility.ColorConstants;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GElementRect;

import java.awt.*;

public abstract class SortView extends View {

    protected final static int STARTING_X_COORD = 400;
    protected final static int STARTING_Y_COORD = 400;

    protected final double MAXHEIGHT = 200.0;
    protected int currentNumElements;
    protected int arrayLocationValue[];
    protected int arrayLocationValueCopy[];
    protected GElement arrayLocationTextValue[];
    protected GElement arrayLocationBar[];
    protected GElement arrayLocationFrame[];
    protected GElement arrayLocationIndex[];
    protected int Xpos[];
    protected int Ypos[];
    protected boolean showBars;
    protected boolean showIndices;

    public SortView() {
        showIndices = true;
        showBars = true;
        waitscalefactor = 10;

        int i;
        minwaitscalefactor = 0.05;
        sleeptime = 25;
    }

    /*--This method should be overridden by subclasses--*/
    protected void callFunction(int function) {

    }

    public void toggleShowIndices() {
        showIndices = !showIndices;
        for (int i = 0; i < currentNumElements; i++)
            arrayLocationIndex[i].setLabelVisible(!arrayLocationIndex[i].isLabelVisible());
        repaint();

    }

    protected void swap(int index1, int index2) {

        Vector2D pathForFirstBar[] = null;
        Vector2D pathForSecondBar[] = null;
        Vector2D pathForFirstTextValue[];
        Vector2D pathForSecondTextValue[];

        int numsteps = Math.max(
                (int) (Math.abs(arrayLocationTextValue[index1].getPositionX()
                        - arrayLocationTextValue[index2].getPositionX()) / 2), 10);


        // 1) Compute paths for graphical object to follow
        pathForFirstTextValue = createPath(arrayLocationTextValue[index1].getPosition(), arrayLocationTextValue[index2].getPosition(),
                numsteps);
        pathForSecondTextValue = createPath(arrayLocationTextValue[index2].getPosition(), arrayLocationTextValue[index1].getPosition(),
                numsteps);

        if (showBars) {
            double targetYPositionForFirstBar = arrayLocationBar[index1].getPositionY() - Ypos[index1] + Ypos[index2];
            Vector2D targetPositionForFirstBar = new Vector2D(arrayLocationBar[index2].getPositionX(), targetYPositionForFirstBar);
            pathForFirstBar = createPath(arrayLocationBar[index1].getPosition(), targetPositionForFirstBar, numsteps);

            double targetYPositionForSecondBar = arrayLocationBar[index2].getPositionY() - Ypos[index2] + Ypos[index1];
            Vector2D targetPositionForSecondBar = new Vector2D(arrayLocationBar[index1].getPositionX(), targetYPositionForSecondBar);
            pathForSecondBar = createPath(arrayLocationBar[index2].getPosition(), targetPositionForSecondBar, numsteps);
        }

        // 2) Animate movement along computed paths
        for (int i = 0; i < numsteps; i++) {
            arrayLocationTextValue[index1].setPosition(pathForFirstTextValue[i]);
            arrayLocationTextValue[index2].setPosition(pathForSecondTextValue[i]);

            if (showBars) {
                arrayLocationBar[index1].setPosition(pathForFirstBar[i]);
                arrayLocationBar[index2].setPosition(pathForSecondBar[i]);

            }

            repaintwaitmin();
        }

        // 3) Update pointers
        swapPointers(index1,index2);
    }

    private void swapPointers(int index1, int index2) {

        int tmp = arrayLocationValue[index1];
        arrayLocationValue[index1] = arrayLocationValue[index2];
        arrayLocationValue[index2] = tmp;

        GElement tmp2 = arrayLocationTextValue[index1];
        arrayLocationTextValue[index1] = arrayLocationTextValue[index2];
        arrayLocationTextValue[index2] = tmp2;

        if (showBars) {
            tmp2 = arrayLocationBar[index1];
            arrayLocationBar[index1] = arrayLocationBar[index2];
            arrayLocationBar[index2] = tmp2;
        }

    }


    public boolean greaterThan(int index1, int index2) {

        boolean greaterThan = arrayLocationValue[index1] > arrayLocationValue[index2];

        if (greaterThan) {
            displayMessage(arrayLocationValue[index1]+" > "+arrayLocationValue[index2]+", swap elements.");
        } else {

            if (arrayLocationValue[index1] < arrayLocationValue[index2]) {
                displayMessage(arrayLocationValue[index1]+" < "+arrayLocationValue[index2]+", no swap required.");
            } else {
                displayMessage(arrayLocationValue[index2]+" = "+arrayLocationValue[index1]+", no swap required.");
            }
        }

        // Highlight elements which are being compared
        arrayLocationTextValue[index1].setLabelColor(ColorConstants.ANDROID_RED);
        arrayLocationTextValue[index2].setLabelColor(ColorConstants.ANDROID_RED);
        if (showBars) {
            arrayLocationBar[index1].setOutlineColor(ColorConstants.ANDROID_RED);
            arrayLocationBar[index2].setOutlineColor(ColorConstants.ANDROID_RED);
        }
        repaintwait();

        // Reset elements back to their default colour.
        arrayLocationTextValue[index1].setLabelColor(Color.BLACK);
        arrayLocationTextValue[index2].setLabelColor(Color.BLACK);
        if (showBars) {
            arrayLocationBar[index1].setOutlineColor(Color.BLACK);
            arrayLocationBar[index2].setOutlineColor(Color.BLACK);
        }

        return greaterThan;
    }

    @Override
    public void restart() {
        setNewInput((int[]) arrayLocationValueCopy.clone());
    }

    public void setNewInput(int[] numArray) {
        setRootElement(null);
        currentNumElements = numArray.length;
        intialiseElements();
        assignElements(numArray);
        setUpExplainationText("Message Text");
    }

    private void intialiseElements() {

        arrayLocationValue = new int[currentNumElements];
        arrayLocationValueCopy = new int[currentNumElements];
        arrayLocationTextValue = new GElement[currentNumElements];
        arrayLocationIndex = new GElement[currentNumElements];
        arrayLocationFrame = new GElement[currentNumElements];
        arrayLocationBar = new GElement[currentNumElements];
        Xpos = new int[currentNumElements];
        Ypos = new int[currentNumElements];

    }

    private void assignElements(int[] numArray) {

        arrayLocationValue = numArray;
        cloneCurrentInput();

        for (int i = 0; i < currentNumElements; i++) {
            Xpos[i] = STARTING_X_COORD + i * 20;
            Ypos[i] = STARTING_Y_COORD;

            arrayLocationIndex[i] = createLabel(String.valueOf(i), Xpos[i], Ypos[i] + 22, false);
            arrayLocationIndex[i].setLabelVisible(showIndices);
            arrayLocationIndex[i].setLabelColor(Color.BLUE);
            arrayLocationFrame[i] = createRectangle("", Xpos[i], Ypos[i] + 2, 20, 20, false);
            arrayLocationFrame[i].setFillColor(ColorConstants.ANDROID_BLUE);
        }

        // This has to be done in a seperate loop to ensure Z-ordering of
        // elements on the canvas
        for (int i = 0; i < currentNumElements; i++) {
            arrayLocationBar[i] = buildGrapicElement(arrayLocationValue[i], Xpos[i], Ypos[i]);
            arrayLocationBar[i].setFillColor(ColorConstants.ANDROID_BLUE);
            ((GElementRect) arrayLocationBar[i]).setElementVisible(showBars);
            arrayLocationTextValue[i] = createLabel(String.valueOf(arrayLocationValue[i]), Xpos[i], Ypos[i]);
        }

        repaint();

    }

    private void cloneCurrentInput() {
        arrayLocationValueCopy = arrayLocationValue.clone();
    }

    private GElement buildGrapicElement(int value, int xbase, int ybase) {
        int height = (int) (((double) value) / 100 * MAXHEIGHT);
        return createRectangle("", xbase, getYPosGraphic(value, ybase), 10, height, false);
    }

    private int getYPosGraphic(int value, int y) {
        return (int) (y - ((((double) value) / 100 * MAXHEIGHT) / 2 + 12));
    }

    public void toggleShowBars() {
        showBars = !showBars;
        for (int i = 0; i < currentNumElements; i++) {
            if (showBars) {
                arrayLocationBar[i] = buildGrapicElement(arrayLocationValue[i], Xpos[i], Ypos[i]);
                arrayLocationBar[i].setFillColor(ColorConstants.ANDROID_BLUE);
                ((GElementRect) arrayLocationBar[i]).setElementVisible(showBars);
            } else {
                removeAny(arrayLocationBar[i]);
            }
        }
        repaint();
    }
}