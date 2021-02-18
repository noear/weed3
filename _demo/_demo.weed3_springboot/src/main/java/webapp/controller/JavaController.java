package webapp.controller;

import org.noear.weed.DbContext;
import org.noear.weed.cache.ICacheServiceEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import webapp.model.AppxModel;



@RequestMapping("/java")
@RestController
public class JavaController {
    @Autowired
    DbContext db2;

    @Autowired
    ICacheServiceEx cache;

    //
    // 好久没弄 spring boot 了，视图没弄出来（试下面几个json的）
    //
    @RequestMapping("demo0/html")
    public ModelAndView demo0() throws Exception {
        ModelAndView mv = new ModelAndView("view");

        Object _map = demo3();
        mv.addObject("map", _map);

        return mv;
    }

    @RequestMapping("demo1/json")
    public Object demo1() throws Exception {
        //
        // select app_id from appx limit 1
        //
        return db2.table("appx")
                .limit(1)
                .select("app_id")
                .getValue();
    }

    @RequestMapping("demo2/json")
    public Object demo2() throws Exception {
        //
        // select * from appx where app_id = @{app_id} limit 1
        //
        int app_id = 48;

        return db2.table("appx")
                .whereEq("app_id", app_id)
                .limit(1)
                .select("*")
                .caching(cache)
                .cacheTag("app_" + app_id)
                .getItem(AppxModel.class);
    }

    @RequestMapping("demo3/json")
    public Object demo3() throws Exception {
        //
        // select * from ${tb} where app_id = @{app_id} limit 1
        //
        int app_id = 48;
        String tb = "appx";

        Object tmp = db2.table(tb)
                .whereEq("app_id", app_id)
                .limit(1)
                .select("*")
                .getMap();

        cache.clear("test");

        return tmp;
    }

    @RequestMapping("demo4/json")
    public Object demo4() throws Exception {
        int app_id = 48;

        //
        // select * from appx where app_id>@{app_id} order by app_id asc limit 4
        //
        return db2.table("appx")
                .where("app_id>?",48)
                .orderBy("app_id ASC")
                .limit(4)
                .select("*")
                .getList(AppxModel.class);
    }

    @RequestMapping("demo5/json")
    public Object demo5() throws Exception {
        //
        // select app_id from appx limit 4
        //
        return db2.table("appx")
                .limit(4)
                .select("app_id")
                .getArray(0);
    }
}
