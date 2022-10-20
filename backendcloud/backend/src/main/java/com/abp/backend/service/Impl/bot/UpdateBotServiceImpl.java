package com.abp.backend.service.Impl.bot;

import com.abp.backend.mapper.BotMapper;
import com.abp.backend.pojo.Bot;
import com.abp.backend.pojo.User;
import com.abp.backend.service.Impl.utils.UserDetailsImpl;
import com.abp.backend.service.bot.UpdateBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateBotServiceImpl implements UpdateBotService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> updateBot(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();



        Integer bot_id = Integer.valueOf(data.get("bot_id"));

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();
        Bot bot = botMapper.selectById(bot_id);

        if (bot == null) {
            map.put("error_message", "Bot does not exist or has been deleted");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "No permission to modify this bot");
            return map;
        }

        if (title == null || title.length() == 0) {
            map.put("error_message", "Bot title cannot be empty");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "Bot title length cannot be greater than 100");
            return map;
        }

        if (description == null || description.length() == 0) {
            description = "This user is lazy and did not write a code description";
        }

        if (description.length() > 300) {
            map.put("error_message", "Bot description length cannot be greater than 300");
            return map;
        }


        if (content == null || content.length() == 0) {
            map.put("error_message", "Code cannot be empty");
            return map;
        }

        if (description.length() > 10000) {
            map.put("error_message", "Code length cannot be greater than 10000");
            return map;
        }

        Date now = new Date();

        Bot new_bot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getCreatetime(),
                now
        );

        botMapper.updateById(new_bot);
        map.put("error_message", "success");
        return map;
    }
}
