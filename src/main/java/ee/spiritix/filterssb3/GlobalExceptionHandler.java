package ee.spiritix.filterssb3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<>("HTTP 400 Error occured: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    return new ResponseEntity<>("An unexpected HTTP 500 error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}