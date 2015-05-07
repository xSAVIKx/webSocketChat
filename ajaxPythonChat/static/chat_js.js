//Numeric representation of the last time we received a message from the server
var last_update = -1;

$(document).ready(function () {

    getData();

    var inputBox = document.getElementById("inputbox");

    inputbox.addEventListener("keydown", function (e) {
        if (!e) {
            var e = window.event;
        }

        if (e.keyCode == 13) {
            e.preventDefault(); // sometimes useful
            postData(inputbox.value);
            inputbox.value = "";
        }
    }, false);

});

var getData = function () {
    $.ajax({
               type: "GET",
               // set the destination for the query
               url: 'http://127.0.0.1:8000/api/?last_update=' + last_update,
               // needs to be set to true to avoid browser loading icons
               async: true,
               cache: false,
               timeout: 100000,
               // process a successful response
               success: function (data) {
                   // append the message list with the new message
                   //$("#message_list ul").prepend($('<li>' + 'success' + '</li>'));
                   data = $.parseJSON(data);
                   var message = null;
                   if (data.constructor === Array) {
                       message = data[0];
                       data.forEach(function (dt) {
                           $("#message_list ul").prepend($('<li>' + dt.fields.msg + '</li>'));
                       });
                   } else {
                       message = data;
                       $("#message_list ul").prepend($('<li>' + message.fields.msg + '</li>'));
                   }
                   // set last_update
                   if (message != null && message.construct !== Array)
                       last_update = message.fields.timestamp;
               },
               complete: function () {
                   getData();
               },
               error: function (err) {
                   if (err.statusText !== "timeout" && err.status !== 200)
                       console.log("unexpected error: " + err);
               }
           });
};

var postData = function (data) {
    $.ajax({
               type: "POST",
               // set the destination for the query
               url: 'http://127.0.0.1:8000/api/?last_update=' + last_update,
               data: {new_message: data},
               // needs to be set to true to avoid browser loading icons
               async: true,
               cache: false
           });
}