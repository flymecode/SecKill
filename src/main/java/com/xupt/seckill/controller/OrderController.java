/**
 * @author: maxu1
 * @date: 2019/1/27 18:58
 */

package com.xupt.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.xupt.seckill.error.BusinessException;
import com.xupt.seckill.error.EmBusinessError;
import com.xupt.seckill.response.CommonReturnType;
import com.xupt.seckill.service.ItemService;
import com.xupt.seckill.service.OrderService;
import com.xupt.seckill.service.PromoService;
import com.xupt.seckill.service.model.OrderModel;
import com.xupt.seckill.service.model.UserModel;
import com.xupt.seckill.until.SysConstant;
import com.xupt.seckill.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.*;

/**
 * @author maxu
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PromoService promoService;

    @Autowired
    private RedisTemplate redisTemplate;

    private ExecutorService executorService;

    private RateLimiter orderCreateRateLimiter;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(20);
        orderCreateRateLimiter = RateLimiter.create(300);
    }

    @PostMapping("/generateToke")
    public CommonReturnType generateToke(@RequestParam(name = "itemId") Integer itemId, HttpServletRequest httpServletRequest,
                                        @RequestParam(name = "promoId") Integer promoId) throws BusinessException {

        String token = httpServletRequest.getParameterMap().get("token")[0];
        if (StringUtils.isNotEmpty(token)) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户没有登陆");
        }
        // 获取用户登陆的信息
        UserModel userModel = (UserModel)redisTemplate.opsForValue().get(token);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户没有登陆");
        }
        // 获取秒杀令牌
        String promoToken = promoService.generateSecondKillToken(promoId, itemId, userModel.getId());
        // 返回对应的结果
        if (promoToken == null) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "生成令牌不合法");
        }
        return CommonReturnType.create(promoToken);
    }
    @PostMapping("/createorder")
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
                                        @RequestParam(name = "amount") Integer amount,
                                        @RequestParam(name = "promoId") Integer promoId,
                                        @RequestParam(name = "promoToken") String promoToken,
                                         HttpSession session,
                                        HttpServletRequest httpServletRequest) throws BusinessException {

        // 设置令牌桶
        double acquire = orderCreateRateLimiter.acquire();
        if (acquire <= 0) {
            throw new BusinessException(EmBusinessError.RATE_LIMIT);
        }
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if (StringUtils.isNotEmpty(token)) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户没有登陆");
        }
        // 获取用户登陆的信息
        UserModel userModel = (UserModel)redisTemplate.opsForValue().get(token);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户没有登陆");
        }
        if (promoId != null) {
            String inRedisPromoToken =(String)redisTemplate.opsForValue().get("promo_token_" + promoId+"_userId_"+userModel.getId()+"_itemId_"+itemId);
            if (inRedisPromoToken == null) {
                // 跑
            }
            if (!StringUtils.equals(promoToken, inRedisPromoToken)) {
                // 抛异常
            }
        }

        // 队列泄洪
        // 同步调用线程池的submit方法
        Future<Object> future = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 加入库存流水的init状态
                Integer stockLogId = itemService.initStockLog(itemId, amount);

                // TODO 完成下单的事务型消息机制还没有完成
                // 获取用户的信息
                UserVO userVO = (UserVO) session.getAttribute(SysConstant.LOGIN_USER);
                OrderModel orderModel = orderService.createOrder(promoId, userVO.getId(), itemId, amount, stockLogId);
                return null;
            }
        });

        try {
            Object o = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return CommonReturnType.create(null);

    }


}
