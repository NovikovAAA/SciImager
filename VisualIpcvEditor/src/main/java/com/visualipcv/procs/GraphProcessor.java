package com.visualipcv.procs;

import com.visualipcv.core.CommonException;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.Graph;
import com.visualipcv.core.GraphElement;
import com.visualipcv.core.GraphExecutionContext;
import com.visualipcv.core.GraphExecutionData;
import com.visualipcv.core.GraphExecutionException;
import com.visualipcv.core.InputNodeSlot;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.OutputNodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorBuilder;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.ProcessorProperty;

import java.util.ArrayList;
import java.util.List;

public class GraphProcessor extends Processor {
    private final Graph graph;

    public GraphProcessor(Graph graph) throws CommonException {
        super(new ProcessorBuilder()
            .setName(graph.getName())
            .setCategory(graph.getDocument().getName())
            .setModule(graph.getId().toString())
            .setInputProperties(getInputProperties(graph))
            .setOutputProperties(getOutputProperties(graph)));

        this.graph = graph;
    }

    public void rebuild() throws CommonException {
        rebuild(new ProcessorBuilder()
                .setName(graph.getName())
                .setCategory(graph.getDocument().getName())
                .setModule(graph.getId().toString())
                .setInputProperties(getInputProperties(graph))
                .setOutputProperties(getOutputProperties(graph)));
        ProcessorLibrary.update();
    }

    @Override
    public DataBundle execute(DataBundle inputs, DataBundle props) throws CommonException {
        DataBundle outputs = new DataBundle();

        for(ProcessorProperty in : getInputProperties()) {
            Node property = graph.findProperty(in.getName());
            OutputNodeSlot output = property.getOutputSlots().get(0);
            GraphExecutionData.store(output, inputs.read(property.getName()));
        }

        GraphExecutionContext context = props.read("Context");

        if(context == null) {
            context = new GraphExecutionContext();
            props.write("Context", context);
        }

        try {
            graph.execute(context, false);
        } catch (GraphExecutionException e) {
            throw new CommonException(e.getMessage());
        }

        int index = 0;

        for(Node node : graph.getOutputNodes()) {
            for(InputNodeSlot slot : node.getInputSlots()) {
                Object value = GraphExecutionData.load(slot);
                outputs.write(getNameForOutput(node, slot, index), value);
                ++index;
            }
        }

        return outputs;
    }

    private static String getNameForOutput(Node node, NodeSlot slot, int index) {
        return slot.getProperty().getName() + " " + Integer.toString(index);
    }

    private static List<ProcessorProperty> getInputProperties(Graph graph) {
        List<ProcessorProperty> properties = new ArrayList<>();

        for(GraphElement node : graph.getNodes()) {
            if(!(node instanceof Node))
                continue;

            Node n = (Node)node;

            if(n.isProxy())
                continue;

            Processor processor = n.findProcessor();

            if(processor != null) {
                if(processor.isProperty()) {
                    ProcessorProperty property = n.getInputSlots().get(0).getProperty();
                    String name = node.getName();
                    properties.add(new ProcessorProperty(name, property.getType()));
                }
            }
        }

        return properties;
    }

    private static List<ProcessorProperty> getOutputProperties(Graph graph) {
        List<ProcessorProperty> properties = new ArrayList<>();
        List<Node> nodes = graph.getOutputNodes();

        int index = 0;

        for(Node node : nodes) {
            for(NodeSlot slot : node.getInputSlots()) {
                ProcessorProperty property = new ProcessorProperty(getNameForOutput(node, slot, index), slot.getProperty().getType());
                properties.add(property);
                ++index;
            }
        }

        return properties;
    }
}
