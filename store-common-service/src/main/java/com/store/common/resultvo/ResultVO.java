package com.store.common.resultvo;

import com.store.common.constant.StatusCode;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ResultVO<T> implements Serializable {
    private Integer code;
    private String message;
    private Boolean success;
    private T data;
    private LocalDateTime timestamp;

    public ResultVO() {
        this.timestamp = LocalDateTime.now();
    }

    public ResultVO(Integer code, String message, Boolean success, T data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResultVO<T> success() {
        return new ResultVO<>(StatusCode.SUCCESS_CODE, "操作成功", true, null);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(StatusCode.SUCCESS_CODE, "操作成功", true, data);
    }

    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(StatusCode.SUCCESS_CODE, message, true, data);
    }

    public static <T> ResultVO<T> fail() {
        return new ResultVO<>(StatusCode.FAIL_CODE, "操作失败", false, null);
    }

    public static <T> ResultVO<T> fail(String message) {
        return new ResultVO<>(StatusCode.FAIL_CODE, message, false, null);
    }

    public static <T> ResultVO<T> fail(Integer code, String message) {
        return new ResultVO<>(code, message, false, null);
    }


}
