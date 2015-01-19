package adsv.avlTreeModel;

import edu.usfca.xj.appkit.gview.object.GElement;

/**
 * Created by CK on 08/01/2015.
 */
public class AVLVertex {

    public int value;
    public AVLVertex parent;
    public AVLVertex leftChild;
    public AVLVertex rightChild;
    public int height;

    public GElement graphicalVertex; //Circle (internal vertex) , Square (leaf vertex)

    public static AVLVertex newVertex(int value, GElement graphicalRoot) {
        AVLVertex newVertex = new AVLVertex();
        newVertex.value = value;
        newVertex.height = 1;
        newVertex.graphicalVertex = graphicalRoot;

        return newVertex;
    }

    public static AVLVertex newLeafVertex(GElement graphicalLeaf) {
        AVLVertex leafVertex = new AVLVertex();
        leafVertex.leftChild = null;
        leafVertex.rightChild = null;
        leafVertex.height = 0;
        leafVertex.graphicalVertex = graphicalLeaf;

        return leafVertex;
    }


    public boolean isLeafVertex() {
        return (leftChild == null && rightChild == null) && parent != null ;
    }

    public boolean isInternalVertex() {
        return (leftChild != null && rightChild != null) && parent != null ;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void setPosition(int x, int y) {
        this.graphicalVertex.setPosition(x, y);
    }

    public boolean isLeftChild() {
        return (this.isLeafVertex() || this.isInternalVertex()) && parent.leftChild == this;
    }

    public boolean isRightChild() {
        return (this.isLeafVertex() || this.isInternalVertex()) && parent.rightChild == this;
    }



}
