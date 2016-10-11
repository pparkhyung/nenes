var wsUri = "ws://localhost:8080/nenes/ws";

var eventOn = false;

function init() {
  output = document.getElementById("output");
  websocket = new WebSocket(wsUri);
  send_message();
}
function send_message() {
  websocket.onopen = function(evt) {
    onOpen(evt)
  };
  websocket.onmessage = function(evt) {
    onMessage(evt)
  };
  websocket.onerror = function(evt) {
    onError(evt)
  };
}

function onOpen(evt) {
  //writeToScreen("Connected to Endpoint!");
  doSend(textID.value);
}
function onMessage(evt) {
  //writeToScreen("Message Received: " + evt.data);//디버깅용
  informEvent(evt); // 응용에서 구현 중...
}
function onError(evt) {
  //writeToScreen('ERROR: ' + evt.data);
}
function doSend(message) {
  //writeToScreen("Message Sent: " + message);
  websocket.send(message);
  // websocket.close();
}
function writeToScreen(message) {
  var pre = document.createElement("p");
  pre.style.wordWrap = "break-word";
  pre.innerHTML = message;
  output.appendChild(pre);
}
window.addEventListener("load", init, false);
