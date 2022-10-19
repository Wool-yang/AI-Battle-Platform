package com.abp.backend.service.Impl.user;

import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.Bot;
import com.abp.backend.pojo.User;
import com.abp.backend.service.Impl.utils.UserDetailsImpl;
import com.abp.backend.service.user.account.UpdateDetailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateDetailInfoServiceImpl implements UpdateDetailInfoService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> updateDetailInfo(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String email = data.get("email");
        String phone = data.get("phone");
        String other = data.get("other");

        Map<String, String> map = new HashMap<>();

        if (user == null) {
            map.put("error_message", "System error, please try again");
            return map;
        }

        if (email.length() > 100) {
            map.put("error_message", "Email length cannot be greater than 100");
            return map;
        }

        if (phone.length() > 50) {
            map.put("error_message", "Phone length cannot be greater than 50");
            return map;
        }

        if (other.length() > 500) {
            map.put("error_message", "Some thing else length cannot be greater than 500");
            return map;
        }

        user.setEmail(email);
        user.setPhone(phone);
        user.setOther(other);

        userMapper.updateById(user);
        map.put("error_message", "success");
        return map;
    }
}
