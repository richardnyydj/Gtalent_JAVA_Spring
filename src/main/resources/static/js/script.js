console.log("DOM 已加載完成(使用 defer)");
const button_defer = document.getElementById("btn");
button_defer.addEventListener("click", (e) => {
    alert(`按鈕已被點擊！(使用 defer) - 事件類型: ${e.type}, 按鈕 ID: ${e.target.id}`);
});