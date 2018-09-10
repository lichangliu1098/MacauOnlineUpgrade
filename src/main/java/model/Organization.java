package model;

import com.cloudcare.common.lang.annotation.Label;
import org.joda.time.DateTime;

import javax.validation.constraints.Size;

public class Organization {

    public Organization() {
        this.available = true;
    }

    @Label("是否有效")
    private Boolean available;

    @Size(max=64)
    @Label("业务编号")
    private String bizId;

    @Label("能否管理该租户")
    private Boolean canAdminTenant;

    @Label("能否管理该钱包")
    private Boolean canAdminWallet;

    @Size(max=32)
    @Label("分类")
    private String category;

    @Label("創建時間")
    private DateTime createTime;

    @Size(max=64)
    @Label("描述")
    private String description;

    @Size(max=24)
    @Label("編號")
    private String id;

    @Label("级别")
    private Integer level;

    @Size(max=128)
    @Label("名称")
    private String name;

    @Size(max=32)
    @Label("組織類型，如部門、職位、企業或者項目組等")
    private String orgType;

    @Size(max=24)
    @Label("上级编号")
    private String pid;

    @Size(max=24)
    @Label("超级管理员用户")
    private String superAdminUser;

    @Size(max=24)
    @Label("對應的租戶")
    private String tenantId;

    @Label("最后修改时间")
    private DateTime updateTime;

    @Size(max=24)
    @Label("钱包")
    private String walletId;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Boolean getCanAdminTenant() {
        return canAdminTenant;
    }

    public void setCanAdminTenant(Boolean canAdminTenant) {
        this.canAdminTenant = canAdminTenant;
    }

    public Boolean getCanAdminWallet() {
        return canAdminWallet;
    }

    public void setCanAdminWallet(Boolean canAdminWallet) {
        this.canAdminWallet = canAdminWallet;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSuperAdminUser() {
        return superAdminUser;
    }

    public void setSuperAdminUser(String superAdminUser) {
        this.superAdminUser = superAdminUser;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public String toString() {
         return String.format("Organization { available : %s,bizId : %s,canAdminTenant : %s,canAdminWallet : %s,category : %s,createTime : %s,description : %s,id : %s,level : %s,name : %s,orgType : %s,pid : %s,superAdminUser : %s,tenantId : %s,updateTime : %s,walletId : %s }",available,bizId,canAdminTenant,canAdminWallet,category,createTime,description,id,level,name,orgType,pid,superAdminUser,tenantId,updateTime,walletId);
    }

}