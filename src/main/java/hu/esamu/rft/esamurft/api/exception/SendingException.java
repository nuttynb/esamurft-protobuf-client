package hu.esamu.rft.esamurft.api.exception;

public class SendingException extends Exception {

    public SendingException() {
        super();
    }

    public SendingException(String message) {
        super(message);
    }

    public SendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
