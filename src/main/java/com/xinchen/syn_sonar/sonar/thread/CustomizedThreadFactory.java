/**
 * Copyright(c): 2017 com.mjduan All rights reserved.
 * 项目名：java-learn
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Executors.DefaultThreadFactory的代码
 *
 * @author mjduan@yahoo.com mjduan 2018-04-27 01:27
 * @version 1.0
 * @since 1.0
 */
public class CustomizedThreadFactory implements ThreadFactory {
    private static final String DEFAULT_POOL_PREFIX_NAME = "sonarsyn";
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    private CustomizedThreadFactory(String poolPrefixName) {
        SecurityManager var1 = System.getSecurityManager();
        this.group = var1 != null ? var1.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = poolPrefixName + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public static ThreadFactory customizedThreadFactory() {
        return new CustomizedThreadFactory(DEFAULT_POOL_PREFIX_NAME);
    }

    public static ThreadFactory customizedThreadFactory(String poolPrefixName) {
        return new CustomizedThreadFactory(poolPrefixName);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(this.group, runnable, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        if (thread.getPriority() != 5) {
            thread.setPriority(5);
        }
        return thread;
    }
}
