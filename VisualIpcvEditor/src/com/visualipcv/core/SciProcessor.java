package com.visualipcv.core;

import com.visualipcv.scripts.SciScript;

public class SciProcessor extends Processor {
    SciScript script;

    public SciProcessor(String name, String module, String category, SciScript script) {
        super(name, module, category, script.getInputProperties(), script.getOutputProperties());
        this.script = script;
    }

    public DataBundle execute(DataBundle inputs, DataBundle state) {
        return script.run(inputs);
    }
}
