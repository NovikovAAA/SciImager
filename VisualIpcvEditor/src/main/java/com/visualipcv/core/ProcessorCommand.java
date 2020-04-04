package com.visualipcv.core;

public abstract class ProcessorCommand {
    public abstract void execute(DataBundle state);
    public abstract String getName();
}
