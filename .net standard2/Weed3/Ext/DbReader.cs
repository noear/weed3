using System;
using System.Data;
using System.Globalization;

namespace Noear.Weed {
    /// <summary>
    /// 可以返回Null而不发出异常的DataReader
    /// </summary>
    internal class DbReader : IDisposable
    {
        private IDataReader _InnerReader;

        public DbReader(IDataReader reader)
        {
            _InnerReader = reader;

            _InnerReadMetaData();
        }

        #region IDataReader 成员

        private int _FieldCount = 0;
        public int FieldCount
        {
            get { return _FieldCount; }
        }

        public void Close()
        {
            _InnerReader.Close();
        }

        public bool Read()
        {
            return _InnerReader.Read();
        }

        /// <summary>
        /// 如果不存在,则返回Null
        /// </summary>
        public object this[string name]
        {
            get 
            {
                int i = IndexOf(name);

                if (i < 0)
                    return null;
                else
                    return _InnerReader[i]; 
            }
        }

        public object this[int i]
        {
            get { return _InnerReader[i]; }
        }
        
        #endregion

        #region IDataReader Ex 成员

        private string[] _MetaData;
        internal string[] MetaData
        {
            get
            {               
                return _MetaData;
            }
        }

        private void _InnerReadMetaData()
        {
            int count = _InnerReader.FieldCount;
            _MetaData = new string[count];
            _FieldCount = _InnerReader.FieldCount;

            for (int i = 0; i < count; i++)
                _MetaData[i] = _InnerReader.GetName(i);
        }

        private static CompareInfo compare = CultureInfo.InvariantCulture.CompareInfo;
        public int IndexOf(string name)
        {
            for (int i = 0; i < FieldCount; i++)
            {
                if (compare.Compare(MetaData[i], name, CompareOptions.OrdinalIgnoreCase) == 0)
                    return i;
            }

            return -1;
        }

        public string GetName(int index)
        {
            return _InnerReader.GetName(index);
        }

        #endregion


        #region IDisposable 成员

        public void Dispose()
        {
            _InnerReader.Dispose();
            _MetaData = null;
        }

        #endregion
    }
}
