package com.abp.backend.controller.bot;

import com.abp.backend.service.bot.RemoveBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RemoveBotController {
    @Autowired
    private RemoveBotService removeBotService;

    @DeleteMapping("/api/user/bot/remove")
    public Map<String, String> removeBot(@RequestParam Map<String, String> data) {
        return removeBotService.removeBot(data);
    }
}
