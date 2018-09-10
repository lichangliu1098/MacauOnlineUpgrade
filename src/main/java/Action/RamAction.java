package Action;

import Utils.AliyunClient;
import Utils.Exception.AliyunApiException;
import Utils.SqlSessionIndustry;
import com.aliyuncs.ram.model.v20150501.*;
import com.cloudcare.common.lang.LocaleBizServiceException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dict.*;
import model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

/**
 * Created by guyan on 2018/9/9.
 */
public class RamAction {
    public static final String SYSTEM = "System";
    public static final String CUSTOM = "Custom";
    String ROOT_ORG_ID = "0";
    String ROOT_ORG_GROUP_NAME = "root-organization-group-0";


    public String  createPolicyVersionAndSetDefault(String roleName, String rolePolicyDocumentStr, AccessKey key){
        CreatePolicyVersionRequest request = new CreatePolicyVersionRequest();
        request.setPolicyName(roleName);
        request.setPolicyDocument(rolePolicyDocumentStr);
        request.setSetAsDefault(Boolean.TRUE);
        CreatePolicyVersionResponse response = AliyunClient.getInstance().process(request, key);
        return response.getPolicyVersion().getVersionId();
    }

    public List<ListPolicyVersionsResponse.PolicyVersion> listPolicyVersions(String roleName, AccessKey key){
        ListPolicyVersionsRequest request = new ListPolicyVersionsRequest();
        request.setPolicyName(roleName);
        request.setPolicyType(RamAction.CUSTOM);
        ListPolicyVersionsResponse response = AliyunClient.getInstance().process(request, key);
        return response.getPolicyVersions();
    }

    public void deletePolicyVersion(String roleName, String versionId, AccessKey key){
        DeletePolicyVersionRequest request=new DeletePolicyVersionRequest();
        request.setPolicyName(roleName);
        request.setVersionId(versionId);
        DeletePolicyVersionResponse response = AliyunClient.getInstance().process(request, key);
    }

