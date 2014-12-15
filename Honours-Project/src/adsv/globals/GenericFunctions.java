package adsv.globals;

public class GenericFunctions {

	public static boolean isValidNumber(String currentString) {
		if (!currentString.isEmpty() && currentString.matches("\\d{1,2}")) {
			return true;
		} else {
			return false;
		}
	}
}
