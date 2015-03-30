package chat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chat.coder.CommandPojoDecoder;
import chat.coder.CommandPojoEncoder;
import chat.model.Command;
import chat.spring.model.CommandPojo;

@ServerEndpoint(value = "/chat", encoders = CommandPojoEncoder.class, decoders = CommandPojoDecoder.class)
public class MyWebChat {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MyWebChat.class);
	private static final String USERNAME_SESSION_ATTRIBUTE = "username";

	private static Set<Session> peers = SetUtils
			.synchronizedSet(new HashSet<Session>());

	@OnMessage
	public String onMessage(Session session, CommandPojo command) {

		if (logger.isDebugEnabled()) {
			logger.debug("onMessage(String) - start");
		}
		String returnCode = "ERROR";
		String sender = getSender(session);
		command.setSender(sender);
		switch (command.getCommand()) {
		case LOGIN:
			sender = command.getArgumentValue();
			command.setSender("SYSTEM");
			if (hasSuchSender(sender)) {
				returnCode = "ERROR";
			} else if (StringUtils.isNotBlank(sender)) {
				setSender(session, sender);
				sendMessage(session, command);
				returnCode = "OK";
			} else {
				returnCode = "ERROR";
			}
			break;
		case LOGOUT:
			sender = command.getArgumentValue();
			command.setSender("SYSTEM");
			if (StringUtils.isNotBlank(sender)
					&& hasSuchSender(session, sender)) {
				setSender(session, sender);
				sendMessage(session, command);
				returnCode = "OK";
			} else {
				returnCode = "ERROR";
			}
			break;
		case SEND_MESSAGE:
			returnCode = sendMessage(session, command);
			break;
		case NO_COMMAND:
			break;
		default:
			throw new UnsupportedOperationException(
					"This command is not implemented yet");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("onMessage(String) - end");
		}
		return returnCode;
	}

	private boolean hasSuchSender(String username) {
		for (Session session : peers) {
			if (hasSuchSender(session, username)) {
				return true;
			}
		}
		return false;
	}

	private boolean hasSuchSender(Session session, String username) {
		return StringUtils.equals((CharSequence) session.getUserProperties()
				.get(USERNAME_SESSION_ATTRIBUTE), username);
	}

	private void setSender(Session session, String username) {
		session.getUserProperties().put(USERNAME_SESSION_ATTRIBUTE, username);
	}

	private String getSender(Session session) {
		String username = (String) session.getUserProperties().get(
				USERNAME_SESSION_ATTRIBUTE);
		return username;
	}

	private String sendMessage(Session user, CommandPojo message) {
		for (Session peer : peers) {
			if (peer.isOpen()) {
				try {
					if (peer.equals(user)
							&& (!message.getCommand().equals(Command.LOGIN) || !message
									.getCommand().equals(Command.LOGIN))) {
						CommandPojo selfMessage = new CommandPojo(message);
						selfMessage.setSender("You");
						peer.getBasicRemote().sendObject(selfMessage);
					} else {
						peer.getBasicRemote().sendObject(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return "ERROR";
				} catch (EncodeException e) {
					e.printStackTrace();
				}
			}
		}
		return "OK";
	}

	@OnOpen
	public void onOpen(Session peer) {
		synchronized (peers) {
			peers.add(peer);
		}
	}

	@OnClose
	public void onClose(Session peer) {
		synchronized (peers) {
			peers.remove(peer);
		}
	}
}
