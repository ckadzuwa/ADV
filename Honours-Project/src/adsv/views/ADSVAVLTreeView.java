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

    // The x and y coordination of the root
    private static final int ROOT_X_POSITION = 500;
    private static final int ROOT_Y_POSITION = 50;

    //
    private static final int INSERTION_X_POSITION = 100;
    private static final int INSERTION_Y_POSITION = 100;

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
        //If the tree is empty
        if (root == null) {
            root = insertRootVertex(vertexToInsert);
        } else {
            AVLVertex newVertex = createVertexAt(vertexToInsert, INSERTION_X_POSITION, INSERTION_Y_POSITION);
            repaintwait();
            AVLVertex vertexBeingReplaced = findInsertionLocation(vertexToInsert);
        }
    }

    private AVLVertex insertRootVertex(int vertexToInsert) {
        return createVertexAt(vertexToInsert, ROOT_X_POSITION, ROOT_Y_POSITION);
    }

    private void insertIntoTree(int vertexToInsert) {
        AVLVertex vertexBeingReplaced = findInsertionLocation(vertexToInsert);
    }

    private AVLVertex createVertexAt(int vertexToInsert, int vertexXPosition, int vertexYPosition) {
        GElement graphicalRoot = createCircle(String.valueOf(vertexToInsert), vertexXPosition, vertexYPosition);
        AVLVertex parentVertex = AVLVertex.newVertex(vertexToInsert, graphicalRoot);
        addLeafChildren(parentVertex, vertexXPosition, vertexYPosition);

        return parentVertex;
    }

    private void addLeafChildren(AVLVertex parentVertex, int parentXPosition, int parentYPosition) {
        GElement graphicalLeftLeaf = createRectangle("", childXPosition(parentXPosition, -1), childYPosition(parentYPosition), LEAF_LENGTH, LEAF_LENGTH);
        GElement graphicalRightLeaf = createRectangle("", childXPosition(parentXPosition, 1), childYPosition(parentYPosition), LEAF_LENGTH, LEAF_LENGTH);
        AVLVertex leftLeafVertex = AVLVertex.newLeafVertex(graphicalLeftLeaf);
        AVLVertex rightLeafVertex = AVLVertex.newLeafVertex(graphicalRightLeaf);

        addLeftChild(parentVertex, leftLeafVertex);
        addRightChild(parentVertex, rightLeafVertex);
    }

    private void addLeftChild(AVLVertex parent, AVLVertex leftChild) {
        parent.leftChild = leftChild;
        leftChild.parent = parent;
        createLink(parent.graphicalVertex, leftChild.graphicalVertex, GLink.SHAPE_ARC, GElement.ANCHOR_CENTER, GElement.ANCHOR_TOP, "", 1);
    }

    private void addRightChild(AVLVertex parent, AVLVertex rightChild) {
        parent.rightChild = rightChild;
        rightChild.parent = parent;
        createLink(parent.graphicalVertex, rightChild.graphicalVertex, GLink.SHAPE_ARC, GElement.ANCHOR_CENTER, GElement.ANCHOR_TOP, "", 1);
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
