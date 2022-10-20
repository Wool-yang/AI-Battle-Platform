package com.abp.backend.service.Impl.user;

import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.User;
import com.abp.backend.service.user.account.RegisterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword, Integer photo) {
        Map<String, String> map = new HashMap<>();

        if (username == null) {
            map.put("error_message", "Username can not be empty");
            return map;
        }

        if (password == null || confirmedPassword == null) {
            map.put("error_message", "Password can not be empty");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "Username can not be empty");
            return map;
        }

        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "Password can not be empty");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "Username length cannot be greater than 100");
            return map;
        }

        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "Password length cannot be greater than 100");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "The passwords entered twice do not match");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "Username already exists");
            return map;
        }

        String encodePassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodePassword, photo, 1500, null, null, false, null, null, null);
        userMapper.insert(user);

        map.put("error_message", "success");
        return map;
    }
}
