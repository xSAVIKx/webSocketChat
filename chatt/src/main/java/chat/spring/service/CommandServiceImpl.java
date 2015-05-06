package chat.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.model.Constants;
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

  @Override
  public List<CommandPojo> getCommandsFilteredByDate(final DateTime fromDate) {
    List<CommandPojo> commands = getAllCommands();
    CollectionUtils.filter(commands, new Predicate<CommandPojo>() {
      @Override
      public boolean evaluate(CommandPojo object) {
        DateTime timestamp = DateTime.parse(object.getTimestamp(), Constants.DATE_TIME_FORMATTER);
        return timestamp.isAfter(fromDate);
      }

    });
    return commands;
  }
}
