package chat.coder;

import java.io.IOException;
import java.util.List;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import chat.spring.model.CommandPojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class ListCommandPojoDecoder implements Decoder.Text<List<CommandPojo>> {
	private ObjectMapper objectMapper;
	private CollectionType collectionType;

	@Override
	public void init(EndpointConfig config) {
		objectMapper = new ObjectMapper();
		collectionType = objectMapper.getTypeFactory().constructCollectionType(
				List.class, CommandPojo.class);

	}

	@Override
	public void destroy() {
		objectMapper = null;

	}

	@Override
	public List<CommandPojo> decode(String s) throws DecodeException {
		List<CommandPojo> commandPojos = null;
		try {
			commandPojos = objectMapper.readValue(s, collectionType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return commandPojos;
	}

	@Override
	public boolean willDecode(String s) {
		return true;
	}

}
