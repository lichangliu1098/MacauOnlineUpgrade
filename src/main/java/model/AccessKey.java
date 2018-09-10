package model;

import dict.AKType;
import dict.CloudType;

/**
 * Created by guyan on 2018/9/9.
 */
public class AccessKey {
    private CloudType cloudType;
    private String url;
    private String accessKeyId;
    private String accessKeySecret;
    private AKType akType;

    public AccessKey() {
        this(null, null);
    }

    public AccessKey(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.cloudType = CloudType.PublicCloud;
        this.akType = AKType.Bid;
    }

    public CloudType getCloudType() {
        return cloudType;
    }

    public void setCloudType(CloudType cloudType) {
        this.cloudType = cloudType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public AKType getAkType() {
        return akType;
    }

    public void setAkType(AKType akType) {
        this.akType = akType;
    }

    @Override
    public String toString() {
        return "AccessKey{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                '}';
    }
}
