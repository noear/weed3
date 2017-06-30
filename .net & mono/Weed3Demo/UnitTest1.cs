using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

using Weed3Demo.test.model;
using Weed3Demo.test.store;
using Weed3Demo.test.table;

namespace Weed3Demo {
    [TestClass]
    public class UnitTest1 {
        [TestMethod]
        public void TestMethod1() {

            string[] args = new string[] { "1", "2" };

            var l = DbConfig.test.table("users").where("mobile in (?...)",args).select("*").getDataList();

            //long num = DbConfig.test.sql("SELECT 10 FROM users WHERE UserID=? LIMIT 1", 1).getValue<long>(0);

            //Assert.AreEqual(10, num);
        }

        [TestMethod]
        public void TestMethod21() {
            user_get sp = new user_get();
            sp.userID = 1;

            var m = sp.getItem(new UserModel());

        }

        [TestMethod]
        public void TestMethod22() {
            user_update sp = new user_update();
            sp.userID = 1;
            sp.city = "xxx";
            sp.vipTime = DateTime.Now;

            int num = sp.execute();
            if (num > 0)
                return;
        }

        [TestMethod]
        public void TestMethod31() {
            users tb = new users();

            tb.Nickname = "xxxx";
            tb.City = "xx";

            long num = tb.insert();
            if (num > 0)
                return;
        }

        [TestMethod]
        public void TestMethod32() {
            users tb = new users();

            tb.Nickname = "xxxx";
            tb.City = "xx";

            long num = tb.where("UserID=?", 1).update();
            if (num > 0)
                return;
        }

        [TestMethod]
        public void TestMethod33() {
            users tb = new users();

            long num = tb.where("UserID=?", 1L).delete();
            if (num > 0)
                return;
        }

        [TestMethod]
        public void TestMethod34() {
            users tb = new users();

            long num = tb.where("UserID=?", 1L)
                         .select("UserID")
                         .getValue(0L);
            if (num > 0)
                return;
        }

        [TestMethod]
        public void TestMethod35() {
            users tb = new users();

            var m = tb.where("UserID<? AND sex=?", 100, 1)
                      .limit(1)
                      .select("UserID,Nickname,Sex")
                      .getItem(new UserModel());

            if (m.UserID > 0)
                return;
        }
    }
}
