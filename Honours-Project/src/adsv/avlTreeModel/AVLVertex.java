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

    public static AVLVertex newRootVertex(int value, GElement graphicalRoot, GElement leftLeafChild, GElement rightLeafChild) {
        AVLVertex root = new AVLVertex();
        root.value = value;
        root.parent = null;
        root.leftChild = AVLVertex.newLeafVertex(root, leftLeafChild);
        root.rightChild = AVLVertex.newLeafVertex(root, rightLeafChild);
        root.height = 1;
        root.graphicalVertex = graphicalRoot;

        return root;
    }

    public static AVLVertex newLeafVertex(AVLVertex parent, GElement graphicalLeaf) {
        AVLVertex leafVertex = new AVLVertex();
        leafVertex.parent = parent;
        leafVertex.leftChild = null;
        leafVertex.rightChild = null;
        leafVertex.height = 0;
        leafVertex.graphicalVertex = graphicalLeaf;
        return leafVertex;
    }


    public boolean isLeafVertex() {
        return (leftChild == null) && (rightChild == null);
    }

    public boolean isInternalVertex() {
        return !isLeafVertex();
    }

    public void setPosition(int x, int y) {
        this.graphicalVertex.setPosition(x, y);
    }

}
