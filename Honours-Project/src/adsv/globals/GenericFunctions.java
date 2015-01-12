package adsv.globals;

public class GenericFunctions {

	private static final String TWO_DIGIT_NUMBER = "\\d{1,2}";

	public static boolean isValidNumber(String currentString) {
		return !currentString.isEmpty() && currentString.matches(TWO_DIGIT_NUMBER);
	}

	public static boolean isValidNumberList(String currentString) {
		return !currentString.isEmpty() && currentString.matches("("+TWO_DIGIT_NUMBER+","+TWO_DIGIT_NUMBER+")"+"+");
	}
}
