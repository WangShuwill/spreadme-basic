package org.spreadme.commons.thread;

import java.util.concurrent.TimeUnit;

public abstract class ThreadUtils {

    public static void sleep(long timeout, TimeUnit timeunit) {
        try {
            timeunit.sleep(timeout);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
