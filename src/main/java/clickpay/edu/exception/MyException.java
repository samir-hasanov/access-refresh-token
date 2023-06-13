package clickpay.edu.exception;

public class MyException extends Exception{
    private Integer code;

    public MyException(Integer code,String message) {
        super(message);
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }
}
