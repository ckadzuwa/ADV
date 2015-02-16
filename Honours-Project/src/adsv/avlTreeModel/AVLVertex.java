package adsv.avlTreeModel;

import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GElementLabel;

/**
 * Created by CK on 08/01/2015.
 */
public class AVLVertex {

    public Integer value;
    public AVLVertex parent;
    public AVLVertex leftChild;
    public AVLVertex rightChild;
    public int height;
    public int depth;
    public int rowIndex;

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

    public boolean hasParent() {
        return parent != null;
    }

    public boolean isLeafVertex() {
        return value == null;
    }

    public boolean isInternalVertex() {
        return !isLeafVertex();
    }


    public void setPosition(int x, int y) {
        this.graphicVertex.setPosition(x, y);
    }

    public boolean isLeftChild() {
        return hasParent() && parent.leftChild == this;
    }

    public boolean isRightChild() {
        return hasParent() && parent.rightChild == this;
    }


}
