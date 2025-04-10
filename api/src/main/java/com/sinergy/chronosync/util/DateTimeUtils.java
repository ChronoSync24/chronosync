package com.sinergy.chronosync.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public final class DateTimeUtils {

	private DateTimeUtils() {
	}

	/**
	 * Converts a UTC LocalDateTime to a specified time zone.
	 *
	 * @param utcTime   The UTC timestamp to convert
	 * @param targetZone The target time zone to convert to
	 * @return The converted LocalDateTime in the target zone
	 */
	public static LocalDateTime convertFromUtcToZone(LocalDateTime utcTime, ZoneId targetZone) {
		return utcTime.atZone(ZoneOffset.UTC)
			.withZoneSameInstant(targetZone)
			.toLocalDateTime();
	}
}