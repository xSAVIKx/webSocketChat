package chat.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.spring.db.CommandRepository;
import chat.spring.model.CommandPojo;

@Service
@Transactional
public class CommandServiceImpl implements CommandService {
	@Autowired
	private CommandRepository repository;

	@Override
	public CommandPojo saveCommand(CommandPojo command) {
		return repository.saveAndFlush(command);
	}

	@Override
	public CommandPojo findCommandById(long id) {
		CommandPojo command = repository.findOne(id);
		return command;
	}

	@Override
	public List<CommandPojo> getAllCommands() {
		return repository.findAll();
	}

}
