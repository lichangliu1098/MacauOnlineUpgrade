package Action;

import Utils.AliyunClient;
import Utils.Exception.AliyunApiException;
import Utils.SqlSessionIndustry;
import com.aliyuncs.ram.model.v20150501.DeleteGroupRequest;
import com.aliyuncs.ram.model.v20150501.ListUsersForGroupRequest;
import com.aliyuncs.ram.model.v20150501.ListUsersForGroupResponse;
import com.aliyuncs.ram.model.v20150501.RemoveUserFromGroupRequest;
import dict.AKType;
import dict.CloudType;
import dict.Database;
import model.AccessKey;
import model.Organization;
import model.ProviderAccount;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.util.List;

/**
 * Created by guyan on 2018/9/10.
 */
public class RollbackAction {
    static String ROOT_ORG_ID = "0";
    static String ROOT_ORG_GROUP_NAME = "root-organization-group-0";

    //执行此操作的前提是数据库已经回滚完毕
    public static void rollBackSystemPolicy() {
        RamAction.updateSystemRolePolicy();
    }

    //删除已经创建的ram group
    public static void rollBackRamGroup() {
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
        if (CollectionUtils.isNotEmpty(providerAccountList)) {
            SqlSession finalUserSession = userSession;
            providerAccountList.forEach(providerAccount -> {
                AccessKey key = new AccessKey(providerAccount.getAccessKeyId(),providerAccount.getAccessKeySecret());
                key.setAkType(AKType.Uid);
                key.setCloudType(CloudType.PrivateCloud);

                //查找租户下面的所有组织，包括部门和项目
                String tenantId = providerAccount.getTenantId();
                List<Organization> orgByTenantList = finalUserSession.selectList("selectOrgByTenant", tenantId);
                finalUserSession.commit();

                for (Organization org : orgByTenantList) {
                    deleteRamGroup(org.getId(), key);
                }
            });
        }
    }

    public static void deleteRamGroup(String groupName, AccessKey key) {
        if (groupName.equals(ROOT_ORG_ID)) {
            groupName = ROOT_ORG_GROUP_NAME;
        }

        //必须先删除ram group下所有的成员
        ListUsersForGroupRequest listUsersForGroupRequest = new ListUsersForGroupRequest();
        listUsersForGroupRequest.setGroupName(groupName);
        try {
            ListUsersForGroupResponse userListResponse = AliyunClient.getInstance().process(listUsersForGroupRequest, key);
            List<ListUsersForGroupResponse.User> userList = userListResponse.getUsers();
            for (ListUsersForGroupResponse.User user : userList) {
                RemoveUserFromGroupRequest removeUserFromGroupRequest = new RemoveUserFromGroupRequest();
                removeUserFromGroupRequest.setGroupName(groupName);
                removeUserFromGroupRequest.setUserName(user.getUserName());
                AliyunClient.getInstance().process(removeUserFromGroupRequest, key);
            }
        }catch (AliyunApiException e) {
            if ("EntityNotExist.Group".equals(e.getErrorCode())) {
                return;
            }
        }

        DeleteGroupRequest deleteGroupRequest = new DeleteGroupRequest();
        deleteGroupRequest.setGroupName(groupName);
        AliyunClient.getInstance().process(deleteGroupRequest, key);
    }

    @Test
    public void test() {

    }
}
