package com.abp.backend.controller.bot;

import com.abp.backend.service.bot.UpdateBotService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateBotController {
    @Autowired
    private UpdateBotService updateBotService;

    @PutMapping("/api/user/bot/update/")
    public Map<String, String> updateBot(@RequestParam Map<String, String> data) {
        return updateBotService.updateBot(data);
    }
}
