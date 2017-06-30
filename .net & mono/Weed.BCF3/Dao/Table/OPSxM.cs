
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
    public class OPSxM : QTable
    {
        public OPSxM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        public OPSxM(string sdqName) : base(sdqName) { }

        public override IQBinder Clone()
        {
            return new OPSxM();
        }
        
        protected override void OnInit()
        {
            this.TableName = "BCF_OPSx";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //
            Add(LK_OBJT = New<int>("LK_OBJT", QDbType.NUM, DbType.Int32, true));
            Add(LK_OBJT_ID = New<int>("LK_OBJT_ID", QDbType.NUM, DbType.Int32, false));
            Add(Tags = New<string>("Tags", QDbType.Text, DbType.String, false));
            Add(OPSx = New<string>("OPSx", QDbType.Text, DbType.String, false)); 
        }

        public QColumnT<int> LK_OBJT;
        public QColumnT<int> LK_OBJT_ID;
        public QColumnT<string> Tags;
        public QColumnT<string> OPSx;
    }
}
    