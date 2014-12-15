import java.awt.Dimension;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class Test {

	public static void main(String[] args) {

		// Graph<V, E> where V is the type of the vertices
		// and E is the type of the edges

		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();

		int numNodes = 12;

		for (int i = 0; i < numNodes; i++) {
			g.addVertex(new Integer(i));
		}

		for (int i = 0; i < numNodes; i++) {
			for (int j = i; j < numNodes; j++) {
				if (i == j) {
					continue;
				}
				int num = (int) (Math.random() * 2);
				if (num == 1) {
					continue;
				} else {

					num = (int) (Math.random() * 2);

					if (num == 1) {
						num = (int) (Math.random() * 2);
						if (num == 0) {
							g.addEdge("Edge:" + i + "-" + j, i, j, EdgeType.DIRECTED);
							g.addEdge("Edge:" + j + "-" + i, j, i, EdgeType.DIRECTED);
						}
					}
				}

			}
		}

		System.out.println(g.getEdgeCount());

		// Let's see what we have. Note the nice output from the
		// SparseMultigraph<V,E> toString() method
		System.out.println("The graph g = " + g.toString());
		// Note that we can use the same nodes and edges in two different
		// graphs.

		// Layout<Integer, String> layout = new CircleLayout(g);
		// Layout<Integer, String> layout = new SpringLayout<Integer,
		// String>(g);
		Layout<Integer, String> layout = new FRLayout<Integer, String>(g);

		layout.setSize(new Dimension(700, 700)); // sets the initial size of the
													// space

		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(layout);
		vv.setPreferredSize(new Dimension(700, 700)); // Sets the viewing area

		// size
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

		System.out.println("------------------------------------------");
		for (Integer v : g.getVertices()) {
			System.out.print(v + "--");
			System.out.println("(" + ((AbstractLayout<Integer, String>) layout).getX(v) + ","
					+ ((AbstractLayout<Integer, String>) layout).getY(v) + ")");
		}

	}

}
