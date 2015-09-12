using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.Globalization;
using System.Reflection;

namespace Weed.Json
{
    public static class JConvert
    {
        public static string  ToJson(bool val)
        {
            return (val ? "true" : "false");
        }

        public static string ToJson(byte val)
        {
            return val.ToString();
        }

        public static string ToJson(char val)
        {
            return val.ToString();
        }

        public static string ToJson(DateTime val)
        {
            return "new Date(" + val.Ticks.ToString() + ")";
        }

        public static string ToJson(decimal val)
        {
            return val.ToString(null, CultureInfo.InvariantCulture);
        }

        public static string ToJson(double val)
        {
            return val.ToString(null, CultureInfo.InvariantCulture);
        }

        public static string ToJson(Enum val)
        {
            return val.ToString();
        }

        public static string ToJson(float val)
        {
            return val.ToString(null, CultureInfo.InvariantCulture);
        }

        public static string ToJson(Guid guid)
        {
            return guid.ToString("D");
        }

        public static string ToJson(sbyte val)
        {
            return val.ToString();
        }

        public static string ToJson(short val)
        {
            return val.ToString();
        }

        public static string ToJson(int val)
        {
            return val.ToString();
        }

        public static string ToJson(long val)
        {
            return val.ToString();
        }

        public static string ToJson(ushort val)
        {
            return val.ToString();
        }

        public static string ToJson(uint val)
        {
            return val.ToString();
        }

        public static string ToJson(ulong val)
        {
            return val.ToString();
        }

        public static string ToJson(string val)
        {
            return "\"" + Escape.Json(val) + "\"";
        }

        public static JDom ToJson(object val, Type type)
        {
            JDom dom = new JDom();

            foreach (PropertyInfo p in type.GetProperties())
            {
                if (p.CanRead && p.Name != "Item")
                    dom[p.Name].Val(p.GetValue(val, null));
            }

            return dom;
        }

        public static JItem ToJson(IEnumerable valList)
        {
            JItem dom = new JItem();
            
            foreach (object obj in valList)
            {
                dom.Add(obj);
            }

            return dom;
        }



        #region ToJson(object)
        public static string ToJson(object val)
        {
            if (val == null || val == DBNull.Value)
                return "null";

            if (val is IConvertible)
            {
                IConvertible cvt = val as IConvertible;

                switch (cvt.GetTypeCode())
                {
                    case TypeCode.Boolean:
                        return ToJson((bool)val);

                    case TypeCode.Char:
                        return ToJson((char)val);

                    case TypeCode.SByte:
                        return ToJson((sbyte)val);

                    case TypeCode.Byte:
                        return ToJson((byte)val);

                    case TypeCode.Int16:
                        return ToJson((short)val);

                    case TypeCode.Int32:
                        return ToJson((int)val);

                    case TypeCode.Int64:
                        return ToJson((long)val);

                    case TypeCode.UInt16:
                        return ToJson((ushort)val);

                    case TypeCode.UInt32:
                        return ToJson((uint)val);

                    case TypeCode.UInt64:
                        return ToJson((ulong)val);

                    case TypeCode.Single:
                        return ToJson((float)val);

                    case TypeCode.Double:
                        return ToJson((double)val);

                    case TypeCode.Decimal:
                        return ToJson((decimal)val);

                    case TypeCode.DateTime:
                        return ToJson((DateTime)val);

                    case TypeCode.String:
                        return ToJson((string)val);
                }
            }

            return null;
            //throw new WeedException("不支持[" + val.GetType().FullName + "]进行JSON序列化");
        }
        #endregion
    }
}
