package ee.taltech.inbankbackend.exceptions;

public abstract class ExceptionTemplate extends Exception {
    private final String message;
    private final Throwable cause;

    public ExceptionTemplate(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
