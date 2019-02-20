package com.lib_logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.lib_logger.Logger.ASSERT;
import static com.lib_logger.Logger.DEBUG;
import static com.lib_logger.Logger.ERROR;
import static com.lib_logger.Logger.INFO;
import static com.lib_logger.Logger.VERBOSE;
import static com.lib_logger.Logger.WARN;

public class LoggerPrinter implements Printer {
    private static final int JSON_INDENT = 2;

    private final ThreadLocal<String> localTag = new ThreadLocal<>();

    //    private final List<LogAdapter> logAdapters = new ArrayList<>();
    private final Map<String, LogAdapter> logAdapterMap = new HashMap<>();

    @Override
    public Printer t(@Nullable String tag) {
        if (tag != null) {
            localTag.set(tag);
        }
        return this;
    }

    @Override
    public void d(@NonNull String message, @Nullable Object... args) {
        log(DEBUG, null, message, args);
    }

    @Override
    public void d(@Nullable Object object) {
        log(DEBUG, null, Utils.toString(object));
    }

    @Override
    public void e(@NonNull String message, @Nullable Object... args) {
        e(null, message, args);
    }

    @Override
    public void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        log(ERROR, throwable, message, args);
    }

    @Override
    public void w(@NonNull String message, @Nullable Object... args) {
        log(WARN, null, message, args);
    }

    @Override
    public void i(@NonNull String message, @Nullable Object... args) {
        log(INFO, null, message, args);
    }

    @Override
    public void v(@NonNull String message, @Nullable Object... args) {
        log(VERBOSE, null, message, args);
    }

    @Override
    public void wtf(@NonNull String message, @Nullable Object... args) {
        log(ASSERT, null, message, args);
    }

    @Override
    public void json(@Nullable String json) {
        if (Utils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message);
                return;
            }
            e("Invalid Json");
        } catch (JSONException e) {
            e("Invalid Json");
        }
    }

    @Override
    public void xml(@Nullable String xml) {
        if (Utils.isEmpty(xml)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e("Invalid xml");
        }
    }

    @Override
    public void clearLogAdapters() {
        logAdapterMap.clear();
//        logAdapters.clear();
    }

    @Override
    public void removeAdapter(@NonNull String key) {
        if (Utils.isEmpty(key)) {
            return;
        }
        logAdapterMap.remove(key);
    }

    @Override
    public void addAdapter(@NonNull LogAdapter adapter) {
        Utils.checkNotNull(adapter);
        logAdapterMap.put(adapter.getClass().getSimpleName(),
                adapter);
//        logAdapters.add(Utils.checkNotNull(adapter));
    }

    private String getTag() {
        String tag = localTag.get();
        if (tag != null) {
            localTag.remove();
            return tag;
        }
        return null;
    }

    /**
     * 创建消息
     *
     * @param message
     * @param args
     * @return
     */
    private String createMessage(@NonNull String message, @Nullable Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }

    @Override
    public synchronized void log(int priority,
                                 @Nullable String tag,
                                 @Nullable String message,
                                 @Nullable Throwable throwable) {
        if (throwable != null && message != null) {
            message += " : " + Utils.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = Utils.getStackTraceString(throwable);
        }
        if (Utils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        Iterator<Map.Entry<String, LogAdapter>> it = logAdapterMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, LogAdapter> next = it.next();
            LogAdapter adapter = next.getValue();
            if (adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, message);
            }
        }

//        for (LogAdapter adapter : logAdapters) {
//            if (adapter.isLoggable(priority, tag)) {
//                adapter.log(priority, tag, message);
//            }
//        }
    }

    private synchronized void log(int priority,
                                  @Nullable Throwable throwable,
                                  @NonNull String msg,
                                  @Nullable Object... args) {
        Utils.checkNotNull(msg);

        String tag = getTag();
        String message = createMessage(msg, args);
        log(priority, tag, message, throwable);
    }
}
