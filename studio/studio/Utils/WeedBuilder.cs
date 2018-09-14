using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

using weedstudio.exts;
using weedstudio.Model;
using weedstudio.Utils;
using Noear.Snacks;

namespace weedstudio.Utils {
    public enum BuildType
    {
        Table,
        Entity,
        Process,
        SqlOps,
        SqlSdq,
        Html,
        DbMap,
        DbMapCache,
        DbGuide,
    }

    
    public class WeedBuilder
    {
        DbContextEx db;

        public WeedBuilder(DbContextEx db) {
            this.db = db;
        }

        private string ClassName;
        private bool IsJava;

        private Encoding _Encoding = Encoding.UTF8;
        protected Encoding Encoding
        {
            get { return _Encoding; }
            set { _Encoding = value; }
        }
        public string TempletFile { get; set; }
        public string TableName = null;
        public string SdqName = "";
        public List<PropertyModel> Columns { get; set; }
        public string TargetFolder { get; set; }
        public ONode args { get; set; } = new ONode();

        /// <summary>
        /// 执行前,请设置:TargetFolder,NameSpace,Columns,VSNET
        /// </summary>
        /// <param name="type"></param>
        public void Build() {
          

            this.ClassName = Class(TableName);
            
            

            if (TempletFile != null && TempletFile.IndexOf(".java.") > 0)
                this.IsJava = true;
            else
                this.IsJava = false;

            var sc = TempletApi.getTempletByFile(TempletFile);

            StringBuilder codeWriter = new StringBuilder();

            codeWriter.Append(sc.Main)
                      //.Replace("{namespace}", this.NameSpace)
                      .Replace("{classname}", this.ClassName)
                      .Replace("{tablename}", this.TableName)
                      .Replace("{time}", DateTime.Now.ToString("yyyy/MM/dd hh:mm:ss"))
                      .Replace("{sdqname}", this.SdqName);

            args.forEach((k, v) => {
                codeWriter.Replace("{" + k + "}", v.getString());
            });

            if (sc.Main.IndexOf("{keycolname}") > 0) {
                PropertyModel keyColumn = GetKeyCol();
                codeWriter.Replace("{keycolname}", N(keyColumn))
                          .Replace("{keysqltype}", keyColumn.Type)
                          .Replace("{keycolnote}", keyColumn.Note.Replace("\r\n", ""));
            }

            foreach (string key in sc.Keys) {
                string itemFormat = sc.GetItemFormat(key);

                if (itemFormat != null) {
                    codeWriter.Replace("{" + key + "}", GetItem(itemFormat));
                }
            }

            StringBuilder fileName = new StringBuilder(sc.File);


            fileName
                .Replace("{classname}", this.ClassName)
                .Replace("{tablename}", this.TableName)
                //.Replace("{namespace}", this.NameSpace)
                .Replace("{time}", DateTime.Now.ToString("yyyy/MM/dd hh:mm:ss"))
                .Replace("{sdqname}", this.SdqName);

            args.forEach((k, v) => {
                fileName.Replace("{" + k + "}", v.getString());
            });


            foreach (ReplaceItem x in sc.ReplaceItems)
                codeWriter.Replace(x.Old, x.New);

            var code = codeWriter.Replace('\ufeff', ' ').ToString().Trim();

            if (sc.IsAppend)
                File.AppendAllText(TargetFolder + "\\" + fileName.ToString(), code, this.Encoding);
            else
                File.WriteAllText(TargetFolder + "\\" + fileName.ToString(), code, this.Encoding);
        }
        

        private PropertyModel GetKeyCol()
        {
            foreach (PropertyModel col in Columns)
            {
                if (col.IsKey)
                    return col;
            }

            return Columns[0];
        }

