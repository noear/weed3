
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
    public class ResourceM : QTable
    {
        internal static ResourceM G = new ResourceM(null);

        public ResourceM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        public ResourceM(string sdqName) : base(sdqName) { }

        public override IQBinder Clone()
        {
            return new ResourceM();
        }
        
        protected override void OnInit()
        {
            this.TableName = "BCF_Resource";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //
            Add(RSID = New<int>("RSID", QDbType.NUM, DbType.Int32, true));
            Add(RS_Code = New<string>("RS_Code", QDbType.Text, DbType.String, false));
            Add(CN_Name = New<string>("CN_Name", QDbType.Text, DbType.String, false));
            Add(EN_Name = New<string>("EN_Name", QDbType.Text, DbType.String, false));
            Add(Uri_Path = New<string>("Uri_Path", QDbType.Text, DbType.String, false));
            Add(Uri_Target = New<string>("Uri_Target", QDbType.Text, DbType.String, false));
            Add(Ico_Path = New<string>("Ico_Path", QDbType.Text, DbType.String, false));
            Add(Order_Index = New<int>("Order_Index", QDbType.NUM, DbType.Int32, false));
            Add(Note = New<string>("Note", QDbType.Text, DbType.String, false));
            Add(Tags = New<string>("Tags", QDbType.Text, DbType.String, false));
            Add(Is_Disabled = New<bool>("Is_Disabled", QDbType.Bool, DbType.Boolean, false));
            Add(Create_Time = New<DateTime>("Create_Time", QDbType.Time, DbType.Time, false));
            Add(Last_Update = New<DateTime>("Last_Update", QDbType.Time, DbType.Time, false)); 
        }

        public QColumnT<int> RSID;
        public QColumnT<string> RS_Code;
        public QColumnT<string> CN_Name;
        public QColumnT<string> EN_Name;
        public QColumnT<string> Uri_Path;
        public QColumnT<string> Uri_Target;
        public QColumnT<string> Ico_Path;
        public QColumnT<int> Order_Index;
        public QColumnT<string> Note;
        public QColumnT<string> Tags;
        public QColumnT<bool> Is_Disabled;
        public QColumnT<DateTime> Create_Time;
        public QColumnT<DateTime> Last_Update;
    }
}
    