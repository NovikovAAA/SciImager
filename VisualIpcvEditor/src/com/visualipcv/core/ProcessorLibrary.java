package com.visualipcv.core;

import com.visualipcv.procs.*;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

public class ProcessorLibrary {
    private static List<Processor> processors = new ArrayList<>();
    public static native List<ProcessorUID> getProcessorList();

    public static List<Processor> getProcessors() {
        return processors;
    }

    public static Processor findProcessor(String module, String name) {
        for(Processor processor : getProcessors()) {
            if(processor.getModule().equals(module) && processor.getName().equals(name)) {
                return processor;
            }
        }
        return null;
    }

    public static void load() {
        loadProcessorsFromPackage("com.visualipcv.procs");
    }

    public static void loadProcessorsFromPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Processor>> types = reflections.getSubTypesOf(Processor.class);

        for(Class<? extends Processor> proc : types) {
            try {
                processors.add(proc.newInstance());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
