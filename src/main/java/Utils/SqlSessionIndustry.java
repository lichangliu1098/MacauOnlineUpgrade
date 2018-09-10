package Utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SqlSessionIndustry {

    private static final String CONFIGURATION_PATH = "mybatis-config.xml";
    private static final Map<String, org.apache.ibatis.session.SqlSessionFactory> SQLSESSIONFACTORYS = new HashMap();
 
    /**
     * 根据指定的DataSourceEnvironment获取对应的SqlSessionFactory
     */
    public static org.apache.ibatis.session.SqlSessionFactory getSqlSessionFactory(String datasource) {
 
        org.apache.ibatis.session.SqlSessionFactory sqlSessionFactory = SQLSESSIONFACTORYS.get(datasource);
        if (sqlSessionFactory != null)
            return sqlSessionFactory;
        else {
            InputStream inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream(CONFIGURATION_PATH);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, datasource);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SQLSESSIONFACTORYS.put(datasource, sqlSessionFactory);
            return sqlSessionFactory;
        }
    }
}