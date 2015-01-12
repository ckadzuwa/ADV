package adsv.views;

import adsv.avlTreeModel.AVLVertex;
import adsv.globals.Constants;
import adsv.globals.GenericFunctions;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;

public class ADSVAVLTreeView extends DSView {

    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int FIND = 3;

    // The width/height difference between two levels in the tree for insertion
    private static final int WIDTH_DELTA = 20;
    private static final int HEIGHT_DELTA = 50;

    private static final int ROOT_X_POSITION = 500;
    private static final int ROOT_Y_POSITION = 50;

    //Length for a sqaure leaf vertex
    private static final int LEAF_LENGTH = 20;

    AVLVertex root;
    private GElement highlightCircle;

    public ADSVAVLTreeView() {
        super();
        createLabel("", 1, 1); // Minor hack to ensure canvas
        // root element is set
    }


    public void runAlgorithm(int algorithm, Object param) {
        String list = (String) param;
        switch (algorithm) {
            case INSERT:
                runTreeInsertion(list);
                break;
            case DELETE:
                runTreeDeletion(list);
                break;
            case FIND:
                runTreeFind(list);
                break;
        }
    }

    private void runTreeInsertion(String list) {

        //If the list is a single number
        if (GenericFunctions.isValidNumber(list)) {
            insertVertex(Integer.parseInt(list));
        } else {
            //Otherwise , if the list is composed of several numbers
            if (GenericFunctions.isValidNumberList(list)) {
                String[] numbers = list.split(",");

                for (int i = 0; i < numbers.length; i++) {
                    insertVertex(Integer.parseInt(numbers[i]));
                }
            }
        }

    }

    private void insertVertex(int vertexToInsert) {
        // If the tree is empty
        if (root == null) {
            insertRootVertex(vertexToInsert);
        } else {
           AVLVertex vertexBeingReplaced = findInsertionLocation(vertexToInsert);
        }
    }

    private void insertRootVertex(int vertexToInsert) {
        GElement graphicalRoot = createCircle(String.valueOf(vertexToInsert), ROOT_X_POSITION, ROOT_Y_POSITION);
        GElement graphicalLeftLeaf = createRectangle("", childXPosition(ROOT_X_POSITION, -1), childYPosition(ROOT_Y_POSITION), LEAF_LENGTH, LEAF_LENGTH);
        GElement graphicalRightLeaf = createRectangle("", childXPosition(ROOT_X_POSITION, 1), childYPosition(ROOT_Y_POSITION), LEAF_LENGTH, LEAF_LENGTH);
        root = AVLVertex.newRootVertex(vertexToInsert, graphicalRoot, graphicalLeftLeaf, graphicalRightLeaf);
        addEdge(root, root.leftChild);
        addEdge(root, root.rightChild);
    }

    private void addEdge(AVLVertex from, AVLVertex to) {
        createLink(from.graphicalVertex, to.graphicalVertex, GLink.SHAPE_ARC, GElement.ANCHOR_CENTER, GElement.ANCHOR_TOP, "", 1);
    }

    private int childXPosition(int parentXPosition, int offsetSign) {
        return parentXPosition + (offsetSign * WIDTH_DELTA);
    }

    private int childYPosition(int parentYPosition) {
        return parentYPosition + HEIGHT_DELTA;
    }


    private void runTreeDeletion(Object param) {
    }

    private void runTreeFind(String list) {

        //If the list is a single number
        if (GenericFunctions.isValidNumber(list)) {
            findElement(Integer.parseInt(list));
        } else {
            //Otherwise , if the list is composed of several numbers
            if (GenericFunctions.isValidNumberList(list)) {
                String[] numbers = list.split(",");

                for (int i = 0; i < numbers.length; i++) {
                    findElement(Integer.parseInt(numbers[i]));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //Returns vertex if element found , null otherwise
    private AVLVertex findElement(int k) {
        if (root == null) {
            return null;
        } else {
            AVLVertex u = root;
            setHighlightCircleAtRoot();
            while (!u.isLeafVertex() && u.value != k) {
                if (k < u.value) {
                    u = u.leftChild;
                } else {
                    u = u.rightChild;
                }
                animateVertexVisit(u);
            }
            removeAny(highlightCircle);
            repaint();
            if (!u.isLeafVertex() && u.value == k) {
                return u;
            } else {
                return null;
            }
        }
    }

    private AVLVertex findInsertionLocation(int k) {
        if (root == null) {
            return null;
        } else {
            AVLVertex u = root;
            setHighlightCircleAtRoot();
            while (!u.isLeafVertex()) {
                if (k < u.value) {
                    u = u.leftChild;
                } else {
                    u = u.rightChild;
                }
                animateVertexVisit(u);
            }
            removeAny(highlightCircle);
            return u;
        }
    }

    private void animateVertexVisit(AVLVertex u) {
        AnimatePath(highlightCircle, highlightCircle.getPosition(), u.graphicalVertex.getPosition(), 40);
        repaintwait();
    }

    private void setHighlightCircleAtRoot() {
        highlightCircle = createCircle("", root.graphicalVertex.getPositionX(), root.graphicalVertex.getPositionY());
        highlightCircle.setOutlineColor(Constants.ANDROID_RED);
        repaintwait();
    }


}
