package com.visualipcv;

import com.visualipcv.scripts.SciProperty;
import com.visualipcv.scripts.SciScript;

import java.util.ArrayList;
import java.util.List;

public class SciProcessor extends Processor {
    SciScript script;

    public SciProcessor(String name, String module, String category, SciScript script) {
        super(name, module, category, script.getInputProperties(), script.getOutputProperties());
        this.script = script;
    }

    public List<Object> execute(List<Object> inputs) {
        return script.run(inputs);
    }
}
