using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace Noear.Weed {
    public class SQLBuilder {
        private StringBuilder builder = new StringBuilder();
        internal List<Object> paramS = new List<Object>();

        StringBuilder b_builder = new StringBuilder();
        List<Object> b_paramS = new List<Object>();

        public void clear() {
            builder.Clear();
            paramS.Clear();
        }

        //备分状态
        internal void backup() {
            b_builder.Append(builder.ToString());
            b_paramS.AddRange(paramS);
        }
        //还原状态
        internal void restore() {
            clear();
            builder.Append(b_builder.ToString());
            paramS.AddRange(b_paramS);
        }

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

        public SQLBuilder remove(int start, int length) {
            builder.Remove(start, length);
            return this;
        }

        public SQLBuilder removeLast() {
            builder.DeleteCharAt(builder.Length - 1);
            return this;
        }

        public int length() {
            return builder.Length;
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
                        if (p1 is ICollection) { //将数组转为单体
                            StringBuilder sb = new StringBuilder();
                            foreach (Object p2 in (ICollection)p1) {
                                paramS.Add(p2);
                                sb.Append("?").Append(",");
                            }

                            int len = sb.Length;
                            if (len > 0)
                                sb.Remove(len - 1, 1);

                            builder.ReplaceFirst("?...", sb.ToString());
                        }
                        else if (p1 is DbQuery) {

                            DbQuery s1 = (DbQuery)p1;

                            foreach (Variate p2 in s1.paramS) {
                                paramS.Add(p2.getValue());
                            }

                            if (s1.paramS.Count > 0) 
                                builder.ReplaceFirst("?...", s1.commandText);
                            else
                                builder.ReplaceFirst("?...", s1.commandText);
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
