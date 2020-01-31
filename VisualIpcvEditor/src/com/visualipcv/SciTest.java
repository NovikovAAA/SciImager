package com.visualipcv;

import com.visualipcv.core.DataBundle;
import com.visualipcv.core.DataType;
import com.visualipcv.core.DataTypeLibrary;
import com.visualipcv.core.ProcessorProperty;
import com.visualipcv.scripts.*;
import com.visualipcv.utils.LinkUtils;
import org.scilab.modules.javasci.JavasciException;

import java.util.ArrayList;
import java.util.List;

public class SciTest {
    static {
        LinkUtils.linkNativeLibraries();
    }

    public static void main(String[] args) throws JavasciException.InitializationException {
        SciRunner runner = new SciRunner();
        ProcessorProperty a = new ProcessorProperty("a", DataType.NUMBER);
        ProcessorProperty b = new ProcessorProperty("b", DataType.NUMBER);

        SciScript script = new SciScript();
        script.setCode("b = a + 10");
        script.addInputProperty(a);
        script.addOutputProperty(b);

        DataBundle in = new DataBundle();
        in.write("a", 1.0);

        DataBundle res = script.run(in);

        for(Object v : res) {
            System.out.println(v.toString());
        }
    }
}
