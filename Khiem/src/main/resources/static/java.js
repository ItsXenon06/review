<script>
function sendNotification(button) {
    // Lấy ID từ thuộc tính data-id của nút được nhấn
    const contractId = button.getAttribute('data-id');

    // Gửi yêu cầu POST lên server
    fetch(`/api/contracts/send-notification/${contractId}`, {
        method: 'POST'
    })
    .then(response => {
        // Kiểm tra phản hồi từ Server
        return response.text().then(text => {
            if (response.ok) {
                alert("Thành công: " + text); // Hiện thông báo màu xanh/thành công
            } else {
                alert("Lỗi: " + text); // Hiện thông báo lỗi (ví dụ: chưa hết hạn)
            }
        });
    })
    .catch(error => {
        console.error('Lỗi:', error);
        alert("Không thể kết nối đến máy chủ!");
    });
}
</script>