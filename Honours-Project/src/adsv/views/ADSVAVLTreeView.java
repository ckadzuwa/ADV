package adsv.views;

import adsv.avlTreeModel.AVLVertex;
import adsv.globals.Constants;
import adsv.globals.GenericFunctions;
import edu.usfca.ds.shapes.DSShapeLink;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;

public class ADSVAVLTreeView extends DSView {

    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int FIND = 3;

    // The width/height difference between two levels in the tree for insertion
    //private static final int WIDTH_DELTA = 20;
    private static final int WIDTH_DELTA = 50;
    private static final int HEIGHT_DELTA = 50;

    // The x and y coordination of the root
    private static final int ROOT_X_POSITION = 500;
    private static final int ROOT_Y_POSITION = 50;

    //
    private static final int INSERTION_X_POSITION = 100;
    private static final int INSERTION_Y_POSITION = 50;

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
            insertIntoTree(vertexToInsert);
        }
    }

    private AVLVertex insertRootVertex(int vertexToInsert) {
        AVLVertex parentVertex = createVertexAt(vertexToInsert, ROOT_X_POSITION, ROOT_Y_POSITION);
        addLeafChildren(parentVertex);

        return parentVertex;
    }

    private void insertIntoTree(int vertexToInsert) {
        AVLVertex newVertex = createVertexAt(vertexToInsert, INSERTION_X_POSITION, INSERTION_Y_POSITION);
        repaintwait();

        AVLVertex leafVertexToReplace = findInsertionLocation(vertexToInsert);
        replaceLeafVertex(newVertex, leafVertexToReplace);
        addLeafChildren(newVertex);
        removeAny(highlightCircle);
    }

    private void replaceLeafVertex(AVLVertex newVertex, AVLVertex leafVertexToReplace) {
        positionVertex(newVertex, leafVertexToReplace);
        AVLVertex parent = leafVertexToReplace.parent;

        // Replace the leaf vertex with the new vertex
        // by adopting new vertex as child
        if (leafVertexToReplace.isLeftChild()) {
            addLeftChild(parent, newVertex);
        } else {
            addRightChild(parent, newVertex);
        }

        removeLink(parent.graphicVertex, leafVertexToReplace.graphicVertex);
        removeAny(leafVertexToReplace.graphicVertex);
    }


    private void positionVertex(AVLVertex newVertex, AVLVertex leafVertexToReplace) {
        GElement newGraphicVertex = newVertex.graphicVertex;
        GElement leafGraphicVertex = leafVertexToReplace.graphicVertex;

        AnimatePath(newGraphicVertex, newGraphicVertex.getPosition(), leafGraphicVertex.getPosition(), 40);
    }

    private AVLVertex createVertexAt(int vertexToInsert, int vertexXPosition, int vertexYPosition) {
        GElement graphicVertex = createCircle(String.valueOf(vertexToInsert), vertexXPosition, vertexYPosition);
        AVLVertex parentVertex = AVLVertex.newVertex(vertexToInsert, graphicVertex);

        return parentVertex;
    }

    private void addLeafChildren(AVLVertex parentVertex) {
        int parentXPosition = (int) parentVertex.graphicVertex.getPositionX();
        int parentYPosition = (int) parentVertex.graphicVertex.getPositionY();

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
        addGraphicEdge(parent, leftChild);
    }

    private void addRightChild(AVLVertex parent, AVLVertex rightChild) {
        parent.rightChild = rightChild;
        rightChild.parent = parent;
        addGraphicEdge(parent, rightChild);
    }

    private void addGraphicEdge(AVLVertex parent, AVLVertex child) {
        DSShapeLink edge;

        //If the child is a leaf vertex , edge should be anchored to the top of square
        if (child.isLeafVertex()) {
            edge = createLink(parent.graphicVertex, child.graphicVertex, GLink.SHAPE_ARC, GElement.ANCHOR_CENTER, GElement.ANCHOR_TOP, "", 1);
        } else {
            // Otherwise anchor the edge centrally
            edge = createLink(parent.graphicVertex, child.graphicVertex, GLink.SHAPE_ARC, GElement.ANCHOR_CENTER, GElement.ANCHOR_CENTER, "", 1);
        }

        edge.setArrowVisible(false);
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
            return u;
        }
    }

    private void animateVertexVisit(AVLVertex u) {
        AnimatePath(highlightCircle, highlightCircle.getPosition(), u.graphicVertex.getPosition(), 40);
        repaintwait();
    }

    private void setHighlightCircleAtRoot() {
        highlightCircle = createCircle("", root.graphicVertex.getPositionX(), root.graphicVertex.getPositionY());
        highlightCircle.setOutlineColor(Constants.ANDROID_RED);
        repaintwait();
    }


}
