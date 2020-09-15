package webapp.controller;

import org.noear.solon.annotation.XController;
import org.noear.solon.annotation.XMapping;
import org.noear.solon.core.ModelAndView;

@XController
public class _HomeController {
    @XMapping("/")
    public Object home() {
        return new ModelAndView("nav.htm", null);
    }
}
