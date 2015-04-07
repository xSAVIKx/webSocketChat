package chat.model;

import org.apache.commons.lang3.StringUtils;

import chat.coder.CommandDeserializer;
import chat.coder.CommandSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CommandSerializer.class)
@JsonDeserialize(using = CommandDeserializer.class)
public enum Command {
	SEND_MESSAGE("message"), LOGIN("username"), LOGOUT("username"), NO_COMMAND(
			""), LOAD_HISTORY("date_from");
	private String argumentName;

	Command(String argumetName) {
		this.argumentName = argumetName;
	}

	public String getArgumentName() {
		return argumentName;
	}

	public static Command getCommandByName(String name) {
		try {
			Command command = Command.valueOf(StringUtils.upperCase(name));
			return command;
		} catch (Exception e) {
			//
		}
		return Command.NO_COMMAND;
	}
}