        private string GetItem(string itemFormat) {
            StringBuilder sb = new StringBuilder();

            foreach (PropertyModel col in Columns) {
                if (itemFormat.IndexOf("{unkeycolname}") >= 0 && col.IsKey)
                    continue;

                if (itemFormat.IndexOf("{keycolname}") >= 0 && col.IsKey == false)
                    continue;

                sb.Append(itemFormat);

                sb.Replace("{colname}", N(col));
                sb.Replace("{popname}", Up1(N(col)).TrimStart('_'));//首字母大写


                sb.Replace("{def}", DEF(col));
                sb.Replace("{def2}", DEF2(col));
                sb.Replace("{type}", ST(col));
                sb.Replace("{ntype}", EST(col));
                sb.Replace("{dbtype}", DT(col));
                sb.Replace("{iskey}", (col.IsKey ? "true" : "false"));
                sb.Replace("{sqltype}", col.Type);
                sb.Replace("{note}", col.Note.Replace("\r\n", ""));
                sb.Replace("{unkeycolname}", N(col));
                sb.Replace("{indexcolname}", N(col));
                sb.Replace("{keycolname}", N(col));
                sb.Replace("{colnote}", col.Note);
                sb.Replace("{tryout}", (col.IsKey ? " OUT" : ""));

                sb.Replace("{classname}", this.ClassName)
                  //.Replace("{namespace}", this.NameSpace)
                  .Replace("{tablename}", this.TableName)
                  .Replace("{time}", DateTime.Now.ToString("yyyy/MM/dd hh:mm:ss"))
                  .Replace("{sdqname}", this.SdqName);

                args.forEach((k, v) => {
                    sb.Replace("{" + k + "}", v.getString());
                });
            }


            if (sb.Length > 0 && sb[sb.Length - 1] == ',')
                sb.Remove(sb.Length - 1, 1);

            return sb.ToString();
        }

        #region 对类或属性名称进行预处理
        /// <summary>
        /// 首字母大写,去掉复数，‘ ’=>'_'
        /// </summary>
        public static string Class(string tableName)
        {
            if (tableName == null)
                return "";

            StringBuilder sb = new StringBuilder();
            String temp = tableName.Replace(' ', '_');
            String[] pts = temp.Split('_');
            foreach (String p1 in pts)
            {
                if (p1.Length > 1)
                    sb.Append(p1[0].ToString().ToUpper() + p1.Substring(1));
                else
                    sb.Append(p1.ToUpper());
            }

            return Weed.Escape.ToSingular(sb.ToString());

        }

        /// <summary>
        /// 首字母大写，‘ ’=>'_'
        /// </summary>
        private string Up1(string text1)
        {
            if (text1 == null)
                return "";

            string temp = text1.Replace(' ','_');

            if (temp.Length > 1) // 如果有多的字符,则首字母大写
                return temp[0].ToString().ToUpper() + temp.Substring(1);
            else
                return temp.ToUpper(); //否则全部大写
        }

        #endregion

        #region 获取类型

        /// <summary>
        /// 获取名称
        /// </summary>
        private string N(PropertyModel col)
        {
            return col.Name.TrimStart('@');
        }

        /// <summary>
        /// 获取System.Type
        /// </summary>
        /// <returns>System.Type</returns>
        private string ST(PropertyModel col)
        {
            string sqlType = col.Type.Split('(')[0].ToLower();


            switch (sqlType)
            {
                case "system.boolean":
                case "bit":
                case "bool":
                    {
                        if (IsJava)
                            return "boolean";
                        else
                            return "bool";//bool
                    }

                case "system.byte":
                case "tinyint":
                    {
                        if (col.Type.ToLower() == "tinyint(1)")
                        {
                            if (IsJava)
                                return "boolean";
                            else
                                return "bool";//bool
                        }
                        else
                            return "byte";//byte
                    }

                case "system.int16":
                case "smallint":
                    return "short";//short

                case "system.int32":
                case "int":
                case "integer":
                    return "int";//int

                case "system.int64":
                case "bigint":
                    return "long";//long

                case "system.single":
                case "real":
                case "float":
                case "single":
                    return "float";//float

                case "system.double":
                case "double":
                    return "double"; //double

                case "system.decimal":
                case "money":
                case "smallmoney":
                case "numeric":
                case "decimal":
                    return "decimal";//decimal

                case "system.string":
                case "char":
                case "nchar":
                case "varchar":
                case "nvarchar":
                case "text":
                case "ntext":
                case "xml":
                    {
                        if (IsJava)
                            return "String";
                        else
                            return "string";//string
                    }

                   
                case "image":
                case "binary":
                case "varbinary":
                case "bolb":
                    return "byte[]";

                case "system.dateTime":
                case "datetime":
                case "datetime2":
                case "date":
                case "smalldatetime":
                    {
                        if (IsJava)
                            return "Date";
                        else
                            return "DateTime";//DateTime
                    }
                case "time":
                    {
                        if (IsJava)
                            return "Time";
                        else
                            return "DateTime";//DateTime
                    }

                default:
                    {
                        if (IsJava)
                            return "String";
                        else
                            return "string";//string
                    }
            }
        }

