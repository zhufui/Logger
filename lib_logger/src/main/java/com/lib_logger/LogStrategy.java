package com.lib_logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * log打印策略
 */
public interface LogStrategy {
    void log(int priority, @Nullable String tag, @NonNull String message);
}
