package com.abp.backend.controller.user;

import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("{id}")
    public User getById(@PathVariable Integer id) {
        return userMapper.selectById(id);
    }

    @PostMapping
    public Boolean addUser(@RequestBody  User user) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return userMapper.insert(user) > 0;
    }

    @DeleteMapping("{id}")
    public Boolean deleteById(@PathVariable Integer id) {
        return userMapper.deleteById(id) > 0;
    }
}
