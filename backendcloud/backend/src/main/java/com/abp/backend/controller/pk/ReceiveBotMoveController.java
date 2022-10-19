package com.abp.backend.controller.pk;

import com.abp.backend.service.pk.ReceiveBotMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ReceiveBotMoveController {
    @Autowired
    private ReceiveBotMoveService receiveBotMoveService;

    @PostMapping("/pk/receive/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer oppoId = Integer.parseInt(Objects.requireNonNull(data.getFirst("oppo_id")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        return receiveBotMoveService.receiveBotMove(userId, oppoId, direction);
    }
}
