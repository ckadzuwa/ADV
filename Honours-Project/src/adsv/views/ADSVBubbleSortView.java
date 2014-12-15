package adsv.views;

public class ADSVBubbleSortView extends ADSVSortView {

	public void bubbleSort() {

		for (int i = 0; i < currentNumElements - 1; i++) {
			for (int j = 0; j < currentNumElements - 1 - i; j++) {
				if (lessThan(j + 1, j))
					swap(j + 1, j);
			}
		}
	}

	public void callFunction() {
		bubbleSort();
	}

}
