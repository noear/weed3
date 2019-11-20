package webapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/")
@RestController
public class _HomeController {
    //
    // 好久没弄 spring boot 了，视图没弄出来
    //
    @RequestMapping("")
    public void home(HttpServletResponse response) throws Exception{
        response.sendRedirect("/nav.htm");
    }
}
