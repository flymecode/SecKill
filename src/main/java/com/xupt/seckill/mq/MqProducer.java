package com.xupt.seckill.mq;

import org.springframework.stereotype.Component;

/**
 * @author maxu
 * @date 2019/6/25
 */
@Component
public class MqProducer {

    public boolean transactionAsyncReduceStock(Integer itemId, Integer amount) {
        return false;
    }
}
