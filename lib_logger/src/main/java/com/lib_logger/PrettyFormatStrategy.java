package com.lib_logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 优雅地格式化输出日志
 */
public class PrettyFormatStrategy implements FormatStrategy {

    private static final int CHUNK_SIZE = 4000;
    private static final int MIN_STACK_OFFSET = 5;

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    private final int methodCount;
    private final int methodOffset;
    private final boolean showThreadInfo;
    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String tag;

    private PrettyFormatStrategy(@NonNull Builder builder) {
        Utils.checkNotNull(builder);

        methodCount = builder.methodCount;
        methodOffset = builder.methodOffset;
        showThreadInfo = builder.showThreadInfo;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        Utils.checkNotNull(message);

        String tag = formatTag(onceOnlyTag);

        logTopBorder(priority, tag);
        logHeaderContent(priority, tag, methodCount);

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            if (methodCount > 0) {
                logDivider(priority, tag);
            }
            logContent(priority, tag, message);
            logBottomBorder(priority, tag);
            return;
        }
        if (methodCount > 0) {
            logDivider(priority, tag);
        }
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(priority, tag, new String(bytes, i, count));
        }
        logBottomBorder(priority, tag);
    }

    private void logTopBorder(int logType, @Nullable String tag) {
        logChunk(logType, tag, TOP_BORDER);
    }

    /**
     * 打印日志的详细信息，获取线程的堆栈信息，
     * 然后打印出类名，线程名，方法名，所在行数等信息
     *
     * @param logType
     * @param tag
     * @param methodCount
     */
    @SuppressWarnings("StringBufferReplaceableByString")
    private void logHeaderContent(int logType, @Nullable String tag, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (showThreadInfo) {
            logChunk(logType, tag, HORIZONTAL_LINE + " Thread: " + Thread.currentThread().getName());
            logDivider(logType, tag);
        }
        String level = "";
        int stackOffset = getStackOffset(trace) + methodOffset;

        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(HORIZONTAL_LINE)
                    .append(' ')
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            logChunk(logType, tag, builder.toString());
        }
    }

    private void logBottomBorder(int logType, @Nullable String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    private void logDivider(int logType, @Nullable String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    private void logContent(int logType, @Nullable String tag, @NonNull String chunk) {
        Utils.checkNotNull(chunk);

        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            logChunk(logType, tag, HORIZONTAL_LINE + " " + line);
        }
    }

    private void logChunk(int priority, @Nullable String tag, @NonNull String chunk) {
        Utils.checkNotNull(chunk);

        logStrategy.log(priority, tag, chunk);
    }

    private String getSimpleClassName(@NonNull String name) {
        Utils.checkNotNull(name);

        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private int getStackOffset(@NonNull StackTraceElement[] trace) {
        Utils.checkNotNull(trace);

        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LoggerPrinter.class.getName()) && !name.equals(Logger.class.getName())) {
                return --i;
            }
        }
        return -1;
    }

    @Nullable
    private String formatTag(@Nullable String tag) {
        if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
            return this.tag + "-" + tag;
        }
        return this.tag;
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        int methodCount = 2;//方法数
        int methodOffset = 0;//方法偏移量
        boolean showThreadInfo = true;//是否显示线程信息
        @Nullable
        LogStrategy logStrategy;
        @Nullable
        String tag = "PRETTY_LOGGER";

        private Builder() {
        }

        @NonNull
        public Builder methodCount(int val) {
            methodCount = val;
            return this;
        }

        @NonNull
        public Builder methodOffset(int val) {
            methodOffset = val;
            return this;
        }

        @NonNull
        public Builder showThreadInfo(boolean val) {
            showThreadInfo = val;
            return this;
        }

        @NonNull
        public Builder logStrategy(@Nullable LogStrategy val) {
            logStrategy = val;
            return this;
        }

        @NonNull
        public Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @NonNull
        public PrettyFormatStrategy build() {
            if (logStrategy == null) {
                logStrategy = new LogcatLogStrategy();
            }
            return new PrettyFormatStrategy(this);
        }
    }
}
