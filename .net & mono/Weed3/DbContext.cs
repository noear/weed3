using System;
using System.Configuration;
using System.Data.Common;

namespace Noear.Weed {

    public delegate DbProviderFactory DriveHandler(string providerName);

    /**
     * Created by noear on 14-6-12.
     * 数据库上下文
     */
    public class DbContext {
        public static class Drive {
            public static DriveHandler get;
        }

        private String _url;
        private String _schemaName;
        private DbProviderFactory _provider;

        //基于线程池配置（如："proxool."） //默认为mysql
        public DbContext(String schemaName, string url, DbProviderFactory provider) {
            _provider = provider;
            _schemaName = schemaName;

            if (url.IndexOf('=') < 0) {
                var set = ConfigurationManager.ConnectionStrings[url];
                _url = set.ConnectionString;
            }
            else {
                _url = url;
            }
        }

        /*是否配置了schema*/
        public bool hasSchema() { return _schemaName != null; }

        /*获取schema*/
        public String getSchema() {
            return _schemaName;
        }

        /*获取连接*/
        public DbConnection getConnection() {
            DbConnection conn = _provider.CreateConnection();

            conn.ConnectionString = _url;
            return conn;
        }

        public DbQuery sql(String code, params object[] args) {
            return new DbQuery(this).sql(new SQLBuilder().append(code, args));
        }

        /*获取process执行对象*/
        public DbStoredProcedure call(String process) {
            return new DbStoredProcedure(this).call(process);
        }

        /*获取一个表对象［用于操作插入也更新］*/
        public DbTableQuery table(String table) {
            return new DbTableQuery(this).table(table);
        }

        public DbTran tran(Action<DbTran> handler) {
            return new DbTran(this).execute(handler);
        }

        public DbTran tran() {
            return new DbTran(this);
        }
        
    }
}
