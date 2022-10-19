package com.abp.backend.controller.user.account;

import com.abp.backend.service.user.account.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdatePasswordController {
    @Autowired
    private UpdatePasswordService updatePasswordService;

    @PutMapping("/api/user/account/changepasswd/")
    public Map<String, String> updateBot(@RequestParam Map<String, String> data) {
        return updatePasswordService.updatePassword(data);
    }
}
