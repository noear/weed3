package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.annotation.XSingleton;
import org.noear.solon.core.ModelAndView;
import org.noear.weed.BaseMapper;
import org.noear.weed.DbContext;
import org.noear.weed.annotation.Db;
import webapp.dso.SqlAnnotation;
import webapp.model.AppxModel;

import java.util.HashMap;
import java.util.Map;


@XMapping("/annot")
@XSingleton(true)
@XController
public class AnnotController {
    @Db
    DbContext db2;

    @Db
    SqlAnnotation mapper;

    @Db
    BaseMapper<AppxModel> mapper2;

    @XMapping("demo0/html")
    public ModelAndView demo0() throws Exception {
        ModelAndView mv = new ModelAndView("view.ftl");

        Object _map = demo3();
        mv.put("map", _map);

        return mv;
    }

    @XMapping("demo1/json")
    public Object demo1() throws Exception {
        Map<String, Object> tmp = new HashMap<>();

        tmp.put("mapper2", mapper2.selectById(23));
        tmp.put("mapper", mapper.appx_get());

        return tmp;
    }

    @XMapping("demo2/json")
    public Object demo2() throws Exception {
        return mapper.appx_get2(48);
    }

    @XMapping("demo3/json")
    public Object demo3() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get3("appx",48);
    }

    @XMapping("demo4/json")
    public Object demo4() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getlist(1);
    }

    @XMapping("demo5/json")
    public Object demo5() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getids();
    }

}
