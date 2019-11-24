package webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@RequestMapping("/")
@Controller
public class _HomeController {

    @RequestMapping("/")
    public Object home() {
        return new ModelAndView("nav");
//        return "nav";
    }
}
