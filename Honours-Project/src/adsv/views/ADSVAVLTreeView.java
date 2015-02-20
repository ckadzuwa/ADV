package adsv.views;

import adsv.avlTreeModel.AVLVertex;
import adsv.globals.GenericFunctions;
import edu.usfca.ds.shapes.DSShapeLink;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.*;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ADSVAVLTreeView extends DSView {

    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int FIND = 3;

    // The width/height difference between two levels in the tree for insertion
    //private static final int WIDTH_DELTA = 20;
    private static final int WIDTH_DELTA = 50;
    private static final int HEIGHT_DELTA = 100;

    private static final int INSERTION_X_POSITION = 100;
    private static final int INSERTION_Y_POSITION = 50;

    //Length for a square leaf vertex
    private static final int LEAF_LENGTH = 20;

    private static final int STEPS = 40;

    AVLVertex root;
    private GElement highlightCircle;
    private GElement draggingCircle;
    private DSShapeLink rotatingEdge;

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
            addLeafChildren(root);
        } else {
            AVLVertex vertexInserted = insertIntoTree(vertexToInsert);
            addLeafChildren(vertexInserted);
            updateHeightsOnInsertionPath(vertexInserted);
            checkTreeBalanced(vertexInserted);
        }
    }

    private void updateHeightsOnInsertionPath(AVLVertex vertex) {
        vertex.height = childOfGreaterHeight(vertex.leftChild, vertex.rightChild).height + 1;
        vertex.label.setLabelColor(Color.RED);
        repaintwait();
        vertex.label.setLabel(String.valueOf(vertex.height));
        repaintwait();
        vertex.label.setLabelColor(Color.BLUE);
        repaintwait();

        if (vertex.hasParent()) {
            updateHeightsOnInsertionPath(vertex.parent);
        }
    }

    private void checkTreeBalanced(AVLVertex vertex) {
        animateVertexVisit(vertex);

        if (isVertexBalanced(vertex)) {
            if (vertex.hasParent()) {
                checkTreeBalanced(vertex.parent);
            }
        } else {
            removeHighlightCircle();
            repaintwait();
            balanceTree(vertex);
        }

        removeHighlightCircle();
    }

    private void balanceTree(AVLVertex vertexZ) {
        AVLVertex vertexY = childOfGreaterHeight(vertexZ.leftChild, vertexZ.rightChild);
        AVLVertex vertexX = childOfGreaterHeight(vertexY.leftChild, vertexY.rightChild);

        boolean bothLeftChildren = vertexY.isLeftChild() == vertexX.isLeftChild();
        boolean bothRightChildren = vertexX.isRightChild() == vertexY.isRightChild();

        if (bothLeftChildren || bothRightChildren) {
            performSingleRotation(vertexZ, vertexY, vertexX);
        } else {
            performDoubleRotation(vertexZ, vertexY, vertexX);
        }

    }

    private void performDoubleRotation(AVLVertex vertexZ, AVLVertex vertexY, AVLVertex vertexX) {
        AVLVertex subtreeX = getSibling(vertexX);
        AVLVertex subtreeY = getSibling(vertexY);
        AVLVertex subtreeV = vertexX.leftChild;
        AVLVertex subtreeW = vertexX.rightChild;

        labelVertices(vertexZ, vertexY, vertexX);
        labelAndColourSubtrees(subtreeV, subtreeW, subtreeX, subtreeY);

        //Rotation 1
        boolean vertexXWasLeftChild = vertexX.isLeftChild();
        boolean vertexYWasLeftChild = vertexY.isLeftChild();
        if (vertexXWasLeftChild) {
            prepareEdgeRotation(subtreeW, vertexX, vertexY, vertexXWasLeftChild);
        } else {
            prepareEdgeRotation(subtreeV, vertexX, vertexY, vertexXWasLeftChild);
        }
        repaintwait();
        balanceVertexZ(vertexX, vertexXWasLeftChild, vertexY, vertexYWasLeftChild);
        repaintwait();

        //Rotation 2
        vertexXWasLeftChild = vertexX.isLeftChild();
        boolean vertexZWasLeftChild = vertexZ.isLeftChild();
        if (vertexXWasLeftChild) {
            prepareEdgeRotation(subtreeW, vertexX, vertexZ, vertexXWasLeftChild);
        } else {
            prepareEdgeRotation(subtreeV, vertexX, vertexZ, vertexXWasLeftChild);
        }

        repaintwait();
        balanceVertexZ(vertexX, vertexYWasLeftChild, vertexZ, vertexZWasLeftChild);
        repaintwait();

        blankVertexLabels(root);
        repaintwait();
        redrawHeights(root);
    }

    private void labelVertices(AVLVertex vertexZ, AVLVertex vertexY, AVLVertex vertexX) {
        blankVertexLabels(root);
        repaintwait();
        vertexZ.label.setLabel("z");
        repaintwait();
        vertexY.label.setLabel("y");
        repaintwait();
        vertexX.label.setLabel("x");
        repaintwait();
    }

    private void blankVertexLabels(AVLVertex vertex) {
        vertex.label.setLabel("");
        vertex.graphicVertex.setFillColor(null);
        if (!vertex.isLeafVertex()) {
            blankVertexLabels(vertex.leftChild);
            blankVertexLabels(vertex.rightChild);
        }
    }

    private void performSingleRotation(AVLVertex vertexZ, AVLVertex vertexY, AVLVertex vertexX) {
        AVLVertex subtreeX = getSibling(vertexX);
        AVLVertex subtreeY = getSibling(vertexY);
        AVLVertex subtreeV = vertexX.leftChild;
        AVLVertex subtreeW = vertexX.rightChild;

        labelVertices(vertexZ, vertexY, vertexX);
        labelAndColourSubtrees(subtreeV, subtreeW, subtreeX, subtreeY);

        boolean vertexYWasLeftChild = vertexY.isLeftChild();
        boolean vertexZWasLeftChild = vertexZ.isLeftChild();
        prepareEdgeRotation(subtreeX, vertexY, vertexZ, vertexYWasLeftChild);
        balanceVertexZ(vertexY, vertexYWasLeftChild, vertexZ, vertexZWasLeftChild);
        repaintwait();

        blankVertexLabels(root);
        repaintwait();
        redrawHeights(root);
    }

    private void redrawHeights(AVLVertex vertex) {
        vertex.label.setLabel(String.valueOf(vertex.height));
        if (!vertex.isLeafVertex()) {
            redrawHeights(vertex.leftChild);
            redrawHeights(vertex.rightChild);
        }

    }

    private void adoptLeftChild(AVLVertex parent, AVLVertex child) {
        parent.leftChild = child;
        child.parent = parent;
    }

    private void adoptRightChild(AVLVertex parent, AVLVertex child) {
        parent.rightChild = child;
        child.parent = parent;
    }

    private void balanceVertexZ(AVLVertex vertexY, boolean vertexYWasLeftChild, AVLVertex vertexZ, boolean vertexZWasLeftChild) {

        if (vertexZ == root) {
            root = vertexY;
            vertexY.depth = 0;
            vertexY.rowIndex = 1;
            vertexY.parent = null;
        } else {

            if (vertexZWasLeftChild) {
                adoptLeftChild(vertexZ.parent, vertexY);
            } else {
                adoptRightChild(vertexZ.parent, vertexY);
            }

            DSShapeLink edge = (DSShapeLink) getLink(vertexZ.parent.graphicVertex, vertexZ.graphicVertex);
            edge.setTarget(vertexY.graphicVertex);
        }

        if (vertexYWasLeftChild) {
            adoptRightChild(vertexY, vertexZ);
        } else {
            adoptLeftChild(vertexY, vertexZ);
        }
        DSShapeLink edge = (DSShapeLink) getLink(vertexZ.graphicVertex, vertexY.graphicVertex);
        edge.setSource(vertexY.graphicVertex);
        edge.setTarget(vertexZ.graphicVertex);

        // Update tree model to reflect changes
        // due to rotation
        updateTreeModel(root);
        animateTreeRotation(vertexZ);
    }

    private void animateTreeRotation(AVLVertex vertexZ) {
        AtomicInteger graphicElementIdStamper = new AtomicInteger();
        AtomicInteger pathIdStamper = new AtomicInteger();

        LinkedHashMap<Integer, GElement> idToGraphicElement = new LinkedHashMap<>();
        LinkedHashMap<Integer, Vector2D[]> idToPath = new LinkedHashMap<>();

        idToGraphicElement.put(graphicElementIdStamper.incrementAndGet(), draggingCircle);
        Vector2D finalEdgePosition = new Vector2D(getVertexTreeXPosition(vertexZ), getVertexTreeYPosition(vertexZ));
        idToPath.put(pathIdStamper.incrementAndGet(), createPath(draggingCircle.getPosition(), finalEdgePosition, STEPS));

        stampGraphicElements(graphicElementIdStamper, idToGraphicElement);
        computeMovementPaths(pathIdStamper, idToPath);

        for (int i = 0; i < STEPS; i++) {
            for (Integer id : idToGraphicElement.keySet()) {
                GElement graphicElement = idToGraphicElement.get(id);
                Vector2D path[] = idToPath.get(id);
                graphicElement.moveToPosition(path[i]);
            }
            repaintwaitmin();
        }

        rotatingEdge.setSource(vertexZ.graphicVertex);
    }

    private void computeMovementPaths(AtomicInteger idStamper, LinkedHashMap<Integer, Vector2D[]> idToPath) {
        computePath(root, idStamper, idToPath);
    }

    private void computePath(AVLVertex vertex, AtomicInteger idStamper, LinkedHashMap<Integer, Vector2D[]> idToPath) {
        double x = getVertexTreeXPosition(vertex);
        double y = getVertexTreeYPosition(vertex);
        Vector2D vertexTargetPosition = new Vector2D(x, y);
        Vector2D[] vertexPath = createPath(vertex.graphicVertex.getPosition(), vertexTargetPosition, STEPS);

        idToPath.put(idStamper.incrementAndGet(), vertexPath);


        Vector2D labelTargetPosition = getLabelPosition(vertex, vertexTargetPosition);
        Vector2D[] labelPath = createPath(vertex.label.getPosition(), labelTargetPosition, STEPS);
        idToPath.put(idStamper.incrementAndGet(), labelPath);


        if (!vertex.isLeafVertex()) {
            computePath(vertex.leftChild, idStamper, idToPath);
            computePath(vertex.rightChild, idStamper, idToPath);
        }
    }

    private void stampGraphicElements(AtomicInteger idStamper, LinkedHashMap<Integer, GElement> idToGraphicElement) {
        assignId(root, idStamper, idToGraphicElement);
    }

    private void assignId(AVLVertex vertex, AtomicInteger idStamper, LinkedHashMap<Integer, GElement> idToGraphicElement) {
        idToGraphicElement.put(idStamper.incrementAndGet(), vertex.graphicVertex);
        idToGraphicElement.put(idStamper.incrementAndGet(), vertex.label);

        if (!vertex.isLeafVertex()) {
            assignId(vertex.leftChild, idStamper, idToGraphicElement);
            assignId(vertex.rightChild, idStamper, idToGraphicElement);
        }
    }

    private void updateTreeModel(AVLVertex vertex) {

        if (!vertex.isLeafVertex()) {
            setChildDepth(vertex.leftChild, vertex);
            setChildDepth(vertex.rightChild, vertex);

            setLeftChildRowIndex(vertex.leftChild, vertex);
            setRightChildRowIndex(vertex.rightChild, vertex);

            vertex.height = heightOf(vertex);

            updateTreeModel(vertex.leftChild);
            updateTreeModel(vertex.rightChild);
        }

    }

    private int heightOf(AVLVertex vertex) {
        if (vertex.isLeafVertex()) {
            return 0;
        } else {
            return 1 + Math.max(heightOf(vertex.leftChild), heightOf(vertex.rightChild));
        }
    }

    private void prepareEdgeRotation(AVLVertex subtreeX, AVLVertex vertexY, AVLVertex vertexZ, boolean vertexYWasLeftChild) {
        if (vertexYWasLeftChild) {
            adoptLeftChild(vertexZ, subtreeX);
        } else {
            adoptRightChild(vertexZ, subtreeX);
        }

        setUpEdgeRotation(vertexY, subtreeX);
    }

    private void setUpEdgeRotation(AVLVertex vertexY, AVLVertex subtreeX) {
        rotatingEdge = (DSShapeLink) getLink(vertexY.graphicVertex, subtreeX.graphicVertex);
        draggingCircle = createCircle("", vertexY.graphicVertex.getPositionX(), vertexY.graphicVertex.getPositionY());
        draggingCircle.setElementVisible(false);
        rotatingEdge.setSource(draggingCircle);
    }

    private void labelAndColourSubtrees(AVLVertex subtreeV, AVLVertex subtreeW, AVLVertex subtreeX, AVLVertex subtreeY) {
        colourSubtree(subtreeV, Color.RED);
        subtreeV.label.setLabel("V");
        repaintwait();
        colourSubtree(subtreeW, Color.GREEN);
        subtreeW.label.setLabel("W");
        repaintwait();
        colourSubtree(subtreeX, Color.BLUE);
        subtreeX.label.setLabel("X");
        repaintwait();
        colourSubtree(subtreeY, Color.YELLOW);
        subtreeY.label.setLabel("Y");
        repaintwait();
    }

    private void colourSubtree(AVLVertex subtree, Color color) {
        subtree.graphicVertex.setFillColor(color);

        if (!subtree.isLeafVertex()) {
            colourSubtree(subtree.leftChild, color);
            colourSubtree(subtree.rightChild, color);
        }
    }

    private AVLVertex getSibling(AVLVertex vertex) {
        if (vertex.isLeftChild()) {
            return vertex.parent.rightChild;
        } else {
            return vertex.parent.leftChild;
        }
    }

    private AVLVertex childOfGreaterHeight(AVLVertex leftChild, AVLVertex rightChild) {
        if (leftChild.height >= rightChild.height) {
            return leftChild;
        } else {
            return rightChild;
        }
    }

    private boolean isVertexBalanced(AVLVertex vertex) {
        int leftChildHeight = vertex.leftChild.height;
        int rightChildHeight = vertex.rightChild.height;
        int heightDifference = Math.abs(leftChildHeight - rightChildHeight);

        return heightDifference <= 1;
    }

    private AVLVertex insertRootVertex(int vertexToInsert) {
        AVLVertex rootVertex = AVLVertex.newVertex(vertexToInsert);
        rootVertex.rowIndex = 1;
        setVertexAtTreePosition(rootVertex);
        setVertexLabel(rootVertex);

        return rootVertex;
    }

    private double getVertexTreeXPosition(AVLVertex vertex) {
        double canvasWidth = getRealSize().getWidth();
        return (canvasWidth * vertex.rowIndex) / (Math.pow(2, vertex.depth) + 1);
    }

    private double getVertexTreeYPosition(AVLVertex vertex) {
        return (vertex.depth + 1) * HEIGHT_DELTA;
    }

    private AVLVertex insertIntoTree(int vertexToInsert) {
        AVLVertex newVertex = AVLVertex.newVertex(vertexToInsert);
        setVertexAtAbsolutePosition(newVertex, INSERTION_X_POSITION, INSERTION_Y_POSITION);
        repaintwait();

        AVLVertex leafVertexToReplace = findInsertionLocation(vertexToInsert);
        replaceLeafVertex(newVertex, leafVertexToReplace);
        setVertexLabel(newVertex);

        return newVertex;
    }

    private void setVertexAtAbsolutePosition(AVLVertex newVertex, int insertionXPosition, int insertionYPosition) {
        GElement graphicVertex = createCircle(String.valueOf(newVertex.value), insertionXPosition, insertionYPosition);
        newVertex.graphicVertex = graphicVertex;
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

        addGraphicEdge(parent, newVertex);
        removeLink(parent.graphicVertex, leafVertexToReplace.graphicVertex);
        removeAny(leafVertexToReplace.graphicVertex);
        removeAny(leafVertexToReplace.label);
    }

    private void positionVertex(AVLVertex newVertex, AVLVertex leafVertexToReplace) {
        GElement newGraphicVertex = newVertex.graphicVertex;
        GElement leafGraphicVertex = leafVertexToReplace.graphicVertex;

        AnimatePath(newGraphicVertex, newGraphicVertex.getPosition(), leafGraphicVertex.getPosition(), STEPS);
    }

    private void setVertexAtTreePosition(AVLVertex vertex) {
        double vertexXPosition = getVertexTreeXPosition(vertex);
        double vertexYPosition = getVertexTreeYPosition(vertex);

        GElement graphicVertex = createCircle(String.valueOf(vertex.value), vertexXPosition, vertexYPosition);
        vertex.graphicVertex = graphicVertex;
    }

    private void setVertexLabel(AVLVertex vertex) {
        Vector2D labelPosition = getLabelPosition(vertex, vertex.graphicVertex.getPosition());
        GElementLabel graphicLabel = createLabel(String.valueOf(vertex.height), labelPosition.x, labelPosition.y);
        graphicLabel.setLabelColor(Color.BLUE);
        vertex.label = graphicLabel;
    }

    private Vector2D getLabelPosition(AVLVertex vertex, Vector2D assumedPosition) {
        int sign = -1;
        if (vertex.isRightChild()) {
            sign = 1;
        }

        GElement graphicVertex = vertex.graphicVertex;
        double length;
        if (vertex.isLeafVertex()) {
            length = ((GElementRect) graphicVertex).getWidth() / 2;
        } else {
            length = ((GElementCircle) graphicVertex).getRadius();
        }

        double labelXPosition = assumedPosition.x + (sign * length);
        double labelYPosition = assumedPosition.y - length - 10;

        return new Vector2D(labelXPosition, labelYPosition);
    }

    private void setLeafVertexAtTreePosition(AVLVertex vertex) {
        double vertexXPosition = getVertexTreeXPosition(vertex);
        double vertexYPosition = getVertexTreeYPosition(vertex);

        GElement graphicVertex = createRectangle("", vertexXPosition, vertexYPosition, LEAF_LENGTH, LEAF_LENGTH);
        vertex.graphicVertex = graphicVertex;
    }

    private void addLeafChildren(AVLVertex parentVertex) {
        AVLVertex leftLeafVertex = AVLVertex.newLeafVertex();
        AVLVertex rightLeafVertex = AVLVertex.newLeafVertex();

        addLeftChild(parentVertex, leftLeafVertex);
        addRightChild(parentVertex, rightLeafVertex);

        setLeafVertexAtTreePosition(leftLeafVertex);
        setLeafVertexAtTreePosition(rightLeafVertex);
        setVertexLabel(leftLeafVertex);
        setVertexLabel(rightLeafVertex);

        addGraphicEdge(parentVertex, leftLeafVertex);
        addGraphicEdge(parentVertex, rightLeafVertex);
    }

    private void addLeftChild(AVLVertex parent, AVLVertex leftChild) {
        parent.leftChild = leftChild;
        leftChild.parent = parent;
        setChildDepth(leftChild, parent);
        setLeftChildRowIndex(leftChild, parent);
    }

    private void setLeftChildRowIndex(AVLVertex leftChild, AVLVertex parent) {
        leftChild.rowIndex = parent.rowIndex * 2 - 1;
    }

    private void setChildDepth(AVLVertex child, AVLVertex parent) {
        child.depth = parent.depth + 1;
    }

    private void addRightChild(AVLVertex parent, AVLVertex rightChild) {
        parent.rightChild = rightChild;
        rightChild.parent = parent;
        setChildDepth(rightChild, parent);
        setRightChildRowIndex(rightChild, parent);
    }

    private void setRightChildRowIndex(AVLVertex rightChild, AVLVertex parent) {
        rightChild.rowIndex = parent.rowIndex * 2;
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
            repaintwait();
            removeHighlightCircle();
            return u;
        }
    }

    private void removeHighlightCircle() {
        removeAny(highlightCircle);
        highlightCircle = null;
    }

    private void animateVertexVisit(AVLVertex u) {
        if (highlightCircle == null) {
            setHighlightCircleAtVertex(u);
            repaintwait();
        } else {
            AnimatePath(highlightCircle, highlightCircle.getPosition(), u.graphicVertex.getPosition(), STEPS);
            repaintwait();
        }
    }

    private void setHighlightCircleAtRoot() {
        setHighlightCircleAtVertex(root);
        repaintwait();
    }

    private void setHighlightCircleAtVertex(AVLVertex vertex) {
        highlightCircle = createCircle("", vertex.graphicVertex.getPositionX(), vertex.graphicVertex.getPositionY());
        highlightCircle.setOutlineColor(Color.RED);
    }

}
