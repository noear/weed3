package webapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/")
@RestController
public class _HomeController {
    @RequestMapping("")
    public void home(HttpServletResponse response) throws Exception{
        response.sendRedirect("/nav.htm");
    }
}
