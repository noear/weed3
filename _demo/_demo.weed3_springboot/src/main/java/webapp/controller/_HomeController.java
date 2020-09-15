package webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class _HomeController {

    @RequestMapping("/")
    public String home() {
        return "nav";
    }
}
