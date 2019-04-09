package bylogics.io.pytheas.error;

import bylogics.io.pytheas.domain.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DBExceptionHandler {
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(code= HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleDBException(RecordNotFoundException db){
        return new Error("NOT_FOUND",db.getMessage());
    }
    @ExceptionHandler(DataIntigrityException.class)
    @ResponseStatus(code= HttpStatus.CONFLICT)
    @ResponseBody
    public Error handleDBException(DataIntigrityException db){
        return new Error("DATA_CONFLICT",db.getMessage());
    }

}
