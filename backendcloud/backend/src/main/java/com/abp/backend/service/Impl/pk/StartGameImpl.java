package com.abp.backend.service.Impl.pk;

import com.abp.backend.consumer.WebSocketServer;
import com.abp.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StartGameImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        // System.out.println("start game: " + aId + " " + bId);
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }
}
