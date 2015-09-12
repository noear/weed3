using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

namespace Weed.Json
{
    public enum ItemType
    {
        Array,
        Object,
        Property,
    }
    public class JItem
    {
        ItemType _Type = ItemType.Property;

        /// <summary>
        /// 在数据为Null的时候,输出为数组
        /// </summary>
        public void AsArray()
        {
            _Type = ItemType.Array;
        }

        /// <summary>
        /// 在数据为Null的时候,输出为对象
        /// </summary>
        public void AsObject()
        {
            _Type = ItemType.Object;
        }

        /// <summary>
        /// 在数据为Null的时候,输出为属性
        /// </summary>
        public void AsProperty()
        {
            _Type = ItemType.Property;
        }

        #region Add()
        private List<string> _ValList;
        private void DoAdd(string val)
        {
            if (_ValList == null)
                _ValList = new List<string>();

            _ValList.Add(val);
            AsArray();
        }
        public JItem Add(bool val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(byte val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(char val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(DateTime val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(decimal val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(double val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(Enum val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(float val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(Guid val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(sbyte val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(short val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(int val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(long val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(ushort val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(uint val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(ulong val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(string val)
        {
            DoAdd(JConvert.ToJson(val));

            return this;
        }

        public JItem Add(object val)
        {
            string temp = JConvert.ToJson(val);

            if (temp == null)
                Add(JConvert.ToJson(val, val.GetType()));
            else
                DoAdd(temp);

            return this;
        }

        #endregion

        #region Val(array)
        public void Val(IEnumerable<string> array)
        {
            foreach (string val in array)
                Add(val);

            AsArray();
        }

        public void Val(IEnumerable<int> array)
        {
            foreach (int val in array)
                Add(val);

            AsArray();
        }

        public void Val(IEnumerable<DateTime> array)
        {
            foreach (DateTime val in array)
                Add(val);

            AsArray();
        }

        public void Val(IEnumerable<bool> array)
        {
            foreach (bool val in array)
                Add(val);

            AsArray();
        }
        #endregion

        #region Val()
        private string _Val;
        public void Val(bool val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(byte val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(char val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(DateTime val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(decimal val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(double val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(Enum val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(float val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(Guid val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(sbyte val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(short val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(int val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(long val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(ushort val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(uint val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(ulong val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(string val)
        {
            _Val = JConvert.ToJson(val);
        }

        public void Val(object val)
        {
            _Val = JConvert.ToJson(val);

            if (_Val == null)
            {
                if (val is IEnumerable)
                    Val((IEnumerable)val);
                else
                    Val(JConvert.ToJson(val, val.GetType()));
            }
        }

        public void Val(IEnumerable valList)
        {
            JItem temp = JConvert.ToJson(valList);

            if (temp._DomList != null)
            {
                _DomList = temp._DomList;
                _Dom = _DomList[_DomList.Count - 1];
            }
            else if (temp._ValList != null)
                _ValList = temp._ValList;
        }

        public void Val(JDom obj)
        {
            _Dom = obj;
        }

        #endregion

        #region 连级操作支持

        public JItem Add(JDom obj)
        {
            if (_DomList == null)
                _DomList = new List<JDom>();

            _DomList.Add(obj);
            _Dom = obj;

            AsArray();

            return this;
        }

        public JDom Add()
        {
            JDom temp = new JDom();
            Add(temp);

            return temp;
        }

        private List<JDom> _DomList;
        private JDom _Dom;
        public JItem this[string name]
        {
            get
            {
                if (_Dom == null)
                    _Dom = new JDom();

                return _Dom[name];
            }
        }

        #endregion

        public string ToJson()
        {
            StringBuilder sb = new StringBuilder();

            WriteTo(sb);

            if (sb[0] != '{') //如果是没有大括号的,则加上大括号
            {
                sb.Insert(0, '{');
                sb.Append('}');
            }

            return sb.ToString();
        }

        internal void WriteTo(StringBuilder sb)
        {
            if (_Val != null && _Type == ItemType.Property)
            {
                sb.Append(_Val);
            }
            else if (_ValList != null)
            {
                sb.Append("[");

                foreach (string val in _ValList)
                {
                    sb.Append(val);
                    sb.Append(",");
                }

                sb.Remove(sb.Length - 1, 1);

                sb.Append("]");
            }
            else if (_DomList != null)
            {
                sb.Append("[");

                foreach (JDom dom in _DomList)
                {
                    dom.WriteTo(sb);
                    sb.Append(",");
                }

                sb.Remove(sb.Length - 1, 1);

                sb.Append("]");
            }
            else if (_Dom != null)
            {
                _Dom.WriteTo(sb);
            }
            else
            {
                if (_Type == ItemType.Object)
                    sb.Append("{}");
                else if (_Type == ItemType.Array)
                    sb.Append("[]");
                else
                    sb.Append("null");
            }
        }       
    }
}
