package br.com.unisinos.es.t2.adapter.in.web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
class ApiResponse<T> {
    private int statusCode;
    private String error;
    private String errorDetail;
    private String message;
    private T data;

    public static ResponseEntity<ApiResponse<Void>> success(int statusCode, String message) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.statusCode = statusCode;
        response.message = message;
        return ResponseEntity.status(statusCode).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(int statusCode, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.statusCode = statusCode;
        response.message = message;
        response.data = data;
        return ResponseEntity.status(statusCode).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> error(int statusCode, String error) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.statusCode = statusCode;
        response.error = error;
        return ResponseEntity.status(statusCode).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> error(int statusCode, String error, String errorDetail) {
        ApiResponse<Void> response = new ApiResponse<>();
        response.statusCode = statusCode;
        response.error = error;
        response.errorDetail = errorDetail;
        return ResponseEntity.status(statusCode).body(response);
    }
}
