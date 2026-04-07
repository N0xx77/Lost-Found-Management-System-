package service;

public class AppException extends Exception{
    final private String errorCode;

    public AppException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        return this.errorCode;
    }
    
}
