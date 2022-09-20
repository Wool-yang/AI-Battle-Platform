package com.abp.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/pk")
public class BotInfoController {
    @RequestMapping("/getbotinfo")
    public List<String> getBotInfo() {
        LinkedList<String> strings = new LinkedList<>();
        strings.add("122");
        strings.add("af");
        return strings;
    }
}
