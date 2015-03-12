package adv.directedGraphModel;

public class EdgePair {

    private int fromValue;
    private int toValue;

    public EdgePair(int fromValue, int toValue) {
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public EdgePair(String fromValue, String toValue) {
        this.fromValue = Integer.parseInt(fromValue);
        this.toValue = Integer.parseInt(toValue);
    }

    public int getFromValue() {
        return fromValue;
    }

    public int getToValue() {
        return toValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EdgePair edgePair = (EdgePair) o;

        if (fromValue != edgePair.fromValue) return false;
        if (toValue != edgePair.toValue) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromValue;
        result = 31 * result + toValue;
        return result;
    }

    public static EdgePair key(String fromValue, String toValue) {
        return new EdgePair(Integer.parseInt(fromValue), Integer.parseInt(toValue));
    }

    public static EdgePair key(int fromValue, int toValue) {
        return new EdgePair(fromValue, toValue);
    }

    // Check if edge pair recorded details
    // for vertex which has underwent renaming
    public boolean requiresUpdate(int oldVertexValue) {
        return fromValue == oldVertexValue || toValue == oldVertexValue;
    }

    // Return an updated edge for edge pair that has had
    // at least one of its vertices renamed
    // Note: Generally only one vertex in edge pair relationship
    // changes , 2 in the case of a self loop.
    public EdgePair updatedEdge(int oldVertexValue, int renamedVertexValue) {
        int updatedFromValue = fromValue;
        int updatedToValue = toValue;

        if (fromValue == oldVertexValue) {
            updatedFromValue = renamedVertexValue;
        }

        if (toValue == oldVertexValue) {
            updatedToValue = renamedVertexValue;
        }

        return key(updatedFromValue, updatedToValue);
    }


}