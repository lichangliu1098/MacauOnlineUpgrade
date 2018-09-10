package Utils.Exception;

import com.cloudcare.common.lang.BizRuntimeException;
import com.cloudcare.common.lang.ResponseCode;

public class ProviderClientException extends BizRuntimeException {
    private String requestUrl;

    public ProviderClientException(String message, String errorCode, String requestId, String requestUrl) {
        super(message, ResponseCode.INTERNAL_SERVER_ERROR, errorCode);
        this.requestUrl = requestUrl;
        setRequestId(requestId);
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

}