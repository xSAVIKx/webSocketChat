package chat.spring.service;

import java.util.List;

import chat.spring.model.CommandPojo;

public interface CommandService {
	CommandPojo saveCommand(CommandPojo command);

	CommandPojo findCommandById(long id);

	List<CommandPojo> getAllCommands();

}
