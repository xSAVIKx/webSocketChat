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
		var timestamp = '<div class="label label-default">' + msg.timestamp
				+ '</div>';
		var sender = '<div class="user label label-info">' + msg.sender
				+ '</div>';
		var message = '<div class="message badge">' + msg.argumentValue
				+ '</div>';
		var $messageLine = '<div class="row">' + timestamp + sender + message
				+ '</div>';
		$chatWindow.append($messageLine);
		$chatWindow.scrollTop($chatWindow.prop('scrollHeight'));
	}
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
function getLogoutCommand(username) {
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
	wsocket.onclose = function() {
		wsocket.send(getLogoutCommand($nickName.val()));
	}
}

function leaveRoom(error) {
	if (error != '') {
		alert(error);
	}
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