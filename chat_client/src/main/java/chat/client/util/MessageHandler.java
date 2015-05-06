package chat.client.util;

import java.util.List;

import chat.spring.model.CommandPojo;

public interface MessageHandler {
  public void handleMessage(CommandPojo message);

  public boolean isLoggedIn();

  public List<CommandPojo> getHistory();
}
