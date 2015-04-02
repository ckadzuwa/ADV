package adv.avlTreeModel;

import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GElementLabel;

import java.security.InvalidParameterException;

// Bookkeeping structure used to model AVL tree
public class AVLTreeVertex {

    public Integer key;
    public AVLTreeVertex parent;
    public AVLTreeVertex leftChild;
    public AVLTreeVertex rightChild;
    public Integer height;
    public Integer depth;
    public Integer rowIndex; // Used to store the location of a vertex in a row of the tree

    public GElement graphicVertex; //Circle (internal vertex) , Square (leaf vertex)
    public GElementLabel label; // Used for the height of a vertex

    public static AVLTreeVertex newVertex(int value) {
        AVLTreeVertex newVertex = new AVLTreeVertex();
        newVertex.key = value;
        newVertex.height = 1;

        return newVertex;
    }

    public static AVLTreeVertex newLeafVertex() {
        AVLTreeVertex leafVertex = new AVLTreeVertex();
        leafVertex.key = null;
        leafVertex.leftChild = null;
        leafVertex.rightChild = null;
        leafVertex.height = 0;

        return leafVertex;
    }

    public boolean hasTwoLeafChildren() {
        return isInternalVertex() && leftChild.isLeafVertex() && rightChild.isLeafVertex();
    }

    public boolean isInternalVertex() {
        return !isLeafVertex();
    }

    public boolean isLeafVertex() {
        return key == null;
    }

    public boolean isRightLeafVertex() {
        return isLeafVertex() && isRightChild();
    }

    public boolean isLeftLeafVertex() {
        return isLeafVertex() && isLeftChild();
    }

    public boolean rightChildIsInternalVertex() {
        return isInternalVertex() && rightChild.isInternalVertex();
    }

    public boolean leftChildIsInternalVertex() {
        return isInternalVertex() && leftChild.isInternalVertex();
    }

    public void setPosition(int x, int y) {
        this.graphicVertex.setPosition(x, y);
    }

    public boolean isLeftChild() {
        return hasParent() && parent.leftChild == this;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean isRightChild() {
        return hasParent() && parent.rightChild == this;
    }

    public void removeValue() {

        // Don't set to null since
        // vertex then becomes a leaf vertex
        this.key = -1;
        graphicVertex.setLabel("");

    }

    public void setKey(Integer key) {

        if (key >= 0) {
            this.key = key;
            graphicVertex.setLabel(String.valueOf(key));
        } else {
            throw new InvalidParameterException();
        }

    }
}
