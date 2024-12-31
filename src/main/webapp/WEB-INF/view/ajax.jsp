<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.14.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://code.jquery.com/ui/1.14.1/jquery-ui.js"></script>
    <title>AJAX Example</title>
</head>
<body>
	<h1>AJAX Example-XMLHttpRequest</h1>
    <input type="text" id="InputID" placeholder="請輸入要查詢的ID">
    <button id="fetchButton">點擊取得資料</button>
    <div id="result"></div>
    <script>
        // 方法1:使用XMLHttpRequest
        document.getElementById("fetchButton").addEventListener("click", function() {
          const xhr = new XMLHttpRequest();
          const id = document.getElementById("inputId").value;
          // 檢查有沒有輸入id
          if(!id.trim()) { // 沒有輸入id
            document.getElementById("result").textContent = "請輸入ID";
            return;
          }

          xhr.open("GET", `http://localhost:8080/model?id=${"${id}"}`, true);
          xhr.onload = function () {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                document.getElementById("result").innerHTML =
                `<h2>${"${data.id}"}</h2><h3>${"${data.name}"}</h3>`
            } else {
                document.getElementById("result").textContent = "無法取得資料";
            }
          }
          xhr.send();
        })

        // 方法2:使用fetch API
        fetch(`/model?id=${"${id}"}`).then((response)=>{
            return response.json();
        }).then((data)=>{
            document.getElementById("result").innerHTML =
            `<h2>${"${data.id}"}</h2><h3>${"${data.name}"}</h3>`
        }).catch((error)=>{
            document.getElementById("result").textContent = `無法取得數據：${"${error.message}"}`;
        });

        // 方法3:使用JQuery ajax
        $(document).ready(function() {
            $("#fetchButton").click(()=> {
                const id = $("#InputID").val();
                $.ajax({
                    url: `/model?id=${"${id}"}`,
                    type: "GET",
                    dataType: "json",
                    success: function(data) {
                        $("#result").html(`<h2>${data.id}</h2><h3>${data.name}</h3>`);
                        data = {
                            id: 1,
                            name: "John",
                            jobs: [
                                {
                                    id: 1,
                                    name: "Manager",
                                    period: 2
                                },
                                {
                                    id: 2,
                                    name: "Engineer",
                                    period: 3
                                }

                            ]
                        }
                    },
                    error: function(xhr, status, error) {
                        $("#result").text(`無法取得數據:${error}`);
                    }
                });
            });
        });
    </script>
</body>
</html>