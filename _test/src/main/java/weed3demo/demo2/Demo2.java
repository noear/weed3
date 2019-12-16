package weed3demo.demo2;

import org.noear.weed.DbContext;
import org.noear.weed.DbTableQuery;

import java.util.Map;

public class Demo2 {
    DbContext db = new DbContext();

    public Object searchBy(Integer id, String name, Integer type) throws Exception {
        DbTableQuery qr = db.table("user").where("1=1");
        if (id != null) {
            qr.andEq("id", id);
        }

        if (name != null) {
            qr.andEq("name", name);
        }

        if (type != null && type > 2) {
            qr.andEq("type", type);
        }

        return qr.limit(50).select("*").getMapList();
    }

    public Object searchBy2(Integer id, String name, Integer type) throws Exception {
        return db.table("user")
                .where("1=1")
                .andIf(id != null, "id=?", id)
                .andIf(name != null, "name=?", name)
                .andIf(type != null && type > 2, "type=?", type)
                .limit(50)
                .select("*").getMapList();
    }

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

    public void insert(Map<String, Object> map) throws Exception {
        DbTableQuery qr = db.table("user");
        map.forEach((k, v) -> {
            if (v != null) {
                qr.set(k, v);
            }
        });
        qr.insert();
    }

    public void insert2(Map<String, Object> map) throws Exception {
        db.table("user").setMapIf(map, (k, v) -> v != null).insert();
    }
}
