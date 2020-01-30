package com.visualipcv;

import java.util.Objects;

public class ProcessorUID {
    private String name;
    private String module;

    public ProcessorUID(String name, String module) {
        this.name = name;
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessorUID that = (ProcessorUID) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(module, that.module);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, module);
    }

    @Override
    public String toString() {
        return "ProcessorUID{" +
                "name='" + name + '\'' +
                ", module='" + module + '\'' +
                '}';
    }
}
