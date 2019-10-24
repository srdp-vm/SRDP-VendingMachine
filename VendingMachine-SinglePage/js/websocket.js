/**
 * 前端通过JS实现websocket连接
 */

var websocket = null;
var url = "ws://localhost:8080/VMServer/websocket/phone";
// 判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
  websocket = new WebSocket(url);
} else {
  alert('当前浏览器不支持websocket');
}

function closePageForm() {
  window.opener = null;
  window.open('', '_self');
  window.close();
}

// 连接发生错误的回调方法
websocket.onerror = function () {
  alert("网络连接失败");
  //closePageForm();
  console.log("WebSocket连接发生错误");
};

// 连接成功建立的回调方法
websocket.onopen = function () {
  //连接成功后显示开门按钮
  $('#index').show();
  console.log("WebSocket连接成功！");
}

// 接收到消息的回调方法
websocket.onmessage = function (event) {
  console.log("<< " + event.data);
  var instruction = JSON.parse(event.data);
  var operation = instruction.operation;
  if (operation == 'add') {
    add(instruction.item);
  } else if (operation == 'del') {
    del(instruction.item);
  } else if (operation == 'settleup') {
    settleUp()
  } else if (operation == 'occupied') {
    alert("售货机正在使用中！请等待使用完毕！");
    websocket.close();
    closePageForm();
  } else if (operation == "offline") {
    alert('售货机离线，当前不可用！');
    websocket.close();
    closePageForm();
  }
}

// 连接关闭的回调方法
websocket.onclose = function () {
  console.log("WebSocket连接关闭！");
}

// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
  // 关闭WebSocket连接
  websocket.close();
}

// 发送消息
function sendMessage(message) {
  websocket.send(message);
  console.log(">> " + message);
}
