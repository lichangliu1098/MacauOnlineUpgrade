package model;

import com.cloudcare.common.lang.annotation.Label;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by guyan on 2018/9/9.
 */
public class ProviderAccount {
    private String accessKeyId;

    private String accessKeySecret;

    private Boolean available;

    private String id;

    private String loginTicket;

    private String odpsBaseId;

    private Integer odpsTenantId;

    private String password;

    private String resourcePoolId;

    private String resourceProviderId;

    private String tenantId;

    private String uid;

    private String user;

    private Long version;

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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginTicket() {
        return loginTicket;
    }

    public void setLoginTicket(String loginTicket) {
        this.loginTicket = loginTicket;
    }

    public String getOdpsBaseId() {
        return odpsBaseId;
    }

    public void setOdpsBaseId(String odpsBaseId) {
        this.odpsBaseId = odpsBaseId;
    }

    public Integer getOdpsTenantId() {
        return odpsTenantId;
    }

    public void setOdpsTenantId(Integer odpsTenantId) {
        this.odpsTenantId = odpsTenantId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResourcePoolId() {
        return resourcePoolId;
    }

    public void setResourcePoolId(String resourcePoolId) {
        this.resourcePoolId = resourcePoolId;
    }

    public String getResourceProviderId() {
        return resourceProviderId;
    }

    public void setResourceProviderId(String resourceProviderId) {
        this.resourceProviderId = resourceProviderId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
