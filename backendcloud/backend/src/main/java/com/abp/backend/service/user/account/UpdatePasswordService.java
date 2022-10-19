package com.abp.backend.service.user.account;

import java.util.Map;

public interface UpdatePasswordService {
    Map<String, String> updatePassword(Map<String, String> data);
}
