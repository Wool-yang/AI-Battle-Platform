package com.abp.backend.controller.user.account;

import com.abp.backend.service.user.account.UpdateDetailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateDetailInfoController {
    @Autowired
    private UpdateDetailInfoService updateDetailInfoService;

    @PutMapping("/api/user/account/updatedetailinfo/")
    public Map<String, String> updateBot(@RequestParam Map<String, String> data) {
        return updateDetailInfoService.updateDetailInfo(data);
    }
}
