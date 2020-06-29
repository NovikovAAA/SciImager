package com.visualipcv.scripts;

import com.visualipcv.Console;
import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.Document;
import com.visualipcv.core.IDocumentPart;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.core.SciProcessor;
import com.visualipcv.core.io.ProcessorPropertyEntity;
import com.visualipcv.core.io.SciScriptEntity;
import com.visualipcv.editor.Editor;
import com.visualipcv.procs.GraphProcessor;
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
        onChanged();
    }

    @Override
    public String getName() {
        return name;
    }

    public void addInputProperty(ProcessorProperty property) {
        inputProperties.add(property);
        onChanged();
    }

    public void addOutputProperty(ProcessorProperty property) {
        outputProperties.add(property);
        onChanged();
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
        onChanged();
    }

    public DataBundle run(DataBundle inputs) {
        for(ProcessorProperty property : inputProperties) {
            SciConverter converter = SciConverters.getConverterForType(property.getType());
            ScilabType sciValue = converter.fromJavaToScilab(property.getName(), inputs.read(property.getName()));
            SciRunner.set(property.getName(), sciValue);
        }

        SciRunner.execute(code);
        DataBundle result = new DataBundle();

        for(ProcessorProperty property : outputProperties) {
            SciConverter converter = SciConverters.getConverterForType(property.getType());
            Object value = converter.fromScilabToJava(property.getName(), SciRunner.get(property.getName()));
            result.write(property.getName(), value);
        }

        return result;
    }

    private void onChanged() {
        if(getDocument() == null)
            return;

        Processor processor = ProcessorLibrary.findProcessor(getId());

        if(processor == null) {
            try {
                ProcessorLibrary.addProcessor(new SciProcessor(this));
            } catch (CommonException e) {
                Console.error(e.getMessage());
            }

            return;
        }

        if(processor instanceof SciProcessor) {
            try {
                ((SciProcessor)processor).rebuild();
            } catch (CommonException e) {
                Console.write(e.getMessage());
            }
        }
    }

    @Override
    public void onOpen() {
        onChanged();
    }

    @Override
    public void onClose() {
        Processor processor = ProcessorLibrary.findProcessor(getId());

        if(processor != null) {
            ProcessorLibrary.removeProcessor(processor);
        }

        Editor.closeWindow(null, this);
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
