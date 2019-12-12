package weed3demo.demo2;

import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;
import weed3demo.DbUtil;

public class Demo2 {
    DbContext db = DbUtil.db;

    public Object demo1(String name, String akey) throws Exception {
        DbTableQuery qr = db.table("appx").where("1=1");

        if (name != null) {
            qr.and("name=?", name);
        }

        if (akey != null) {
            qr.and("akey=?", akey);
        }

        return qr.limit(1).select("*").getMapList();
    }

    public Object demo1_2(String name, String akey) throws Exception {
        return db.table("appx").where("1=1").build((qr) -> {
            if (name != null) {
                qr.and("name=?", name);
            }

            if (akey != null) {
                qr.and("akey=?", akey);
            }
        }).limit(1).select("*").getMapList();
    }

    public Object demo1_3(String name, String akey) throws Exception {
        return db.table("appx")
                .where("1=1")
                .andIf(name != null, "name=?", name)
                .andIf(akey != null, "akey=?", akey)
                .limit(1).select("*").getMapList();
    }


    public void demo2(String name, String note, String akey) throws Exception {
        DbTableQuery qr = db.table("appx").set("log_fulltime", "$NOW()");

        if (name != null) {
            qr.set("name", name);
        }

        if (note != null) {
            qr.set("note", note);
        }

        if (akey != null) {
            qr.set("akey", akey);
        }

        qr.insert();
    }

    public void demo2_2(String name, String note, String akey) throws Exception {
        db.table("appx").set("log_fulltime", "$NOW()").build(qr -> {
            if (name != null) {
                qr.set("name", name);
            }

            if (note != null) {
                qr.set("note", note);
            }

            if (akey != null) {
                qr.set("akey", akey);
            }
        }).insert();
    }

    public void demo2_3(String name, String note, String akey) throws Exception {
        db.table("appx")
                .set("log_fulltime", "$NOW()")
                .setIf(name != null, "name", name)
                .setIf(note != null, "note", note)
                .setIf(akey != null, "akey", akey)
                .insert();
    }
}
