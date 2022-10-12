package com.abp.backend.consumer;

import com.abp.backend.consumer.utils.Game;
import com.abp.backend.consumer.utils.JwtAuthentication;
import com.abp.backend.mapper.BotMapper;
import com.abp.backend.mapper.RecordMapper;
import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.Bot;
import com.abp.backend.pojo.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
// 映射的链接
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    // 储存所有链接，用于根据用户 id 寻找到对应的链接
    // 创建静态变量对所有实例可见，由于每一个实例都是一个线程，所以公共变量需要是线程安全的
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    private User user;                      // 维护当前连接储存的用户信息

    private Game game;                      // 维护当前连接的 Game 线程
    private Session session = null;         // 每个链接由 Session 维护，使用 Session 向 Client 发送信息


    /*Spring 的容器管理默认是单例的，只会注入一次，
    而 WebSocket 是多对象的，每当有新的 Client 的时候都会创建一个新的实例对象
    可以把注入对象声明为静态对象*/
    private static UserMapper userMapper;

    public static RecordMapper recordMapper;

    private static BotMapper botMapper;

    // 在多个 SpringBoot 工程间进行通信
    public static RestTemplate restTemplate;
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    public Game getGame() {
        return game;
    }

    // 在建立、关闭链接时触发
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected!");

        // 解析 token 获取 userId
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId);
        Bot botA = botMapper.selectById(aBotId);

        User b = userMapper.selectById(bId);
        Bot botB = botMapper.selectById(bBotId);

        Game game = new Game(
                17,
                18,
                38,
                a.getId(),
                botA,
                b.getId(),
                botB);

        game.createMap();

        // 初始化 a、b 的 game
        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;

        game.start();

        // 将地图相关信息封装
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());

        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());

        respGame.put("map", game.getG());

        // 创建返回信息，返回对手的状态
        JSONObject respA = new JSONObject();
        // event 区分信息
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);

        // 将返回信息发送回前端
        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());
        if (users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    private void startMatching(Integer botId) {
        System.out.println("start-matching");

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());

        // 进行通信
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMatching() {
        System.out.println("stop-matching");

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());

        // 进行通信
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(Integer direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1))        // 屏蔽 Bot 参战时玩家操作
                game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(direction);
        }
    }

    // Server 接收到 Client 信息时触发
    // 当做路由来使用，判断当前消息应该交由哪一个函数进行处理
    @OnMessage
    public void onMessage(String message, Session session) {
        // 从 Client 接收消息
        System.out.println("receive message!");
        JSONObject data = JSON.parseObject(message);

        String event = data.getString("event");

        if ("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    // 从后端向前端发送信息
    public void sendMessage(String message) {
        // 异步通信，加锁
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
