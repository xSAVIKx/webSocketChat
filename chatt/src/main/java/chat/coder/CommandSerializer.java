package chat.coder;

import java.io.IOException;

import chat.model.Command;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CommandSerializer extends JsonSerializer<Command> {

	@Override
	public void serialize(Command value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		gen.writeStartObject();
		gen.writeStringField("name", value.name());
		gen.writeEndObject();
	}

}
