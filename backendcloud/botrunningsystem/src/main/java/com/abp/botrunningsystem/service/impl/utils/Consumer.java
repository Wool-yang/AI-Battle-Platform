package com.abp.botrunningsystem.service.impl.utils;

import com.abp.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class Consumer extends Thread {
    private Bot bot;

    private static RestTemplate restTemplate;

    private final static String receiveBotUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();

        // 当前线程执行结束后 | timeout 秒之后 执行之后的语句
        try {
            this.join(timeout);     // 最多等待 timeout 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();       // 中断当前线程
        }
    }

    // 在 code 中的 Bot 类名后加 uid
    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements java.util.function.Supplier<Integer>");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);

        // joor 执行代码需要保证类名不一致，一个类名只会编译一次
        Supplier<Integer> botInterface = Reflect.compile("com.abp.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)).create().get();

        File file = new File("input.txt");
        try (PrintWriter fout = new PrintWriter(file)) {
            fout.println(bot.getInput());
            fout.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Integer direction = botInterface.get();

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.userId.toString());
        data.add("oppo_id", bot.oppoId.toString());
        data.add("direction", direction.toString());

        restTemplate.postForObject(receiveBotUrl, data, String.class);
        // System.out.println("move-direction: " + bot.userId  + " " + direction);
    }
}