        /// <summary>
        /// 获取System.Type ?
        /// </summary>
        /// <returns>System.Type</returns>
        private string EST(PropertyModel col) {
            string sqlType = col.Type.Split('(')[0].ToLower();


            switch (sqlType) {
                case "system.boolean":
                case "bit":
                case "bool":
                    {
                        if (IsJava)
                            return "Boolean";
                        else
                            return "bool?";//bool
                    }

                case "system.byte":
                case "tinyint":
                    {
                        if (col.Type.ToLower() == "tinyint(1)") {
                            if (IsJava)
                                return "Boolean";
                            else
                                return "bool?";//bool
                        }
                        else
                            return "Byte";//byte
                    }

                case "system.int16":
                case "smallint":
                    {
                        if (IsJava)
                            return "Short";//short
                        else
                            return "short?";
                    }

                case "system.int32":
                case "int":
                case "integer":
                    {
                        if (IsJava)
                            return "Integer";
                        else
                            return "int?";
                    }

                case "system.int64":
                case "bigint":
                    {
                        if (IsJava)
                            return "Long";//long
                        else
                            return "long?";//long
                    }

                case "system.single":
                case "real":
                case "float":
                case "single":
                    {
                        if(IsJava)
                            return "Float";//float
                        else
                            return "float?";//float 
                    }

                case "system.double":
                case "double":
                    {
                        if(IsJava)
                            return "Double"; //double
                        else
                            return "double?"; //double
                    }

                case "system.decimal":
                case "money":
                case "smallmoney":
                case "numeric":
                case "decimal":
                    {
                        if(IsJava)
                            return "BigDecimal";//decimal
                        else
                            return "decimal?";//decimal
                    }

                case "system.string":
                case "char":
                case "nchar":
                case "varchar":
                case "nvarchar":
                case "text":
                case "ntext":
                case "xml":
                    {
                        if (IsJava)
                            return "String";
                        else
                            return "string";//string
                    }


                case "image":
                case "binary":
                case "varbinary":
                case "bolb":
                    return "byte[]";

                case "system.dateTime":
                case "datetime":
                case "datetime2":
                case "date":
                case "smalldatetime":
                    {
                        if (IsJava)
                            return "Date";
                        else
                            return "DateTime?";//DateTime
                    }
                case "time":
                    {
                        if (IsJava)
                            return "Time";
                        else
                            return "DateTime?";//DateTime
                    }

                default:
                    {
                        if (IsJava)
                            return "String";
                        else
                            return "string";//string
                    }
            }
        }

        //模型的默认值 
        private string DEF(PropertyModel col) {
            string sqlType = col.Type.Split('(')[0].ToLower();


            switch (sqlType) {
                case "system.boolean":
                case "bit":
                case "bool":
                    {
                        if (IsJava)
                            return "false";
                        else
                            return "false";//bool
                    }

                case "system.byte":
                case "tinyint":
                    {
                        if (col.Type.ToLower() == "tinyint(1)") {
                            if (IsJava)
                                return "false";
                            else
                                return "false";//bool
                        }
                        else
                            return "0";//byte
                    }

                case "system.int16":
                case "smallint":
                    return "0";//short

                case "system.int32":
                case "int":
                case "integer":
                    return "0";//int

                case "system.int64":
                case "bigint":
                    return "0L";//long

                case "system.single":
                case "real":
                case "float":
                case "single":
                    return "0f";//float

                case "system.double":
                case "double":
                    return "0d"; //double

                case "system.decimal":
                case "money":
                case "smallmoney":
                case "numeric":
                case "decimal":
                    return "0";//decimal

                case "system.string":
                case "char":
                case "nchar":
                case "varchar":
                case "nvarchar":
                case "text":
                case "ntext":
                case "xml":
                    {
                        if (IsJava)
                            return "null";
                        else
                            return "(string)null";//string
                    }


                case "image":
                case "binary":
                case "varbinary":
                case "bolb":
                    return "null";

                case "system.dateTime":
                case "datetime":
                case "datetime2":
                case "date":
                case "smalldatetime":
                    {
                        if (IsJava)
                            return "null";
                        else
                            return "DateTime.MinValue";//DateTime
                    }
                case "time":
                    {
                        if (IsJava)
                            return "null";
                        else
                            return "DateTime.MinValue";//DateTime
                    }

                default:
                    {
                        if (IsJava)
                            return "null";
                        else
                            return "(string)null";//string
                    }
            }
        }

