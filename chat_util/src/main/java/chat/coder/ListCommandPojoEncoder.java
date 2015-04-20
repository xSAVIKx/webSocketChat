package chat.coder;

import java.util.List;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import chat.spring.model.CommandPojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListCommandPojoEncoder implements Encoder.Text<List<CommandPojo>> {
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
	public String encode(List<CommandPojo> object) throws EncodeException {
		String encodedString = null;
		try {
			encodedString = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return encodedString;
	}

}
