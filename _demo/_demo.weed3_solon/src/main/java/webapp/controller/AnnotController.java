package webapp.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Singleton;
import org.noear.solon.core.handle.ModelAndView;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;
import webapp.dso.SqlAnnotation;
import webapp.model.AppxModel;

import java.util.HashMap;
import java.util.Map;


@Mapping("/annot")
@Singleton(true)
@Controller
public class AnnotController {
    @Db
    DbContext db2;

    @Db
    SqlAnnotation mapper;

    @Db
    BaseMapper<AppxModel> mapper2;

    @Mapping("demo0/html")
    public ModelAndView demo0() throws Exception {
        ModelAndView mv = new ModelAndView("view.ftl");

        Object _map = demo3();
        mv.put("map", _map);

        return mv;
    }

    @Mapping("demo1/json")
    public Object demo1() throws Exception {
        Map<String, Object> tmp = new HashMap<>();

        tmp.put("mapper2", mapper2.selectById(23));
        tmp.put("mapper", mapper.appx_get());

        return tmp;
    }

    @Mapping("demo2/json")
    public Object demo2() throws Exception {
        return mapper.appx_get2(48);
    }

    @Mapping("demo3/json")
    public Object demo3() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get3("appx",48);
    }

    @Mapping("demo4/json")
    public Object demo4() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getlist(1);
    }

    @Mapping("demo5/json")
    public Object demo5() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getids();
    }

}
