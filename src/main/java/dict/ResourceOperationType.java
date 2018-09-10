package dict;

import com.cloudcare.common.lang.annotation.Label;

import java.io.Serializable;

@Label("资源操作类型")
public enum ResourceOperationType implements Serializable {

    @Label("查看")
    VIEW,

    @Label("购买")
    PURCHASE,

    @Label("基础操作")
    OPERATION,

    @Label("管理操作")
    ADMIN,

    @Label("所有权限")
    ALL
}
