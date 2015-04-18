package chat.coder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import chat.model.Command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandSerializerTest {
	private ObjectMapper objectMapper;

	@Before
	public void setUp() throws Exception {
		objectMapper = new ObjectMapper();
	}

	@Test
	public void testSerialize() throws JsonProcessingException {
		Command command = Command.getCommandByName("login");
		String result = objectMapper.writeValueAsString(command);
		assertNotNull(result);
		assertTrue(result.contains("LOGIN"));
	}

	@Test
	public void testDeserialize() throws IOException {
		String command = "{\"name\":\"LOGIN\"}";
		Command result = objectMapper.readValue(command, Command.class);
		assertNotNull(result);
		assertEquals(Command.LOGIN, result);
	}

}
