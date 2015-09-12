using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.Common;

namespace Noear.Weed {
    
    /**
     * Created by noear on 14-6-12.
     * 数据库上下文
     */
    public class DbContext {
       
        private String _url;
        private String _schemaName;
        private DbProviderFactory _provider;
        
        static DbProviderFactory provider(string providerString) {
            if (providerString == null)
                return null;

            if (providerString.IndexOf(",") > 0)
                return (DbProviderFactory)Activator.CreateInstance(Type.GetType(providerString, true, true));
            else
                return DbProviderFactories.GetFactory(providerString);
        }

        public DbContext(String schemaName, string name) {
            var set = ConfigurationManager.ConnectionStrings[name];
            var p = provider(set.ProviderName);
            doInit(schemaName, set.ConnectionString, p);
        }
        
        public DbContext(String schemaName, string connectionString, DbProviderFactory provider) {
            doInit(schemaName, connectionString, provider);
        }
        
        protected virtual void doInit(String schemaName, string connectionString, DbProviderFactory provider) {
            _provider = provider;
            _schemaName = schemaName;
            _url = connectionString;
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

        public DbQuery sql(SQLBuilder sqlBuilder) {
            return new DbQuery(this).sql(sqlBuilder);
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
