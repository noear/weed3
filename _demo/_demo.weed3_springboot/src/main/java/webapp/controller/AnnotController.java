package webapp.controller;

import org.noear.weed.DbContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import webapp.dso.SqlAnnotation;


@RequestMapping("/annot")
@RestController
public class AnnotController {
    @Autowired
    @Qualifier("db2")
    DbContext db2;

    @RequestMapping("demo0/html")
    public ModelAndView demo0() throws Exception {
        ModelAndView mv = new ModelAndView("/view.ftl");

        Object _map = demo3();
        mv.addObject("map", _map);

        return mv;
    }

    @RequestMapping("demo1/json")
    public Object demo1() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get();
    }

    @RequestMapping("demo2/json")
    public Object demo2() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get2(48);
    }

    @RequestMapping("demo3/json")
    public Object demo3() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_get3("appx",48);
    }

    @RequestMapping("demo4/json")
    public Object demo4() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getlist(1);
    }

    @RequestMapping("demo5/json")
    public Object demo5() throws Exception {
        return db2.mapper(SqlAnnotation.class).appx_getids();
    }

}
