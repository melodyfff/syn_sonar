/**
 * Copyright(c): 2017 com.mjduan All rights reserved.
 * 项目名：java-learn
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mjduan@yahoo.com mjduan 2018-04-27 01:35
 * @version 1.0
 * @since 1.0
 */
public class CustomizedThreadPoolExecutor {

    public static ThreadPoolExecutor customizedThreadPoolExecutor(int coreSize, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(coreSize, coreSize * 2 > Integer.MAX_VALUE ? coreSize : coreSize * 2, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }

    public static ThreadPoolExecutor customizedThreadPoolExecutor(int coreSize, int maxSize, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(coreSize, maxSize, 200L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }
}
