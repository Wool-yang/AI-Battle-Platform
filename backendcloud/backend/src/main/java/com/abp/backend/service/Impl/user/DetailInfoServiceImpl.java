package com.abp.backend.service.Impl.user;

import com.abp.backend.pojo.User;
import com.abp.backend.service.Impl.utils.UserDetailsImpl;
import com.abp.backend.service.user.account.DetailInfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class DetailInfoServiceImpl implements DetailInfoService {
    @Override
    public Map<String, String> getDetailInfo() {
        // 根据 Security 上下文获取用户 user
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();

        map.put("error_message", "success");
        map.put("rating", user.getRating().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("last_game", sdf.format(user.getLastFinish()));
        map.put("email", user.getEmail());
        map.put("phone", user.getPhone());
        map.put("other", user.getOther());

        return map;
    }
}
