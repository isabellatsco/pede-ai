package br.com.ajufood.pedeai.exception;

public class UnprocessableContentException extends RuntimeException {
    public UnprocessableContentException(String msg) {
        super(msg);
    }
}
