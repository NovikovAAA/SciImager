package com.visualipcv.procs;

import com.visualipcv.*;
import com.visualipcv.core.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsoleOutputProcessor extends Processor {
    private static final long OUTPUT_DELAY = 500;

    public ConsoleOutputProcessor() {
        super(new ProcessorBuilder()
            .setName("ConsoleOutput")
            .setModule("Core")
            .setCategory("Output")
            .addInputProperty(new ProcessorProperty("Text", DataTypes.STRING, true, true)));
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle state) {
        Long time = state.read("Time");

        if(time == null) {
            state.write("Time", System.nanoTime());
            return new DataBundle();
        }

        if(System.nanoTime() - time >= OUTPUT_DELAY * 1000000) {
            DataBundle bundle = new DataBundle();
            Console.write(inputs.read("Text").toString());
            state.write("Time", System.nanoTime());
        }

        return new DataBundle();
    }
}
