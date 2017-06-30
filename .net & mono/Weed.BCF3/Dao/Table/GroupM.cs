
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
    public class GroupM : QTable
    {
        internal static GroupM G = new GroupM(null);

        public GroupM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        public GroupM(string sdqName) : base(sdqName) { }

        public override IQBinder Clone()
        {
            return new GroupM();
        }
        
        protected override void OnInit()
        {
            this.TableName = "BCF_Group";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //
            Add(PGID = New<int>("PGID", QDbType.NUM, DbType.Int32, true));
            Add(P_PGID = New<int>("P_PGID", QDbType.NUM, DbType.Int32, false));
            Add(R_PGID = New<int>("R_PGID", QDbType.NUM, DbType.Int32, false));
            Add(PG_Code = New<string>("PG_Code", QDbType.Text, DbType.String, false));
            Add(CN_Name = New<string>("CN_Name", QDbType.Text, DbType.String, false));
            Add(EN_Name = New<string>("EN_Name", QDbType.Text, DbType.String, false));
            Add(In_Level = New<int>("In_Level", QDbType.NUM, DbType.Int32, false));
            Add(Tags = New<string>("Tags", QDbType.Text, DbType.String, false));
            Add(Order_Index = New<int>("Order_Index", QDbType.NUM, DbType.Int32, false));
            Add(Is_Disabled = New<bool>("Is_Disabled", QDbType.Bool, DbType.Boolean, false));
            Add(Is_Visibled = New<bool>("Is_Visibled", QDbType.Bool, DbType.Boolean, false));
            Add(Create_Time = New<DateTime>("Create_Time", QDbType.Time, DbType.Time, false));
            Add(Last_Update = New<DateTime>("Last_Update", QDbType.Time, DbType.Time, false)); 
        }

        public QColumnT<int> PGID;
        public QColumnT<int> P_PGID;
        public QColumnT<int> R_PGID;
        public QColumnT<string> PG_Code;
        public QColumnT<string> CN_Name;
        public QColumnT<string> EN_Name;
        public QColumnT<int> In_Level;
        public QColumnT<string> Tags;
        public QColumnT<int> Order_Index;
        public QColumnT<bool> Is_Disabled;
        public QColumnT<bool> Is_Visibled;
        public QColumnT<DateTime> Create_Time;
        public QColumnT<DateTime> Last_Update;
    }
}
    