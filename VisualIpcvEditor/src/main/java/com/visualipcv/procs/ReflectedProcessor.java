package com.visualipcv.procs;

import com.visualipcv.Console;
import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypeLibrary;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorProperty;
import org.opencv.core.Mat;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ReflectedProcessor extends Processor {
    private Method method;

    public ReflectedProcessor(Method method) throws CommonException {
        super(addProperties(new ProcessorBuilder()
            .setCategory("Reflected")
            .setModule("Core")
            .setName(method.getName()), method));
        this.method = method;
    }

    private static ProcessorBuilder addProperties(ProcessorBuilder builder, Method method) throws CommonException {
        try {
            if(!Modifier.isStatic(method.getModifiers())) {
                builder.addInputProperty(new ProcessorProperty("Target", DataTypeLibrary.getUniqueFromClass(method.getDeclaringClass())));
            }
            for(Parameter parameter : method.getParameters()) {
                DataType type = DataTypeLibrary.getUniqueFromClass(parameter.getType());
                builder.addInputProperty(new ProcessorProperty(parameter.getName(), type));
            }
            DataType type = DataTypeLibrary.getUniqueFromClass(method.getReturnType());
            builder.addOutputProperty(new ProcessorProperty("Result", type));
        } catch(CommonException e) {
            Console.write("Creation reflection processor for method " + method.getName() + " failed");
            throw e;
        }

        return builder;
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) throws CommonException {
        DataBundle bundle = new DataBundle();
        Object target = inputs.read("Target");
        List<Object> args = new ArrayList<>();

        for(ProcessorProperty property : getInputProperties()) {
            if(property.getName().equals("Target"))
                continue;
            args.add(inputs.read(property.getName()));
        }

        try {
            Object result = method.invoke(target, args.toArray());
            DataBundle outputs = new DataBundle();
            outputs.write("Result", result);
            return outputs;
        } catch (Exception e) {
            throw new CommonException("Failed invocation reflection processor " + getName());
        }
    }
}
