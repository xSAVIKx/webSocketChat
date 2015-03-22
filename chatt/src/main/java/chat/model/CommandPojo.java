package chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

@Entity
@Table(name = "MESSAGE")
public class CommandPojo {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	@Column(name = "command")
	@Enumerated(EnumType.STRING)
	private Command command;
	@Column(name = "value")
	private String argumentValue;
	@Column(name = "sender")
	private String sender;
	@Column(name = "timestamp")
	private String timestamp;

	public CommandPojo() {

		timestamp = DateTime.now().toString(DateTimeFormat.mediumDateTime());
	}

	public CommandPojo(CommandPojo commandPojo) {
		this.command = commandPojo.getCommand();
		this.argumentValue = commandPojo.getArgumentValue();
		this.sender = commandPojo.getSender();
		this.timestamp = commandPojo.getTimestamp();
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String getArgumentValue() {
		return argumentValue;
	}

	public void setArgumentValue(String argumentValue) {
		this.argumentValue = argumentValue;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
