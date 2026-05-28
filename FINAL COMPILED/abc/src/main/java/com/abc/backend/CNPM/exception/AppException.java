package main.java.com.abc.backend.CNPM.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    // ── Static factory helpers ──────────────────────────────

    public static AppException khongTimThay(String entity, Object id) {
        return new AppException(entity + " không tìm thấy với ID: " + id, HttpStatus.NOT_FOUND);
    }

    public static AppException daTotnai(String entity, String field, Object value) {
        return new AppException(entity + " đã tồn tại với " + field + ": " + value, HttpStatus.CONFLICT);
    }

    public static AppException duLieuKhongHopLe(String message) {
        return new AppException(message, HttpStatus.BAD_REQUEST);
    }
}
