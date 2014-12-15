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

package adsv.graphs.dg;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import javax.swing.JOptionPane;

import adsv.globals.Constants;
import edu.usfca.vas.app.Localized;
import edu.usfca.xj.appkit.gview.GView;
import edu.usfca.xj.appkit.gview.object.GElement;
import edu.usfca.xj.appkit.gview.object.GLink;
import edu.usfca.xj.appkit.utils.XJAlert;
import edu.usfca.xj.foundation.XJXMLSerializable;

// This class is bassed on GElementFAMachine
// (found in Jean Bovet VAS app) but has been 
// modified
public class GElementDirectedGraph extends GElement implements XJXMLSerializable {

    private HashMap vertices;
    private HashMap edges;
    private boolean edgeModificationAllowed;

    public GElementDirectedGraph(boolean edgeModificationAllowed) {
        vertices = new HashMap();
        edges = new HashMap();
        this.edgeModificationAllowed = edgeModificationAllowed;
    }

    public void addVertexAtXY(String s, double x, double y) {
        GElementVertex vertex = new GElementVertex(s, x, y);
        vertices.put(vertex.getVertexValue(), vertex);
        addElement(vertex);
    }

    public GElementVertex getVertex(String name) {
        return (GElementVertex) vertices.get(name);
    }

    public GElementVertex getVertex1(GLink link) {
        return (GElementVertex) link.source;
    }

    public GElementVertex getVertex2(GLink link) {
        return (GElementVertex) link.target;
    }

    // public GLink getTransition(FATransition transition) {
    // ListIterator e = elements.listIterator();
    // while (e.hasNext()) {
    // GElement element = (GElement) e.next();
    // if (element.getClass().equals(GLink.class)) {
    // GLink link = (GLink) element;
    // if (getState1(link).vertex.name.equals(transition.s1)
    // && getState2(link).vertex.name.equals(transition.s2)) {
    // return link;
    // }
    // }
    // }
    // return null;
    // }

    public void removeVertex(GElementVertex s) {
        vertices.remove(s.getVertexValue());
        removeElement(s);
        // Remove any other link which is using the vertex s
        ListIterator e = elements.listIterator();
        while (e.hasNext()) {
            GElement element = (GElement) e.next();
            if (element.getClass().equals(GLink.class)) {
                GLink link = (GLink) element;
                if (link.source == s || link.target == s) {
                    removeElement(link);
                    e = elements.listIterator();
                }
            }
        }
    }

    public void createEdge(GElementVertex source, String sourceAnchorKey, GElementVertex target,
                           String targetAnchorKey, int shape, Point mouse) {

        EdgePair pair = EdgePair.key(source.getVertexValue(), target.getVertexValue());

        if (edges.containsKey(pair)) {
            XJAlert.display(null, "Invalid Edge", "An edge already exists from vertex " + pair.getFirstValue()
                    + " vertex " + pair.getSecondValue());
            return;
        }

        String pattern = "";
        if (edgeModificationAllowed) {
            pattern = (String) JOptionPane.showInputDialog(null, Localized.getString("dgNewEdgeMessage"),
                    Localized.getString("dgNewEdgeTitle"), JOptionPane.QUESTION_MESSAGE, null, null, null);

        }

        if (pattern != null) {
            GLink edge = new GLink(source, sourceAnchorKey, target, targetAnchorKey, shape, pattern, mouse,
                    GView.DEFAULT_LINK_FLATENESS);
            edges.put(new EdgePair(source.getVertexValue(), target.getVertexValue()), edge);
            addElement(edge);
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
            edges.remove(EdgePair.key(edge.source.getLabel(), edge.getTarget().getLabel()));
            edge.pattern = pattern;
            edges.put(EdgePair.key(edge.source.getLabel(), edge.getTarget().getLabel()), edge);
            addElement(edge);
        }

        return pattern != null;
    }

    public void removeEdge(GLink edge) {
        removeElement(edge);
        edges.remove(EdgePair.key(edge.source.getLabel(), edge.target.getLabel()));
    }

    public GLink getEdge(GElementVertex s1, GElementVertex s2) {
        Iterator elements = getElements().iterator();
        while (elements.hasNext()) {
            Object e = elements.next();
            if (e instanceof GLink) {
                GLink l = (GLink) e;
                if (l.source == s1 && l.target == s2)
                    return l;
            }
        }
        return null;
    }

    public void clear() {
        elements.clear();
        vertices.clear();
        edges.clear();
    }

    public boolean containsVertex(String s) {
        return vertices.containsKey(s);
    }

    public String getFirstAvailableVertexValue() {

        for (int i = 0; i < Constants.MAX_VALUE; i++) {
            String numberString = String.valueOf(i);
            if (!this.containsVertex(numberString)) {
                return numberString;
            }
        }

        return null;
    }

    public int getNumberVertices() {
        return vertices.size();
    }

    public void renameVertex(GElementVertex vertex, String newVertexName) {

        String oldVertexName = vertex.getVertexValue();

        GElementVertex storedVertex = (GElementVertex) vertices.get(oldVertexName);
        storedVertex.setVertexValue(newVertexName);

        vertices.remove(oldVertexName);
        vertices.put(newVertexName, storedVertex);
    }

}
