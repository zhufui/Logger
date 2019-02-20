package com.example.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataSupport {
    public static Map getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        return map;
    }

    public static Set<String> getSet() {
        Set<String> s = new HashSet<>();
        s.add("s1");
        s.add("s2");
        s.add("s3");
        return s;
    }

    public static List<String> getList() {
        List<String> list = new ArrayList<>();
        list.add("list1");
        list.add("list2");
        list.add("list3");
        return list;
    }

    public static String[] getArray() {
        String[] s = new String[3];
        s[0] = "array1";
        s[1] = "array2";
        s[2] = "array3";
        return s;
    }

    public static String getJson() {
        String json = "{\"status\": 200,\"data\": {\"curtime\": \"1469025820\",\"issmarter\": \"0\",}}";
        return json;
    }

    public static String getXml() {
        String xml = "";
        return xml;
    }

}
