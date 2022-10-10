package com.abp.backend.consumer;

import com.abp.backend.consumer.utils.Game;
import com.abp.backend.consumer.utils.JwtAuthentication;
import com.abp.backend.mapper.RecordMapper;
import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    // 内存模拟匹配系统，线程安全
    private final static CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();


    private User user;                      // 维护当前连接储存的用户信息

    private Game game;                      // 维护当前连接的 Game 线程
    private Session session = null;         // 每个链接由 Session 维护，使用 Session 向 Client 发送信息


    /*Spring 的容器管理默认是单例的，只会注入一次，
    而 WebSocket 是多对象的，每当有新的 Client 的时候都会创建一个新的实例对象
    可以把注入对象声明为静态对象*/
    private static UserMapper userMapper;

    public static RecordMapper recordMapper;

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
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
            matchPool.remove(this.user);
        }
    }


    private void startMatching() {
        System.out.println("start-matching");
        matchPool.add(this.user);

        // 当 matchPool 中的元素数量大于等于 2，则匹配前两个元素
        // 这里操作没有加锁，只是做演示用
        while (matchPool.size() >= 2) {
            Iterator<User> it = matchPool.iterator();

            User a = it.next();
            User b = it.next();

            matchPool.remove(a);
            matchPool.remove(b);

            Game game = new Game(17, 18, 38, a.getId(), b.getId());
            game.createMap();

            // 初始化 a、b 的 game
            users.get(a.getId()).game = game;
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
            users.get(a.getId()).sendMessage(respA.toJSONString());
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    private void stopMatching() {
        System.out.println("stop-matching");
        matchPool.remove(this.user);
    }

    private void move(Integer direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
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
            startMatching();
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
