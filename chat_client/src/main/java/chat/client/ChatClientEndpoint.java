package chat.client;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import chat.client.util.MessageHandler;
import chat.coder.CommandPojoDecoder;
import chat.coder.CommandPojoEncoder;
import chat.coder.ListCommandPojoEncoder;
import chat.spring.model.CommandPojo;

@ClientEndpoint(encoders = {
    CommandPojoEncoder.class, ListCommandPojoEncoder.class
}, decoders = {
  CommandPojoDecoder.class
})
public class ChatClientEndpoint {
  private Session userSession = null;
  private MessageHandler messageHandler;
  private final static long MILISECONDS_FOR_HISTORY_LOAD = 500;

  public ChatClientEndpoint(final URI endpointURI) {
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      container.connectToServer(this, endpointURI);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public ChatClientEndpoint(final URI endpointURI, MessageHandler messageHandler) {
    this.messageHandler = messageHandler;
    try {
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      container.connectToServer(this, endpointURI);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @OnOpen
  public void onOpen(final Session userSession) {
    this.userSession = userSession;
  }

  @OnClose
  public void onClose(final Session userSession, final CloseReason reason) {
    this.userSession = null;
  }

  @OnMessage
  public void onMessage(final CommandPojo message) {
    if (messageHandler != null) {
      messageHandler.handleMessage(message);
    }
  }

  public boolean isUserLoggedIn() {
    if (messageHandler != null) {
      return messageHandler.isLoggedIn();
    }
    return false;
  }

  public List<CommandPojo> getLoadedHistory() {
    List<CommandPojo> history = null;
    if (messageHandler != null) {
      history = messageHandler.getHistory();
    }
    if (history == null) {
      try {
        Thread.sleep(MILISECONDS_FOR_HISTORY_LOAD);
        history = messageHandler.getHistory();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    if (history != null) {
      return history;
    }
    return Collections.emptyList();
  }

  public void sendMessage(final CommandPojo message) throws IOException, EncodeException {
    userSession.getBasicRemote().sendObject(message);
  }

}
