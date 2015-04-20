package chat;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chat.coder.CommandPojoDecoder;
import chat.coder.CommandPojoEncoder;
import chat.coder.ListCommandPojoDecoder;
import chat.coder.ListCommandPojoEncoder;
import chat.model.Command;
import chat.model.Constants;
import chat.spring.configuration.ApplicationContextProvider;
import chat.spring.model.CommandPojo;
import chat.spring.service.CommandService;

@ServerEndpoint(value = "/chat", encoders = { CommandPojoEncoder.class,
		ListCommandPojoEncoder.class }, decoders = {CommandPojoDecoder.class, ListCommandPojoDecoder.class})
public class MyWebChat {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MyWebChat.class);
	private static final String USERNAME_SESSION_ATTRIBUTE = "username";

	private static Set<Session> peers = SetUtils
			.synchronizedSet(new HashSet<Session>());
	private CommandService commandService;

	@OnMessage
	public String onMessage(Session session, CommandPojo command) {
		if (logger.isDebugEnabled()) {
			logger.debug("onMessage(String) - start");
		}
		String returnCode = Constants.COMMAND_STATUS_ERROR;
		String sender = getSender(session);
		command.setSender(sender);
		switch (command.getCommand()) {
		case LOGIN:
			sender = command.getArgumentValue();
			command.setSender("SYSTEM");
			if (StringUtils.isNotBlank(sender) && !hasSuchSender(sender)) {
				setSender(session, sender);
				sendMessage(session, command);
				returnCode = Command.getLoginStatusOKCommand(sender);
			} else {
				returnCode = Command.getLoginStatusErrorCommand(sender);
			}
			break;
		case LOGOUT:
			sender = command.getArgumentValue();
			command.setSender("SYSTEM");
			if (StringUtils.isNotBlank(sender)
					&& hasSuchSender(session, sender)) {
				setSender(session, sender);
				sendMessage(session, command);
				returnCode = Constants.COMMAND_STATUS_OK;
			}
			break;
		case SEND_MESSAGE:
			returnCode = sendMessage(session, command);
			break;
		case LOAD_HISTORY:
			String fromDateString = command.getArgumentValue();
			DateTime fromDate = new DateTime(Long.valueOf(fromDateString));
			List<CommandPojo> commands = getCommandService()
					.getCommandsFilteredByDate(fromDate);
			try {
				session.getBasicRemote().sendObject(commands);
				returnCode = Constants.COMMAND_STATUS_OK;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
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

				if (peer.equals(user)
						&& isNotLoginAndLogoutCommand(message.getCommand())) {
					CommandPojo selfMessage = new CommandPojo(message);
					selfMessage.setSender("You");
					try {
						peer.getBasicRemote().sendObject(selfMessage);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (EncodeException e) {
						e.printStackTrace();
					}
				} else {
					try {
						peer.getBasicRemote().sendObject(message);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (EncodeException e) {
						e.printStackTrace();
					}
				}

			}
		}
		saveCommandInSeparateThread(message);
		return Constants.COMMAND_STATUS_OK;
	}

	private boolean isNotLoginAndLogoutCommand(Command command) {
		return !Command.LOGIN.equals(command)
				&& !Command.LOGOUT.equals(command);
	}

	private void saveCommandInSeparateThread(final CommandPojo command) {
		if (logger.isInfoEnabled()) {
			logger.info(
					"saveCommandInSeparateThread(CommandPojo) - CommandPojo command={}",
					command);
		}
		Thread thread = new Thread(command.getSender() + command.getTimestamp()) {
			public void run() {
				getCommandService().saveCommand(command);
			};
		};
		thread.start();
	}

	@OnOpen
	public void onOpen(Session peer, EndpointConfig config) {
		// TODO save HTTP sessions to get users IP addresses. try to check this
		// page:
		// http://stackoverflow.com/questions/17936440/accessing-httpsession-from-httpservletrequest-in-a-web-socket-socketendpoint/17994303#17994303
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

	private CommandService getCommandService() {
		if (commandService == null) {
			commandService = ApplicationContextProvider.getApplicationContext()
					.getBean(CommandService.class);
		}
		return commandService;
	}
}
