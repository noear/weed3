
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
    public class User_LinkedM : QTable
    {
        /// <summary>
        /// 
        /// </summary>
        public User_LinkedM() : base(BCF_PARAMS.DEFAULT_SDQ_NAME) { }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="sdqName"></param>
        public User_LinkedM(string sdqName) : base(sdqName) { }
        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public override IQBinder Clone()
        {
            return new User_LinkedM();
        }
        /// <summary>
        /// 
        /// </summary>
        protected override void OnInit()
        {
            this.TableName = "BCF_User_Linked";

            //Add(col = New<type>(string name, QDbType type, bool isKey));
            //
            Add(PUID = New<int>("PUID", QDbType.NUM, DbType.Int32, true));
            Add(LK_OBJT = New<int>("LK_OBJT", QDbType.NUM, DbType.Int32, false));
            Add(LK_OBJT_ID = New<int>("LK_OBJT_ID", QDbType.NUM, DbType.Int32, false));
            Add(LK_Operate = New<string>("LK_Operate", QDbType.Text, DbType.String, false)); 
        }
        /// <summary>
        /// 
        /// </summary>
        public QColumnT<int> PUID;
        /// <summary>
        /// 
        /// </summary>
        public QColumnT<int> LK_OBJT;
        /// <summary>
        /// 
        /// </summary>
        public QColumnT<int> LK_OBJT_ID;
        /// <summary>
        /// 
        /// </summary>
        public QColumnT<string> LK_Operate;
    }
}
    