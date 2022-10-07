package raf.domaci3.exceptions;

import org.springframework.http.HttpStatus;

public class NotAcceptableException extends CustomException{

    public NotAcceptableException(String message) {
        super(message, ErrorCode.NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE);
    }
}
