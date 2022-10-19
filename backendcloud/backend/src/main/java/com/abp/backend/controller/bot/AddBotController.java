package com.abp.backend.controller.bot;

import com.abp.backend.service.bot.AddBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddBotController {
    @Autowired
    private AddBotService addBotService;

    @PostMapping("/api/user/bot/add/")
    public Map<String, String> addBot(@RequestParam Map<String, String> bot) {
        return addBotService.addBot(bot);
    }
}
