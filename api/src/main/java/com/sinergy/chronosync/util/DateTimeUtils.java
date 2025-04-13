package com.sinergy.chronosync.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Utility class providing helper methods for date and time conversion.
 */
public final class DateTimeUtils {

	/**
	 * Converts a {@link LocalDateTime} from UTC to the specified target time zone.
	 *
	 * @param utcTime    the UTC time to convert
	 * @param targetZone the {@link ZoneId} to convert the UTC time to
	 * @return a {@link LocalDateTime} in the specified time zone
	 */
	public static LocalDateTime convertFromUtcToZone(LocalDateTime utcTime, ZoneId targetZone) {
		return utcTime.atZone(ZoneOffset.UTC)
			.withZoneSameInstant(targetZone)
			.toLocalDateTime();
	}
}
