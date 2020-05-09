package com.visualipcv.core;

import com.visualipcv.scripts.SciScript;

import java.util.HashMap;

public class SciProcessor extends Processor {
    SciScript script;

    public SciProcessor(SciScript script) {
        super(new ProcessorBuilder()
                .setName(script.getName())
                .setModule(script.getId().toString())
                .setCategory(script.getDocument().getName())
                .setInputProperties(script.getInputProperties())
                .setOutputProperties(script.getOutputProperties()));
        this.script = script;
    }

    public void rebuild() {
        super.rebuild(new ProcessorBuilder()
                .setName(script.getName())
                .setModule(script.getId().toString())
                .setCategory(script.getDocument().getName())
                .setInputProperties(script.getInputProperties())
                .setOutputProperties(script.getOutputProperties()));
        ProcessorLibrary.update();
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle props) {
        return script.run(inputs);
    }

    public SciScript getScript() {
        return script;
    }
}
