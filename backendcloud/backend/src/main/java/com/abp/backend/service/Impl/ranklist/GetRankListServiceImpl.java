package com.abp.backend.service.Impl.ranklist;

import com.abp.backend.mapper.UserMapper;
import com.abp.backend.pojo.User;
import com.abp.backend.service.ranklist.GetRankListService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRankListServiceImpl implements GetRankListService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getlist(Integer page) {
        IPage<User> userIPage = new Page<>(page, 10);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        queryWrapper.ne("id", 1).ne("id", 2).ne("id", 3);

        List<User> users = userMapper.selectPage(userIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();

        for (User user : users) {
            user.setPassword("");
        }
        resp.put("users_count", userMapper.selectCount(queryWrapper));
        resp.put("users", users);
        return  resp;
    }
}
