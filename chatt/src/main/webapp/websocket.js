var wsocket;
var serviceLocation = "ws://" + document.location.host
		+ document.location.pathname + "chat";
var $nickName;
var $message;
var $chatWindow;

function onMessageReceived(evt) {
	if (evt.data == 'OK') {

	} else if (evt.data == 'ERROR') {
		leaveRoom('some error occured');
	} else {
		var msg = JSON.parse(evt.data); // native API
		processCommand(msg);
	}
}
function processCommand(command) {
	var commandName = command.command.name;
	switch (commandName) {
	case "LOGIN":
		processLoginCommand(command);
		break;
	case "LOGOUT":
		processLogoutCommand(command)
		break;
	case "SEND_MESSAGE":
		processSendMessageCommand(command);
		break;
	default:
		break;
	}
}
function processSendMessageCommand(command) {
	var $messageLine = generateMessageLine(command.timestamp, command.sender,
			command.argumentValue);
	writeMessage($messageLine);
}
function processLoginCommand(command) {
	var $messageLine = generateMessageLine(command.timestamp, command.sender,
			'User "' + command.argumentValue + '" has logged in.');
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
});