package com.sxpcwlkj.mq.entity;

import lombok.Data;

import java.util.*;

/**
 * 队列状态信息类
 * 用于返回RabbitMQ各队列的实时状态信息
 */
@Data
public class QueueStatus {

    // ==================== 基础状态信息 ====================

    /**
     * 主队列状态
     * 可能值：空闲、有消息、检查失败
     */
    private String mainQueueStatus;

    /**
     * 死信队列状态
     * 可能值：空闲、有消息、检查失败
     */
    private String deadLetterQueueStatus;

    /**
     * 延时队列状态
     * 可能值：空闲、有消息、检查失败
     */
    private String delayQueueStatus;

    /**
     * 重试队列状态
     * 可能值：空闲、有消息、检查失败
     */
    private String retryQueueStatus;

    // ==================== 消息数量统计 ====================

    /**
     * 主队列消息数量估算
     */
    private long mainQueueCount;

    /**
     * 死信队列消息数量估算
     */
    private long deadLetterQueueCount;

    /**
     * 延时队列消息数量估算
     */
    private long delayQueueCount;

    /**
     * 重试队列消息数量估算
     */
    private long retryQueueCount;

    /**
     * 总消息数量估算
     */
    private long totalMessages;

    // ==================== 服务状态信息 ====================

    /**
     * 服务状态
     * 可能值：正常、异常
     */
    private String serviceStatus;

    /**
     * 当前使用的消息处理器类型
     */
    private String currentHandler;

    /**
     * 已注册的处理器列表
     */
    private List<String> registeredHandlers;

    /**
     * 状态检查时间
     */
    private Date checkTime;

    /**
     * 错误信息（当serviceStatus为异常时）
     */
    private String errorMessage;

    // ==================== 性能指标 ====================

    /**
     * 最近1小时处理消息数量
     */
    private long messagesProcessedLastHour;

    /**
     * 最近1小时失败消息数量
     */
    private long messagesFailedLastHour;

    /**
     * 处理成功率（百分比）
     */
    private double successRate;

    // ==================== 构造方法 ====================

    public QueueStatus() {
        this.checkTime = new Date();
    }

    // ==================== 业务方法 ====================

    /**
     * 获取队列状态摘要信息
     */
    public String getSummary() {
        return String.format(
            "队列状态摘要 [%s]\n" +
                "📊 消息总数: %d\n" +
                "🟢 正常队列: %s\n" +
                "🔴 死信队列: %s (%d条)\n" +
                "⏰ 延时队列: %s (%d条)\n" +
                "🔄 重试队列: %s (%d条)\n" +
                "🤖 当前处理器: %s\n" +
                "📈 处理成功率: %.2f%%",
            checkTime,
            totalMessages,
            mainQueueStatus,
            deadLetterQueueStatus, deadLetterQueueCount,
            delayQueueStatus, delayQueueCount,
            retryQueueStatus, retryQueueCount,
            currentHandler,
            successRate
        );
    }
    
    /**
     * 获取详细的队列状态报告
     */
    public String getDetailedReport() {
        return String.format(
            "=== MQ队列状态详细报告 ===\n" +
            "检查时间: %s\n" +
            "服务状态: %s\n" +
            "告警级别: %s %s\n" +
            "\n--- 队列状态 ---\n" +
            "主队列: %s (%d条消息)\n" +
            "死信队列: %s (%d条消息)\n" +
            "延时队列: %s (%d条消息)\n" +
            "重试队列: %s (%d条消息)\n" +
            "\n--- 处理器信息 ---\n" +
            "当前处理器: %s\n" +
            "已注册处理器: %s\n" +
            "\n--- 性能指标 ---\n" +
            "消息总数: %d\n" +
            "最近1小时处理: %d条\n" +
            "最近1小时失败: %d条\n" +
            "处理成功率: %.2f%%\n" +
            "========================",
            checkTime,
            serviceStatus,
            getAlertLevel(), getStatusColor(),
            mainQueueStatus, mainQueueCount,
            deadLetterQueueStatus, deadLetterQueueCount,
            delayQueueStatus, delayQueueCount,
            retryQueueStatus, retryQueueCount,
            currentHandler,
            registeredHandlers != null ? registeredHandlers.toString() : "[]",
            totalMessages,
            messagesProcessedLastHour,
            messagesFailedLastHour,
            successRate
        );
    }

    /**
     * 检查是否有异常队列
     */
    public boolean hasIssue() {
        return "有消息".equals(deadLetterQueueStatus) && deadLetterQueueCount > 0;
    }

    /**
     * 获取告警级别
     * @return NORMAL-正常, WARNING-警告, CRITICAL-严重
     */
    public String getAlertLevel() {
        if (!"正常".equals(serviceStatus)) {
            return "CRITICAL";
        }

        if (deadLetterQueueCount > 10) {
            return "CRITICAL";
        } else if (deadLetterQueueCount > 5) {
            return "WARNING";
        } else if (successRate < 95.0) {
            return "WARNING";
        } else {
            return "NORMAL";
        }
    }

