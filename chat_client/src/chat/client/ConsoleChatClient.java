package chat.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.websocket.EncodeException;

import org.joda.time.DateTime;

import chat.client.util.CommandMessageHandler;
import chat.client.util.MessageHandler;
import chat.model.Command;
import chat.spring.model.CommandPojo;

public class ConsoleChatClient {
	private static final String WS_ENDPOINT = "ws://localhost:8080/chatt/chat";
	private final static String MESSAGE_FORMAT = "%s|%s : %s\n";
	private static MessageHandler messageHandler;
	private static ChatClientEndpoint endpoint;
	private final static String EXIT_COMMAND = "--exit";
	private final static String HELP_COMMAND = "--help";
	private final static String LOAD_HISTORY_COMMAND = "--history";
	private final static DateTime TODAY = DateTime.now().secondOfDay()
			.setCopy(0);

	public static void main(String[] args) throws URISyntaxException,
			IOException, EncodeException, InterruptedException {
		messageHandler = new CommandMessageHandler(System.err);
		endpoint = new ChatClientEndpoint(new URI(WS_ENDPOINT), messageHandler);
		boolean firstTime = true;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in, "UTF-8"))) {
			String username = "";
			do {
				if (!firstTime) {
					System.out
							.println("Something goes wrong. Try another name please.");
				}
				username = reader.readLine();
				endpoint.sendMessage(getLoginCommand(username));
				firstTime = false;
				Thread.sleep(100);
			} while (!endpoint.isUserLoggedIn());
			String input = "";
			while (!EXIT_COMMAND.equalsIgnoreCase((input = reader.readLine()))) {
				if (HELP_COMMAND.equalsIgnoreCase(input)) {
					printHelpMessage();
				} else if (LOAD_HISTORY_COMMAND.equalsIgnoreCase(input)) {
					endpoint.sendMessage(getLoadHistoryCommand());
					Thread.sleep(100);
					saveHistoryToFile(endpoint.getLoadedHistory());
					System.err
							.println("History saved successfully. Check file \"history.txt\"");
				} else {
					System.out.println("\r");
					endpoint.sendMessage(getSendMessageCommand(input, username));
					Thread.sleep(100);
				}
			}

			endpoint.sendMessage(getLogoutCommand(username));
			Thread.sleep(100);
		}
		System.out.println("Bye-bye");
	}

	private static void saveHistoryToFile(List<CommandPojo> history) {
		File historyFile = new File("history.txt");
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(
				historyFile))) {
			for (CommandPojo command : history) {
				writer.write(String.format(MESSAGE_FORMAT,
						command.getTimestamp(), command.getSender(),
						command.getArgumentValue()));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void printHelpMessage() {
		System.out
				.println("\nThis is help message.\nUsage:\n--exit - to exit from chat;\n--help - to show this message\n--history - to load todays chat history to file \"history.txt\"\n");
	}

	private static CommandPojo getLoadHistoryCommand() {
		CommandPojo loadHistoryCommand = new CommandPojo();
		loadHistoryCommand.setCommand(Command.LOAD_HISTORY);
		loadHistoryCommand.setArgumentValue(String.valueOf(TODAY.getMillis()));
		return loadHistoryCommand;
	}

	private static CommandPojo getSendMessageCommand(String message,
			String username) {
		CommandPojo sendMessageCommand = new CommandPojo();
		sendMessageCommand.setCommand(Command.SEND_MESSAGE);
		sendMessageCommand.setSender(username);
		sendMessageCommand.setArgumentValue(message);
		return sendMessageCommand;
	}

	private static CommandPojo getLogoutCommand(String username) {
		CommandPojo logoutCommand = new CommandPojo();
		logoutCommand.setCommand(Command.LOGOUT);
		logoutCommand.setSender(username);
		logoutCommand.setArgumentValue(username);
		return logoutCommand;
	}

	private static CommandPojo getLoginCommand(String username) {
		CommandPojo loginComand = new CommandPojo();
		loginComand.setCommand(Command.LOGIN);
		loginComand.setSender(username);
		loginComand.setArgumentValue(username);
		return loginComand;
	}
}
