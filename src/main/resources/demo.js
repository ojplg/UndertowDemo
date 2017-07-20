var socket;

function start(){
    console.log('Starting up ...');
    socket = new WebSocket("ws://localhost:8080/ws");
    socket.onmessage = function(msg){
        console.log("Received " + msg);
        var serverMessagesDiv = document.getElementById("server_messages");
        serverMessagesDiv.innerHTML = msg.data;
    }
}

function sendMessage(){
    var textArea = document.getElementById("text_to_send");
    var text = textArea.value;
    console.log("Sending " + text);
    socket.send(text);
}