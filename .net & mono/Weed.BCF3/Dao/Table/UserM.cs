
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
    public class UserM : QTable
    {
        internal static UserM G = new UserM(null);

        public UserM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        public UserM(string sdqName) : base(sdqName) { }

        public override IQBinder Clone()
        {
            return new UserM();
        }

        protected override void OnInit()
        {
            this.TableName = "BCF_User";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //
            Add(PUID = New<int>("PUID", QDbType.NUM, DbType.Int32, true));
            Add(User_Id = New<string>("User_Id", QDbType.Text, DbType.String, false));
            Add(Pass_Wd = New<string>("Pass_Wd", QDbType.Text, DbType.String, false));
            Add(CN_Name = New<string>("CN_Name", QDbType.Text, DbType.String, false));
            Add(EN_Name = New<string>("EN_Name", QDbType.Text, DbType.String, false));
            Add(PW_Mail = New<string>("PW_Mail", QDbType.Text, DbType.String, false));
            Add(OUT_OBJT = New<int>("OUT_OBJT", QDbType.NUM, DbType.Int32, false));
            Add(OUT_OBJT_ID = New<int>("OUT_OBJT_ID", QDbType.NUM, DbType.Int32, false));
            Add(Tags = New<string>("Tags", QDbType.Text, DbType.String, false));
            Add(Is_Disabled = New<bool>("Is_Disabled", QDbType.Bool, DbType.Boolean, false));
            Add(Is_Visibled = New<bool>("Is_Visibled", QDbType.Bool, DbType.Boolean, false));
            Add(Create_Time = New<DateTime>("Create_Time", QDbType.Time, DbType.Time, false));
            Add(Last_Update = New<DateTime>("Last_Update", QDbType.Time, DbType.Time, false));
        }

        public QColumnT<int> PUID;
        public QColumnT<string> User_Id;
        public QColumnT<string> Pass_Wd;
        public QColumnT<string> CN_Name;
        public QColumnT<string> EN_Name;
        public QColumnT<string> PW_Mail;
        public QColumnT<int> OUT_OBJT;
        public QColumnT<int> OUT_OBJT_ID;
        public QColumnT<string> Tags;
        public QColumnT<bool> Is_Disabled;
        public QColumnT<bool> Is_Visibled;
        public QColumnT<DateTime> Create_Time;
        public QColumnT<DateTime> Last_Update;
        
    }
}
    