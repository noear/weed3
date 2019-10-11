package org.noear.weed;

import org.noear.weed.ext.Act1;
import org.noear.weed.ext.Act1Ex;
import org.noear.weed.utils.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by noear on 14-6-12.
 * 数据库上下文
 */
public class DbContext {
    /**
     * 最后次执行命令
     */
    public Command lastCommand;
    /**
     * 充许多片段执行
     */
    public boolean allowMultiQueries;
    /**
     * 编译模式（用于产生代码）
     */
    public boolean isCompilationMode = false;


    //数据集名称
    private String _schemaName;
    //字段格式符
    private String _fieldFormat;
    private String _fieldFormat_start;
    //对象格式符
    private String _objectFormat;
    private String _objectFormat_start;
    //特性支持
    private Map<String, String> _attrMap = new HashMap<>();
    //数据源
    private DataSource _dataSource;
    //代码注解
    private String _codeHint = null;

    //
    // 构建函数 start
    //
    public DbContext() {
    }

    //基于线程池配置（如："proxool."）
    //fieldFormat："`%`"
    public DbContext(String schemaName, String url) {
        _schemaName = schemaName;
        _dataSource = new DbDataSource(url);
    }

    //基于手动配置（无线程池）
    public DbContext(String schemaName, String url, String user, String password) {
        _schemaName = schemaName;
        _dataSource = new DbDataSource(url, user, password);
    }

    public DbContext(String schemaName, DataSource dataSource) {
        _schemaName = schemaName;
        _dataSource = dataSource;
    }

    //
    // 构建函数 end
    //

    /**
     * 特性设置
     */
    public DbContext attrSet(String name, String value) {
        _attrMap.put(name, value);
        return this;
    }

    /**
     * 特性获取
     */
    public String attr(String name) {
        return _attrMap.get(name);
    }


    /**
     * 数据源设置
     */
    public DbContext dataSourceSet(DataSource dataSource) {
        _dataSource = dataSource;
        return this;
    }

    /**
     * 获取数据源
     */
    public DataSource dataSource() {
        return _dataSource;
    }


    /**
     * 数据集合名称设置
     */
    public DbContext schemaNameSet(String schemaName) {
        _schemaName = schemaName;
        return this;
    }

    /**
     * 代码注解设置
     */
    public DbContext codeHintSet(String hint) {
        _codeHint = hint;
        return this;
    }

    /**
     * 代码注解获取
     */
    public String codeHint() {
        return _codeHint;
    }


    /*是否配置了schema*/
    public boolean hasSchema() {
        return _schemaName != null;
    }

    /*获取schema*/
    public String getSchema() {
        return _schemaName;
    }

    //
    // 执行能力支持
    //

    /**
     * 获取连接
     */
    public Connection getConnection() throws SQLException {
        return _dataSource.getConnection();
    }

    /**
     * 执行代码，返回影响行数
     */
    public int exec(String code, Object... args) throws Exception {
        return new DbQuery(this).sql(new SQLBuilder().append(code, args)).execute();
    }

    /**
     * 输入SQL，获取查询器
     */
    public DbQuery sql(String code, Object... args) {
        return sql(new SQLBuilder().append(code, args));
    }

    /**
     * 输入SQL builder，获取查询器
     */
    public DbQuery sql(Act1<SQLBuilder> buildRuner) {
        SQLBuilder sql = new SQLBuilder();
        buildRuner.run(sql);
        return sql(sql);
    }

    /**
     * 输入SQL builder，获取查询器
     */
    public DbQuery sql(SQLBuilder sqlBuilder) {
        return new DbQuery(this).sql(sqlBuilder);
    }


    /**
     * 输入process name，获取process执行对象
     */
    public DbProcedure call(String process) {
        if (process.indexOf(" ") > 0) {
            return new DbQueryProcedure(this).sql(process);
        } else {
            return new DbStoredProcedure(this).call(process);
        }
    }


    /**
     * 获取一个表对象［用于操作插入也更新］
     */
    public DbTableQuery table(String table) {
        return new DbTableQuery(this).table(table);
    }

    public DbTran tran(Act1Ex<DbTran, SQLException> handler) throws SQLException {
        return new DbTran(this).execute(handler);
    }

    public DbTran tran() {
        return new DbTran(this);
    }

    public DbTranQueue tranQueue(Act1Ex<DbTranQueue, SQLException> handler) throws SQLException {
        return new DbTranQueue().execute(handler);
    }

    //
    // 格式化处理
    //

    /**
     * 字段格式符设置
     */
    public DbContext fieldFormatSet(String format) {
        _fieldFormat = format;
        if (format != null && format.length() > 1) {
            _fieldFormat_start = format.substring(0, 1);
        } else {
            _fieldFormat_start = "";
        }

        return this;
    }

    /**
     * 对象格式符设置
     */
    public DbContext objectFormatSet(String format) {
        _objectFormat = format;
        if (format != null && format.length() > 1) {
            _objectFormat_start = format.substring(0, 1);
        } else {
            _objectFormat_start = "";
        }
        return this;
    }


    /**
     * 字段格式化（用于：set(..,v)）
     */
    public String field(String name) {
        if (StringUtils.isEmpty(_fieldFormat)) {
            return name;
        } else {
            if (name.startsWith(_fieldFormat_start)) {
                return name;
            } else {
                return _fieldFormat.replace("%", name);
            }
        }
    }

    /**
     * 对象格式化（用于：from(..), join(..)）
     */
    public String object(String name) {
        if (StringUtils.isEmpty(_objectFormat)) {
            return name;
        }

        if (name.startsWith(_objectFormat_start) || name.indexOf(".") > 0) {
            return name;
        }

        if (name.indexOf(" ") < 0) {
            return _objectFormat.replace("%", name);
        }

        String[] ss = name.split(" ");

        if (ss.length != 2) {
            return name;
        }

        StringBuilder sb = StringUtils.borrowBuilder();

        sb.append(_objectFormat.replace("%", ss[0]))
          .append(" ")
          .append(ss[1]);

        return StringUtils.releaseBuilder(sb);
    }

}
