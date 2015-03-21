package chat.coder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import chat.CommandPojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandPojoEncoder implements Encoder.Text<CommandPojo> {
	private ObjectMapper objectMapper;

	@Override
	public void init(EndpointConfig config) {
		objectMapper = new ObjectMapper();
	}

	@Override
	public void destroy() {
		objectMapper = null;
	}

	@Override
	public String encode(CommandPojo object) throws EncodeException {
		String encodedString = null;
		try {
			encodedString = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return encodedString;
	}

}
