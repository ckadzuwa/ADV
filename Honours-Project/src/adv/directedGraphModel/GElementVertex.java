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

import java.awt.Graphics2D;

import edu.usfca.xj.appkit.gview.base.Anchor2D;
import edu.usfca.xj.appkit.gview.object.GElementCircle;
import edu.usfca.xj.foundation.XJXMLSerializable;

public class GElementVertex extends GElementCircle implements XJXMLSerializable {

	private String vertexKey;

	public GElementVertex() {
		setDraggable(true);
	}

	public GElementVertex(String vertexName, double x, double y) {
		setVertexKey(vertexName);
		setPosition(x, y);
		setDraggable(true);
	}

	public void setVertexKey(String state) {
		this.vertexKey = state;
	}

	public String getVertexKey() {
		return vertexKey;
	}

	public String getLabel() {
		return getVertexKey();
	}

	public boolean acceptIncomingLink() {
		return true;
	}

	public boolean acceptOutgoingLink() {
		return true;
	}

	public void updateAnchors() {
		setAnchor(ANCHOR_CENTER, position, Anchor2D.DIRECTION_FREE);
	}

	public void drawShape(Graphics2D g) {
		super.drawShape(g);
	}

}
