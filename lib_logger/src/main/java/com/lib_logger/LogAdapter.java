package com.lib_logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 日志适配器接口
 */
public interface LogAdapter {
    boolean isLoggable(int priority, @Nullable String tag);

    void log(int priority, @Nullable String tag, @NonNull String message);
}
