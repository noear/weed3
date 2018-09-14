using Noear.Weed;
using System;
using System.Collections.Generic;
using Weed3Demo.demo.model;

namespace Weed3Demo.demo.table {
    public class demo_table {
        static DbContext db = DbConfig.pc_user;

        public static void demo_insert() {
            long row_id = db.table("test")
                            .insert(new DataItem().set("log_time", "$DATE(NOW())"));

            if (row_id == 0)
                return;
        }

        public static void demo_update() {

            //1
            db.table("test")
              .where("id IN (?...)", new int[] { 15,14,16})
              .update(new DataItem().set("txt", "NOW()xx").set("num", 44));

            //2. 加别名
            db.table("test t")
              .where("t.id=?", 17)
              .update(new DataItem().set("t.txt", "fff").set("num", 111));

            
            //1
            db.table("test")
              .where("id IN (?...)", db.table("user_info").where("user_id<?", 16).select("user_id"))
              .update(new DataItem().set("txt", "NOW()xx").set("num", 44));
        }

        public static void demo_delete() {
            db.table("test")
              .where("id=?", 12)
              .delete();
        }

        public static List<UserInfoModel> demo_select() {
            var list = db.table("user_info")
                         .where("user_id<?", 10)
                         .select("user_id,name,sex")
                         .getList(new UserInfoModel());

            return list;
        }

        public static void demo_select_join() {
            var dt = db.table("test a")
                       .innerJoin("user_info b").on("b.user_id=a.id")
                       .where("a.id<?", 20)
                       .limit(100)
                       .select("a.*,b.name").getDataList();


            int count = dt.getRowCount();
            if (count > 0)
                return;
        }

        public static void demo_select_complex() {
            var dt = db.table("test a")
                       .innerJoin("user_info b").on("b.user_id=a.id")
                       .where("a.id<15")
                       .groupBy("a.num HAVING a.num>1")
                       .orderBy("a.num DESC")
                       .select("num,COUNT(b.user_id)")
                       .getDataList();

            int count = dt.getRowCount();
            if (count > 0)
                return;
        }

        public static DataList demo_select_complex_pin() {
            var dbTable = doGetAllInvite(1, 20, "u", 10001, 0);

            return dbTable;
        }

        private static DataList doGetAllInvite(int pageIndex, int pageSize, String where, long whereVal, int _static) {
            DataList dl;
            DbContext db = DbConfig.pc_pool;

            int start = pageSize * (pageIndex - 1);

            //代码拼装
            var qr = db.table("$.invites").where("1=1");
            if (whereVal > 0) {
                if (where == "u")
                    qr.and("(master_id=? OR user_id=?)", whereVal, whereVal);
                else
                    qr.and(where + "=?", whereVal);
            }

            if (_static == 1)
                qr.and("(master_id > 10020 OR user_id > ？)", 10020);

            if (_static == 2)
                qr.and("(master_id <= 10020 AND user_id <= 10020)");

            qr.orderBy("invite_id DESC")
              .limit(start, pageSize);

            dl = qr.select("*").getDataList();

            return dl;
        }

        public static DataList ddd() {
            DbContext db = DbConfig.pc_bcf;

            //1.找出我所有的资源
            var rids = db.table("BCF_Resource r")
                    .innerJoin("BCF_Resource_Linked rl").on("r.RSID=rl.RSID")
                    .where("rl.LK_OBJT_ID=? AND rl.LK_OBJT=?", 12737, 7)
                    .select("r.RSID")
                    .getArray<int>("RSID");

            //2.找出资源相关的组id
            var pids = db.table("BCF_Resource_Linked rl")
                    .where("rl.LK_OBJT=? AND rl.RSID IN (?...)", 2, rids)
                    .select("DISTINCT rl.LK_OBJT_ID")
                    .getArray<int>("LK_OBJT_ID");

            //3.找出相关组的诚意情
            return db.table("BCF_Group")
                            .where("PGID IN (?...) AND Is_Disabled=0 AND Is_Visibled=1", pids)
                            .orderBy("Order_Index")
                            .select("*")
                            .getDataList();
        }
    }
}
