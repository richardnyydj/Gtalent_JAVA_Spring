<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>JavaScript 資料顯示範例</title>
    <script>
        function testGlobal() {
            global = 10;
        }

        // 此為一個閉包(closure)
        function createCounter() {
            let count = 0;
            return function () {
                count++;
                document.getElementById("counter").textContent = count;
            };
        }
        const counter = createCounter();

         function checkGrade() {
            let score = prompt("請輸入您的分數:");
            let grade;
            if (score >= 90) {
                grade = "A";
            } else if (score >= 80) {
                grade = "B";
            } else if (score >= 70) {
                grade = "C";
            } else {
                grade = "F";
            }
            alert(`您的等級是: ${"${grade}"}`);
        }
        function showFruits() {
            let fruits = ["蘋果", "香蕉", "櫻桃"];
            let list = "";
            for (let i = 0; i < fruits.length; i++) {
                list += `<li>${"${fruits[i]}"}</li>`;
            }
            document.getElementById("fruitList").innerHTML = list;
        }
    </script>
</head>
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
    <button onclick="showFruits()">顯示水果清單</button>
    <ul id="fruitList">
        <li>蘋果</li>
        <li>香蕉</li>
        <li>櫻桃</li>
    </ul>
</body>
<body>
    <button id="btn">點我</button>
    <button id="byOnClickBtn" onclick="onClickBtn(event)">點我 by onclick event</button>
    <button id="changeContentBtn" onclick="changeContent()">點擊更改內容</button>
    <div id="outer">
        <button id="inner">點擊我</button>
    </div>
    <a id="link" href="https://www.google.com" target="_blank">google連結</a>
    <form id="form" action="/model/register" method="post">
        <label for="name">姓名</label>
        <input type="text" name="name" id="name">
        <button type="submit">送出</button>
    </form>
    <script>
        console.log("DOM 已加載完成(放在body結尾)");
        const button_elem = document.getElementById("btn");
        button_elem.onclick = () => {
            alert("按鈕已被點擊！(放在body結尾)");
        };

        document.getElementById("outer").addEventListener("click", () => {
            console.log("實際點擊元素", e.target.id);
            console.log("事件元素", e.currentTarget.id);
        });

        document.getElementById("link").addEventListener("click", (e) => {
            e.preventDefault();
            alert("連結已被點擊！");
        });

        document.getElementById("inner").addEventListener("click", () => {
            alert("實際點擊元素", e.target.id);
            alert("事件元素", e.currentTarget.id);
        });

        document.getElementById("child").addEventListener("click", () => {
            console.log("子元素被點擊");
        });


    </script>
</body>
</html>