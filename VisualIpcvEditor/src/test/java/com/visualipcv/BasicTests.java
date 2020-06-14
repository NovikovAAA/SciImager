package com.visualipcv;

import com.visualipcv.core.DataTypes;
import com.visualipcv.core.Graph;
import com.visualipcv.core.GraphExecutionContext;
import com.visualipcv.core.GraphExecutionData;
import com.visualipcv.core.GraphExecutionException;
import com.visualipcv.core.Node;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.procs.AbsDiffProcessor;
import com.visualipcv.procs.ConvertImageFormatProcessor;
import com.visualipcv.procs.GetImageTypeProcessor;
import com.visualipcv.procs.ImageSourceProcessor;
import com.visualipcv.procs.NumberSourceProcessor;
import com.visualipcv.procs.ToStringProcessor;
import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.CvType;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BasicTests {

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

    @Test
    public void checkDataTransition() throws GraphExecutionException {
        ProcessorLibrary.disablePlugins();

        Graph graph = new Graph(null);

        Node numberSource = new Node(graph, new NumberSourceProcessor(), 0.0, 0.0);
        graph.addNode(numberSource);
        numberSource.getInputNodeSlot("Number").setValue(10.0);

        Node toString = new Node(graph, new ToStringProcessor(), 0.0, 0.0);
        graph.addNode(toString);

        toString.getInputNodeSlot("Data").connect(numberSource.getOutputNodeSlot("Result"));
        GraphExecutionContext context = new GraphExecutionContext();
        toString.execute(context);

        String res = (String)GraphExecutionData.load(toString.getOutputNodeSlot("String"));
        Assert.assertEquals(res, "10.0");
    }

    @Test
    public void checkNodeRemove() throws GraphExecutionException {
        ProcessorLibrary.disablePlugins();

        Graph graph = new Graph(null);

        Node numberSource = new Node(graph, new NumberSourceProcessor(), 0.0, 0.0);
        graph.addNode(numberSource);
        numberSource.getInputNodeSlot("Number").setValue(10.0);

        Node toString = new Node(graph, new ToStringProcessor(), 0.0, 0.0);
        graph.addNode(toString);

        toString.getInputNodeSlot("Data").connect(numberSource.getOutputNodeSlot("Result"));
        graph.removeNode(numberSource);

        Assert.assertEquals(graph.getNodes().size(), 1);
        Assert.assertEquals(graph.getConnections().size(), 0);
    }

    @Test
    public void checkAnyType() throws GraphExecutionException {
        ProcessorLibrary.disablePlugins();

        Graph graph = new Graph(null);

        Node numberSource1 = new Node(graph, new NumberSourceProcessor(), 0.0, 0.0);
        Node numberSource2 = new Node(graph, new NumberSourceProcessor(), 0.0, 0.0);
        Node absDiff = new Node(graph, new AbsDiffProcessor(), 0.0, 0.0);

        graph.addNode(numberSource1);
        graph.addNode(numberSource2);
        graph.addNode(absDiff);

        absDiff.getInputNodeSlot("A").connect(numberSource1.getOutputNodeSlot("Result"));
        absDiff.getInputNodeSlot("B").connect(numberSource2.getOutputNodeSlot("Result"));

        Assert.assertEquals(absDiff.getInputNodeSlot("A").getActualType(), DataTypes.DOUBLE);
        absDiff.getInputNodeSlot("A").disconnect();
        Assert.assertEquals(absDiff.getOutputNodeSlot("Result").getActualType(), DataTypes.DOUBLE);
        absDiff.getInputNodeSlot("B").disconnect();
        Assert.assertEquals(absDiff.getOutputNodeSlot("Result").getActualType(), DataTypes.ANY);
    }

    @Test
    public void clearGraph() {
        ProcessorLibrary.disablePlugins();

        Graph graph = new Graph(null);

        for(int i = 0; i < 10; i++) {
            Node node = new Node(graph, new NumberSourceProcessor(), 0.0, 0.0);
            graph.addNode(node);
        }

        while(!graph.getNodes().isEmpty()) {
            graph.removeNode(graph.getNodes().get(0));
        }

        Assert.assertEquals(graph.getNodes().size(), 0);
    }

    /*@Test
    public void checkEnums() throws GraphExecutionException {
        ProcessorLibrary.disablePlugins();
        Graph graph = new Graph(null);

        Node imageConverter = new Node(graph, new ConvertImageFormatProcessor(), 0.0, 0.0);
        Node imageSource = new Node(graph, new ImageSourceProcessor(), 0.0, 0.0);
        Node getType = new Node(graph, new GetImageTypeProcessor(), 0.0, 0.0);
        Node toString = new Node(graph, new ToStringProcessor(), 0.0, 0.0);

        graph.addNode(imageConverter);
        graph.addNode(imageSource);
        graph.addNode(getType);
        graph.addNode(toString);

        imageSource.getInputNodeSlot("Path").setValue(Paths.get("../Resources/test.png").toAbsolutePath().toString());
        imageConverter.getInputNodeSlot("Target").setValue(CvType.CV_16U);
        imageConverter.getInputNodeSlot("Image").connect(imageSource.getOutputNodeSlot("Image"));
        getType.getInputNodeSlot("Image").connect(imageConverter.getOutputNodeSlot("Result"));
        toString.getInputNodeSlot("Data").connect(getType.getOutputNodeSlot("Type"));

        GraphExecutionContext context = new GraphExecutionContext();
        toString.execute(context);

        String value = (String)GraphExecutionData.load(toString.getOutputSlots().get(0));
        Assert.assertEquals(value, "2");
    }*/
}
