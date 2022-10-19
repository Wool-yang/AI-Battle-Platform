package com.abp.backend.service.Impl.user;

import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.User;
import com.abp.backend.service.Impl.utils.UserDetailsImpl;
import com.abp.backend.service.user.account.UpdatePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdatePhotoServiceImpl implements UpdatePhotoService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> updatePhoto(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Integer photo = Integer.parseInt(data.get("photo"));

        Map<String, String> map = new HashMap<>();

        if (photo == null) {
            map.put("error_message", "System error, please try again");
            return map;
        }

        user.setPhoto(photo);

        userMapper.updateById(user);
        map.put("error_message", "success");
        return map;
    }
}