        private string DEF2(PropertyModel col) {
            string sqlType = col.Type.Split('(')[0].ToLower();


            switch (sqlType) {
                case "system.boolean":
                case "bit":
                case "bool": {
                        if (IsJava)
                            return "false";
                        else
                            return "false";//bool
                    }

                case "system.byte":
                case "tinyint": {
                        if (col.Type.ToLower() == "tinyint(1)") {
                            if (IsJava)
                                return "false";
                            else
                                return "false";//bool
                        }
                        else
                            return "0";//byte
                    }

                case "system.int16":
                case "smallint":
                    return "0";//short

                case "system.int32":
                case "int":
                case "integer":
                    return "0";//int

                case "system.int64":
                case "bigint":
                    return "0L";//long

                case "system.single":
                case "real":
                case "float":
                case "single":
                    return "0f";//float

                case "system.double":
                case "double":
                    return "0d"; //double

                case "system.decimal":
                case "money":
                case "smallmoney":
                case "numeric":
                case "decimal":
                    return "0";//decimal

                case "system.string":
                case "char":
                case "nchar":
                case "varchar":
                case "nvarchar":
                case "text":
                case "ntext":
                case "xml": {
                        if (IsJava)
                            return "null";
                        else
                            return "\"\"";//string
                    }


                case "image":
                case "binary":
                case "varbinary":
                case "bolb":
                    return "null";

                case "system.dateTime":
                case "datetime":
                case "datetime2":
                case "date":
                case "smalldatetime": {
                        if (IsJava)
                            return "null";
                        else
                            return "DateTime.MinValue";//DateTime
                    }
                case "time": {
                        if (IsJava)
                            return "null";
                        else
                            return "DateTime.MinValue";//DateTime
                    }

                default: {
                        if (IsJava)
                            return "null";
                        else
                            return "\"\"";//string
                    }
            }
        }


        /// <summary>
        /// 获取System.Data.DbType
        /// </summary>
        /// <param name="col"></param>
        /// <returns>System.Data.DbType</returns>
        private string DT(PropertyModel col)
        {
            string sqlType = col.Type.Split('(')[0].ToLower();

            switch (sqlType)
            {
                case "system.boolean":
                case "bit":
                case "bool":
                    return "DbType.Boolean";//bool

                case "system.byte":
                case "tinyint":
                    {
                        if (col.Type.ToLower() == "tinyint(1)")
                            return "DbType.Boolean";
                        else
                            return "DbType.Byte";//byte
                    }

                case "system.int16":
                case "smallint":
                    return "DbType.Int16";//short

                case "system.int32":
                case "int":
                case "integer":
                    return "DbType.Int32";//int

                case "system.int64":
                case "bigint":
                    return "DbType.Int64";//long

                case "system.single":
                case "single":
                case "real":
                    return "DbType.Single";//float

                case "dystem.double":
                case "float"://在sqlserver 里 real相当于float; float 相当于 double;
                case "double":
                    return "DbType.Double";

                case "system.decimal":
                case "money":
                case "smallmoney":
                case "numeric":
                case "decimal":
                    return "DbType.Decimal";//decimal

                case "system.string":
                case "char":
                case "nchar":
                case "varchar":
                case "nvarchar":
                case "text":
                case "ntext":
                case "xml":
                    return "DbType.String";//string

                case "image":
                case "binary":
                case "varbinary":
                case "bolb":
                    return "DbType.Binary";//Binary

                case "system.dateTime":
                case "datetime":
                case "datetime2":
                case "date":
                case "time":
                case "smalldatetime":
                    return "DbType.DateTime";//DateTime

                default:
                    return "DbType.String";
            }
        }

        
        #endregion
    }
}
