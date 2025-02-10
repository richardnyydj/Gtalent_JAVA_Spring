<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>事件處理範例</title>
    <script>
        document.addEventListener("DOMContentLoaded", () => {
            console.log("所有資源已加載完成(使用DOMContentLoaded)");
            const button = document.getElementById("btn");
            button.addEventListener("click", () => {
                alert("按鈕已被點擊！(使用DOMContentLoaded)");
            });
        });
        window.onload = function() {
            console.log("所有資源已加載完成(使用window.onload)");
            const button = document.getElementById("btn");
            button.addEventListener("click", () => {
                alert("按鈕已被點擊！(使用window.onload)");
            });
        };
        function onClickBtn(e) {
            console.log(e); // 事件對象            
            alert(`按鈕已被點擊！(使用onclick)事件類型: ${"${e.type}"}, 按鈕 ID: ${"${e.target.id}"}`);
        }
        function changeContent() {
            let element = document.getElementById("changeContentBtn");
            element.textContent = "Hello, JSP and JavaScript!";
        }
    </script>
</head>
<body>
    <button id="btn">點我</button>
    <button id="byOnClickBtn" onclick="onClickBtn(event)">點我 by onclick event</button>
    <button id="changeContentBtn" onclick="changeContent()">點擊更改內容</button>
    <script>
        console.log("DOM 已加載完成(放在body結尾)");
        const button_elem = document.getElementById("btn");
        button_elem.onclick = () => {
            alert("按鈕已被點擊！(放在body結尾)");
        };
    </script>
</body>
</html>
