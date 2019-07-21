package com.xupt.seckill.pojo;

import lombok.Data;

/**
 * @author maxu
 * @date 2019/7/20
 */
@Data
public class StockLog {
    private int id;
    private int amount;
    private int status;
    private int itemId;
}
