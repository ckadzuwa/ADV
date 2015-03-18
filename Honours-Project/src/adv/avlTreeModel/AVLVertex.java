package adv.avlTreeModel;

import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GElementLabel;

import java.security.InvalidParameterException;

/**
 * Created by CK on 08/01/2015.
 */
public class AVLVertex {

    public Integer value;
    public AVLVertex parent;
    public AVLVertex leftChild;
    public AVLVertex rightChild;
    public Integer height;
    public Integer depth;
    public Integer rowIndex;

    public GElement graphicVertex; //Circle (internal vertex) , Square (leaf vertex)
    public GElementLabel label;

    public static AVLVertex newVertex(int value) {
        AVLVertex newVertex = new AVLVertex();
        newVertex.value = value;
        newVertex.height = 1;

        return newVertex;
    }

    public static AVLVertex newLeafVertex() {
        AVLVertex leafVertex = new AVLVertex();
        leafVertex.value = null;
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
        return value == null;
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
        this.value = -1;
        graphicVertex.setLabel("");

    }

    public void setValue(Integer value) {

        if (value >= 0) {
            this.value = value;
            graphicVertex.setLabel(String.valueOf(value));
        } else {
            throw new InvalidParameterException();
        }

    }
}