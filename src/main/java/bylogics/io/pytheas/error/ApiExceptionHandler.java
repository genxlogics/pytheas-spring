package bylogics.io.pytheas.error;

import bylogics.io.pytheas.domain.Error;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleValidation(MethodArgumentNotValidException ex) {
        BindingResult bindingResult=ex.getBindingResult();
        FieldError fieldError=bindingResult.getFieldError();
        String defaultMsg=fieldError.getDefaultMessage();
        return new Error("INVALID_INPUT",defaultMsg);
    }
}
