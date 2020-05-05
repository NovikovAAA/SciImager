package com.visualipcv.core;

import java.util.HashMap;

public class GraphExecutionContext {
    private HashMap<Object, DataBundle> data = new HashMap<>();

    public Object load(Object object, String key) {
        DataBundle state = data.computeIfAbsent(object, (Object obj) -> new DataBundle());
        return state.read(key);
    }

    public void store(Object object, String key, Object value) {
        DataBundle state = data.computeIfAbsent(object, (Object obj) -> new DataBundle());
        state.write(key, value);
    }

    public DataBundle load(Object object) {
        return data.computeIfAbsent(object, (Object obj) -> new DataBundle());
    }
}
