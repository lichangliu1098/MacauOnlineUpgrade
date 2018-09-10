import Action.RamAction;
import Utils.SqlSessionIndustry;
import com.google.common.collect.Lists;
import dict.Database;
import model.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Maps;

import static Action.RamAction.*;


/**
 * Created by guyan on 2018/9/9.
 */
public class main {



    @Test
    public void test(){


//        modifyPolicyDocument("modifyPolicyDocument");

//        checkWhetherPolicyExist("系統管理員", new AccessKey("", ""));
        updateSystemRolePolicy();

//        RamAction ramAction = new RamAction();
//        ramAction.createOssRamGroup();
    }

    public static void main(String[] args) {
        String roleName = "系統管理員";
//        modifyPolicyDocument(roleName, "modifyPolicyDocument");
    }
}
