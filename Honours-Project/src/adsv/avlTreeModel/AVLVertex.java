package adsv.avlTreeModel;

import edu.usfca.xj.appkit.gview.object.GElement;

/**
 * Created by CK on 08/01/2015.
 */
public class AVLVertex {

    public Integer value;
    public AVLVertex parent;
    public AVLVertex leftChild;
    public AVLVertex rightChild;
    public int height;

    public GElement graphicVertex; //Circle (internal vertex) , Square (leaf vertex)

    public static AVLVertex newVertex(int value, GElement graphicVertex) {
        AVLVertex newVertex = new AVLVertex();
        newVertex.value = value;
        newVertex.height = 1;
        newVertex.graphicVertex = graphicVertex;

        return newVertex;
    }

    public static AVLVertex newLeafVertex(GElement graphicalLeaf) {
        AVLVertex leafVertex = new AVLVertex();
        leafVertex.value = null;
        leafVertex.leftChild = null;
        leafVertex.rightChild = null;
        leafVertex.height = 0;
        leafVertex.graphicVertex = graphicalLeaf;

        return leafVertex;
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
        return parent != null && parent.leftChild == this;
    }

    public boolean isRightChild() {
        return parent != null && parent.rightChild == this;
    }


}
