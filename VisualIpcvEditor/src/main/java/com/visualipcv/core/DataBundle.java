package com.visualipcv.core;

import javafx.beans.property.Property;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataBundle implements Iterable<Object> {
    public Map<String, Object> values = new HashMap<String, Object>();

    public DataBundle() {

    }

    public <T> void write(String name, T value) {
        values.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T read(String name) {
        return (T)values.get(name);
    }

    @Override
    public Iterator<Object> iterator() {
        return values.values().iterator();
    }

    public void clear() {
        values.clear();
    }

    public void clear(String key) {
        values.remove(key);
    }
}
