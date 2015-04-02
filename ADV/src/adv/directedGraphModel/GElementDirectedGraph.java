/*

[The "BSD licence"]
Copyright (c) 2004 Jean Bovet
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package adv.directedGraphModel;

import adv.utility.InputConstraints;
import edu.usfca.vas.app.Localized;
import edu.usfca.xj.appkit.gview.GView;
import edu.usfca.xj.appkit.gview.base.Vector2D;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;
import edu.usfca.xj.appkit.utils.XJAlert;
import edu.usfca.xj.foundation.XJXMLSerializable;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// This class is based on GElementFAMachine
// (found in Jean Bovet VAS app) but has been 
// modified for directed graphs
public class GElementDirectedGraph extends GElement implements XJXMLSerializable {

    private GElementVertex[] vertices;
    private HashMap<Edge, GLink> edges;
    private boolean edgeModificationAllowed;

    public GElementDirectedGraph(boolean edgeModificationAllowed) {
        vertices = new GElementVertex[InputConstraints.MAX_NUM_ELEMENTS];
        edges = new HashMap<Edge, GLink>();
        this.edgeModificationAllowed = edgeModificationAllowed;
    }

    public void clear() {
        elements.clear();
        vertices = new GElementVertex[InputConstraints.MAX_NUM_ELEMENTS];
        edges.clear();
    }

    //------------------------------------
    // VERTEX METHODS
    //------------------------------------

    public GElementVertex[] getVertexSet() {
        return vertices;
    }

    public int getNumberVertices() {

        int numberOfVerticesPresent = 0;

        for (int i = 0 ; i < vertices.length ; i++) {
            if (vertices[i] != null) {
                numberOfVerticesPresent++;
            }
        }

        return numberOfVerticesPresent;

    }

    public void addVertexAtXY(String vertexKey, double x, double y) {
        GElementVertex vertex = new GElementVertex(vertexKey, x, y);
        vertex.setFillColor(Color.WHITE);
        int i = Integer.parseInt(vertex.getVertexKey());
        vertices[i] = vertex;
        addElement(vertex);
    }

    public void removeVertex(GElementVertex vertex) {
        int i = Integer.parseInt(vertex.getVertexKey());
        vertices[i] = null;
        removeElement(vertex);
        // Remove any other link which is using the vertex s
        ListIterator e = elements.listIterator();
        while (e.hasNext()) {
            GElement element = (GElement) e.next();
            if (element.getClass().equals(GLink.class)) {
                GLink link = (GLink) element;
                if (link.source == vertex || link.target == vertex) {
                    removeEdge(link);
                    e = elements.listIterator();
                }
            }
        }
    }

    public boolean containsVertex(String s) {
        int i = Integer.parseInt(s);
        return vertices[i] != null;
    }


    public Vector2D vertexPosition(int vertexKey) {
        return getVertex(vertexKey).getPosition();
    }

    public GElementVertex getVertex(Integer key) {
        return vertices[key];
    }

    public String lowestNumberedUnusedVertex() {

        for (int i = 0; i < InputConstraints.MAX_INPUT_VALUE; i++) {
            String numberString = String.valueOf(i);
            if (!this.containsVertex(numberString)) {
                return numberString;
            }
        }

        return null;
    }

    //------------------------------------
    // EDGE METHODS
    //------------------------------------

    public Set<Edge> getEdgeSet() {
        return edges.keySet();
    }

    public GLink getEdge(int fromVertex, int toVertex) {
        return edges.get(Edge.key(fromVertex, toVertex));
    }

    public void createEdge(GElementVertex source, String sourceAnchorKey, GElementVertex target,
                           String targetAnchorKey, int shape, Point mouse) {

        Edge edge = Edge.key(source.getVertexKey(), target.getVertexKey());

        if (edges.containsKey(edge)) {
            XJAlert.display(null, "Invalid Edge", "An edge already exists from vertex " + edge.getFromVertex()
                    + " vertex " + edge.getToVertex());
            return;
        }

        String pattern = "";
        if (edgeModificationAllowed) {
            pattern = (String) JOptionPane.showInputDialog(null, Localized.getString("dgNewEdgeMessage"),
                    Localized.getString("dgNewEdgeTitle"), JOptionPane.QUESTION_MESSAGE, null, null, null);

        }

        if (pattern != null) {
            GLink link = new GLink(source, sourceAnchorKey, target, targetAnchorKey, shape, pattern, mouse,
                    GView.DEFAULT_LINK_FLATENESS);
            edges.put(new Edge(source.getVertexKey(), target.getVertexKey()), link);
            addElement(link);
        }

    }

    public boolean editEdge(GLink edge) {

        String pattern = "";
        if (edgeModificationAllowed) {
            pattern = (String) JOptionPane.showInputDialog(null, Localized.getString("dgEditEdgeMessage"),
                    Localized.getString("dgEditEdgeTitle"), JOptionPane.QUESTION_MESSAGE, null, null, edge.pattern);
        }

        if (pattern != null) {
            removeEdge(edge);
            edges.remove(Edge.key(edge.source.getLabel(), edge.getTarget().getLabel()));
            edge.pattern = pattern;
            edges.put(Edge.key(edge.source.getLabel(), edge.getTarget().getLabel()), edge);
            addElement(edge);
        }

        return pattern != null;
    }

    public void removeEdge(GLink edge) {
        removeElement(edge);
        edges.remove(Edge.key(edge.source.getLabel(), edge.target.getLabel()));
    }

    //------------------------------------
    // ADJACENCY LIST METHODS
    //------------------------------------

    public HashMap<Integer, TreeSet<Integer>> adjacencyList() {
        HashMap<Integer, TreeSet<Integer>> adjacencyList = new HashMap<Integer, TreeSet<Integer>>();

        for (Edge edge : edges.keySet()) {
            String fromVertex = edges.get(edge).getSource().getLabel();
            String neighbour = edges.get(edge).getTarget().getLabel();
            recordNeighbour(adjacencyList, fromVertex, neighbour);
        }

        return adjacencyList;
    }

    private void recordNeighbour(HashMap<Integer, TreeSet<Integer>> adjacencyList, String fromVertex, String neighbour) {
        int fromValue = Integer.parseInt(fromVertex);
        int toValue = Integer.parseInt(neighbour);
        TreeSet currentNeighbors = adjacencyList.get(fromValue);

        if (currentNeighbors == null) {
            TreeSet<Integer> neighbors = new TreeSet<Integer>();
            neighbors.add(toValue);
            adjacencyList.put(fromValue, neighbors);
        } else {
            currentNeighbors.add(toValue);
        }
    }

}
