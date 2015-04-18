package chat.coder;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import chat.spring.model.CommandPojo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandPojoDecoder implements Decoder.Text<CommandPojo> {
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
	public CommandPojo decode(String s) throws DecodeException {
		CommandPojo command = null;
		try {
			command = objectMapper.readValue(s, CommandPojo.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return command;
	}

	@Override
	public boolean willDecode(String s) {
		return true;
	}

}
