package chat.model;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
	public static final String DATE_TIME_PATTERN_STRING = "YYYY-MM-dd HH:mm:ss";
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat
			.forPattern(DATE_TIME_PATTERN_STRING);
	public static final String COMMAND_STATUS_OK = "true";
	public static final String COMMAND_STATUS_ERROR = "false";
}
