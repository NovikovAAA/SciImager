package com.visualipcv.procs;

import com.visualipcv.*;
import com.visualipcv.core.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsoleOutputProcessor extends Processor {
    private static final long OUTPUT_DELAY = 500;
    private static long currentTime = -1;

    public ConsoleOutputProcessor() {
        super(new ProcessorBuilder()
            .setName("ConsoleOutput")
            .setModule("Core")
            .setCategory("Output")
            .addInputProperty(new ProcessorProperty("Text", DataTypes.STRING, true, true)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle props) {
        if(currentTime == -1)
            currentTime = System.nanoTime();

        if(System.nanoTime() - currentTime >= OUTPUT_DELAY * 1000000) {
            DataBundle bundle = new DataBundle();
            Console.write(inputs.read("Text").toString());
            currentTime = System.nanoTime();
        }

        return new DataBundle();
    }
}
