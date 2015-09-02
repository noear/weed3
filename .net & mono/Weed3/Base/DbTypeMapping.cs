using System;
using System.Collections.Generic;
using System.Data;

namespace Noear.Weed {
    internal static class DbTypeMapping {
        static Dictionary<string, DbType> TYPE_CACHE;
        static void tryInit() {
            if (TYPE_CACHE == null) {
                TYPE_CACHE = new Dictionary<string, DbType>();
                TYPE_CACHE.Add("AnsiString", DbType.AnsiString);
                TYPE_CACHE.Add("Binary", DbType.Binary);
                TYPE_CACHE.Add("Byte", DbType.Byte);
                TYPE_CACHE.Add("Boolean", DbType.Boolean);
                TYPE_CACHE.Add("Currency", DbType.Currency);
                TYPE_CACHE.Add("Date", DbType.Date);
                TYPE_CACHE.Add("DateTime", DbType.DateTime);
                TYPE_CACHE.Add("Decimal", DbType.Decimal);
                TYPE_CACHE.Add("Double", DbType.Double);
                TYPE_CACHE.Add("Guid", DbType.Guid);
                TYPE_CACHE.Add("Int16", DbType.Int16);
                TYPE_CACHE.Add("Int32", DbType.Int32);
                TYPE_CACHE.Add("Int64", DbType.Int64);
                TYPE_CACHE.Add("Object", DbType.Object);
                TYPE_CACHE.Add("SByte", DbType.SByte);
                TYPE_CACHE.Add("Single", DbType.Single);
                TYPE_CACHE.Add("String", DbType.String);
                TYPE_CACHE.Add("Time", DbType.Time);
                TYPE_CACHE.Add("UInt16", DbType.UInt16);
                TYPE_CACHE.Add("UInt32", DbType.UInt32);
                TYPE_CACHE.Add("UInt64", DbType.UInt64);
                TYPE_CACHE.Add("VarNumeric", DbType.VarNumeric);
                TYPE_CACHE.Add("AnsiStringFixedLength", DbType.AnsiStringFixedLength);
                TYPE_CACHE.Add("StringFixedLength", DbType.StringFixedLength);
                TYPE_CACHE.Add("Xml", DbType.Xml);
                TYPE_CACHE.Add("DateTime2", DbType.DateTime2);
                TYPE_CACHE.Add("DateTimeOffset", DbType.DateTimeOffset);
            }
        }

        public static DbType GetType(String typeName) {
            tryInit();

            if (TYPE_CACHE.ContainsKey(typeName))
                return TYPE_CACHE[typeName];
            else
                return DbType.Object;
        }
    }
}
