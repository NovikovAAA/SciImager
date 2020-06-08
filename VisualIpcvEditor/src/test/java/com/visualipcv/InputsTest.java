package com.visualipcv;

import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.procs.NumberSourceProcessor;
import org.junit.Assert;
import org.junit.Test;

public class InputsTest {

    @Test
    public void checkSimpleInput() {
        ProcessorLibrary.disablePlugins();

        Graph graph = new Graph(null);
        Node node = new Node(graph, new NumberSourceProcessor(), 0.0, 0.0);
        graph.addNode(node);
        node.getInputNodeSlot("Number").setValue(10.0);

        Assert.assertEquals(node.getInputNodeSlot("Number").getValue(), 10.0);
        Assert.assertEquals(graph.getNodes().size(), 1);
    }
}
