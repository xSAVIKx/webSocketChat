package chat.coder;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import javax.websocket.EncodeException;

import org.junit.Before;
import org.junit.Test;

import chat.model.Command;
import chat.spring.model.CommandPojo;

public class ListCommandPojoEncoderTest {
  private ListCommandPojoEncoder encoder;

  @Before
  public void setUp() throws Exception {
    encoder = new ListCommandPojoEncoder();
    encoder.init(null);
  }

  @Test
  public void testEncode() throws EncodeException {
    CommandPojo commandPojo = new CommandPojo();
    commandPojo.setCommand(Command.LOGIN);
    commandPojo.setArgumentValue("testUsername");
    commandPojo.setSender("sender");
    String result = encoder.encode(Arrays.asList(commandPojo));
    assertNotNull(result);
  }

}
