package com.visualipcv.core;

public class ProcessorVersionMismatchException extends CommonException {
    private Node node;
    private ProcessorUID processor;

    public ProcessorVersionMismatchException(Node node, ProcessorUID processor) {
        super("Processor version mismatch");
        this.node = node;
        this.processor = processor;
    }

    public Node getNode() {
        return node;
    }

    public ProcessorUID getProcessor() {
        return processor;
    }
}
