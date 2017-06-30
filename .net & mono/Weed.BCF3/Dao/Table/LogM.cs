
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
    public class LogM : QTable
    {
        public static LogM G = new LogM(null);

        public LogM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        public LogM(string sdqName) : base(sdqName) { }

        public override IQBinder Clone()
        {
            return new LogM();
        }
        
        protected override void OnInit()
        {
            this.TableName = "BCF_Log";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //

            Add(LGID = New<int>("LGID", QDbType.NUM, DbType.Int32, true));
            Add(LG_Type = New<int>("LG_Type", QDbType.NUM, DbType.Int32, false));
            Add(PUID = New<int>("PUID", QDbType.NUM, DbType.Int32, false));
            Add(CN_Name = New<string>("CN_Name", QDbType.Text, DbType.String, false));
            Add(EN_Name = New<string>("EN_Name", QDbType.Text, DbType.String, false));
            Add(At_Where = New<string>("At_Where", QDbType.Text, DbType.String, false));
            Add(On_When = New<DateTime>("On_When", QDbType.Time, DbType.Time, false));
            Add(Vs_Who = New<string>("Vs_Who", QDbType.Text, DbType.String, false));
            Add(Vs_Who_Key = New<string>("Vs_Who_Key", QDbType.Text, DbType.String, false));
            Add(Do_What = New<string>("Do_What", QDbType.Text, DbType.String, false));
            Add(Note = New<string>("Note", QDbType.Text, DbType.String, false));
            
            Add(State = New<short>("State", QDbType.NUM, DbType.Int32, false));
            Add(Exe_SQL = New<string>("Exe_SQL", QDbType.Text, DbType.String, false));
            
        }

        public QColumnT<int> LGID;
        public QColumnT<int> LG_Type;
        public QColumnT<int> PUID;
        public QColumnT<string> CN_Name;
        public QColumnT<string> EN_Name;
        public QColumnT<string> At_Where;
        public QColumnT<DateTime> On_When;
        public QColumnT<string> Vs_Who;
        public QColumnT<string> Vs_Who_Key;
        public QColumnT<string> Do_What;
        public QColumnT<string> Note;
        public QColumnT<short> State;

        public QColumnT<string> Exe_SQL;
    }
}
    