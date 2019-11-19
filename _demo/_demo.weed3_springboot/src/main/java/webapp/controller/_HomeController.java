package webapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/")
@RestController
public class _HomeController {
    @RequestMapping("")
    public Object home(){
        return new ModelAndView("/nav.ftl");
    }
}
