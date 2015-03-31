package adv.views;

import adv.avlTreeModel.AVLVertex;
import adv.utility.InputConstraints;
import edu.usfca.ds.shapes.DSShapeLink;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.*;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AVLTreeView extends View {

    // Algorithmic operations
    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int FIND = 3;

    // Single rotation cases
    private static final int CLOCKWISE = 4;
    private static final int ANTI_CLOCKWISE = 5;

    // Double rotation cases
    private static final int CLOCKWISE_ANTI_CLOCKWISE = 6;
    private static final int ANTI_CLOCKWISE_CLOCKWISE = 7;

    private static int rotationCurrentlyPerforming;

    // The width/height difference between two levels in the tree for insertion
    //private static final int WIDTH_DELTA = 20;
    private static final int WIDTH_DELTA = 50;
    private static final int HEIGHT_DELTA = 100;
    private static final int INSERTION_X_POSITION = 100;
    private static final int INSERTION_Y_POSITION = 50;

    //Colours for subtrees
    private static final Color SUBTREE_V_COLOR = Color.YELLOW;
    private static final Color SUBTREE_W_COLOR = Color.decode("#4FC3F7");
    private static final Color SUBTREE_X_COLOR = Color.decode("#FFB74D");
    private static final Color SUBTREE_Y_COLOR = Color.LIGHT_GRAY;




    //Length for a square leaf vertex
    private static final int LEAF_LENGTH = 20;

    private static final int STEPS = 40;

    AVLVertex root;
    private GElementCircle highlightCircle;
    private GElement draggingCircle;
    private DSShapeLink rotatingEdge;
    private GElementRect highlightLine;

    public AVLTreeView() {
        super();
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
        if (InputConstraints.isValidNumber(list)) {
            insertVertex(Integer.parseInt(list));
        } else {
            //Otherwise , if the list is composed of several numbers
            if (InputConstraints.isValidNumberList(list)) {
                String[] numbers = list.split(",");

                for (int i = 0; i < numbers.length; i++) {
                    insertVertex(Integer.parseInt(numbers[i]));
                    addOneSecondDelay();
                }
            }
        }

    }

    private void addTwoSecondDelay() {
        if (!skipAnimation) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void addOneSecondDelay() {
        if (!skipAnimation) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void addHalfSecondDelay() {
        if (!skipAnimation) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertVertex(int vertexToInsert) {
        //If the tree is empty
        if (root == null) {
            displayMessage("Insert " + vertexToInsert + " as the root vertex.");
            repaintwait();
            root = insertRootVertex(vertexToInsert);
            addLeafChildren(root);
            repaint();
        } else {
            AVLVertex vertexInserted = insertIntoTree(vertexToInsert);
            addLeafChildren(vertexInserted);

            addTwoSecondDelay();
            updateHeightsOnPath(vertexInserted.parent);

            addTwoSecondDelay();
            displayMessage("Check if any vertex on the insertion path is unbalanced.");
            repaintwait();
            checkTreeBalanced(vertexInserted.parent);
        }
    }

    // Update the heights on the insertion/deletion path
    private void updateHeightsOnPath(AVLVertex vertex) {

        int heightBefore = vertex.height;
        vertex.height = heightOf(vertex);
        int heightAfter = vertex.height;

        displayMessage("Check if vertex " + vertex.key + "'s height needs an update.");
        setHighlightLineAt(vertex.label);
        repaintwait();


        if (heightBefore != heightAfter) {
            displayMessage("Update vertex " + vertex.key + "'s height from " + heightBefore + " to " + heightAfter + ".");
        } else {
            displayMessage("Vertex " + vertex.key + "'s height unchanged.");
        }

        vertex.label.setLabel(String.valueOf(vertex.height));
        repaintwait();


        if (vertex.hasParent()) {
            updateHeightsOnPath(vertex.parent);
        }

        removeHighlightLine();
        repaint();
    }

    private void removeHighlightLine() {
        removeAny(highlightLine);
        highlightLine = null;
    }

    private void setHighlightLineAt(GElementLabel heightLabel) {

        double heightLabelWidth = heightLabel.getFrame().rectangle().getWidth();

        if (highlightLine == null) {
            highlightLine = createRectangle("", heightLabel.getPositionX(), heightLabel.getPositionY() + heightLabelWidth + 2, heightLabelWidth, 0.05);
            highlightLine.setOutlineColor(Color.BLUE);
        } else {
            highlightLine.setPosition(heightLabel.getPositionX(), heightLabel.getPositionY() + heightLabelWidth + 2);
        }

    }

    private void checkTreeBalanced(AVLVertex vertex) {
        animateVertexVisit(vertex);

        if (isVertexBalanced(vertex)) {
            if (vertex.hasParent()) {
                displayMessage("Vertex " + vertex.key + " is balanced, checking parent.");
                repaintwait();
                checkTreeBalanced(vertex.parent);
            } else {
                displayMessage("Vertex " + vertex.key + " is balanced.");
                repaintwait();
            }
        } else {
            displayMessage("Vertex " + vertex.key + " is unbalanced!");
            repaintwait();

            //removeHighlightCircle();
            balanceTree(vertex);

        }


        displayMessage("The entire tree is balanced.");
        repaintwait();
        removeHighlightCircle();
    }

    private void balanceTree(AVLVertex vertexZ) {
        AVLVertex vertexY = childOfGreaterHeight(vertexZ.leftChild, vertexZ.rightChild);
        AVLVertex vertexX = childOfGreaterHeight(vertexY.leftChild, vertexY.rightChild);

        boolean leftLeftCase = (vertexY.isLeftChild() && vertexX.isLeftChild());
        boolean rightRightCase = (vertexX.isRightChild() && vertexY.isRightChild());


        if (leftLeftCase) {
            rotationCurrentlyPerforming = CLOCKWISE;
            performSingleRotation(vertexZ, vertexY, vertexX);
        } else if (rightRightCase) {
            rotationCurrentlyPerforming = ANTI_CLOCKWISE;
            performSingleRotation(vertexZ, vertexY, vertexX);
        } else {
            boolean leftRightCase = vertexY.isLeftChild() && vertexX.isRightChild();

            if (leftRightCase) {
                rotationCurrentlyPerforming = ANTI_CLOCKWISE_CLOCKWISE;
            } else {
                rotationCurrentlyPerforming = CLOCKWISE_ANTI_CLOCKWISE;
            }

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
        repaintwait();

        displayRotationCase();

        displayFirstRotationTypeMessage();
        singleRotationInDoubleRotation(vertexX, subtreeV, subtreeW, vertexY); // Rotation 1

        displaySecondRotationTypeMessage();
        singleRotationInDoubleRotation(vertexX, subtreeV, subtreeW, vertexZ); // Rotation 2
        resetTreeHeights();
    }

    private void displayFirstRotationTypeMessage() {
        if (rotationCurrentlyPerforming == CLOCKWISE_ANTI_CLOCKWISE) {
            displayMessage("Perform a clockwise rotation first.");
        } else {
            displayMessage(" to perform an anti-clockwise rotation first.");
        }

        repaintwait();
    }

    private void displaySecondRotationTypeMessage() {
        if (rotationCurrentlyPerforming == CLOCKWISE_ANTI_CLOCKWISE) {
            displayMessage("Finish with an anti-clockwise rotation to balance tree.");
        } else {
            displayMessage("Finish with an clockwise rotation to balance tree.");
        }

        repaintwait();
    }

    private void resetTreeHeights() {
        blankVertexLabels(root);
        repaintwait();
        redrawHeights(root);
    }

    private void singleRotationInDoubleRotation(AVLVertex vertexX, AVLVertex subtreeV, AVLVertex subtreeW, AVLVertex vertexXParent) {
        boolean vertexXWasLeftChild = vertexX.isLeftChild();
        boolean vertexYWasLeftChild = vertexXParent.isLeftChild();

        AVLVertex subtreeToMove;
        if (vertexXWasLeftChild) {
            subtreeToMove = subtreeW;
        } else {
            subtreeToMove = subtreeV;
        }

        moveSubtree(subtreeToMove, vertexX, vertexXWasLeftChild, vertexXParent);
        rotateAroundVertex(vertexX, vertexXWasLeftChild, vertexXParent, vertexYWasLeftChild);
        repaintwait();
    }

    private void labelVertices(AVLVertex vertexZ, AVLVertex vertexY, AVLVertex vertexX) {
        blankVertexLabels(root);
        repaintwait();


        displayMessage("Let "+vertexZ.key +" be unbalanced vertex z.");
        vertexZ.label.setLabel("z");
        removeHighlightCircle();
        addHalfSecondDelay();
        repaintwait();


        displayMessage("Let "+vertexY.key +" be vertex y (child of z of greater height).");
        vertexY.label.setLabel("y");
        addHalfSecondDelay();
        repaintwait();


        displayMessage("Let "+vertexX.key +" be vertex x (child of y of greater height).");
        vertexX.label.setLabel("x");
        addHalfSecondDelay();
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
        moveSubtree(subtreeX, vertexY, vertexYWasLeftChild, vertexZ);
        displayRotationCase();
        rotateAroundVertex(vertexY, vertexYWasLeftChild, vertexZ, vertexZWasLeftChild);
        repaintwait();

        resetTreeHeights();
    }

    private void displayRotationCase() {
        if (rotationCurrentlyPerforming == CLOCKWISE) {
            displayMessage("Perform a clockwise rotation to balance tree.");
        } else if (rotationCurrentlyPerforming == ANTI_CLOCKWISE){
            displayMessage("Perform an anti-clockwise rotation to balance tree.");
        } else if (rotationCurrentlyPerforming == CLOCKWISE_ANTI_CLOCKWISE) {
            displayMessage("Perform an clockwise, anti-clockwise rotation to balance tree.");
        } else {
            displayMessage("Perform an anti-clockwise, clockwise rotation to balance tree.");
        }

        repaintwait();
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

    private void rotateAroundVertex(AVLVertex rotateVertex, boolean rotateVertexWasLeftChild, AVLVertex rotateVertexParent, boolean parentWasLeftChild) {

        if (rotateVertexParent == root) {
            setAsRoot(rotateVertex);
        } else {
            adoptGrandChild(rotateVertexParent.parent, rotateVertexParent, rotateVertex, parentWasLeftChild);
        }

        moveParentDown(rotateVertex, rotateVertexWasLeftChild, rotateVertexParent);
        updateTreeModel(root); // Update tree model to reflect changes
        animateTreeMovement(rotateVertexParent, true);
    }

    //Adopt grandchild in place of parent
    private void adoptGrandChild(AVLVertex grandParent, AVLVertex parent, AVLVertex grandChild, boolean parentWasLeftChild) {

        if (parentWasLeftChild) {
            adoptLeftChild(grandParent, grandChild);
        } else {
            adoptRightChild(grandParent, grandChild);
        }

        DSShapeLink edge = (DSShapeLink) getLink(grandParent.graphicVertex, parent.graphicVertex);
        edge.setTarget(grandChild.graphicVertex);

    }

    private void moveParentDown(AVLVertex rotateVertex, boolean rotateVertexWasLeftChild, AVLVertex rotateVertexParent) {
        if (rotateVertexWasLeftChild) {
            adoptRightChild(rotateVertex, rotateVertexParent);
        } else {
            adoptLeftChild(rotateVertex, rotateVertexParent);
        }

        DSShapeLink edge = (DSShapeLink) getLink(rotateVertexParent.graphicVertex, rotateVertex.graphicVertex);
        edge.setSource(rotateVertex.graphicVertex);
        edge.setTarget(rotateVertexParent.graphicVertex);
    }

    private void setAsRoot(AVLVertex vertex) {
        root = vertex;
        vertex.depth = 0;
        vertex.rowIndex = 1;
        vertex.parent = null;
    }

    private void animateTreeMovement(AVLVertex vertex, boolean isTreeRotation) {

        // Structures to assign ids to graphic elements
        AtomicInteger graphicElementIdStamper = new AtomicInteger();
        LinkedHashMap<Integer, GElement> idToGraphicElement = new LinkedHashMap<>();

        // Structures for assigning ids for paths for graphic elements
        AtomicInteger pathIdStamper = new AtomicInteger();
        LinkedHashMap<Integer, Vector2D[]> idToPath = new LinkedHashMap<>();

        if (isTreeRotation) {
            prepareEdgeRotation(vertex, graphicElementIdStamper, idToGraphicElement, pathIdStamper, idToPath);
        }

        prepareVertexMovement(graphicElementIdStamper, idToGraphicElement, pathIdStamper, idToPath);
        animateMovement(idToGraphicElement, idToPath);

        if (isTreeRotation) {
            rotatingEdge.setSource(vertex.graphicVertex);
            removeDraggingCircle();
        }
    }

    private void removeDraggingCircle() {
        removeAny(draggingCircle);
        draggingCircle = null;
    }

    private void prepareEdgeRotation(AVLVertex vertex, AtomicInteger graphicElementIdStamper,
                                     LinkedHashMap<Integer, GElement> idToGraphicElement, AtomicInteger pathIdStamper,
                                     LinkedHashMap<Integer, Vector2D[]> idToPath) {

        idToGraphicElement.put(graphicElementIdStamper.incrementAndGet(), draggingCircle);
        Vector2D finalEdgePosition = new Vector2D(getVertexTreeXPosition(vertex), getVertexTreeYPosition(vertex));
        idToPath.put(pathIdStamper.incrementAndGet(), createPath(draggingCircle.getPosition(), finalEdgePosition, STEPS));

    }

    private void prepareVertexMovement(AtomicInteger graphicElementIdStamper, LinkedHashMap<Integer, GElement> idToGraphicElement,
                                       AtomicInteger pathIdStamper, LinkedHashMap<Integer, Vector2D[]> idToPath) {

        assignIDsToGraphicElements(graphicElementIdStamper, idToGraphicElement);
        computePathsForGraphicElements(pathIdStamper, idToPath);

    }

    private void animateMovement(LinkedHashMap<Integer, GElement> idToGraphicElement, LinkedHashMap<Integer, Vector2D[]> idToPath) {

        for (int i = 0; i < STEPS; i++) {
            for (Integer id : idToGraphicElement.keySet()) {
                GElement graphicElement = idToGraphicElement.get(id);
                Vector2D path[] = idToPath.get(id);
                graphicElement.moveToPosition(path[i]);
            }
            repaintwaitmin();
        }

    }

    private void computePathsForGraphicElements(AtomicInteger idStamper, LinkedHashMap<Integer, Vector2D[]> idToPath) {
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

    private void assignIDsToGraphicElements(AtomicInteger idStamper, LinkedHashMap<Integer, GElement> idToGraphicElement) {
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

    private void moveSubtree(AVLVertex subTreeToMove, AVLVertex formerParent, boolean formerParentWasLeftChild, AVLVertex newParent) {
        if (formerParentWasLeftChild) {
            adoptLeftChild(newParent, subTreeToMove);
        } else {
            adoptRightChild(newParent, subTreeToMove);
        }

        setUpEdgeRotation(formerParent, subTreeToMove);
    }

    private void setUpEdgeRotation(AVLVertex vertexY, AVLVertex subtreeX) {
        rotatingEdge = (DSShapeLink) getLink(vertexY.graphicVertex, subtreeX.graphicVertex);
        draggingCircle = createCircle("", vertexY.graphicVertex.getPositionX(), vertexY.graphicVertex.getPositionY());
        draggingCircle.setElementVisible(false);
        rotatingEdge.setSource(draggingCircle);
    }

    private void labelAndColourSubtrees(AVLVertex subtreeV, AVLVertex subtreeW, AVLVertex subtreeX, AVLVertex subtreeY) {
        displayMessage("V is the subtree rooted at x's left child.");
        colourSubtree(subtreeV, SUBTREE_V_COLOR);
        subtreeV.label.setLabel("V");
        addHalfSecondDelay();
        repaintwait();


        displayMessage("W is the subtree rooted at x's right child.");
        colourSubtree(subtreeW, SUBTREE_W_COLOR);
        subtreeW.label.setLabel("W");
        addHalfSecondDelay();
        repaintwait();


        displayMessage("X is the subtree rooted at x's sibling.");
        colourSubtree(subtreeX, SUBTREE_X_COLOR);
        subtreeX.label.setLabel("X");
        addHalfSecondDelay();
        repaintwait();


        displayMessage("Y is the subtree rooted at y's sibling.");
        colourSubtree(subtreeY, SUBTREE_Y_COLOR);
        subtreeY.label.setLabel("Y");
        addHalfSecondDelay();
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
        setAsRoot(rootVertex);
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
        displayMessage("Prepare to insert vertex " + vertexToInsert + " into the tree.");
        repaintwait();

        AVLVertex leafVertexToReplace = findInsertionLocation(vertexToInsert);
        displayMessage("Insert vertex " + vertexToInsert + " at leaf location.");
        repaintwait();
        positionVertex(newVertex, leafVertexToReplace);
        replaceLeafVertex(newVertex, leafVertexToReplace);
        setVertexLabel(newVertex);
        repaint();

        return newVertex;
    }

    private void setVertexAtAbsolutePosition(AVLVertex newVertex, int insertionXPosition, int insertionYPosition) {
        GElement graphicVertex = createCircle(String.valueOf(newVertex.key), insertionXPosition, insertionYPosition);
        newVertex.graphicVertex = graphicVertex;
    }

    private void replaceLeafVertex(AVLVertex newVertex, AVLVertex leafVertexToReplace) {
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
        removeHighlightCircle();
    }

    private void positionVertex(AVLVertex newVertex, AVLVertex leafVertexToReplace) {
        GElement newGraphicVertex = newVertex.graphicVertex;
        GElement leafGraphicVertex = leafVertexToReplace.graphicVertex;

        AnimatePath(newGraphicVertex, newGraphicVertex.getPosition(), leafGraphicVertex.getPosition(), STEPS);
    }

    private void setVertexAtTreePosition(AVLVertex vertex) {
        double vertexXPosition = getVertexTreeXPosition(vertex);
        double vertexYPosition = getVertexTreeYPosition(vertex);

        GElement graphicVertex = createCircle(String.valueOf(vertex.key), vertexXPosition, vertexYPosition);
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

    private void runTreeFind(String list) {

        //If the list is a single number
        if (InputConstraints.isValidNumber(list)) {
            findElement(Integer.parseInt(list), true);
        } else {
            //Otherwise , if the list is composed of several numbers
            if (InputConstraints.isValidNumberList(list)) {
                String[] numbers = list.split(",");

                for (int i = 0; i < numbers.length; i++) {
                    findElement(Integer.parseInt(numbers[i]), true);
                    if (!skipAnimation) {
                        addOneSecondDelay();
                    }
                }
            }
        }

    }

    //Returns vertex if element found , null otherwise
    private AVLVertex findElement(int k, boolean hideHighlightCircle) {

        if (root == null) {
            displayMessage("No elements found since the tree is empty!");
            repaintwait();
            return null;
        } else {
            displayMessage("Search for " + k + ", starting from root vertex " + root.key + ".");
            AVLVertex u = root;
            setHighlightCircleAtRoot();

            while (!u.isLeafVertex() && u.key != k) {

                if (k < u.key) {
                    displayMessage(k + " < " + u.key + ", go left.");
                    u = u.leftChild;
                    repaintwait();
                } else {
                    displayMessage(k + " > " + u.key + ", go right.");
                    u = u.rightChild;
                    repaintwait();
                }

                animateVertexVisit(u);
            }

            if (hideHighlightCircle) {
                removeHighlightCircle();
                repaint();
            }

            if (!u.isLeafVertex() && u.key == k) {
                displayMessage("Found vertex with key " + k + ".");
                repaintwait();
                return u;
            } else {
                displayMessage("No such element with key " + k + ".");
                repaintwait();
                return null;
            }
        }
    }

    private AVLVertex findInsertionLocation(int k) {
        if (root == null) {
            displayMessage("No elements in tree! Insert " + k + " as root vertex.");
            repaintwait();
            return null;
        } else {
            displayMessage("Find insertion location for " + k + ", starting from root vertex " + root.key + ".");
            AVLVertex u = root;
            setHighlightCircleAtRoot();

            while (!u.isLeafVertex()) {
                if (k < u.key) {
                    displayMessage(k + " < " + u.key + ", go left.");
                    u = u.leftChild;
                    repaintwait();
                } else if (k == u.key) {

                    displayMessage("Found " + k + " high up in tree.");
                    repaintwait();

                    u = findInOrderPredecessor(u);

                    // Vertex u is a leaf is we only have one
                    // vertex with k as the key
                    if (!u.isLeafVertex()) {
                        u = u.rightChild;
                    }

                } else {
                    displayMessage(k + " > " + u.key + ", go right.");
                    u = u.rightChild;
                    repaintwait();
                }
                animateVertexVisit(u);
            }
            return u;
        }
    }

    private AVLVertex appropriateLeafLocation(int k, AVLVertex u) {

        AVLVertex leafLocation;

        // If the vertex with the largest key no greater than k
        // has a smaller key than the key k
        if (u.key < k) {
            leafLocation = u.rightChild;
        } else {
            // Else if the vertex's key is equal to k
            leafLocation = u.leftChild;
        }

        return leafLocation;

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

    private void runTreeDeletion(String list) {
        //If the list is a single number
        if (InputConstraints.isValidNumber(list)) {
            deleteVertex(Integer.parseInt(list));
        } else {
            //Otherwise , if the list is composed of several numbers
            if (InputConstraints.isValidNumberList(list)) {
                String[] numbers = list.split(",");

                for (int i = 0; i < numbers.length; i++) {
                    deleteVertex(Integer.parseInt(numbers[i]));
                    addOneSecondDelay();
                }
            }
        }

    }

    private void deleteVertex(int vertex) {
        AVLVertex vertexToDelete = findElement(vertex, false);

        if (vertexToDelete == null) {
            removeHighlightCircle();
        } else {

            AVLVertex deletedVertexPlaceHolder;

            // Vertex to delete is root or internal vertex with two leaves
            if (vertexToDelete.hasTwoLeafChildren()) {
                removeHighlightCircle();
                deletedVertexPlaceHolder = simpleDeletion(vertexToDelete);
            } else {
                displayMessage("Remove "+vertexToDelete.key +"'s key.");
                vertexToDelete.removeValue();
                repaintwait();
                deletedVertexPlaceHolder = complexDeletion(vertexToDelete);
                repaintwait();
            }

            finishHandlingDeletion(deletedVertexPlaceHolder);

        }
    }

    private void finishHandlingDeletion(AVLVertex deletedVertexPlaceHolder) {
        // Null when all vertices have been removed from tree
        if (deletedVertexPlaceHolder != null) {
            addOneSecondDelay();

            updateHeightsOnPath(deletedVertexPlaceHolder.parent);
            displayMessage("Check if any vertex is unbalanced on the deletion path.");
            repaintwait();

            checkTreeBalancedAfterDeletion(deletedVertexPlaceHolder);

            displayMessage("The entire tree is balanced.");
            repaintwait();
        } else {
            displayMessage("The tree is now empty.");
            repaint();
        }
    }

    private AVLVertex complexDeletion(AVLVertex vertexToDelete) {

        AVLVertex u;

        if (vertexToDelete.leftChildIsInternalVertex()) {
            displayMessage("Use the greatest key in left subtree to replace deleted key.");
            repaintwait();
            u = findInOrderPredecessor(vertexToDelete);
        } else {
            displayMessage("Use key to the right to replace deleted key.");
            repaintwait();
            u = vertexToDelete.rightChild;
            animateVertexVisit(u);
        }

        return inheritValueFromVertexU(vertexToDelete, u);
    }

    private AVLVertex inheritValueFromVertexU(AVLVertex vertexToDelete, AVLVertex u) {


        displayMessage("Move the key "+u.key +" up to replace deleted vertex.");
        floatValueUp(vertexToDelete, u);
        repaintwait();

        if (u.hasTwoLeafChildren()) {
            AVLVertex leafVertex = AVLVertex.newLeafVertex();
            replaceInternalVertex(leafVertex, u);

            return leafVertex;
        } else {
            AVLVertex uParent = u.parent;
            AVLVertex uLeftChild = u.leftChild;
            adoptGrandChild(uParent, u, uLeftChild, u.isLeftChild());
            updateTreeModel(root);
            removePredecessorGraphicElements(u);
            animateTreeMovement(root, false); // Fix vertex positioning

            return uLeftChild;
        }

    }

    private void removePredecessorGraphicElements(AVLVertex u) {
        removeLink(u.graphicVertex, u.rightChild.graphicVertex);
        removeAny(u.graphicVertex);
        removeAny(u.label);
        removeAny(u.rightChild.graphicVertex);
        removeAny(u.rightChild.label);
        removeLink(u.graphicVertex, u.leftChild.graphicVertex);
    }

    private void floatValueUp(AVLVertex vertexToDelete, AVLVertex u) {
        String vertexValue = u.graphicVertex.getLabel();

        u.removeValue();
        GElementLabel floatingValue = createLabel(vertexValue, u.graphicVertex.getPosition());
        AnimatePath(floatingValue, floatingValue.getPosition(), vertexToDelete.graphicVertex.getPosition(), STEPS);
        removeAny(floatingValue);

        vertexToDelete.setKey(Integer.parseInt(vertexValue));

        repaintwait();
        removeHighlightCircle();
    }

    private AVLVertex findInOrderPredecessor(AVLVertex vertex) {

        displayMessage("Go left once.");
        AVLVertex u = vertex.leftChild;
        repaintwait();

        animateVertexVisit(u);

        while (u.rightChildIsInternalVertex()) {
            displayMessage("Go right until right child is a leaf.");
            u = u.rightChild;
            animateVertexVisit(u);
        }

        repaintwait();
        return u;
    }

    private AVLVertex simpleDeletion(AVLVertex vertexToDelete) {
        if (vertexToDelete == root) {
            removeGraphicalElementsForVertex(root);
            root = null;
            return null;
        } else {
            AVLVertex leafVertex = AVLVertex.newLeafVertex();
            replaceInternalVertex(leafVertex, vertexToDelete);
            return leafVertex;
        }
    }

    private void checkTreeBalancedAfterDeletion(AVLVertex u) {

        while (u != root) {
            AVLVertex z = u.parent;
            animateVertexVisit(z);

            if (!isVertexBalanced(z)) {
                displayMessage("Vertex " + z.key + " is unbalanced!");
                repaintwait();
                balanceTree(z);
            } else {
                displayMessage("Vertex " + z.key + " is balanced.");
                repaintwait();
            }

            u = u.parent;

        }

        removeHighlightCircle();
    }

    private void replaceInternalVertex(AVLVertex leafVertex, AVLVertex vertexToDelete) {
        AVLVertex parent = vertexToDelete.parent;

        // Replace the internal vertex with the leaf
        // vertex by adopting leaf vertex as child
        if (vertexToDelete.isLeftChild()) {
            addLeftChild(parent, leafVertex);
        } else {
            addRightChild(parent, leafVertex);
        }

        removeGraphicalElementsForVertex(vertexToDelete);

        setLeafVertexAtTreePosition(leafVertex);
        setVertexLabel(leafVertex);
        addGraphicEdge(parent, leafVertex);

        repaintwaitmin();
    }

    private void removeGraphicalElementsForVertex(AVLVertex vertexToDelete) {

        if (vertexToDelete.hasParent()) {
            removeLink(vertexToDelete.parent.graphicVertex, vertexToDelete.graphicVertex);
        }
        removeAny(vertexToDelete.graphicVertex);
        removeAny(vertexToDelete.label);

        removeLink(vertexToDelete.graphicVertex, vertexToDelete.leftChild.graphicVertex);
        removeAny(vertexToDelete.leftChild.graphicVertex);
        removeAny(vertexToDelete.leftChild.label);

        removeLink(vertexToDelete.graphicVertex, vertexToDelete.rightChild.graphicVertex);
        removeAny(vertexToDelete.rightChild.graphicVertex);
        removeAny(vertexToDelete.rightChild.label);

        repaintwaitmin();
    }

}
