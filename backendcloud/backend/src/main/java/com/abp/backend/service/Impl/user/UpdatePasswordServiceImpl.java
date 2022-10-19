package com.abp.backend.service.Impl.user;

import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.User;
import com.abp.backend.service.Impl.utils.UserDetailsImpl;
import com.abp.backend.service.user.account.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdatePasswordServiceImpl implements UpdatePasswordService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> updatePassword(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String encodeOldPassword = userMapper.selectById(user.getId()).getPassword();
        String oldPassword = data.get("old");
        String newPassword = data.get("new");
        String confirmedNewPassword = data.get("cnew");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Map<String, String> map = new HashMap<>();

        if (oldPassword == null || oldPassword.length() == 0) {
            map.put("error_message", "old password cannot be empty");
            return map;
        }

        if (oldPassword.length() > 100) {
            map.put("error_message", "old password length cannot be greater than 100");
            return map;
        }

        if (newPassword == null || newPassword.length() == 0) {
            map.put("error_message", "New password cannot be empty");
            return map;
        }

        if (newPassword.length() > 100) {
            map.put("error_message", "New password length cannot be greater than 100");
            return map;
        }

        if (confirmedNewPassword == null || confirmedNewPassword.length() == 0) {
            map.put("error_message", "Confirmed new password cannot be empty");
            return map;
        }

        if (confirmedNewPassword.length() > 100) {
            map.put("error_message", "Confirmed new password length cannot be greater than 100");
            return map;
        }

        if (!bCryptPasswordEncoder.matches(oldPassword, encodeOldPassword)) {
            map.put("error_message", "Wrong old password");
            return map;
        }

        if (!confirmedNewPassword.equals(newPassword)) {
            map.put("error_message", "The new password does not match the confirmed new password");
            return map;
        }

        String encodeNewPassword = bCryptPasswordEncoder.encode(newPassword);

        user.setPassword(encodeNewPassword);

        userMapper.updateById(user);
        map.put("error_message", "success");
        return map;
    }
}
