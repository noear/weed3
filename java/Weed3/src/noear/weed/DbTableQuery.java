package noear.weed;

/**
 * Created by noear on 14/11/12.
 *
 * $.       //表空间占位数（即数据库名）
 * $fcn     //SQL函数占位符
 * ?        //参数占位符
 * ?...     //数组型参数占位符
 */
public class DbTableQuery extends DbTableQueryBase<DbTableQuery> {
    public DbTableQuery(DbContext context) {
        super(context);
    }
}