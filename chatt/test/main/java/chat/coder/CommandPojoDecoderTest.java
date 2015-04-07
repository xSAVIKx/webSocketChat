package chat.coder;

import static org.junit.Assert.assertNotNull;

import javax.websocket.DecodeException;

import org.junit.Before;
import org.junit.Test;

import chat.spring.model.CommandPojo;

public class CommandPojoDecoderTest {
	private CommandPojoDecoder decoder;

	@Before
	public void setUp() throws Exception {
		decoder = new CommandPojoDecoder();
		decoder.init(null);
	}

	@Test
	public void testDecode() throws DecodeException {
		String command = "{\"command\":{\"name\":\"SEND_MESSAGE\",\"argumentName\":\"message\"},\"argumentValue\":\"this is test message\",\"sender\":\"sender\"}";
		CommandPojo result = decoder.decode(command);

		assertNotNull(result);
	}
}
