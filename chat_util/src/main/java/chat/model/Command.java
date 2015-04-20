package chat.model;

import org.apache.commons.lang3.StringUtils;

import chat.coder.CommandDeserializer;
import chat.coder.CommandSerializer;
import chat.spring.model.CommandPojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CommandSerializer.class)
@JsonDeserialize(using = CommandDeserializer.class)
public enum Command {
	SEND_MESSAGE("message"), LOGIN("username"), LOGIN_STATUS_OK("username"), LOGIN_STATUS_ERROR(
			"status"), LOGOUT("username"), NO_COMMAND(""), LOAD_HISTORY(
			"date_from"), RESULT_STATUS_COMMAND("result_status");
	private String argumentName;
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

	public static String getLoginStatusOKCommand(String username) {
		try {
			CommandPojo command = new CommandPojo();
			command.setCommand(Command.LOGIN_STATUS_OK);
			command.setArgumentValue(username);
			return OBJECT_MAPPER.writeValueAsString(command);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getLoginStatusErrorCommand(String username) {
		try {
			CommandPojo command = new CommandPojo();
			command.setCommand(Command.LOGIN_STATUS_ERROR);
			command.setArgumentValue(username);
			return OBJECT_MAPPER.writeValueAsString(command);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
