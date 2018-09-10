package model;

import dict.ResourceOperationType;

/**
 * Created by guyan on 2018/9/9.
 */
public class RolePolicy {
    private String name;
    private String resourceProviderName;
    private ResourceOperationType resourceOperationType;
    private String policyDocument;
    private String resourceName;

    public String getPolicyDocument() {
        return policyDocument;
    }

    public void setPolicyDocument(String policyDocument) {
        this.policyDocument = policyDocument;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceProviderName() {
        return resourceProviderName;
    }

    public void setResourceProviderName(String resourceProviderName) {
        this.resourceProviderName = resourceProviderName;
    }

    public ResourceOperationType getResourceOperationType() {
        return resourceOperationType;
    }

    public void setResourceOperationType(ResourceOperationType resourceOperationType) {
        this.resourceOperationType = resourceOperationType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
