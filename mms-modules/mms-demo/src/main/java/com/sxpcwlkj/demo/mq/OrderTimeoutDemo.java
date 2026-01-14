package com.sxpcwlkj.demo.mq;

import com.sxpcwlkj.mq.entity.OrderTimeoutMessage;
import com.sxpcwlkj.mq.service.OrderTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单超时取消功能演示
 * 展示如何使用订单超时延时取消功能
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderTimeoutDemo {

    private final OrderTimeoutService orderTimeoutService;

    /**
     * 示例1：创建订单时发送超时取消消息（30分钟后自动取消）
     */
    public void createOrderWithTimeout(String orderId, String buyerId) {
        log.info("创建订单并设置30分钟超时: orderId={}", orderId);
        
        // 构建订单超时消息
        OrderTimeoutMessage message = new OrderTimeoutMessage();
        message.setOrderId(orderId);
        message.setOrderNo("ORDER_" + System.currentTimeMillis());
        message.setOrderStatus("PENDING"); // 待支付
        message.setBuyerId(buyerId);
        message.setBuyerName("张三");
        message.setOrderAmount(new BigDecimal("99.99"));
        message.setCreateTime(LocalDateTime.now());
        message.setExpireTime(LocalDateTime.now().plusMinutes(30));
        message.setBusinessType("ORDER_TIMEOUT");
        message.setCancelReason("订单超时未支付");
        
        // 发送超时消息（30分钟 = 1800000毫秒）
        boolean success = orderTimeoutService.sendOrderTimeoutMessage(message, 1800000L);
        
        if (success) {
            log.info("✅ 订单超时消息发送成功: orderId={}, messageId={}", 
                orderId, message.getMessageId());
        } else {
            log.error("❌ 订单超时消息发送失败: orderId={}", orderId);
        }
    }

    /**
     * 示例2：快速创建订单超时任务（使用简化方法）
     */
    public void quickCreateOrderTimeout(String orderId) {
        log.info("快速创建订单超时任务: orderId={}", orderId);
        
        // 15分钟后自动取消
        long delayTime = 15 * 60 * 1000L; // 15分钟
        
        boolean success = orderTimeoutService.sendQuickOrderTimeout(
            orderId, 
            "PENDING", 
            delayTime
        );
        
        if (success) {
            log.info("✅ 订单超时任务创建成功: orderId={}, 延时={}分钟", 
                orderId, delayTime / 60000);
        }
    }

    /**
     * 示例3：秒杀订单（5分钟超时）
     */
    public void createSeckillOrder(String orderId, String buyerId) {
        log.info("创建秒杀订单，5分钟超时: orderId={}", orderId);
        
        OrderTimeoutMessage message = OrderTimeoutMessage.buildOrderTimeout(
            orderId, 
            "PENDING", 
            5 * 60 * 1000L // 5分钟
        );
        
        message.setBuyerId(buyerId);
        message.setOrderAmount(new BigDecimal("199.99"));
        message.setBusinessType("SECKILL_TIMEOUT");
        message.setCancelReason("秒杀订单超时未支付");
        
        orderTimeoutService.sendOrderTimeoutMessage(message, 5 * 60 * 1000L);
        
        log.info("✅ 秒杀订单超时任务已创建: orderId={}", orderId);
    }

    /**
     * 示例4：预售订单（2小时超时）
     */
    public void createPresaleOrder(String orderId, String buyerId) {
        log.info("创建预售订单，2小时超时: orderId={}", orderId);
        
        OrderTimeoutMessage message = new OrderTimeoutMessage();
        message.setOrderId(orderId);
        message.setOrderStatus("PENDING");
        message.setBuyerId(buyerId);
        message.setOrderAmount(new BigDecimal("999.99"));
        message.setBusinessType("PRESALE_TIMEOUT");
        message.setCancelReason("预售订单超时未支付");
        message.setCreateTime(LocalDateTime.now());
        message.setExpireTime(LocalDateTime.now().plusHours(2));
        
        // 2小时 = 7200000毫秒
        boolean success = orderTimeoutService.sendOrderTimeoutMessage(message, 7200000L);
        
        if (success) {
            log.info("✅ 预售订单超时任务已创建: orderId={}, 2小时后自动取消", orderId);
        }
    }

    /**
     * 示例5：支付成功后取消超时任务
     */
    public void onPaymentSuccess(String orderId, String messageId) {
        log.info("订单支付成功，取消超时任务: orderId={}, messageId={}", orderId, messageId);
        
        boolean cancelled = orderTimeoutService.cancelOrderTimeout(messageId);
        
        if (cancelled) {
            log.info("✅ 订单超时任务已取消: orderId={}, messageId={}", orderId, messageId);
        } else {
            log.warn("⚠️ 取消订单超时任务失败（可能已执行或不存在）: orderId={}", orderId);
        }
    }

    /**
     * 示例6：不同场景的延时时间配置
     */
    public void demonstrateDelayTimeScenarios() {
        log.info("演示不同业务场景的延时时间配置:");
        
        // 普通订单：30分钟
        long normalOrderDelay = 30 * 60 * 1000L;
        log.info("  - 普通订单: {}ms ({}分钟)", normalOrderDelay, normalOrderDelay / 60000);
        
        // 秒杀订单：5分钟
        long seckillOrderDelay = 5 * 60 * 1000L;
        log.info("  - 秒杀订单: {}ms ({}分钟)", seckillOrderDelay, seckillOrderDelay / 60000);
        
        // 预售订单：1小时
        long presaleOrderDelay = 60 * 60 * 1000L;
        log.info("  - 预售订单: {}ms ({}小时)", presaleOrderDelay, presaleOrderDelay / 3600000);
        
        // 团购订单：2小时
        long groupOrderDelay = 2 * 60 * 60 * 1000L;
        log.info("  - 团购订单: {}ms ({}小时)", groupOrderDelay, groupOrderDelay / 3600000);
        
        // 测试订单：1分钟（用于测试）
        long testOrderDelay = 60 * 1000L;
        log.info("  - 测试订单: {}ms ({}分钟)", testOrderDelay, testOrderDelay / 60000);
    }

    /**
     * 示例7：批量创建订单超时任务
     */
    public void batchCreateOrderTimeout(String[] orderIds) {
        log.info("批量创建订单超时任务，订单数量: {}", orderIds.length);
        
        int successCount = 0;
        int failCount = 0;
        
        for (String orderId : orderIds) {
            boolean success = orderTimeoutService.sendQuickOrderTimeout(
                orderId, 
                "PENDING", 
                30 * 60 * 1000L // 30分钟
            );
            
            if (success) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        log.info("批量创建完成: 成功={}, 失败={}, 总计={}", 
            successCount, failCount, orderIds.length);
    }

    /**
     * 示例8：检查队列状态
     */
    public void checkQueueStatus() {
        String status = orderTimeoutService.getOrderTimeoutQueueStatus();
        log.info("订单超时队列状态: {}", status);
    }

    /**
     * 示例9：完整的订单创建流程（推荐使用）
     */
    public String createOrderWithCompleteFlow(String buyerId, BigDecimal amount) {
        try {
            // 1. 生成订单ID
            String orderId = "ORDER_" + System.currentTimeMillis();
            log.info("开始创建订单: orderId={}, buyerId={}, amount={}", 
                orderId, buyerId, amount);
            
            // 2. 创建订单记录（保存到数据库）
            // Order order = new Order();
            // order.setOrderId(orderId);
            // order.setBuyerId(buyerId);
            // order.setAmount(amount);
            // order.setStatus("PENDING");
            // order.setCreateTime(LocalDateTime.now());
            // orderMapper.insert(order);
            
            // 3. 构建超时消息
            OrderTimeoutMessage timeoutMessage = new OrderTimeoutMessage();
            timeoutMessage.setOrderId(orderId);
            timeoutMessage.setOrderNo(orderId);
            timeoutMessage.setOrderStatus("PENDING");
            timeoutMessage.setBuyerId(buyerId);
            timeoutMessage.setOrderAmount(amount);
            timeoutMessage.setCreateTime(LocalDateTime.now());
            timeoutMessage.setExpireTime(LocalDateTime.now().plusMinutes(30));
            timeoutMessage.setBusinessType("ORDER_TIMEOUT");
            timeoutMessage.setCancelReason("订单超时未支付");
            
            // 4. 发送超时取消消息（30分钟）
            boolean timeoutSuccess = orderTimeoutService.sendOrderTimeoutMessage(
                timeoutMessage, 
                30 * 60 * 1000L
            );
            
            if (!timeoutSuccess) {
                log.error("发送订单超时消息失败: orderId={}", orderId);
                // 可以考虑回滚订单创建或记录告警
            }
            
            // 5. 保存消息ID（用于支付成功后取消）
            String messageId = timeoutMessage.getMessageId();
            // Redis存储：order:timeout:messageId:{orderId} -> messageId
            // redisUtil.setCacheObject("order:timeout:messageId:" + orderId, messageId, Duration.ofHours(2));
            
            log.info("✅ 订单创建成功: orderId={}, messageId={}", orderId, messageId);
            return orderId;
            
        } catch (Exception e) {
            log.error("创建订单失败: buyerId={}, amount={}", buyerId, amount, e);
            return null;
        }
    }

    /**
     * 示例10：订单支付成功的完整流程
     */
    public boolean onOrderPaid(String orderId) {
        try {
            log.info("处理订单支付成功: orderId={}", orderId);
            
            // 1. 更新订单状态
            // orderMapper.updateStatus(orderId, "PAID");
            
            // 2. 从Redis获取消息ID
            // String messageId = redisUtil.getCacheObject("order:timeout:messageId:" + orderId);
            String messageId = "模拟的messageId"; // 实际应从Redis获取
            
            // 3. 取消超时任务
            if (messageId != null) {
                boolean cancelled = orderTimeoutService.cancelOrderTimeout(messageId);
                if (cancelled) {
                    log.info("✅ 订单超时任务已取消: orderId={}", orderId);
                } else {
                    log.warn("⚠️ 取消超时任务失败（可能已执行）: orderId={}", orderId);
                }
            }
            
            // 4. 其他业务逻辑（发货、积分等）
            log.info("✅ 订单支付处理完成: orderId={}", orderId);
            return true;
            
        } catch (Exception e) {
            log.error("处理订单支付失败: orderId={}", orderId, e);
            return false;
        }
    }
}
