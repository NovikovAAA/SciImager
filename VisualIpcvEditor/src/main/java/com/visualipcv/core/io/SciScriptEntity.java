package com.visualipcv.core.io;

import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.scripts.SciScript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SciScriptEntity implements Serializable {
    private UUID id;
    private String name;
    private List<ProcessorPropertyEntity> inputProperties = new ArrayList<>();
    private List<ProcessorPropertyEntity> outputProperties = new ArrayList<>();
    private String code;

    public SciScriptEntity(SciScript script) {
        for(ProcessorProperty property : script.getInputProperties()) {
            inputProperties.add(new ProcessorPropertyEntity(property));
        }

        for(ProcessorProperty property : script.getOutputProperties()) {
            outputProperties.add(new ProcessorPropertyEntity(property));
        }

        this.id = script.getId();
        this.code = script.getCode();
        this.name = script.getName();
    }

    public List<ProcessorPropertyEntity> getInputProperties() {
        return inputProperties;
    }

    public List<ProcessorPropertyEntity> getOutputProperties() {
        return outputProperties;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }
}
