package webapp.controller;

import org.noear.weed.DbContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import webapp.dso.SqlMapper;
import webapp.dso.SqlMapper2;
import webapp.model.AppxModel;

import java.util.Map;


@RestController
public class DemoController {
    @Autowired
    @Qualifier("db1")
    DbContext db1;

    @Autowired
    @Qualifier("db2")
    DbContext db2;


    //demo1:使用数据库查询，并返回json
    @GetMapping("/demo1/json")
    public Object demo1() throws Exception{
        Map map = db1.table("appx").limit(1).select("*").getMap();

        return map;
    }

    //demo2:使用带连接池的数据库查询，并返回json
    @GetMapping("/demo2/json")
    public Object demo2() throws Exception{
        return db2.table("appx").limit(1).select("*").getMap();
    }

    //demo3:使用带连接池的数据库查询，并返回json
    @GetMapping("/demo3/json")
    public Object demo3() throws Exception{
        return db2.call("@webapp.dso.appx_get").getMap();
    }

    //demo4:使用Xmlsql Mapper查询，并返回json
    @GetMapping("/demo4/json")
    public Object demo4() throws Exception{
        SqlMapper tmp = db2.mapper(SqlMapper.class);

        return tmp.appx_get();
    }

    //demo5:使用sql 注解查询，并返回int
    @GetMapping("/demo5/json")
    public Object demo5() throws Exception{
        SqlMapper2 tmp = db2.mapper(SqlMapper2.class);

        return tmp.appx_get();
    }

    //demo6:使用sql querey查询，并返回model
    @GetMapping("/demo6/json")
    public Object demo6_1() throws Exception{
        return
        db2.call("select * from appx where app_id = @app_id limit 1")
                .set("app_id",1)
                .getItem(AppxModel.class);
    }

    //demo7:使用sql 注解 查询，并返回map
    @GetMapping("/demo7/json")
    public Object demo7() throws Exception{
        SqlMapper2 tmp = db2.mapper(SqlMapper2.class);

        return tmp.appx_get3("appx",1);
    }

    //demo8:使用sql 注解 查询，并返回list
    @GetMapping("/demo8/json")
    public Object demo8() throws Exception{
        SqlMapper2 tmp = db2.mapper(SqlMapper2.class);

        return tmp.appx_getlist(1);
    }

}