    /**
     * 获取状态颜色标识
     */
    public String getStatusColor() {
        switch (getAlertLevel()) {
            case "CRITICAL": return "🔴";
            case "WARNING": return "🟡";
            case "NORMAL": return "🟢";
            default: return "⚪";
        }
    }

    /**
     * 转换为简化的状态信息（用于监控仪表盘）
     */
    public Map<String, Object> toSimpleMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", checkTime.getTime());
        map.put("totalMessages", totalMessages);
        map.put("deadLetterCount", deadLetterQueueCount);
        map.put("successRate", successRate);
        map.put("alertLevel", getAlertLevel());
        map.put("statusColor", getStatusColor());
        map.put("currentHandler", currentHandler);
        return map;
    }

    /**
     * 转换为详细的JSON格式（用于API返回）
     */
    public Map<String, Object> toDetailMap() {
        Map<String, Object> map = new HashMap<>();

        // 基础信息
        map.put("checkTime", checkTime);
        map.put("serviceStatus", serviceStatus);
        map.put("alertLevel", getAlertLevel());
        map.put("statusColor", getStatusColor());

        // 队列状态
        Map<String, Object> queues = new HashMap<>();
        queues.put("main", createQueueInfo("主队列", mainQueueStatus, mainQueueCount));
        queues.put("deadLetter", createQueueInfo("死信队列", deadLetterQueueStatus, deadLetterQueueCount));
        queues.put("delay", createQueueInfo("延时队列", delayQueueStatus, delayQueueCount));
        queues.put("retry", createQueueInfo("重试队列", retryQueueStatus, retryQueueCount));
        map.put("queues", queues);

        // 统计信息
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMessages", totalMessages);
        stats.put("messagesProcessedLastHour", messagesProcessedLastHour);
        stats.put("messagesFailedLastHour", messagesFailedLastHour);
        stats.put("successRate", successRate);
        map.put("statistics", stats);

        // 处理器信息
        Map<String, Object> handlerInfo = new HashMap<>();
        handlerInfo.put("current", currentHandler);
        handlerInfo.put("registered", registeredHandlers);
        map.put("handlers", handlerInfo);

        // 错误信息
        if (errorMessage != null) {
            map.put("error", errorMessage);
        }

        return map;
    }

    private Map<String, Object> createQueueInfo(String name, String status, long count) {
        Map<String, Object> info = new HashMap<>();
        info.put("name", name);
        info.put("status", status);
        info.put("messageCount", count);
        info.put("hasMessages", count > 0);
        return info;
    }

    // ==================== Builder模式（可选） ====================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final QueueStatus status = new QueueStatus();

        public Builder mainQueue(String status, long count) {
            this.status.setMainQueueStatus(status);
            this.status.setMainQueueCount(count);
            return this;
        }

        public Builder deadLetterQueue(String status, long count) {
            this.status.setDeadLetterQueueStatus(status);
            this.status.setDeadLetterQueueCount(count);
            return this;
        }

        public Builder delayQueue(String status, long count) {
            this.status.setDelayQueueStatus(status);
            this.status.setDelayQueueCount(count);
            return this;
        }

        public Builder retryQueue(String status, long count) {
            this.status.setRetryQueueStatus(status);
            this.status.setRetryQueueCount(count);
            return this;
        }

        public Builder serviceStatus(String serviceStatus) {
            this.status.setServiceStatus(serviceStatus);
            return this;
        }

        public Builder currentHandler(String handler) {
            this.status.setCurrentHandler(handler);
            return this;
        }

        public Builder registeredHandlers(List<String> handlers) {
            this.status.setRegisteredHandlers(handlers);
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.status.setErrorMessage(errorMessage);
            return this;
        }

        public Builder statistics(long processed, long failed) {
            this.status.setMessagesProcessedLastHour(processed);
            this.status.setMessagesFailedLastHour(failed);
            if (processed + failed > 0) {
                double rate = (double) processed / (processed + failed) * 100;
                this.status.setSuccessRate(Math.round(rate * 100.0) / 100.0);
            } else {
                this.status.setSuccessRate(100.0);
            }
            return this;
        }

        public QueueStatus build() {
            // 计算总数
            long total = status.getMainQueueCount() + status.getDeadLetterQueueCount() +
                status.getDelayQueueCount() + status.getRetryQueueCount();
            status.setTotalMessages(total);

            // 更新时间
            status.setCheckTime(new Date());

            return status;
        }
    }

    // ==================== 示例用法 ====================

    /**
     * 创建示例状态对象（用于测试）
     */
    public static QueueStatus createExample() {
        return QueueStatus.builder()
            .mainQueue("空闲", 0)
            .deadLetterQueue("有消息", 3)
            .delayQueue("空闲", 0)
            .retryQueue("有消息", 2)
            .serviceStatus("正常")
            .currentHandler("distribution")
            .registeredHandlers(Arrays.asList("default", "distribution", "order"))
            .statistics(150, 5)
            .build();
    }
}
