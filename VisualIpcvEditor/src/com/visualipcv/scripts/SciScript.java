package com.visualipcv.scripts;

import com.visualipcv.ProcessorProperty;
import org.scilab.modules.types.ScilabType;

import java.util.ArrayList;
import java.util.List;

public class SciScript {
    private List<ProcessorProperty> inputProperties = new ArrayList<>();
    private List<ProcessorProperty> outputProperties = new ArrayList<>();
    private String code;

    public SciScript() {

    }

    public void addInputProperty(ProcessorProperty property) {
        inputProperties.add(property);
    }

    public void addOutputProperty(ProcessorProperty property) {
        outputProperties.add(property);
    }

    public List<ProcessorProperty> getInputProperties() {
        return inputProperties;
    }

    public List<ProcessorProperty> getOutputProperties() {
        return outputProperties;
    }

    public ProcessorProperty getInputPropertyByName(String name) {
        for(ProcessorProperty property : inputProperties) {
            if(property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public ProcessorProperty getOutputPropertyByName(String name) {
        for(ProcessorProperty property : outputProperties) {
            if(property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Object> run(List<Object> inputs) {
        assert(inputProperties.size() == inputs.size());

        for(int i = 0; i < inputs.size(); i++) {
            SciConverter converter = SciConverters.getConverterForType(inputProperties.get(i).getType());
            ScilabType sciValue = converter.fromJavaToScilab(inputs.get(i));
            SciRunner.set(inputProperties.get(i).getName(), sciValue);
        }

        SciRunner.execute(code);
        List<Object> result = new ArrayList<>();

        for(int i = 0; i < outputProperties.size(); i++) {
            SciConverter converter = SciConverters.getConverterForType(outputProperties.get(i).getType());
            Object value = converter.fromScilabToJava(SciRunner.get(outputProperties.get(i).getName()));
            result.add(value);
        }

        return result;
    }
}
