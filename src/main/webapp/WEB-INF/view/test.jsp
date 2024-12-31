<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title> 測試頁面</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <style>
        h1 {
            color: red;
        }
        .font-16{
            font-size: 16px;
        }
    </style>
</head>

<body>
	<h1 class="font-16">This is the body of the index view for Richard</h1>
    <p>${message}</p>
    <h2 class="font-16">${description}</h2>
    <h3 class="font-16">這是h3標題</h3>
    <a target ="_blank" href = https://www.facebook.com/>Facebook</a>
    <!-- img、input tag不需要結尾標記-->
    <img src="/static/images/1.jpg" alt="這是一張圖片" />
    <form action="/model/register" method="post">
        <label for= "name">姓名</label>
        <input type="text" name="name" id="name" />
        <button type="submit">送出</button>
    </form>
</body>
</html>