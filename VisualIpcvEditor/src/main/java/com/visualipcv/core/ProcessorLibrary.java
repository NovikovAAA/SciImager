package com.visualipcv.core;

import com.visualipcv.procs.*;
import org.opencv.core.Mat;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

public class ProcessorLibrary extends Refreshable {
    private List<Processor> processors = new ArrayList<>();
    public static native void loadPlugins(String path);
    public static native void loadPluginsWithManualRegister(String path);
    public static native List<ProcessorUID> getProcessorList();

    private static ProcessorLibrary instance;

    public static ProcessorLibrary getInstance() {
        if(instance == null)
            instance = new ProcessorLibrary();
        return instance;
    }

    public ProcessorLibrary() {
        loadProcessorsFromPackage("com.visualipcv.procs");
        loadPlugins();
        List<ProcessorUID> uids = getProcessorList();

        for (ProcessorUID uid: uids) {
            processors.add(new NativeProcessor(uid));
        }
    }

    public void loadProcessorsFromPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Processor>> types = reflections.getSubTypesOf(Processor.class);

        for(Class<? extends Processor> proc : types) {
            try {
                processors.add(proc.newInstance());
            } catch(Exception e) {

            }
        }
    }

    public static List<Processor> getProcessors() {
        return getInstance().processors;
    }

    public static Processor findProcessor(String module, String name) {
        for(Processor processor : getProcessors()) {
            if(processor.getModule().equals(module) && processor.getName().equals(name)) {
                return processor;
            }
        }
        return null;
    }

    public static void addProcessor(Processor processor) {
        getInstance().processors.add(processor);
        getInstance().refresh();
    }

    private static void loadPlugins() {
        String pluginsPath = System.getProperty("user.dir") + "/Plugins";
        loadPlugins(pluginsPath);
    }
}
