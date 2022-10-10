package com.abp.backend.service.Impl.bot;

import com.abp.backend.mapper.BotMapper;
import com.abp.backend.pojo.Bot;
import com.abp.backend.pojo.User;
import com.abp.backend.service.Impl.utils.UserDetailsImpl;
import com.abp.backend.service.bot.RemoveBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveBotServiceImpl implements RemoveBotService {
    @Autowired
    private BotMapper botMapper;
    
    @Override
    public Map<String, String> removeBot(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        Integer bot_id = Integer.valueOf(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id);

        if (bot == null) {
            map.put("error_message", "Bot不存在或已经被删除");
            return map;
        }

        if(!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "没有权限删除此Bot");
            return map;
        }

        botMapper.deleteById(bot_id);
        map.put("error_message", "success");
        return map;
    }
}
