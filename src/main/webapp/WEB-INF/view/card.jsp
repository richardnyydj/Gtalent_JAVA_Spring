<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Christmas Card with Tree</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
  <style>
    body {
      font-family: Arial, sans-serif;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      margin: 0;
      background: linear-gradient(to bottom, #001f3f, #0074D9);
      overflow: hidden;
    }

    .card-container {
      perspective: 1000px; /* Enables 3D flipping effect */
    }

    .card {
      width: 300px;
      height: 400px;
      border: 2px solid #ddd;
      border-radius: 15px;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
      transform-style: preserve-3d;
      transition: transform 0.6s ease;
      cursor: pointer;
      position: relative;
    }

    .menu {
      position: absolute;
      top: -65px;
      right: 10px;
      z-index: 10;
    }

    .card:hover {
      transform: rotateY(180deg);
    }

    .card-content {
      position: absolute;
      width: 100%;
      height: 100%;
      backface-visibility: hidden;
      border-radius: 15px;
    }

    /* Card Front */
    .card-front {
      background: linear-gradient(to top, #ffafbd, #ffc3a0);
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
    }

    /* Tree Design */
    .tree {
      position: relative;
      width: 0;
      height: 0;
      border-left: 50px solid transparent;
      border-right: 50px solid transparent;
      border-bottom: 80px solid #2ecc71; /* Tree green color */
    }

    .tree-text {
      position: fixed; /* 固定位置 */
      top: 0; /* 靠頂部 */
      left: 0;
      width: 100%; /* 占滿整個寬度 */
      text-align: center; /* 水平置中 */
      padding: 10px 0; /* 增加上下間距 */
      font-size: 24px; /* 調整文字大小 */
      font-weight: bold;
      z-index: 1000; /* 確保在其他元素之上 */
    }

    .tree::before, .tree::after {
      content: '';
      position: absolute;
      left: -40px;
      top: 60px;
      width: 0;
      height: 0;
      border-left: 70px solid transparent;
      border-right: 70px solid transparent;
      border-bottom: 100px solid #2ecc71;
    }

    .tree::after {
      top: 120px;
      left: -60px;
      border-left: 90px solid transparent;
      border-right: 90px solid transparent;
      border-bottom: 120px solid #2ecc71;
    }

    .trunk {
      position: relative;
      width: 20px;
      height: 40px;
      background: #8e44ad;
      margin: 0 auto;
      margin-top: 10px;
    }

    .star {
      position: absolute;
      top: -30px;
      left: -15px;
      width: 0;
      height: 0;
      border-left: 15px solid transparent;
      border-right: 15px solid transparent;
      border-bottom: 25px solid #f1c40f;
      transform: rotate(35deg);
    }

    .star::before, .star::after {
      content: '';
      position: absolute;
      width: 0;
      height: 0;
      border-left: 15px solid transparent;
      border-right: 15px solid transparent;
      border-bottom: 25px solid #f1c40f;
    }

    .star::before {
      transform: rotate(-70deg);
      top: 0;
      left: -15px;
    }

    .star::after {
      transform: rotate(70deg);
      top: -15px;
      left: -15px;
    }

    /* Card Back */
    .card-back {
      background: linear-gradient(to bottom, #4facfe, #00f2fe);
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      transform: rotateY(180deg);
      text-align: center;
    }

    .card-back h1 {
      font-size: 24px;
      color: white;
      margin-bottom: 10px;
    }

    .card-back p {
      font-size: 18px;
      color: white;
      margin: 0 20px;
    }

    /* Snowflake Animation */
    .snowflake {
      position: absolute;
      color: white;
      font-size: 20px;
      top: -10%;
      animation: fall 10s linear infinite;
    }

    @keyframes fall {
      0%, 100% {
        transform: translateY(0) rotate(0deg);
        opacity: 1;
      }
      100% {
        transform: translateY(110vh) rotate(360deg);
        opacity: 0.5;
      }
    }

    /* Generate multiple snowflakes */
    .snowflake:nth-child(1) {
      left: 10%;
      animation-delay: 0s;
      animation-duration: 8s;
    }

    .snowflake:nth-child(2) {
      left: 20%;
      animation-delay: 2s;
      animation-duration: 6s;
    }

    .snowflake:nth-child(3) {
      left: 50%;
      animation-delay: 4s;
      animation-duration: 10s;
    }

    .snowflake:nth-child(4) {
      left: 70%;
      animation-delay: 6s;
      animation-duration: 7s;
    }

    .snowflake:nth-child(5) {
      left: 90%;
      animation-delay: 8s;
      animation-duration: 9s;
    }
  </style>
</head>
<body>
  <div class="card-container">
    <nav class="menu navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Menu
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="#">My Profile</a></li>
                            <li><a class="dropdown-item" href="#">Settings</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="#">Upgrade Plan</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="card">
      <div class="card-content card-front">
        <h3 class="tree-text">Merry Christmas!<br>Click to Flip</h3>
        <div class="tree">
          <div class="star"></div>
        </div>
        <div class="trunk"></div>
      </div>
      <div class="card-content card-back">
        <h1>Merry Christmas!</h1>
        <p>Wishing you joy, peace, and love this holiday season!</p>
      </div>
    </div>
  </div>

  <!-- Snowflake Effect -->
  <div class="snowflake">❄</div>
  <div class="snowflake">❄</div>
  <div class="snowflake">❄</div>
  <div class="snowflake">❄</div>
  <div class="snowflake">❄</div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>
