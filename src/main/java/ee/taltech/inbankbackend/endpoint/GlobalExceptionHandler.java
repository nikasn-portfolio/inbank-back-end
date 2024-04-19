package ee.taltech.inbankbackend.endpoint;

import ee.taltech.inbankbackend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InvalidPersonalCodeException.class, InvalidLoanAmountException.class, InvalidLoanPeriodException.class, InvalidLoanClientAgeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<DecisionResponse> handleLoanInputException(ExceptionTemplate ex) {
        DecisionResponse response = new DecisionResponse();
        return wrapIntoResponseEntity(ex,HttpStatus.BAD_REQUEST, response);
    }

    @ExceptionHandler(NoValidLoanException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<DecisionResponse> handleValidLoanException(NoValidLoanException ex) {
        DecisionResponse response = new DecisionResponse();
        return wrapIntoResponseEntity(ex,HttpStatus.NOT_FOUND, response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<DecisionResponse> handleAllExceptions(Exception ex) {
        DecisionResponse response = new DecisionResponse();
        return wrapIntoResponseEntityWithCustomErrorMessage("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    private ResponseEntity<DecisionResponse> wrapIntoResponseEntity (Throwable ex, HttpStatus status, DecisionResponse response){
        response.setErrorMessage(ex.getMessage());
        return ResponseEntity
                .status(status)
                .body(response);
    }
    private ResponseEntity<DecisionResponse> wrapIntoResponseEntityWithCustomErrorMessage (String errorMessage, HttpStatus status, DecisionResponse response){
        response.setErrorMessage(errorMessage);
        return ResponseEntity
                .status(status)
                .body(response);
    }
}
