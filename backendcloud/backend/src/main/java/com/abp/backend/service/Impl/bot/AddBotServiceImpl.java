package com.abp.backend.service.Impl.bot;

import com.abp.backend.mapper.BotMapper;
import com.abp.backend.pojo.Bot;
import com.abp.backend.pojo.User;
import com.abp.backend.service.Impl.utils.UserDetailsImpl;
import com.abp.backend.service.bot.AddBotService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddBotServiceImpl implements AddBotService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> addBot(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String, String> map = new HashMap<>();

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


        QueryWrapper<Bot> botQueryWrapper = new QueryWrapper<>();
        botQueryWrapper.eq("user_id", user.getId());
        if (botMapper.selectCount(botQueryWrapper) >= 10) {
            map.put("error_message", "A user can only create up to 10 bots");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, now, now);

        botMapper.insert(bot);
        map.put("error_message", "success");

        return map;
    }
}
