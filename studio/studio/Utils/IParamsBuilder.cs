using System;
using System.Collections.Generic;
using System.Text;

using Noear.Weed;
using weedstudio.Model;

namespace weedstudio.Utils {
    public interface IParamsBuilder {
        List<PropertyModel> GetParams(string spName, DbContext sqlRun);
    }

    public class MySqlParamsBuilder : IParamsBuilder {

        #region IParamsBuilder 成员

        public List<PropertyModel> GetParams(string spName, DbContext db)
        {
            List<PropertyModel> dt = new List<PropertyModel>();

            string dbName = db.getSchema();

            byte[] temp = db.sql("SELECT param_list FROM mysql.proc p WHERE db=? AND name=?", dbName, spName)
                            .getValue<byte[]>(null);

            if (temp != null)
            {
                string paramS = Encoding.UTF8.GetString(temp);

                foreach (string p in paramS.Split(','))
                {
                    string p1 = p.Trim();

                    if (p.Length > 2)
                    {
                        //Name,Type,IsKey（关键字或输出项）,Note,Default
                        string[] p2 = p1.Split(new char[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);
                        if (p2.Length == 3)
                            dt.Add(new PropertyModel(p2[1],
                                p2[2],
                                p2[0].ToLower().IndexOf("out") >= 0,
                                "",
                                ""));
                        else
                            dt.Add(new PropertyModel(p2[0],
                                p2[1],
                                false,
                                "",
                                ""));

                    }
                }
            }
            return dt;
        }

        #endregion
    }
}
