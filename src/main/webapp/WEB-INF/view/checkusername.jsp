<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Check User Name</title>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.14.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://code.jquery.com/ui/1.14.1/jquery-ui.js"></script>
    <style>
        .valid {
            color: green;
        }
        .invalid {
            color: red;
        }
    </style>
</head>
<body>
    <input type="text" id="username" placeholder="請輸入要查詢的Name" />
    <button id="btn">查詢</button>
    <div id="feedback"></div>
    <script>
        // 使用jquery ajax
        $(document).ready(function(){
            $("#btn").click(function(){
                const username = $("#username").val();
                $.ajax({
                    url: "http://localhost:8080/model/check-username", // 替換為伺服器的 API URL
                    type: "POST", // POST 方法
                    data: { username: username }, // 傳遞用戶名參數
                    dataType: "json", // 預期返回 JSON 格式
                    success: function (response) {
                        if (response.isAvailable) {
                            $("#feedback").text("用戶名可用").removeClass("invalid").addClass("valid");
                        } else {
                            $("#feedback").text("用戶名已被使用").removeClass("valid").addClass("invalid");
                        }
                    },
                });
            }.bind(this))
        })
    </script>
</body>
</html>