using System;
using System.Collections.Generic;

using System.Text;

using Weed;
using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// 资源扩展视图
    /// </summary>
    public class ResourceExView : QView
    {
        #region IQView 成员
        /// <summary>
        /// 
        /// </summary>
        /// <param name="asName"></param>
        /// <returns></returns>
        public override QView As(string asName)
        {
            this.AsName = asName;

            PKG.As(asName);
            SRC.As(asName);

            return this;
        }

        #endregion

        private bool IsOnlyLink = false;

        private GroupM PKG = new GroupM();
        private ResourceM SRC = new ResourceM();

        private Resource_LinkedM __link = new Resource_LinkedM();

        /// <summary>
        /// 构造函数
        /// </summary>
        /// <param name="isOnlyLink">是否办需要关联信息</param>
        public ResourceExView(bool isOnlyLink)
        {
            this.IsOnlyLink = isOnlyLink;

            PKG.CN_Name.AsName = "PG_CN_Name";
            PKG.EN_Name.AsName = "PG_EN_Name";
            PKG.Order_Index.AsName = "PG_Order_Index";
            PKG.Is_Disabled.AsName = "PG_Is_Disabled";

            PKG.IncludeAll(false);

            PKG.PGID.IsInc = true;
            PKG.Is_Disabled.IsInc = true;

            if (IsOnlyLink == false)
            {
                PKG.CN_Name.IsInc = true;
                PKG.EN_Name.IsInc = true;
                PKG.Order_Index.IsInc = true;
            }
            else //[IsOnlyLink == true]
            {
                SRC.IncludeAll(false);
                SRC.Is_Disabled.IsInc = true;
                SRC.RSID.IsInc = true;
            }

            __link.IncludeAll(false);

            SQuery SQ = new SQuery();

            this.Code =
                SQ.From(SRC)
                    .InnerJoin(__link).On(SRC.RSID == __link.RSID)
                    .InnerJoin(PKG).On(PKG.PGID == __link.LK_OBJT_ID)
                .Where(PKG.R_PGID == Package.DEFINE)
                .SubSelect().Code;

            this.As("RV");

            //视图建好后,重新调整名称
            //
            InnerColumn(PKG.CN_Name, "PG_CN_Name");
            InnerColumn(PKG.EN_Name, "PG_EN_Name");
            InnerColumn(PKG.Order_Index, "PG_Order_Index");
            InnerColumn(PKG.Is_Disabled, "PG_Is_Disabled");
        }
        
        /// <summary>
        /// 资源RSI
        /// </summary>
        public QColumn RSID { get { return SRC.RSID; } }

        /// <summary>
        /// 资源包PGID
        /// </summary>
        public QColumn PGID { get { return PKG.PGID; } }

        /// <summary>
        /// 资源包.是否被禁用
        /// </summary>
        public QColumn PKG_Is_Disabled { get { return PKG.Is_Disabled; } }

        /// <summary>
        /// 资源包.排序
        /// </summary>
        public QColumn PKG_Order_Index { get { return PKG.Order_Index; } }

        /// <summary>
        /// 是否被禁用
        /// </summary>
        public QColumn Is_Disabled { get { return SRC.Is_Disabled; } }

        /// <summary>
        /// 排序
        /// </summary>
        public QColumn Order_Index { get { return SRC.Order_Index; } }
    }
}
