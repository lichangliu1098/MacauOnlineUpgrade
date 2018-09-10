package Utils.Exception;

import com.aliyuncs.exceptions.ErrorType;

public class AliyunApiException extends ProviderClientException {
    private ErrorType errorType;

    public AliyunApiException(String message, String errorCode, String requestId, ErrorType errorType, String requestUrl) {
        super(message, errorCode,requestId,requestUrl);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

}