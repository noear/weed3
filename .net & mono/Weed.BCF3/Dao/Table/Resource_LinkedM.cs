
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
    public class Resource_LinkedM : QTable
    {
        public Resource_LinkedM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        public Resource_LinkedM(string sdqName) : base(sdqName) { }

        public override IQBinder Clone()
        {
            return new Resource_LinkedM();
        }
        
        protected override void OnInit()
        {
            this.TableName = "BCF_Resource_Linked";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //
            Add(RSID = New<int>("RSID", QDbType.NUM, DbType.Int32, true));
            Add(LK_OBJT = New<int>("LK_OBJT", QDbType.NUM, DbType.Int32, false));
            Add(LK_OBJT_ID = New<int>("LK_OBJT_ID", QDbType.NUM, DbType.Int32, false));
            Add(LK_Operate = New<string>("LK_Operate", QDbType.Text, DbType.String, false));
            Add(P_Express = New<byte>("P_Express", QDbType.NUM, DbType.Byte, false)); 
        }

        public QColumnT<int> RSID;
        public QColumnT<int> LK_OBJT;
        public QColumnT<int> LK_OBJT_ID;
        public QColumnT<string> LK_Operate;
        public QColumnT<byte> P_Express;
    }
}
    