    public void createOssRamGroup () {
        SqlSessionFactory adapterFactory = SqlSessionIndustry.getSqlSessionFactory(Database.adapterDatabase.getLabel());
        SqlSession adapterSession = adapterFactory.openSession();

        List<ProviderAccount> providerAccountList = null;
        try {
            providerAccountList = adapterSession.selectList("queryAvailableProviderAccount");
            adapterSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            adapterSession.close();
        }

        //连接cbis_user数据库
        SqlSessionFactory userFactory = SqlSessionIndustry.getSqlSessionFactory(Database.userDatabase.getLabel());
        SqlSession userSession = userFactory.openSession();
        List<Organization> orgList = Lists.newArrayList();
        Map<String, List<String>> orgMemberMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(providerAccountList)) {
            SqlSession finalUserSession = userSession;
            SqlSession finalAdapterSession = adapterSession;
            providerAccountList.forEach(providerAccount -> {
                AccessKey key = new AccessKey(providerAccount.getAccessKeyId(),providerAccount.getAccessKeySecret());
                key.setAkType(AKType.Uid);
                key.setCloudType(CloudType.PrivateCloud);

                //查找租户下面的所有组织，包括部门和项目
                String tenantId = providerAccount.getTenantId();
                List<Organization> orgByTenantList = finalUserSession.selectList("selectOrgByTenant", tenantId);
                finalUserSession.commit();
                orgList.addAll(orgByTenantList);

                //对于每个org，找出其所有成员
                for (Organization org : orgByTenantList) {
                    String orgId = org.getId();
                    List<String> memberList = finalUserSession.selectList("selectMemberByOrg", orgId);
                    finalUserSession.commit();

                    //将部门或项目下的每个成员都加入到对应的ramGroup中
                    if (CollectionUtils.isNotEmpty(memberList)) {
                        String accountIdStr = StringUtils.join(memberList, "','");
                        List<RamAccount> ramAccountList = finalAdapterSession.selectList("queryRamAccountByAccountId", accountIdStr);
                        finalUserSession.commit();
                        System.out.println();
//                        joinGroup(orgId, ramAccountList, key);
                    }
                    orgMemberMap.put(org.getId(), memberList);
                }
            });
        }
    }

    public void joinGroup(String groupName, List<RamAccount> ramAccountList, AccessKey key) {
        if (groupName.equals(ROOT_ORG_ID)) {
            groupName = ROOT_ORG_GROUP_NAME;
        }

        createRamGroup(groupName, key);
        for (RamAccount ramAccount : ramAccountList) {
            tryJoinRamGroup(groupName, ramAccount.getUsername(), key);
        }
    }

    private void tryJoinRamGroup(String groupName, String username, AccessKey key) {
        try {
            AddUserToGroupRequest request = new AddUserToGroupRequest();
            request.setGroupName(groupName);
            request.setUserName(username);
            AliyunClient.getInstance().process(request, key);
        } catch (AliyunApiException e) {
            if (!e.getErrorCode().equals("EntityAlreadyExists.User.Group")) {
                throw e;
            }
        }
    }

    private void createRamGroup(String groupName, AccessKey key) {
        try {
            GetGroupRequest getGroupRequest = new GetGroupRequest();
            getGroupRequest.setGroupName(groupName);
            AliyunClient.getInstance().process(getGroupRequest, key);
        } catch (AliyunApiException e) {
            if (e.getErrorCode().equals("EntityNotExist.Group")) {
                CreateGroupRequest groupRequest = new CreateGroupRequest();
                groupRequest.setGroupName(groupName);
                AliyunClient.getInstance().process(groupRequest, key);
            }
        }
    }



    public static void updateRolePolicy(String policyName, List<String> policyDocumentList, AccessKey uidAccessKey) {
        String rolePolicyDocumentStr = "{\"Statement\":[],\"Version\": \"1\"}";
        Map<String, List> rolePolicyDocumentMap = com.cloudcare.common.lang.serialize.JSON.toBean(rolePolicyDocumentStr, HashMap.class);
        List<Map<String, String>> statementList = new ArrayList<>();
        for (String  policyDoc : policyDocumentList) {
            if (isNotEmpty(policyDoc)) {
                List<Map<String, String>> list = (List<Map<String, String>>) com.cloudcare.common.lang.serialize.JSON.toBean(policyDoc, HashMap.class).get("Statement");
                statementList.addAll(list);
            } else {
//                System.out.println((MarkerFactory.getMarker(LogMarker.BUSINESS), getClass().getSimpleName() + ",privilege -" + privilegeResult.getName() + "- policy document is null"));
            }
        }
        rolePolicyDocumentMap.put("Statement", statementList);

        if (rolePolicyDocumentMap.get("Statement").size() > 0) {
            rolePolicyDocumentStr = com.cloudcare.common.lang.serialize.JSON.toString(rolePolicyDocumentMap);
            //长度检查
            if (rolePolicyDocumentStr.length() >= 2048) {
                throw new LocaleBizServiceException("Aliyun.Ram.PolicyDocumentOverLen-2048-Bytes");
            }
            // TODO: 2018/9/10 暂时不更新策略
            updatePolicyAndSetDefault(policyName, rolePolicyDocumentStr, uidAccessKey);
        }
    }

    private static void updatePolicyAndSetDefault(String roleName, String rolePolicyDocumentStr, AccessKey key) {
        RamAction policyAction = new RamAction();

        String versionId = policyAction.createPolicyVersionAndSetDefault(roleName, rolePolicyDocumentStr, key);

        //获取其他版本策略，并删除，只保留新创建的默认版本
        List<ListPolicyVersionsResponse.PolicyVersion> policyVersionList = policyAction.listPolicyVersions(roleName, key);

        //遍历删除所有非默认策略
        policyVersionList.forEach(policyVersion -> {
            if (!policyVersion.getIsDefaultVersion()) {
                policyAction.deletePolicyVersion(roleName, policyVersion.getVersionId(), key);
            }
        });
    }

    public static boolean checkWhetherPolicyExist(String roleName, AccessKey uidAccessKey) {
        try {
            GetPolicyRequest request = new GetPolicyRequest();
            request.setPolicyName(roleName);
            request.setPolicyType(CUSTOM);
            GetPolicyResponse response = AliyunClient.getInstance().process(request, uidAccessKey);

            GetPolicyVersionRequest versionRequest = new GetPolicyVersionRequest();
            versionRequest.setPolicyName(roleName);
            versionRequest.setVersionId(response.getPolicy().getDefaultVersion());
            GetPolicyVersionResponse process = AliyunClient.getInstance().process(versionRequest, uidAccessKey);

            return response.getPolicy()== null ? false : true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void updateSystemRolePolicy() {
        SqlSessionFactory securitySessionFactory = SqlSessionIndustry.getSqlSessionFactory(Database.securityDatabase.getLabel());
        SqlSession securitySession = securitySessionFactory.openSession();

        SqlSessionFactory adapterSessionFactory = SqlSessionIndustry.getSqlSessionFactory(Database.adapterDatabase.getLabel());
        SqlSession adapterSession = adapterSessionFactory.openSession();

        try {
            //Step2:根据现有系统默认角色在数据库中的权限，直接修改系统该系统默认角色的ram策略（每个系统默认角色的ram策略在阿里侧只有一个，为所有权限组装成的字符串）
            Map<String, List<RolePolicy>> rolePrivilegesMap = Maps.newHashMap();
            List<RolePolicy> rolePolicyList = securitySession.selectList("queryRolePolicy");
            securitySession.commit();
            for (RolePolicy rolePolicy : rolePolicyList) {
                String policyName = rolePolicy.getName();

                List<RolePolicy> rolePolicies = rolePrivilegesMap.get(policyName);
                if (rolePolicies == null) {
                    rolePolicies = Lists.newArrayList();
                }

                //去掉购买权限
                boolean condition = (rolePolicy.getResourceProviderName().equals("aliyun") || rolePolicy.getResourceProviderName().equals("aliyunPrivate"))
                        && nonNull(rolePolicy.getResourceOperationType()) && isNotEmpty(rolePolicy.getPolicyDocument());
                if (condition) {
                    if (!rolePolicy.getResourceOperationType().equals(ResourceOperationType.PURCHASE)) {
                        rolePolicies.add(rolePolicy);
                    }
                }

                rolePrivilegesMap.put(rolePolicy.getName(), rolePolicies);
            }

            for (Map.Entry<String, List<RolePolicy>> entry : rolePrivilegesMap.entrySet()) {
                String policyName = entry.getKey();
                List<RolePolicy> rolePolicies = entry.getValue();

                //同种资源同时包含查看和操作资源权限的，去掉查看权限，因为操作权限包含了查看权限
                //倒着删除，避开list删除陷阱
                for (int i = rolePolicies.size() - 1; i >= 0; i--) {
                    RolePolicy privilegeResultCache = rolePolicies.get(i);
                    if (privilegeResultCache.getResourceOperationType().equals(ResourceOperationType.OPERATION)) {
                        for (int j = rolePolicies.size() - 1; j >= 0; j--) {
                            RolePolicy privilegeResultInner = rolePolicies.get(j);
                            if (privilegeResultInner.getResourceName().equals(privilegeResultCache.getResourceName()) && privilegeResultInner.getResourceOperationType().equals(ResourceOperationType.VIEW)) {
                                rolePolicies.remove(privilegeResultInner);
                                break;
                            }
                        }
                    }
                }

                //查找出有对应角色用户的uid
                ProviderAccount providerAccount = adapterSession.selectOne("queryProviderAccountByRole", policyName);
                adapterSession.commit();
                if (Objects.nonNull(providerAccount)) {
                    AccessKey uidAccessKey = new AccessKey(providerAccount.getAccessKeyId(), providerAccount.getAccessKeySecret());
                    if (checkWhetherPolicyExist(policyName, uidAccessKey)) {
                        List<String> policyDocumentList = rolePrivilegesMap.get(policyName).stream().map(r -> r.getPolicyDocument()).collect(Collectors.toList());
                        updateRolePolicy(policyName, policyDocumentList, uidAccessKey);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("updateSystemRolePolicy error : " + e.getMessage());
            e.printStackTrace();
            throw e ;
        } finally {
            securitySession.close();
            adapterSession.close();
        }

        //Step3: 对于当前系统中的所有active用户，创建其所在的部门&项目的ram group，并把用户对应的ram account加入到ram group
    }

    public static void modifyPolicyDocument(String updateAction) {
        //Step1:修复现有产品oss、quickbi、datahub权限的策略
        SqlSessionFactory securitySessionFactory = SqlSessionIndustry.getSqlSessionFactory(Database.securityDatabase.getLabel());
        SqlSession securitySession = securitySessionFactory.openSession();
        try {
            securitySession.update(updateAction);
            securitySession.commit();
        } catch (Exception e) {
            System.out.println("modifyPolicyDocument error : " + e.getMessage());
            e.printStackTrace();
            throw e ;
        } finally {
            securitySession.close();
        }
    }

    @Test
    public void test(){
        RamAction.updateSystemRolePolicy();
//        RollbackAction.deleteRamGroup("test1",  new AccessKey("olmojHDglsqbXpiz", "DLShZXgKm4bWs1TieYugAXtoOCzq2T"));
//        CreateGroupRequest getGroupRequest = new CreateGroupRequest();
//        getGroupRequest.setGroupName("test");
//        AliyunClient.getInstance().process(getGroupRequest, new AccessKey("olmojHDglsqbXpiz", "DLShZXgKm4bWs1TieYugAXtoOCzq2T"));
//
//        AddUserToGroupRequest request = new AddUserToGroupRequest();
//        request.setGroupName("test");
//        request.setUserName("test_admin");
//        AliyunClient.getInstance().process(request, new AccessKey("olmojHDglsqbXpiz", "DLShZXgKm4bWs1TieYugAXtoOCzq2T"));
//
//        DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest();
//        deleteGroupRequest.setGroupName("test");
//        AliyunClient.getInstance().process(deleteGroupRequest, new AccessKey("olmojHDglsqbXpiz", "DLShZXgKm4bWs1TieYugAXtoOCzq2T"));
    }
}
