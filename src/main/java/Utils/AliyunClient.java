package Utils;

import Utils.Exception.AliyunApiException;
import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.http.X509TrustAll;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.common.collect.Maps;
import model.AccessKey;

import java.util.Map;

/**
 * Created by guyan on 2018/9/9.
 */
public class AliyunClient {
    private String privateRegionId = "cn-neimeng-env10-d01";

    private static AliyunClient singleInstance;
    private Map<String, String> productDomainMap;

    private AliyunClient() {
        productDomainMap = Maps.newHashMap();
        productDomainMap.put("Ram", "ram.aliyun.com");
    }

    public synchronized static AliyunClient getInstance() {
        if (null == singleInstance) {
            singleInstance = new AliyunClient();
        }
        return singleInstance;
    }


    public <T extends AcsResponse> T process(AcsRequest<T> request, AccessKey key){
        System.setProperty("socksProxySet", "true");
        System.setProperty("socksProxyHost", "localhost");
        System.setProperty("socksProxyPort", "1080");

        String regionId = request.getRegionId();
        String product = request.getProduct();
        String endpoint = productDomainMap.get(product);
        request.setRegionId(privateRegionId);

        try {
            DefaultProfile.addEndpoint("aliyun.com", privateRegionId, product, endpoint);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IClientProfile profile = DefaultProfile.getProfile(regionId, key.getAccessKeyId(), key.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        request.setConnectTimeout(60000);
        request.setReadTimeout(60000);
//        request.setHttpContentType(FormatType.JSON);
        request.setAcceptFormat(FormatType.JSON);

        if (ProtocolType.HTTPS.equals(request.getProtocol())) {
            X509TrustAll.ignoreSSLCertificate();
        }

        try {
            T response = client.getAcsResponse(request, true, 3);

            return response;
        } catch (ClientException e) {
            e.printStackTrace();
            throw new AliyunApiException(e.getErrMsg(), e.getErrCode(), e.getRequestId(), e.getErrorType(), request.getUrl());
        } finally {
        }
    }
}
