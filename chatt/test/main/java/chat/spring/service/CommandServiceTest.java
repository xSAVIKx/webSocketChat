package chat.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import chat.model.Command;
import chat.spring.configuration.ApplicationConfig;
import chat.spring.configuration.HibernateConfiguration;
import chat.spring.db.CommandRepository;
import chat.spring.model.CommandPojo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class,
		HibernateConfiguration.class })
public class CommandServiceTest {
	static AbstractApplicationContext context;
	@Autowired
	CommandService commandService;
	@Autowired
	CommandRepository commandRepository;

	CommandPojo commandPojo;
	CommandPojo savedPojo;

	@BeforeClass
	public static void setUpClass() {
		context = new AnnotationConfigApplicationContext(
				ApplicationConfig.class);

	}

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
		savedPojo = commandService.saveCommand(commandPojo);

		List<CommandPojo> pojoFromDB = commandService.getAllCommands();
		Assert.assertEquals(1, pojoFromDB.size());
		Assert.assertEquals(pojoFromDB.get(0), savedPojo);
	}

}
