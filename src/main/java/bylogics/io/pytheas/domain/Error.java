package bylogics.io.pytheas.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(content = JsonInclude.Include.NON_NULL)
public class Error {
    @JsonProperty("serverCode")
    private String errorCode;
    @JsonProperty("errorMessage")
    private String message;

    public Error(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public Error() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
