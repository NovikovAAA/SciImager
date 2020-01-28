package com.visualipcv.scripts;

import org.scilab.modules.types.ScilabType;

import java.util.ArrayList;
import java.util.List;

public class SciScript {
    private List<SciProperty> inputProperties = new ArrayList<>();
    private List<SciProperty> outputProperties = new ArrayList<>();
    private String code;

    public SciScript() {

    }

    public void addInputProperty(SciProperty property) {
        inputProperties.add(property);
    }

    public void addOutputProperty(SciProperty property) {
        outputProperties.add(property);
    }

    public List<SciProperty> getInputProperties() {
        return inputProperties;
    }

    public List<SciProperty> getOutputProperties() {
        return outputProperties;
    }

    public SciProperty getInputPropertyByName(String name) {
        for(SciProperty property : inputProperties) {
            if(property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public SciProperty getOutputPropertyByName(String name) {
        for(SciProperty property : outputProperties) {
            if(property.getName().equals(name)) {
                return property;
            }
        }
        return null;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Object> run(SciRunner runner, List<Object> inputs) {
        assert(inputProperties.size() == inputs.size());

        for(int i = 0; i < inputs.size(); i++) {
            SciConverter converter = SciConverters.getConverterForClass(inputs.get(i).getClass());
            ScilabType sciValue = converter.fromJavaToScilab(inputs.get(i));
            runner.set(inputProperties.get(i).getName(), sciValue);
        }

        runner.execute(code);
        List<Object> result = new ArrayList<Object>();

        for(int i = 0; i < outputProperties.size(); i++) {
            SciConverter converter = SciConverters.getConverterForClass(outputProperties.get(i).getType());
            Object value = converter.fromScilabToJava(runner.get(outputProperties.get(i).getName()));
            result.add(value);
        }

        return result;
    }
}
