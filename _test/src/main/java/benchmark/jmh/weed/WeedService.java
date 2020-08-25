package benchmark.jmh.weed;

import benchmark.jmh.BaseService;
import benchmark.jmh.DataSourceHelper;
import benchmark.jmh.weed.mapper.WeedSQLUserMapper;
import benchmark.jmh.weed.model.WeedSQLSysUser;
import benchmark.jmh.weed.model.WeedSysCustomer;
import com.ibm.db2.jcc.am.SqlClientInfoException;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WeedService implements BaseService {
    WeedSQLUserMapper userMapper;
    BaseMapper<WeedSysCustomer> customerMapper;
    AtomicInteger idGen = new AtomicInteger(1000);

    DbContext db;

    public void init() {
        DataSource dataSource = DataSourceHelper.ins();

        this.db = new DbContext("user", dataSource);
        this.userMapper = db.mapper(WeedSQLUserMapper.class);
        this.customerMapper = db.mapperBase(WeedSysCustomer.class);
    }


    @Override
    public void addEntity() {
        WeedSQLSysUser sqlSysUser = new WeedSQLSysUser();
        sqlSysUser.setId(idGen.getAndIncrement());
        sqlSysUser.setCode("abc");

       Long tmp =  userMapper.insert(sqlSysUser, false);

        //System.out.println(tmp);
    }


    @Override
    public Object getEntity() {
        Object tmp=  userMapper.selectById(1);

        //System.out.println(tmp);
        return tmp;
    }



    @Override
    public void lambdaQuery() {
        List<WeedSQLSysUser> list = userMapper.selectList(wq -> wq.whereEq(WeedSQLSysUser::getId, 1));
    }

    @Override
    public void executeJdbcSql() {
        WeedSQLSysUser user = userMapper.selectById(1);
    }

    public void executeJdbcSql2() throws SQLException{
        WeedSQLSysUser user = db.sql("select * from sys_user where id = ?",1)
                .getItem(WeedSQLSysUser.class);
    }

    @Override
    public void executeTemplateSql() {
        WeedSQLSysUser user = userMapper.selectTemplateById(1);
    }

    public void executeTemplateSql2() throws SQLException {
        WeedSQLSysUser user = db.call("select * from sys_user where id = @{id}")
                .set("id",1).getItem(WeedSQLSysUser.class);
    }

    @Override
    public void sqlFile() {
        WeedSQLSysUser user = userMapper.userSelect(1);
        //System.out.println(user);
    }

    @Override
    public void one2Many() {

    }


    @Override
    public void pageQuery() {
        List<WeedSQLSysUser> list = userMapper.queryPage("用户一", 1, 5);
        long count = userMapper.selectCount(wq->wq.whereEq("code","用户一"));
        //System.out.println(list);
    }

    @Override
    public void complexMapping() {

    }
}
