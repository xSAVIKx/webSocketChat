package chat.client.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import chat.spring.model.CommandPojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class CommandMessageHandler implements MessageHandler {
  private final ObjectMapper objectMapper;
  private final PrintStream printStream;
  private final static String MESSAGE_FORMAT = "%s|%s : %s\n";
  private final static String LOGIN_MESSAGE_FORMAT = "User \"%s\" has logged in.";
  private final static String LOGOUT_MESSAGE_FORMAT = "User \"%s\" has logged out.";
  private boolean isLoggedIn = false;
  private CollectionType collectionType;
  private List<CommandPojo> history;

  public CommandMessageHandler(PrintStream printStream) {
    this.printStream = printStream;
    objectMapper = new ObjectMapper();
    collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, CommandPojo.class);
  }

  @Override
  public void handleMessage(CommandPojo message) {
    String sender = message.getSender();
    String timestamp = message.getTimestamp();
    String argumentValue = message.getArgumentValue();
    switch (message.getCommand()) {
      case LOAD_HISTORY:
        history = saveHistory(message.getArgumentValue());
        break;
      case LOGIN:
        break;
      case LOGOUT:
        printStream.printf(MESSAGE_FORMAT, timestamp, sender, String.format(LOGOUT_MESSAGE_FORMAT, argumentValue))
            .flush();
        break;
      case LOGIN_STATUS_OK:
        isLoggedIn = true;
        printStream.printf(MESSAGE_FORMAT, timestamp, sender, String.format(LOGIN_MESSAGE_FORMAT, argumentValue))
            .flush();
        break;
      case LOGIN_STATUS_ERROR:
        isLoggedIn = false;
        printStream.printf("Login error occured. You tried to login with username: %s", argumentValue);
        break;
      case NO_COMMAND:
        break;
      case SEND_MESSAGE:
        printStream.printf(MESSAGE_FORMAT, timestamp, sender, argumentValue).flush();
        break;
      case RESULT_STATUS_COMMAND:
        break;
      default:
        throw new IllegalStateException();
    }
  }

  private List<CommandPojo> saveHistory(String history) {
    try {
      return objectMapper.readValue(history, collectionType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean isLoggedIn() {
    return isLoggedIn;
  }

  @Override
  public List<CommandPojo> getHistory() {
    return history;
  }
}
