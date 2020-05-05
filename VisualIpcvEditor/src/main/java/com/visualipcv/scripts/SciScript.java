package com.visualipcv.scripts;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.Document;
import com.visualipcv.core.IDocumentPart;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.core.io.ProcessorPropertyEntity;
import com.visualipcv.core.io.SciScriptEntity;
import com.visualipcv.editor.Editor;
import org.scilab.modules.types.ScilabType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SciScript implements IDocumentPart {
    private Document document;
    private UUID id;
    private String name;
    private List<ProcessorProperty> inputProperties = new ArrayList<>();
    private List<ProcessorProperty> outputProperties = new ArrayList<>();
    private String code;

    public SciScript(Document document) {
        this.document = document;
        this.id = UUID.randomUUID();
    }

    public SciScript(Document document, SciScriptEntity entity) {
        this(document);

        for(ProcessorPropertyEntity property : entity.getInputProperties()) {
            inputProperties.add(new ProcessorProperty(property));
        }

        for(ProcessorPropertyEntity property : entity.getOutputProperties()) {
            outputProperties.add(new ProcessorProperty(property));
        }

        this.id = entity.getId();
        this.code = entity.getCode();
        this.name = entity.getName();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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

    public String getCode() {
        return code;
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

    public DataBundle run(DataBundle inputs) {
        for(ProcessorProperty property : inputProperties) {
            SciConverter converter = SciConverters.getConverterForType(property.getType());
            ScilabType sciValue = converter.fromJavaToScilab(inputs.read(property.getName()));
            SciRunner.set(property.getName(), sciValue);
        }

        SciRunner.execute(code);
        DataBundle result = new DataBundle();

        for(ProcessorProperty property : outputProperties) {
            SciConverter converter = SciConverters.getConverterForType(property.getType());
            Object value = converter.fromScilabToJava(SciRunner.get(property.getName()));
            result.write(property.getName(), value);
        }

        return result;
    }

    @Override
    public SciScriptEntity getSerializableProxy() {
        return new SciScriptEntity(this);
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public Document getRoot() {
        return document;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
