package dict;

import com.cloudcare.common.lang.Messages;
import com.cloudcare.common.lang.annotation.Label;

/**
 * Created by guyan on 2018/9/10.
 */
public enum Database {
    userDatabase("cbis_user"),
    securityDatabase("cbis_security"),
    adapterDatabase("cbis_adapter");

    private String label;

    Database(String label) {
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}
