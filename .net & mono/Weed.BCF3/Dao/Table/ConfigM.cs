
using System;
using System.Data;

using Weed;
using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 生成:2008/06/04 08:34:07
    /// 作者:自动生成
    ///	
    /// 备注:请不要轻易手动更改类的实现代码!!!
    /// </summary>
    [Serializable]
    public class ConfigM : QTable
    {
        public ConfigM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        public ConfigM(string sdqName) : base(sdqName) { }

        public override IQBinder Clone()
        {
            return new ConfigM();
        }
        
        protected override void OnInit()
        {
            this.TableName = "BCF_Config";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //
            Add(Name = New<string>("Name", QDbType.Text, DbType.String, true));
            Add(Value = New<string>("Value", QDbType.Text, DbType.String, false));
            Add(Note = New<string>("Note", QDbType.Text, DbType.String, false)); 
        }

        public QColumnT<string> Name;
        public QColumnT<string> Value;
        public QColumnT<string> Note;
    }
}
    