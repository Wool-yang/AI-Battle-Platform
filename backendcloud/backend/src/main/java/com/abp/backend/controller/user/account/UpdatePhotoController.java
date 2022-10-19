package com.abp.backend.controller.user.account;

import com.abp.backend.service.Impl.user.UpdatePhotoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdatePhotoController {
    @Autowired
    private UpdatePhotoServiceImpl updatePhotoService;

    @PutMapping("/api/user/account/changephoto/")
    public Map<String, String> updateBot(@RequestParam Map<String, String> data) {
        return updatePhotoService.updatePhoto(data);
    }
}
