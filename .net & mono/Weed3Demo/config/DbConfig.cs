using Noear.Weed;
using MySql.Data.MySqlClient;
using System.Configuration;
using System.Data.Common;

namespace Weed3Demo {
    public class DbConfig {
        //数据库配置
        public static DbContext test = new DbContext("test", "test");

        public static DbContext pc_user  = new DbContext("pc_bank", "test");
        public static DbContext pc_bank  = new DbContext("pc_bank", "test");
        public static DbContext pc_live  = new DbContext("pc_live", "test");
        public static DbContext pc_base  = new DbContext("pc_base", "test");
        public static DbContext pc_trace = new DbContext("pc_trace", "test");
        public static DbContext pc_pool  = new DbContext("pc_pool", "test");
        public static DbContext pc_bcf   = new DbContext("pc_bcf", "test");

        
        //public static DbContext xxxx  = new DbContext("pc_user","jdbc:mysql://db.zheq.org:3306/pc_user","root","1234",null);

    }
}
