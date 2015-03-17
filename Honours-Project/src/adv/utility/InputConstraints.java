package adv.utility;

public class InputConstraints {

	public static final int MAX_NUM_ELEMENTS = 20;
	public static final int MIN_INPUT_VALUE = 0;
	public static final int MAX_INPUT_VALUE = 99;

	private static final String TWO_DIGIT_NUMBER = "\\d{1,2}";
	private static final String LIST_OF_TWO_DIGIT_NUMBERS = TWO_DIGIT_NUMBER+"(,"+TWO_DIGIT_NUMBER+")"+"+";

	public static boolean isValidNumber(String currentString) {
		return !currentString.isEmpty() && currentString.matches(TWO_DIGIT_NUMBER);
	}

	public static boolean isValidNumberList(String currentString) {
		return !currentString.isEmpty() && currentString.matches(LIST_OF_TWO_DIGIT_NUMBERS);
	}

}
