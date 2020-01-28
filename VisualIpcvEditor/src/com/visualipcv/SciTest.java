package com.visualipcv;

import com.visualipcv.scripts.*;
import org.scilab.modules.javasci.JavasciException;
import org.scilab.modules.types.ScilabType;

import java.util.ArrayList;
import java.util.List;

public class SciTest {
    public static void main(String[] args) throws JavasciException.InitializationException {
        SciRunner runner = new SciRunner();
        SciProperty a = new SciProperty(Double.class, "a");
        SciProperty b = new SciProperty(Double.class, "b");

        SciScript script = new SciScript();
        script.setCode("b = a + 10");
        script.addInputProperty(a);
        script.addOutputProperty(b);

        List<Object> in = new ArrayList<Object>();
        in.add(1.0);

        List<Object> res = script.run(runner, in);

        for(Object v : res) {
            System.out.println(v.toString());
        }
    }
}
