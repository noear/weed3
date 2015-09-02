using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace Noear.Weed {
    internal class SQLBuilder {
        private StringBuilder builder = new StringBuilder();
        internal List<Object> paramS = new List<Object>();

        public SQLBuilder insert(String code, params Object[] args) {
            SQLPartBuilder pb = new SQLPartBuilder(code, args);

            builder.Insert(0, pb.code);
            paramS.InsertRange(0, pb.paramS);
            return this;
        }

        public SQLBuilder append(String code, params Object[] args) {
            SQLPartBuilder pb = new SQLPartBuilder(code, args);

            builder.Append(pb.code);
            paramS.AddRange(pb.paramS);
            return this;
        }

        public override string ToString() {
            return builder.ToString();
        }
       
        //部分构建
        internal class SQLPartBuilder {
            public String code;
            public List<Object> paramS;

            public SQLPartBuilder(String code, params Object[] args) {

                paramS = new List<Object>();

                if (args.Length > 0) {
                    StringBuilder builder = new StringBuilder(code);
                    foreach (Object p1 in args) {
                        if (p1 is IEnumerable) { //将数组转为单体
                            StringBuilder sb = new StringBuilder();
                            foreach (Object p2 in (IEnumerable)p1) {
                                paramS.Add(p2);
                                sb.Append("?").Append(",");
                            }

                            int len = sb.Length;
                            if (len > 0)
                                sb.Remove(len - 1, 1);

                            builder.ReplaceFirst("\\?\\.\\.\\.", sb.ToString());
                        }
                        else if (p1 is DbQuery) {

                            DbQuery s1 = (DbQuery)p1;

                            foreach (Variate p2 in s1.paramS) {
                                paramS.Add(p2.getValue());
                            }

                            if (s1.paramS.Count > 0) {
                                builder.ReplaceFirst("\\?\\.\\.\\.", s1.commandText);
                            }
                            else
                                builder.ReplaceFirst("\\?\\.\\.\\.", s1.commandText);
                        }
                        else {
                            paramS.Add(p1);
                        }
                    }

                    this.code = builder.ToString();
                }
                else {
                    this.code = code;
                }
            }
        }
    }
}
