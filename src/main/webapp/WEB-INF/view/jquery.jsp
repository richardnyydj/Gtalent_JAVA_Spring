<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>事件處理範例</title>
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.14.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://code.jquery.com/ui/1.14.1/jquery-ui.js"></script>
    <script defer src="/js/script_jquery_demo.js"></script>
    <style>
        .ui-menu { width: 150px; }
    </style>
</head>
<body>
    <ul id="menu">
        <li class="ui-state-disabled"><div>Toys (n/a)</div></li>
        <li><div>Books</div></li>
        <li><div>Clothing</div></li>
        <li><div>Electronics</div>
            <ul>
            <li class="ui-state-disabled"><div>Home Entertainment</div></li>
            <li><div>Car Hifi</div></li>
            <li><div>Utilities</div></li>
            </ul>
        </li>
        <li><div>Movies</div></li>
        <li><div>Music</div>
            <ul>
            <li><div>Rock</div>
                <ul>
                <li><div>Alternative</div></li>
                <li><div>Classic</div></li>
                </ul>
            </li>
            <li><div>Jazz</div>
                <ul>
                <li><div>Freejazz</div></li>
                <li><div>Big Band</div></li>
                <li><div>Modern</div></li>
                </ul>
            </li>
            <li><div>Pop</div></li>
            </ul>
        </li>
        <li class="ui-state-disabled"><div>Specials (n/a)</div></li>
    </ul>
</body>
</html>
