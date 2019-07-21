package com.xupt.seckill.mapper;

import com.xupt.seckill.pojo.StockLog;

/**
 * @author maxu
 * @date 2019/7/20
 */
public interface StockLogMapper {
    StockLog selectById(Integer stockLogId);

    void updateById(StockLog stockLog);

}
