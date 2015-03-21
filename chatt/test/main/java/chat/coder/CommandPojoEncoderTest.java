package chat.coder;

import static org.junit.Assert.assertNotNull;

import javax.websocket.EncodeException;

import org.junit.Before;
import org.junit.Test;

import chat.Command;
import chat.CommandPojo;

public class CommandPojoEncoderTest {
	private CommandPojoEncoder encoder;

	@Before
	public void setUp() throws Exception {
		encoder = new CommandPojoEncoder();
		encoder.init(null);
	}

	@Test
	public void testEncode() throws EncodeException {
		CommandPojo commandPojo = new CommandPojo();
		commandPojo.setCommand(Command.LOGIN);
		commandPojo.setArgumentValue("testUsername");
		commandPojo.setSender("sender");
		String result = encoder.encode(commandPojo);
		assertNotNull(result);
	}

}
