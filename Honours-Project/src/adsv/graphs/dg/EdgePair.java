package adsv.graphs.dg;

public class EdgePair {

	private String firstValue;
	private String secondValue;

	public EdgePair(String firstValue, String secondValue) {
		this.firstValue = firstValue;
		this.secondValue = secondValue;
	}

	public String getFirstValue() {
		return firstValue;
	}

	public String getSecondValue() {
		return secondValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstValue == null) ? 0 : firstValue.hashCode());
		result = prime * result + ((secondValue == null) ? 0 : secondValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgePair other = (EdgePair) obj;
		if (firstValue == null) {
			if (other.firstValue != null)
				return false;
		} else if (!firstValue.equals(other.firstValue))
			return false;
		if (secondValue == null) {
			if (other.secondValue != null)
				return false;
		} else if (!secondValue.equals(other.secondValue))
			return false;
		return true;
	}

	public static EdgePair key(String firstValue, String secondValue) {
		return new EdgePair(firstValue, secondValue);
	}
}