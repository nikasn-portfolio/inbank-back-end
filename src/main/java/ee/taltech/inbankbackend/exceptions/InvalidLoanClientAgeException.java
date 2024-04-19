package ee.taltech.inbankbackend.exceptions;

public class InvalidLoanClientAgeException extends ExceptionTemplate{
    public InvalidLoanClientAgeException(String message) {
        super(message, null);
    }
}
