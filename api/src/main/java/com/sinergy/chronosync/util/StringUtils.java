package com.sinergy.chronosync.util;

/**
 * Utility class for working with strings.
 */
public class StringUtils {

	/**
	 * Returns true if string is empty or null;
	 *
	 * @param ref {@link String} string to check
	 * @return {@link Boolean} result
	 */
	public static Boolean isEmpty(String ref) {
		return ref != null && ref.isEmpty();
	}

	/**
	 * Returns true if string is not empty;
	 *
	 * @param ref {@link String} string to check
	 * @return {@link Boolean} result
	 */
	public static Boolean isNotEmpty(String ref) {
		return ref != null && !ref.isEmpty();
	}
}
