package chat.coder;

import java.io.IOException;

import chat.Command;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class CommandDeserializer extends JsonDeserializer<Command> {
	private final static String COMMAND_NAME = "name";

	@Override
	public Command deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		String commandName = node.get(COMMAND_NAME).asText();
		return Command.getCommandByName(commandName);
	}

}
