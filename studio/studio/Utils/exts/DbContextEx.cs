using Noear.Weed;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.Common;
using System.Configuration;

namespace weedstudio.exts {
    public class DbContextEx : DbContext {
        //connectionString属性
        public Dictionary<string, string> atts = new Dictionary<string, string>();
        //builder
        public String builder { get; set; }

        public DbContextEx(string schemaName, string name) : base(schemaName, name) {
            ConnectionStringSettings set = ConfigurationManager.ConnectionStrings[name];

            string temp = set.ProviderName;

            if (temp.IndexOf("MySqlClient") >= 0)
                builder = "mysql";
            else if (temp.IndexOf("SqlClient") >= 0)
                builder = "sqlserver2005";
            else if (temp.IndexOf("OracleClient") >= 0)
                builder = "oracle";
            else if (temp.IndexOf("OleDb") >= 0)
                builder = "access";
            else if (temp.IndexOf("Odbc") >= 0)
                builder = "access";
            else if (temp.IndexOf("SqlServerCe") >= 0)
                builder = "sqlce";
        }

        protected override void doInit(string schemaName, string connectionString, DbProviderFactory provider) {
            base.doInit(schemaName, connectionString, provider);

            atts.Clear();
            foreach (string item in connectionString.Split(';')) {
                if (item.IndexOf('=') > 0)
                    atts.Add(item.Split('=')[0].Trim(), item.Split('=')[1].Trim());
            }
        }
    }
}
