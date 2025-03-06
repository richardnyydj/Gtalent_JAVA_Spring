<!DOCTYPE html>
<html>
<head>
    <title>WebSocket Test</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
</head>
<body>
    <h1>WebSocket Test</h1>
    <input type="text" id="message" placeholder="Enter message"/>
    <button onclick="sendMessage()">Send</button>
    <ul id="messages"></ul>
    <button onclick="sendRabbitMessage()">Send to RabbitMQ</button>

    <script>
        function sendRabbitMessage() {
            var msg = document.getElementById("message").value;
            fetch('/rabbitmq/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(msg)
            }).then(response => response.text())
            .then(data => console.log(data));
        }
    </script>

    <script>
        var socket = new SockJS('/ws');
        var stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/messages', function (message) {
                var msgElement = document.createElement("li");
                msgElement.innerText = message.body;
                document.getElementById("messages").appendChild(msgElement);
            });
        });

        function sendMessage() {
            var msg = document.getElementById("message").value;
            stompClient.send("/app/sendMessage", {}, msg);
        }
    </script>
</body>
</html>