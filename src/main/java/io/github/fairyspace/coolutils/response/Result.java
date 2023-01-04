package io.github.fairyspace.coolutils.response;


public class Result<T> {
    private Integer code;

    private String message;

    private T data;

    private Result(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
        this.data = data;
    }

    private Result(Integer code,String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static <T> Result<T> success(Integer code, String message, T data) {
        return new Result<T>(code,message, data);
    }



    public static Result<Void> success() {
        return new Result<Void>(ResultStatus.SUCCESS, null);
    }


    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultStatus.SUCCESS, data);
    }


    public static <T> Result<T> success(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return success(data);
        }
        return new Result<T>(resultStatus, data);
    }


    public static <T> Result<T> failure(Integer code,String message,T data) {
        return new Result<T>(code,message, data);
    }



    public static <T> Result<T> failure() {
        return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
    }


    public static <T> Result<T> failure(ResultStatus resultStatus) {
        return failure(resultStatus, null);
    }


    public static <T> Result<T> failure(ResultStatus resultStatus, T data) {
        if (resultStatus == null) {
            return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR, null);
        }
        return new Result<T>(resultStatus, data);
    }

    public static <T>Result<T> failure(String message){
        return new Result<T>(ResultStatus.INTERNAL_SERVER_ERROR.getCode(),message, null);
    }

    public static <T>Result<T> failure(Integer code,String message){
        return new Result<T>(code,message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
