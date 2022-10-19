package com.abp.backend.controller.user.account;

import com.abp.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/api/user/account/register")
    public Map<String, String> register(@RequestParam Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String confirmedPassword = data.get("confirmedPassword");
        Integer photo = Integer.valueOf(data.get("photo"));
        return registerService.register(username, password, confirmedPassword, photo);
    }
}
