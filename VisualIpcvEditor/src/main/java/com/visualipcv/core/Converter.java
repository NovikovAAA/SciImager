package com.visualipcv.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Converter {
    private static class ConverterPair {
        private DataType source;
        private DataType target;

        public ConverterPair(DataType source, DataType target) {
            this.source = source;
            this.target = target;
        }

        public DataType getSource() {
            return source;
        }

        public DataType getTarget() {
            return target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConverterPair that = (ConverterPair) o;
            return Objects.equals(source, that.source) &&
                    Objects.equals(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }
    }

    private static Map<ConverterPair, DataTypeConverter> converters = new HashMap<>();

    public static void register(DataType source, DataType target, DataTypeConverter converter) {
        converters.put(new ConverterPair(source, target), converter);
    }

    public static boolean isConvertible(DataType source, DataType target) {
        return converters.containsKey(new ConverterPair(source, target));
    }

    public static Object convert(DataType sourceType, DataType targetType, Object source) {
        return converters.get(new ConverterPair(sourceType, targetType)).convert(source);
    }

    public static void registerDefaultConverters() {
        register(DataTypes.INTEGER, DataTypes.DOUBLE, (Object source) -> ((Integer)source).doubleValue());
        register(DataTypes.DOUBLE, DataTypes.INTEGER, (Object source) -> ((Double)source).intValue());
        register(DataTypes.PATH, DataTypes.STRING, (Object source) -> source);
        register(DataTypes.STRING, DataTypes.PATH, (Object source) -> source);
    }
}
