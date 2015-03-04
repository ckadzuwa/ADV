package adsv.views;

import adsv.globals.Constants;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GElementRect;

import java.awt.*;

public abstract class SortView extends View {

    public final int BUBBLESORT = 1;

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

    protected final static int STARTING_X_COORD = 400;
    protected final static int STARTING_Y_COORD = 400;

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

    private void cloneCurrentInput() {
        arrayLocationValueCopy = arrayLocationValue.clone();
    }

    public void toggleShowIndices() {
        showIndices = !showIndices;
        for (int i = 0; i < currentNumElements; i++)
            arrayLocationIndex[i].setLabelVisible(!arrayLocationIndex[i].isLabelVisible());
        repaint();

    }

    private int getYPosGraphic(int value, int y) {
        return (int) (y - ((((double) value) / 100 * MAXHEIGHT) / 2 + 12));
    }

    private GElement buildGrapicElement(int value, int xbase, int ybase) {
        int height = (int) (((double) value) / 100 * MAXHEIGHT);
        return createRectangle("", xbase, getYPosGraphic(value, ybase), 10, height, false);
    }

    protected void swap(int index1, int index2) {
        int i;
        Vector2D G1from = null;
        Vector2D G1to = null;
        Vector2D G2from = null;
        Vector2D G2to = null;

        Vector2D path1G[] = null;
        Vector2D path2G[] = null;
        Vector2D path1[];
        Vector2D path2[];

        int numsteps = Math.max(
                (int) (Math.abs(arrayLocationTextValue[index1].getPositionX()
                        - arrayLocationTextValue[index2].getPositionX()) / 2), 10);

        double G1newY;
        double G2newY;

        if (showBars) {
            G1newY = arrayLocationBar[index1].getPositionY() - Ypos[index1] + Ypos[index2];
            G2newY = arrayLocationBar[index2].getPositionY() - Ypos[index2] + Ypos[index1];
            G1from = arrayLocationBar[index1].getPosition();
            G2from = arrayLocationBar[index2].getPosition();
            G1to = new Vector2D(G2from.getX(), G1newY);
            G2to = new Vector2D(G1from.getX(), G2newY);
        }

        path1 = createPath(arrayLocationTextValue[index1].getPosition(), arrayLocationTextValue[index2].getPosition(),
                numsteps);
        path2 = createPath(arrayLocationTextValue[index2].getPosition(), arrayLocationTextValue[index1].getPosition(),
                numsteps);
        if (showBars) {
            path1G = createPath(G1from, G1to, numsteps);
            path2G = createPath(G2from, G2to, numsteps);
        }
        for (i = 0; i < numsteps; i++) {
            arrayLocationTextValue[index1].setPosition(path1[i]);
            arrayLocationTextValue[index2].setPosition(path2[i]);
            if (showBars) {
                arrayLocationBar[index1].setPosition(path1G[i]);
                arrayLocationBar[index2].setPosition(path2G[i]);

            }
            repaintwaitmin();

        }
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

    // TODO: Find out is this necessary
    protected void moveto(int index1, int index2) {
        moveto(index1, index2, true);
    }

    protected void moveto(int index1, int index2, boolean killOldindex1) {
        int i;
        Vector2D G1from = null;
        Vector2D G1to = null;

        Vector2D path1G[] = null;
        Vector2D path1[];

        int numsteps = (int) Math.max(
                Math.sqrt((Xpos[index1] - Xpos[index2]) * (Xpos[index1] - Xpos[index2]) + (Ypos[index1] - Ypos[index2])
                        * (Ypos[index1] - Ypos[index2]) / 4), 10);

        double G1newY;
        double G1newX;

        if (showBars) {
            G1newY = getYPosGraphic(arrayLocationValue[index1], Ypos[index2]);
            G1newX = Xpos[index2];
            G1from = arrayLocationBar[index1].getPosition();
            G1to = new Vector2D(G1newX, G1newY);
            if (arrayLocationBar[index2] != null)
                removeAny(arrayLocationBar[index2]);
            arrayLocationBar[index2] = buildGrapicElement(arrayLocationValue[index1], Xpos[index1], Ypos[index1]);
        }
        path1 = createPath(arrayLocationTextValue[index1].getPosition(), new Vector2D(Xpos[index2], Ypos[index2]),
                numsteps);
        if (arrayLocationTextValue[index2] != null)
            removeAny(arrayLocationTextValue[index2]);
        arrayLocationTextValue[index2] = createLabel(String.valueOf(arrayLocationValue[index1]), Xpos[index1],
                Ypos[index1]);
        if (showBars) {
            path1G = createPath(G1from, G1to, numsteps);
        }
        if (killOldindex1) {
            if (showBars)
                removeAny(arrayLocationBar[index1]);
            removeAny(arrayLocationTextValue[index1]);

        }

        for (i = 0; i < numsteps; i++) {
            arrayLocationTextValue[index2].setPosition(path1[i]);
            if (showBars) {
                arrayLocationBar[index2].setPosition(path1G[i]);

            }
            repaintwaitmin();

        }
        arrayLocationValue[index2] = arrayLocationValue[index1];
    }

    protected boolean lessThanBC(int index1, int index2) {
        if ((index1 < 0) || (index1 > currentNumElements) || (index2 < 0) || (index2 > currentNumElements))
            return false;
        return lessThan(index1, index2);
    }

    public boolean lessThan(int index1, int index2) {

        arrayLocationTextValue[index1].setLabelColor(Constants.ANDROID_RED);
        arrayLocationTextValue[index2].setLabelColor(Constants.ANDROID_RED);
        if (showBars) {
            arrayLocationBar[index1].setOutlineColor(Constants.ANDROID_RED);
            arrayLocationBar[index2].setOutlineColor(Constants.ANDROID_RED);
        }
        repaintwait();
        arrayLocationTextValue[index1].setLabelColor(Color.BLACK);
        arrayLocationTextValue[index2].setLabelColor(Color.BLACK);
        if (showBars) {
            arrayLocationBar[index1].setOutlineColor(Color.BLACK);
            arrayLocationBar[index2].setOutlineColor(Color.BLACK);
        }
        return arrayLocationValue[index1] < arrayLocationValue[index2];
    }

    protected void grayElement(int index, boolean graybottom) {
        arrayLocationTextValue[index].setLabelColor(Color.LIGHT_GRAY);
        arrayLocationFrame[index].setOutlineColor(Color.LIGHT_GRAY);
        arrayLocationIndex[index].setLabelColor(Color.LIGHT_GRAY);
        if (showBars)
            arrayLocationBar[index].setOutlineColor(Color.LIGHT_GRAY);
        if (graybottom) {
            arrayLocationTextValue[index + currentNumElements].setLabelColor(Color.LIGHT_GRAY);
            arrayLocationFrame[index + currentNumElements].setOutlineColor(Color.LIGHT_GRAY);
            arrayLocationIndex[index + currentNumElements].setLabelColor(Color.LIGHT_GRAY);
            if (showBars && arrayLocationBar[index + currentNumElements] != null)
                arrayLocationBar[index + currentNumElements].setOutlineColor(Color.LIGHT_GRAY);
        }
    }

    protected void ungrayElement(int index, boolean ungraybottom) {
        arrayLocationTextValue[index].setLabelColor(Color.BLACK);
        arrayLocationFrame[index].setOutlineColor(Color.BLACK);
        arrayLocationIndex[index].setLabelColor(Color.BLUE);
        if (showBars)
            arrayLocationBar[index].setOutlineColor(Color.BLACK);
        if (ungraybottom) {
            arrayLocationTextValue[index + currentNumElements].setLabelColor(Color.BLACK);
            arrayLocationFrame[index + currentNumElements].setOutlineColor(Color.BLACK);
            arrayLocationIndex[index + currentNumElements].setLabelColor(Color.BLUE);
            if (showBars && (arrayLocationBar[index + currentNumElements] != null))
                arrayLocationBar[index + currentNumElements].setOutlineColor(Color.BLACK);

        }

    }

    protected void highlightRange(int low, int high, boolean highlightbottom) {
        int i;
        for (i = 0; i < currentNumElements; i++) {
            if ((i < low) || (i > high)) {
                grayElement(i, highlightbottom);

            } else {
                ungrayElement(i, highlightbottom);
            }
        }
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
            arrayLocationFrame[i].setFillColor(Constants.ANDROID_BLUE);
        }

        // This has to be done in a seperate loop to ensure Z-ordering of
        // elements on the canvas
        for (int i = 0; i < currentNumElements; i++) {
            arrayLocationBar[i] = buildGrapicElement(arrayLocationValue[i], Xpos[i], Ypos[i]);
            arrayLocationBar[i].setFillColor(Constants.ANDROID_BLUE);
            ((GElementRect) arrayLocationBar[i]).setElementVisible(showBars);
            arrayLocationTextValue[i] = createLabel(String.valueOf(arrayLocationValue[i]), Xpos[i], Ypos[i]);
        }

        repaint();

    }

    private void intialiseElements() {

        int allocSize = currentNumElements * 2;

        arrayLocationValue = new int[allocSize];
        arrayLocationValueCopy = new int[allocSize];
        arrayLocationTextValue = new GElement[allocSize];
        arrayLocationIndex = new GElement[allocSize];
        arrayLocationFrame = new GElement[allocSize];
        arrayLocationBar = new GElement[allocSize];
        Xpos = new int[allocSize];
        Ypos = new int[allocSize];

    }

    public void toggleShowBars() {
        showBars = !showBars;
        for (int i = 0; i < currentNumElements; i++) {
            if (showBars) {
                arrayLocationBar[i] = buildGrapicElement(arrayLocationValue[i], Xpos[i], Ypos[i]);
                arrayLocationBar[i].setFillColor(Constants.ANDROID_BLUE);
                ((GElementRect) arrayLocationBar[i]).setElementVisible(showBars);
            } else {
                removeAny(arrayLocationBar[i]);
            }
        }
        repaint();
    }
}