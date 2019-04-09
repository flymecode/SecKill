package com.xupt.seckill.redis;

public class UserKey extends BasePrefix{

    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
}
