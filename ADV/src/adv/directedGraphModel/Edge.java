package adv.directedGraphModel;

// Bookkeeping structure used to record an edge from
// a vertex to a neighbour
public class Edge {

    private int fromVertex;
    private int toVertex;

    public Edge(int fromValue, int toVertex) {
        this.fromVertex = fromValue;
        this.toVertex = toVertex;
    }

    public Edge(String fromVertex, String toVertex) {
        this.fromVertex = Integer.parseInt(fromVertex);
        this.toVertex = Integer.parseInt(toVertex);
    }

    public int getFromVertex() {
        return fromVertex;
    }

    public int getToVertex() {
        return toVertex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (fromVertex != edge.fromVertex) return false;
        if (toVertex != edge.toVertex) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromVertex;
        result = 31 * result + toVertex;
        return result;
    }

    public static Edge key(String fromVertex, String toVertex) {
        return new Edge(Integer.parseInt(fromVertex), Integer.parseInt(toVertex));
    }

    public static Edge key(int fromVertex, int toVertex) {
        return new Edge(fromVertex, toVertex);
    }

    // Check if this edge records the details for
    // any vertex which has underwent renumbering
    public boolean requiresUpdate(int vertexBeingRenumbered) {
        return (fromVertex == vertexBeingRenumbered) || (toVertex == vertexBeingRenumbered);
    }

    // Return an updated edge for edge pair that has had
    // at least one of its vertices renumbered
    // Note: Generally only one vertex in edge pair relationship
    // changes, but in the case of a self loop both change.
    public Edge updatedEdge(int oldVertexValue, int renamedVertexValue) {
        int updatedFromValue = fromVertex;
        int updatedToValue = toVertex;

        if (fromVertex == oldVertexValue) {
            updatedFromValue = renamedVertexValue;
        }

        if (toVertex == oldVertexValue) {
            updatedToValue = renamedVertexValue;
        }

        return key(updatedFromValue, updatedToValue);
    }


}