<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Login success page</title>
</head>
<body>
    <h1>This is login success page</h1>
    <p>name: ${name}</p>
    <p>email: ${email}</p>
    <p>token: ${token}</p>

    <button onclick="logout()">Logout</button> <!-- 新增的登出按鈕 -->
    <script>
        function logout() {
            fetch('/api/auth/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (response.ok) {
                    //alert('Logout successful!');
                    window.location.href = '/page/login'; // Redirect to login page after logout
                } else {
                    alert('Logout failed. Please try again.');
                }
            }).catch(error => {
                console.error('Error during logout:', error);
                alert('An error occurred. Please try again.');
            });
        }
    </script>
</body>
</html>