# 一些新接口的应用说明

#### whereIf(),andIf(),orIf()
> 以andIf()做为示例说明：

* 旧风格代码：
```java
public Object searchBy(Integer id, String name,Integer type) throws Exception {
    DbTableQuery qr = db.table("user").whereTrue();
    if (id != null) {
        qr.and("id=?", id);
    }

    if (name != null) {
        qr.and("name=?", name);
    }

    if (type != null && type > 2) {
        qr.and("type=?", type);
    }

    return qr.limit(50).select("*").getMapList();
}
```

* 新风格代码：
```java
public Object searchBy(Integer id, String name,Integer type) throws Exception {
    return db.table("user")
            .whereTrue()
            .andIf(id != null, "id=?", id)
            .andIf(name != null, "name=?", name)
            .andIf(type != null && type > 2, "type=?", type)
            .limit(50)
            .select("*").getMapList()
}
```

#### setIf(),setMapIf(),setEntityIf()
> 以setMapIf()做为示例说明：

* 旧风格代码：
```java
public void insertUser(Map<String, Object> map) throws Exception {
    DbTableQuery qr = db.table("user");
    map.forEach((k, v) -> {
        if (v != null) {
            qr.set(k, v);
        }
    });
    qr.insert();
}
```

* 新风格代码：
```java
public void insertUser(Map<String, Object> map) throws Exception {
    db.table("user").setMapIf(map, (k, v) -> v != null).insert();
}
```
