package com.abp.backend.controller.pk;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String index() {
        return "pk/index.html";
    }
}
