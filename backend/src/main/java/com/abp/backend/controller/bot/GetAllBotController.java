package com.abp.backend.controller.bot;

import com.abp.backend.pojo.Bot;
import com.abp.backend.service.bot.GetAllBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GetAllBotController {
    @Autowired
    private GetAllBotService getAllBotService;

    @GetMapping("/user/bot/getall")
    public List<Bot> getAllBot() {
        return getAllBotService.GetAllBot();
    }
}
