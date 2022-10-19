package com.abp.backend.controller.user.account;

import com.abp.backend.service.user.account.DetailInfoService;
import com.abp.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DetailInfoController {
    @Autowired
    private DetailInfoService detailInfoService;

    @GetMapping("/api/user/account/detailinfo/")
    public Map<String, String> getDetailInfo() {
        return detailInfoService.getDetailInfo();
    }
}
