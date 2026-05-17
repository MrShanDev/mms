package com.sxpcwlkj.system.monitor;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 慢 SQL 记录器（内存环形队列）
 */
@Component
public class SlowSqlRecorder {

    private final Deque<Entry> deque = new ArrayDeque<>();

    /** 默认最多保留 200 条 */
    private volatile int maxSize = 200;

    /** 默认阈值 500ms */
    private volatile long thresholdMs = 500;

    public void setMaxSize(int maxSize) {
        this.maxSize = Math.max(50, maxSize);
    }

    public void setThresholdMs(long thresholdMs) {
        this.thresholdMs = Math.max(1, thresholdMs);
    }

    public long getThresholdMs() {
        return thresholdMs;
    }

    public void record(String datasource, long elapsedMs, String sql) {
        if (elapsedMs < thresholdMs) return;
        Entry e = new Entry();
        e.setTimeMs(System.currentTimeMillis());
        e.setElapsedMs(elapsedMs);
        e.setDatasource(datasource);
        e.setSql(trimSql(sql));
        synchronized (deque) {
            deque.addFirst(e);
            while (deque.size() > maxSize) {
                deque.removeLast();
            }
        }
    }

    public List<Entry> latest(int limit) {
        int n = Math.max(0, Math.min(limit, maxSize));
        List<Entry> list = new ArrayList<>(n);
        synchronized (deque) {
            int i = 0;
            for (Entry e : deque) {
                list.add(e);
                i++;
                if (i >= n) break;
            }
        }
        return list;
    }

    private String trimSql(String sql) {
        if (sql == null) return "";
        String s = sql.trim();
        if (s.length() <= 800) return s;
        return s.substring(0, 800) + "...";
    }

    @Data
    public static class Entry {
        private Long timeMs;
        private Long elapsedMs;
        private String datasource;
        private String sql;
    }
}

