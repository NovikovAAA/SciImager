package com.visualipcv.core;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class GraphExecutionData {
    private static final ThreadLocal<HashMap<NodeSlot, Object>> calculatedData = ThreadLocal.withInitial(new Supplier<HashMap<NodeSlot, Object>>() {
        @Override
        public HashMap<NodeSlot, Object> get() {
            return new HashMap<>();
        }
    });

    private static final HashMap<Object, HashMap<String, Object>> metadata = new HashMap<>();

    public static void store(NodeSlot slot, Object value) {
        calculatedData.get().put(slot, value);
    }

    public static Object load(NodeSlot slot) {
        return calculatedData.get().get(slot);
    }

    public static void clear(Graph graph, boolean cleanProperties) {
        for(NodeSlot slot : calculatedData.get().keySet()) {
            if(!cleanProperties && slot.getNode().findProcessor() != null) {
                if(slot.getNode().findProcessor().isProperty()) {
                    continue;
                }
            }

            if(slot.getNode().getGraph() == graph) {
                calculatedData.get().put(slot, null);
            }
        }
    }

    public static void storeMeta(Object owner, String key, Object data) {
        HashMap<String, Object> props = metadata.computeIfAbsent(owner, (Object obj) -> new HashMap<>());
        props.put(key, data);
    }

    public static Object loadMeta(Object owner, String key) {
        HashMap<String, Object> props = metadata.computeIfAbsent(owner, (Object obj) -> new HashMap<>());
        return props.get(key);
    }
}
