package webapp.controller;


import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

@Controller
public class _HomeController {
    @Mapping("/")
    public Object home() {
        return new ModelAndView("nav.htm", null);
    }
}
