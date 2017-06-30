using System;
using System.Collections.Generic;
using System.Text;

using Weed.SDQ;

namespace Weed.BCF
{
    /// <summary>
    /// OPS-XML 存储服务
    /// </summary>
    public class OPSxService : BCF_PARAMS
    {
        /// <summary>
        /// 静态实例
        /// </summary>
        public static OPSxService G = new OPSxService();

        /// <summary>
        /// 设置OPSx
        /// </summary>
        /// <param name="LK_OBJT"></param>
        /// <param name="LK_OBJT_ID"></param>
        /// <param name="opsXml"></param>
        public void SetOPSx(int LK_OBJT, int LK_OBJT_ID, string opsXml)
        {
            this.SetOPSx(LK_OBJT, LK_OBJT_ID, "S", opsXml, null);
        }

        /// <summary>
        /// 设置OPSx
        /// </summary>
        /// <param name="LK_OBJT"></param>
        /// <param name="LK_OBJT_ID"></param>
        /// <param name="tags"></param>
        /// <param name="opsXml"></param>
        /// <param name="tran"></param>
        public void SetOPSx(int LK_OBJT, int LK_OBJT_ID, string tags, string opsXml, QDbTran tran)
        {
            OPSxM o = new OPSxM();

            o.LK_OBJT.Value = LK_OBJT;
            o.LK_OBJT_ID.Value = LK_OBJT_ID;
            o.Tags.Value = tags;
            o.OPSx.Value = opsXml;

            o.Where(o.LK_OBJT == LK_OBJT)
             .And(o.LK_OBJT_ID == LK_OBJT_ID)
             .And(o.Tags == tags)
             .UsingFilter(false);

            if (o.Update(tran) == false)
                o.Insert(tran);
        }

        /// <summary>
        /// 获取OPSx
        /// </summary>
        /// <param name="LK_OBJT"></param>
        /// <param name="LK_OBJT_ID"></param>
        /// <returns></returns>
        public string GetOPSx(int LK_OBJT, int LK_OBJT_ID)
        {
            return GetOPSx(LK_OBJT, LK_OBJT_ID, null);
        }

        /// <summary>
        /// 获取OPSx
        /// </summary>
        /// <param name="LK_OBJT"></param>
        /// <param name="LK_OBJT_ID"></param>
        /// <param name="tags"></param>
        /// <returns></returns>
        public string GetOPSx(int LK_OBJT, int LK_OBJT_ID, string tags)
        {
            OPSxM o = new OPSxM();

            o.Where(o.LK_OBJT == LK_OBJT).And(o.LK_OBJT_ID == LK_OBJT_ID);

            if (tags != null)
                o.And(o.Tags.Like("%" + tags + "%"));

            return o.SelectValue<string>(o.OPSx, "");
        }

        /// <summary>
        /// 移除[删除]OPSx
        /// </summary>
        /// <param name="LK_OBJT"></param>
        /// <param name="LK_OBJT_ID"></param>
        public void RemoveOPSx(int LK_OBJT, int LK_OBJT_ID)
        {
            this.RemoveOPSx(LK_OBJT, LK_OBJT_ID, null);
        }

        /// <summary>
        /// 移除[删除]OPSx
        /// </summary>
        /// <param name="LK_OBJT"></param>
        /// <param name="LK_OBJT_ID"></param>
        /// <param name="tran"></param>
        public void RemoveOPSx(int LK_OBJT, int LK_OBJT_ID, QDbTran tran)
        {
            OPSxM o = new OPSxM();

            o.Where(o.LK_OBJT == LK_OBJT)
             .And(o.LK_OBJT_ID == LK_OBJT_ID)
             .UsingFilter(false)
             .Delete(tran);
        }
    }
}
