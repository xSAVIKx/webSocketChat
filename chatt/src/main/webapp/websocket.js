var wsocket;
var dateTimeFormat = "YYYY-MM-DD HH:mm:ss";
var serviceLocation = "ws://" + document.location.host
		+ document.location.pathname + "chat";
var COMMAND_STATUS = {
	OK : {
		value : 'true',
		text : 'COMMAND STATUS OK'
	},
	ERROR : {
		value : 'false',
		text : 'COMMAND STATUS ERROR'
	}
}
var COMMAND = {
	LOGIN_STATUS_OK : 'LOGIN_STATUS_OK',
	LOGIN_STATUS_ERROR : 'LOGIN_STATUS_ERROR',
	LOGOUT : 'LOGOUT',
	SEND_MESSAGE : 'SEND_MESSAGE'
}
var $nickName;
var $message;
var $chatWindow;

function onMessageReceived(evt) {
	if (evt.data == COMMAND_STATUS.OK.value) {
		console.info(COMMAND_STATUS.OK.text)
	} else if (evt.data == COMMAND_STATUS.ERROR.value) {
		console.error(COMMAND_STATUS.ERROR.text)
		leaveRoom('some error occured');
	} else {
		var msg = JSON.parse(evt.data); // native API
		processCommand(msg);
	}
}
function processCommand(command) {
	if (command.constructor === Array) {
		processLoadHistoryCommand(command);
	} else {
		var commandName = command.command.name;
		switch (commandName) {
		case COMMAND.LOGIN_STATUS_OK:
			processLoginStatusOKCommand(command);
			break;
		case COMMAND.LOGIN_STATUS_ERROR:
			processLoginStatusErrorCommand(command);
			break;
		case COMMAND.LOGOUT:
			processLogoutCommand(command)
			break;
		case COMMAND.SEND_MESSAGE:
			processSendMessageCommand(command);
			break;
		default:
			console.log(command);
		}
	}
}
function processLoginStatusOKCommand(command) {
	var $messageLine = generateMessageLine(command.timestamp, command.sender,
			'User "' + command.argumentValue + '" has logged in.');
	writeMessage($messageLine);
}
function processLoginStatusErrorCommand(command) {
	var errorMessage = 'Login error occured. You tried to login with username:'
			+ command.argumentValue;
	leaveRoom(errorMessage);
}
function processLoadHistoryCommand(commands) {
	var newWindow = window.open();
	var messageHistory = '<table border="0" cellspacing="5" cellpadding="10"><thead><tr><th>timestamp</th><th>ip</th><th>sender</th><th>command</th><th>value</th></tr></thead><tbody>';
	commands.forEach(function(command) {
		var row = generateHistoryMessageLine(command.command.name,
				command.timestamp, command.sender, command.argumentValue,
				command.ipAddr);
		messageHistory += row;
	});
	messageHistory += '</tbody><thead><tr><th>timestamp</th><th>ip</th><th>sender</th><th>command</th><th>value</th></tr></thead></table>';
	newWindow.document.write(messageHistory);
	newWindow.document.close();
}
function processSendMessageCommand(command) {
	var $messageLine = generateMessageLine(command.timestamp, command.sender,
			command.argumentValue);
	writeMessage($messageLine);
}
function processLogoutCommand(command) {
	var $messageLine = generateMessageLine(command.timestamp, command.sender,
			'User "' + command.argumentValue + '" has logged out.');
	writeMessage($messageLine);
}
function writeMessage(messageLine) {
	$chatWindow.append(messageLine);
	$chatWindow.scrollTop($chatWindow.prop('scrollHeight'));
}
function generateHistoryMessageLine(commandName, timestamp, sender, messageValue,
		ipAddr) {
	var messageLine = '<tr><td>' + timestamp + '</td>';
	messageLine += '<td>' + ipAddr + '</td>';
	messageLine += '<td>' + sender + '</td>';
	messageLine += '<td>' + commandName + '</td>';
	messageLine += '<td>' + messageValue + '</td></tr>';
	return messageLine;
}
function generateMessageLine(timestamp, sender, messageValue) {
	var timestamp = '<div class="label label-default">' + timestamp + '</div>';
	var sender = '<div class="user label label-info">' + sender + '</div>';
	var message = '<div class="message badge">' + messageValue + '</div>';
	var $messageLine = '<div class="row">' + timestamp + sender + message
			+ '</div>';
	return $messageLine;
}
function getLoginCommand(userName) {
	var loginCommand = '{"command":{"name":"LOGIN"},"argumentValue":"'
			+ userName + '"}';
	return loginCommand;
}
function getMessageCommand(message) {
	var msg = '{"command":{"name":"SEND_MESSAGE"},"argumentValue":"'
			+ $message.val() + '"}';
	return msg;
}
function getLoadHistoryCommand() {
	var msg = '{"command":{"name":"LOAD_HISTORY"},"argumentValue":"'
			+ moment().startOf('day').hours(0).startOf('hour') + '"}';
	return msg;

}
function getLogoutCommand(userName) {
	var logoutCommand = '{"command":{"name":"LOGOUT"},"argumentValue":"'
			+ userName + '"}';
	return logoutCommand;
}
function sendMessage() {
	var msg = getMessageCommand($message.val());
	wsocket.send(msg);
	$message.val('').focus();
}

function connectToChatserver() {
	wsocket = new WebSocket(serviceLocation);
	wsocket.onmessage = onMessageReceived;
	wsocket.onopen = function() {
		wsocket.send(getLoginCommand($nickName.val()));
	}
}

function leaveRoom(error) {
	if (error != '') {
		alert(error);
	}
	wsocket.send(getLogoutCommand($nickName.val()));
	wsocket.close();
	$chatWindow.empty();
	$('.chat-wrapper').hide();
	$('.chat-signin').show();
	$nickName.focus();
}

function loadHistory() {
	wsocket.send(getLoadHistoryCommand());
}

$(document).ready(function() {
	$nickName = $('#nickname');
	$message = $('#message');
	$chatWindow = $('#response');
	$('.chat-wrapper').hide();
	$nickName.focus();

	$('#enterChat').click(function(evt) {
		evt.preventDefault();
		connectToChatserver();
		$('.chat-wrapper h2').text('Chat # ' + $nickName.val());
		$('.chat-signin').hide();
		$('.chat-wrapper').show();
		$message.focus();
	});
	$('#do-chat').submit(function(evt) {
		evt.preventDefault();
		sendMessage()
	});

	$('#leave-room').click(function() {
		leaveRoom('');
	});
	$('#load-history').click(function() {
		loadHistory();
	});
});