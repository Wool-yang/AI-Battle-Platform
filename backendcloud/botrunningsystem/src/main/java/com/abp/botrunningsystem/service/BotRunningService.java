package com.abp.botrunningsystem.service;

public interface BotRunningService {
    String addBot(Integer userId, Integer oppoId, String botCode, String input);
}
