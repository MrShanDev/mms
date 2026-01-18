package com.sxpcwlkj.demo.controller;


import com.sxpcwlkj.common.utils.R;
import com.sxpcwlkj.demo.mq.OrderTimeoutDemo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 订单超时取消功能测试接口
 * 提供 RESTful API 测试订单超时延时取消功能
 */
@RestController
@RequestMapping("/demo/order-timeout")
@Slf4j
@RequiredArgsConstructor
public class OrderTimeoutController {

    private final OrderTimeoutDemo orderTimeoutDemo;

    /**
     * 测试1：创建普通订单（30分钟超时）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/create-normal
     * {
     *   "orderId": "ORDER_001",
     *   "buyerId": "USER_001"
     * }
     */
    @PostMapping("/create-normal")
    public R<String> createNormalOrder(@RequestParam String orderId,
                                       @RequestParam String buyerId) {
        try {
            orderTimeoutDemo.createOrderWithTimeout(orderId, buyerId);
            return R.success("✅ 普通订单创建成功，30分钟后自动取消", orderId);
        } catch (Exception e) {
            log.error("创建普通订单失败", e);
            return R.fail("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 测试2：快速创建订单（使用简化方法）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/create-quick?orderId=ORDER_002
     */
    @PostMapping("/create-quick")
    public R<String> createQuickOrder(@RequestParam String orderId) {
        try {
            orderTimeoutDemo.quickCreateOrderTimeout(orderId);
            return R.success("✅ 订单创建成功，15分钟后自动取消", orderId);
        } catch (Exception e) {
            log.error("快速创建订单失败", e);
            return R.fail("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 测试3：创建秒杀订单（5分钟超时）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/create-seckill
     * {
     *   "orderId": "SECKILL_001",
     *   "buyerId": "USER_001"
     * }
     */
    @PostMapping("/create-seckill")
    public R<String> createSeckillOrder(@RequestParam String orderId,
                                        @RequestParam String buyerId) {
        try {
            orderTimeoutDemo.createSeckillOrder(orderId, buyerId);
            return R.success("✅ 秒杀订单创建成功，5分钟后自动取消", orderId);
        } catch (Exception e) {
            log.error("创建秒杀订单失败", e);
            return R.fail("创建秒杀订单失败: " + e.getMessage());
        }
    }

    /**
     * 测试4：创建预售订单（2小时超时）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/create-presale
     * {
     *   "orderId": "PRESALE_001",
     *   "buyerId": "USER_001"
     * }
     */
    @PostMapping("/create-presale")
    public R<String> createPresaleOrder(@RequestParam String orderId,
                                        @RequestParam String buyerId) {
        try {
            orderTimeoutDemo.createPresaleOrder(orderId, buyerId);
            return R.success("✅ 预售订单创建成功，2小时后自动取消", orderId);
        } catch (Exception e) {
            log.error("创建预售订单失败", e);
            return R.fail("创建预售订单失败: " + e.getMessage());
        }
    }

    /**
     * 测试5：订单支付成功（取消超时任务）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/payment-success
     * {
     *   "orderId": "ORDER_001",
     *   "messageId": "TIMEOUT_1234567890_ABCD1234"
     * }
     */
    @PostMapping("/payment-success")
    public R<String> onPaymentSuccess(@RequestParam String orderId,
                                      @RequestParam String messageId) {
        try {
            orderTimeoutDemo.onPaymentSuccess(orderId, messageId);
            return R.success("✅ 订单支付成功，超时任务已取消", orderId);
        } catch (Exception e) {
            log.error("处理支付成功失败", e);
            return R.fail("处理失败: " + e.getMessage());
        }
    }

    /**
     * 测试6：批量创建订单
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/batch-create
     * {
     *   "orderIds": ["ORDER_001", "ORDER_002", "ORDER_003"]
     * }
     */
    @PostMapping("/batch-create")
    public R<String> batchCreateOrders(@RequestBody String[] orderIds) {
        try {
            orderTimeoutDemo.batchCreateOrderTimeout(orderIds);
            return R.success("✅ 批量创建订单成功，共 " + orderIds.length + " 个订单",
                String.valueOf(orderIds.length));
        } catch (Exception e) {
            log.error("批量创建订单失败", e);
            return R.fail("批量创建失败: " + e.getMessage());
        }
    }

    /**
     * 测试7：查看队列状态
     *
     * 调用示例：
     * GET http://localhost:8080/demo/order-timeout/queue-status
     */
    @GetMapping("/queue-status")
    public R<String> getQueueStatus() {
        try {
            orderTimeoutDemo.checkQueueStatus();
            return R.success("队列状态检查完成，请查看日志");
        } catch (Exception e) {
            log.error("检查队列状态失败", e);
            return R.fail("检查失败: " + e.getMessage());
        }
    }

    /**
     * 测试8：演示不同场景的延时时间
     *
     * 调用示例：
     * GET http://localhost:8080/demo/order-timeout/demo-delay-scenarios
     */
    @GetMapping("/demo-delay-scenarios")
    public R<String> demoDelayScenarios() {
        try {
            orderTimeoutDemo.demonstrateDelayTimeScenarios();
            return R.success("延时场景演示完成，请查看日志");
        } catch (Exception e) {
            log.error("演示失败", e);
            return R.fail("演示失败: " + e.getMessage());
        }
    }

    /**
     * 测试9：完整的订单创建流程（推荐）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/create-complete
     * {
     *   "buyerId": "USER_001",
     *   "amount": 99.99
     * }
     */
    @PostMapping("/create-complete")
    public R<String> createCompleteOrder(@RequestParam String buyerId,
                                         @RequestParam BigDecimal amount) {
        try {
            String orderId = orderTimeoutDemo.createOrderWithCompleteFlow(buyerId, amount);
            if (orderId != null) {
                return R.success("✅ 订单创建成功（完整流程）", orderId);
            } else {
                return R.fail("订单创建失败");
            }
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return R.fail("创建订单失败: " + e.getMessage());
        }
    }

    /**
     * 测试10：订单支付成功（完整流程）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/order-paid?orderId=ORDER_001
     */
    @PostMapping("/order-paid")
    public R<String> orderPaid(@RequestParam String orderId) {
        try {
            boolean success = orderTimeoutDemo.onOrderPaid(orderId);
            if (success) {
                return R.success("✅ 订单支付处理成功", orderId);
            } else {
                return R.fail("订单支付处理失败");
            }
        } catch (Exception e) {
            log.error("处理订单支付失败", e);
            return R.fail("处理失败: " + e.getMessage());
        }
    }

    /**
     * 测试11：创建测试订单（1分钟超时，用于快速测试）
     *
     * 调用示例：
     * POST http://localhost:8080/demo/order-timeout/create-test?orderId=TEST_001&buyerId=USER_001
     */
    @PostMapping("/create-test")
    public R<String> createTestOrder(@RequestParam String orderId,
                                     @RequestParam String buyerId) {
        try {
            log.info("创建测试订单（1分钟超时）: orderId={}", orderId);

            // 使用快速方法创建1分钟超时的订单
            orderTimeoutDemo.quickCreateOrderTimeout(orderId);

            return R.success("✅ 测试订单创建成功，1分钟后自动取消（请等待观察效果）", orderId);
        } catch (Exception e) {
            log.error("创建测试订单失败", e);
            return R.fail("创建测试订单失败: " + e.getMessage());
        }
    }
}
