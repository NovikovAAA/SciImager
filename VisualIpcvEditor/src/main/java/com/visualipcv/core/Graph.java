package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.controller.GraphElementController;
import com.visualipcv.controller.NodeController;
import com.visualipcv.core.io.ConnectionEntity;
import com.visualipcv.core.io.GraphClipboard;
import com.visualipcv.core.io.GraphEntity;
import com.visualipcv.core.io.GroupEntity;
import com.visualipcv.core.io.NodeEntity;
import com.visualipcv.editor.Editor;
import com.visualipcv.procs.GraphProcessor;
import com.visualipcv.utils.ProcUtils;
import com.visualipcv.view.CustomDataFormats;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Graph implements IDocumentPart {
    private Document document;
    private UUID id;
    private String name;
    private ArrayList<GraphElement> nodes = new ArrayList<>();
    private Set<Connection> connections = new HashSet<>();
    private Map<NodeSlot, List<Connection>> slotConnectionAssoc = new HashMap<>();

    public Graph(Document document) {
        this.document = document;
        this.id = UUID.randomUUID();
    }

    public Graph(Document document, GraphEntity graphEntity) {
        this(document);
        name = graphEntity.getName();
        id = graphEntity.getId();

        for(NodeEntity node : graphEntity.getNodes()) {
            try {
                addNode(new Node(this, node));
            } catch (CommonException e) {
                Console.error(e.getMessage());
            }
        }

        for(GroupEntity group : graphEntity.getGroups()) {
            try {
                addNode(new Group(this, group));
            } catch (CommonException e) {
                Console.error(e.getMessage());
            }
        }

        for(ConnectionEntity connection : graphEntity.getConnections()) {
            try {
                addConnection(new Connection(this, connection));
            } catch (CommonException e) {
                Console.error(e.getMessage());
            }
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
        onChanged();
    }

    @Override
    public String getName() {
        return name;
    }

    public void addNode(GraphElement node) {
        nodes.add(node);

        try {
            node.onCreate();
        } catch (GraphExecutionException e) {
            Console.write(e.getMessage());
        }
        onChanged();
    }

    public void addNodes(Collection<GraphElement> nodes) {
        this.nodes.addAll(nodes);

        for(GraphElement node : nodes) {
            try {
                node.onCreate();
            } catch (GraphExecutionException e) {
                Console.write(e.getMessage());
            }
        }
        onChanged();
    }

    public void removeNode(GraphElement node) {
        nodes.remove(node);

        try {
            node.onDestroy();
        } catch (GraphExecutionException e) {
            Console.write(e.getMessage());
        }

        onChanged();
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
        slotConnectionAssoc.computeIfAbsent(connection.getSource(), k -> new ArrayList<>());
        slotConnectionAssoc.computeIfAbsent(connection.getTarget(), k -> new ArrayList<>());
        slotConnectionAssoc.get(connection.getSource()).add(connection);
        slotConnectionAssoc.get(connection.getTarget()).add(connection);
        updateProperties(connection.getSource().getNode(), new HashSet<>());

        onChanged();
    }

    public void removeConnections(NodeSlot slot) {
        List<Connection> connectionsToRemove = new ArrayList<>();

        for(Connection connection : connections) {
            if(connection.getSource() == slot || connection.getTarget() == slot) {
                connectionsToRemove.add(connection);
            }
        }

        for(Connection connection : connectionsToRemove) {
            slotConnectionAssoc.get(connection.getSource()).remove(connection);
            slotConnectionAssoc.get(connection.getTarget()).remove(connection);

            HashSet<Node> updatedNodes = new HashSet<>();
            updateProperties(connection.getSource().getNode(), updatedNodes);
            updateProperties(connection.getTarget().getNode(), updatedNodes);
        }

        connections.removeAll(connectionsToRemove);

        onChanged();
    }

    public List<GraphElement> getNodes() {
        return nodes;
    }

    public Set<Connection> getConnections() {
        return connections;
    }

    public List<Connection> getConnections(NodeSlot slot) {
        slotConnectionAssoc.computeIfAbsent(slot, nodeSlot -> new ArrayList<>());
        return slotConnectionAssoc.get(slot);
    }

    public List<Node> getOutputNodes() {
        List<Node> outputs = new ArrayList<>();

        for(GraphElement node : nodes) {
            if(!(node instanceof Node))
                continue;

            Node n = (Node)node;

            if(n.isProxy())
                continue;

            Processor processor = n.findProcessor();

            if(processor != null) {
                if(n.findProcessor().isOutput()) {
                    outputs.add(n);
                }
            }
        }

        return outputs;
    }

    public List<Node> getProxyNodes() {
        List<Node> proxy = new ArrayList<>();

        for(GraphElement node : nodes) {
            if(!(node instanceof Node))
                continue;

            if(((Node)node).isProxy()) {
                proxy.add((Node)node);
            }
        }

        return proxy;
    }

    private void updateProperties(Node node, Set<Node> updatedNodes) {
        if(updatedNodes.contains(node))
            return;

        ProcUtils.shareType(node);
        updatedNodes.add(node);

        for(InputNodeSlot slot : node.getInputSlots()) {
            for(Connection connection : getConnections(slot)) {
                if(connection.getSource().getNode() != node)
                    updateProperties(connection.getSource().getNode(), updatedNodes);
                if(connection.getTarget().getNode() != node)
                    updateProperties(connection.getTarget().getNode(), updatedNodes);
            }
        }

        for(NodeSlot slot : node.getOutputSlots()) {
            for(Connection connection : getConnections(slot)) {
                if(connection.getSource().getNode() != node)
                    updateProperties(connection.getSource().getNode(), updatedNodes);
                if(connection.getTarget().getNode() != node)
                    updateProperties(connection.getTarget().getNode(), updatedNodes);
            }
        }
    }

    public void execute(GraphExecutionContext context, boolean cleanProperties) throws GraphExecutionException {
        GraphExecutionData.clear(this, cleanProperties);

        for(GraphElement node : this.nodes) {
            if(node instanceof Node)
                ((Node)node).checkProcessorCompatibility();
        }

        List<Node> nodes = getOutputNodes();

        for (Node node : nodes) {
            node.execute(context);
        }
    }

    public <T extends GraphElement> T findNode(UUID id) {
        for(GraphElement node : nodes) {
            if(node.getId().equals(id)) {
                return (T)node;
            }
        }
        return null;
    }

    public Node findProperty(String name) {
        for(GraphElement node : getNodes()) {
            if(!(node instanceof Node))
                continue;

            if(((Node)node).isProxy())
                continue;

            if(((Node)node).findProcessor().isProperty() && node.getName().equals(name)) {
                return (Node)node;
            }
        }

        return null;
    }

    public void onChanged() {
        if(getDocument() == null)
            return;

        Processor processor = ProcessorLibrary.findProcessor(getId());

        if(processor == null) {
            try {
                ProcessorLibrary.addProcessor(new GraphProcessor(this));
            } catch (CommonException e) {
                Console.error(e.getMessage());
            }

            return;
        }

        if(processor instanceof GraphProcessor) {
            try {
                ((GraphProcessor)processor).rebuild();
            } catch (CommonException e) {
                Console.write(e.getMessage());
            }
        }
    }

    @Override
    public Object getSerializableProxy() {
        return new GraphEntity(this);
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public Document getRoot() {
        return document;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void onOpen() {
        onChanged();
    }

    @Override
    public void onClose() {
        Processor processor = ProcessorLibrary.findProcessor(getId());

        if(processor != null) {
            ProcessorLibrary.removeProcessor(processor);
        }

        Editor.closeWindow(null, this);
    }

    public void executeAsync(GraphExecutionContext context, Semaphore semaphore) {
        for(GraphElement node : this.nodes) {
            if(!(node instanceof Node))
                continue;

            if(!((Node)node).isProxy())
                ((Node)node).checkProcessorCompatibility();
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(semaphore != null) {
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                try {
                    execute(context,  true);
                } catch (GraphExecutionException e) {
                    throw new RuntimeException(e);
                } finally {
                    if(semaphore != null) {
                        semaphore.release();
                    }
                }
            }
        });

        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //Console.error(e);
            }
        });

        thread.start();
    }

    public Node replaceWithUpdatedProcessor(Node node, Processor processor) {
        Node newNode = new Node(this, processor, node.getX(), node.getY());
        removeNode(node);
        addNode(newNode);
        return newNode;
    }
}
