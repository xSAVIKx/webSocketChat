package chat;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class CommandPojo {
	private Command command;
	private String argumentValue;
	private String sender;
	private String timestamp;

	public CommandPojo() {

		timestamp = DateTime.now().toString(DateTimeFormat.mediumDateTime());
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String getArgumentValue() {
		return argumentValue;
	}

	public void setArgumentValue(String argumentValue) {
		this.argumentValue = argumentValue;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

}
