package chat.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chat.model.Command;
import chat.spring.configuration.ApplicationConfig;
import chat.spring.configuration.HibernateConfiguration;
import chat.spring.db.CommandRepository;
import chat.spring.model.CommandPojo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    ApplicationConfig.class, HibernateConfiguration.class
})
public class CommandServiceTest {
  @Autowired
  CommandService commandService;
  @Autowired
  CommandRepository commandRepository;

  CommandPojo commandPojo;
  CommandPojo savedPojo;

  @Before
  public void setUp() throws Exception {
    commandPojo = new CommandPojo();
    commandPojo.setArgumentValue("test");
    commandPojo.setCommand(Command.SEND_MESSAGE);
    commandPojo.setSender("sender");
  }

  @After
  public void tearDown() throws Exception {
    if (savedPojo != null)
      try {
        commandRepository.delete(savedPojo);
      } catch (Exception e) {
        // TODO: handle exception
      }
  }

  @Test
  @Transactional
  public void testSaveCommand() {
    savedPojo = commandService.saveCommand(commandPojo);
    long id = savedPojo.getId();

    CommandPojo pojoFromDB = commandService.findCommandById(id);
    Assert.assertEquals(pojoFromDB, savedPojo);
  }

  @Test
  public void testGetAllCommands() {
    List<CommandPojo> pojoFromDBBeforeInsert = commandService.getAllCommands();
    savedPojo = commandService.saveCommand(commandPojo);

    List<CommandPojo> pojoFromDB = commandService.getAllCommands();
    Assert.assertEquals(pojoFromDBBeforeInsert.size() + 1, pojoFromDB.size());
    Assert.assertEquals(pojoFromDB.get(pojoFromDBBeforeInsert.size()), savedPojo);
  }

  @Test
  public void testGetCommandsFilteredByDateReturnEmptyCollection() {
    List<CommandPojo> pojoFromDBBeforeInsert = commandService.getCommandsFilteredByDate(DateTime.now().minusDays(5));
    savedPojo = commandService.saveCommand(commandPojo);

    List<CommandPojo> pojoFromDB = commandService.getCommandsFilteredByDate(DateTime.now().minusDays(5));
    Assert.assertEquals(pojoFromDBBeforeInsert.size(), pojoFromDB.size());
  }

  @Test
  public void testGetCommandsFilteredByDate() {
    List<CommandPojo> pojoFromDBBeforeInsert = commandService.getCommandsFilteredByDate(DateTime.now().plusDays(5));
    savedPojo = commandService.saveCommand(commandPojo);

    List<CommandPojo> pojoFromDB = commandService.getCommandsFilteredByDate(DateTime.now().plusDays(5));
    Assert.assertEquals(pojoFromDBBeforeInsert.size() + 1, pojoFromDB.size());
    Assert.assertEquals(pojoFromDB.get(0), savedPojo);
  }

}
