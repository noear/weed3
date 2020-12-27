package weed3test.hbase;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.noear.snack.ONode;
import org.noear.weed.DbContext;

import java.util.List;
import java.util.Map;

/**
 * @author noear 2020/12/27 created
 */
public class HbaseTest {

    private final static String aliyunUrl = "";

    private final static HikariDataSource dbPhoenixCfg() {
        HikariDataSource ds = new HikariDataSource();

        ds.setJdbcUrl("jdbc:phoenix:thin:url=" + aliyunUrl + ";serialization=PROTOBUF");
        ds.setDriverClassName("org.apache.phoenix.queryserver.client.Driver");


        return ds;
    }

    @Test
    public void test() throws Exception {
        DbContext db = new DbContext(dbPhoenixCfg());

        //db.exe("UPSERT INTO us_population VALUES('CN','Hang Zhou',?)",1);
        //db.exe("UPSERT INTO us_population VALUES('CN','Bei jing',?)",2);

        List<Map<String,Object>> dl = db.table("us_population").limit(2).select("*").getMapList();

        System.out.println(db.lastCommand.fullText());

//        DataItem dl = db.sql("SELECT state as \"State\",count(city) as \"City Count\",sum(population) as \"Population Sum\" " +
//                "FROM us_population WHERE State = ?" +
//                "GROUP BY state " +
//                "ORDER BY sum(population) DESC LIMIT 1","CN")
//                .getDataItem();

        System.out.println(ONode.stringify(dl));
    }
}
