package model;

import com.cloudcare.common.lang.annotation.Label;
import org.joda.time.DateTime;

import javax.validation.constraints.Size;

/**
 * Created by guyan on 2018/9/9.
 */
public class RamAccount {
    public RamAccount() {
        this.available = true;
    }

    @Size(max=64)
    @Label("ak编号")
    private String accessKeyId;

    @Size(max=64)
    @Label("ak密码")
    private String accessKeySecret;

    @Size(max=64)
    @Label("用户账户编号")
    private String accountId;

    @Label("可用状态")
    private Boolean available;

    @Size(max=24)
    @Label("id")
    private String id;

    @Size(max=256)
    @Label("ram子账号sso登出凭证")
    private String loginTicket;

    @Size(max=45)
    @Label("大数据用户编号")
    private String odpsBaseId;

    @Label("大数据组织编号")
    private Integer odpsTenantId;

    @Size(max=45)
    @Label("部门或者组织编号")
    private String orgId;

    @Size(max=64)
    @Label("ram用户id")
    private String ramUserId;

    @Size(max=64)
    @Label("ramUserPwd")
    private String ramUserPwd;

    @Size(max=64)
    @Label("资源池编号")
    private String resourcePoolId;

    @Size(max=64)
    @Label("角色名称")
    private String roleName;

    @Size(max=45)
    @Label("租户编号")
    private String tenantId;

    @Size(max=64)
    @Label("部门/项目uid")
    private String uid;

    @Size(max=64)
    @Label("用户名")
    private String username;

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRamUserId() {
        return ramUserId;
    }

    public void setRamUserId(String ramUserId) {
        this.ramUserId = ramUserId;
    }

    public String getRamUserPwd() {
        return ramUserPwd;
    }

    public void setRamUserPwd(String ramUserPwd) {
        this.ramUserPwd = ramUserPwd;
    }

    public String getResourcePoolId() {
        return resourcePoolId;
    }

    public void setResourcePoolId(String resourcePoolId) {
        this.resourcePoolId = resourcePoolId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return String.format("RamAccount { accessKeyId : %s,accessKeySecret : %s,accountId : %s,available : %s,createTime : %s,id : %s,loginTicket : %s,odpsBaseId : %s,odpsTenantId : %s,orgId : %s,ramUserId : %s,ramUserPwd : %s,resourcePoolId : %s,roleName : %s,tenantId : %s,uid : %s,updateTime : %s,username : %s }",accessKeyId,accessKeySecret,accountId,available,id,loginTicket,odpsBaseId,odpsTenantId,orgId,ramUserId,ramUserPwd,resourcePoolId,roleName,tenantId,uid,username);
    }
}
