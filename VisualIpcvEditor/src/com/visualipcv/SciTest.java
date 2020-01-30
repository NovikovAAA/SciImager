package com.visualipcv;

import com.visualipcv.scripts.*;
import com.visualipcv.utils.LinkUtils;
import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.types.ScilabType;

import java.util.ArrayList;
import java.util.List;

public class SciTest {
    static {
        LinkUtils.linkNativeLibraries();
    }

    public static void main(String[] args) throws JavasciException.InitializationException {
        SciRunner runner = new SciRunner();
        ProcessorProperty a = new ProcessorProperty("a", DataTypeLibrary.getByName(DataType.NUMBER));
        ProcessorProperty b = new ProcessorProperty("b", DataTypeLibrary.getByName(DataType.NUMBER));

        SciScript script = new SciScript();
        script.setCode("b = a + 10");
        script.addInputProperty(a);
        script.addOutputProperty(b);

        List<Object> in = new ArrayList<>();
        in.add(1.0);

        List<Object> res = script.run(in);

        for(Object v : res) {
            System.out.println(v.toString());
        }
    }
}
