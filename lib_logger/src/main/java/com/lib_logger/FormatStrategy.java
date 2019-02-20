package com.lib_logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 格式化策略接口
 */
public interface FormatStrategy {
    void log(int priority, @Nullable String tag, @NonNull String message);
}
