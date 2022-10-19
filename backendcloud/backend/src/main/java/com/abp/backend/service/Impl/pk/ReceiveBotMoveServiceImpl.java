package com.abp.backend.service.Impl.pk;

import com.abp.backend.consumer.WebSocketServer;
import com.abp.backend.consumer.utils.Game;
import com.abp.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer oppoId, Integer direction) {
        // System.out.println("receive bot move: " + userId + " " + oppoId + " " + direction);

        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).getGame();
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        } else if (WebSocketServer.users.get(userId) == null && WebSocketServer.users.get(oppoId) != null) {
            Game game = WebSocketServer.users.get(oppoId).getGame();
            if (game != null && check_game_valid(game.getPlayerA().getId(),
                                                 game.getPlayerB().getId(),
                                                 userId,
                                                 oppoId)) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }
        return "receive bot move";
    }

    private Boolean check_game_valid(Integer GA, Integer GB, Integer a, Integer b) {
        return GA.equals(a) && GB.equals(b) || GA.equals(b) && GB.equals(a);
    }
}
