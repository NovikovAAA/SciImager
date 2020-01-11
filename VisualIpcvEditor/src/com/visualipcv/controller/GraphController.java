package com.visualipcv.controller;

import com.visualipcv.Graph;
import com.visualipcv.Node;
import com.visualipcv.NodeSlot;
import com.visualipcv.Processor;
import com.visualipcv.view.*;
import com.visualipcv.view.events.DragDropEventListener;
import com.visualipcv.view.events.GraphModifiedEventListener;
import com.visualipcv.view.events.NodeEventListener;
import com.visualipcv.view.events.NodeSlotEventListener;

import java.awt.*;
import java.util.HashMap;

public class GraphController implements GraphModifiedEventListener {
    private GraphView graphView;
    private Graph graph;

    private HashMap<Node, NodeView> nodeToView = new HashMap<>();
    private HashMap<NodeView, Node> viewToNode = new HashMap<>();
    private HashMap<NodeSlot, SlotConnection> connections = new HashMap<>();

    public GraphController(Graph graph, GraphView view) {
        this.graph = graph;
        this.graphView = view;
        this.graph.addGraphEventListener(this);

        this.graphView.setDropListener(new DragDropEventListener() {
            @Override
            public void onDrop(Object payload, Point location) {
                if(!(payload instanceof Processor)) {
                    return;
                }
                graph.addNode(new Node((Processor)payload, location.x, location.y));
            }
            @Override
            public void onBeginDrag(Object payload) {

            }
        });
    }

    private NodeSlot findSlotByView(Node node, NodeView nodeView, NodeSlotView slotView) {
        for(int i = 0; i < node.getInputSlotCount(); i++) {
            if(nodeView.getInputSlot(i) == slotView) {
                return node.getInputSlot(i);
            }
        }
        for(int i = 0; i < node.getOutputSlotCount(); i++) {
            if(nodeView.getOutputSlot(i) == slotView) {
                return node.getOutputSlot(i);
            }
        }
        return null;
    }

    private NodeSlotView findViewBySlot(Node node, NodeSlot slot) {
        NodeView view = nodeToView.get(node);

        for(int i = 0; i < node.getInputSlotCount(); i++) {
            if(node.getInputSlot(i) == slot) {
                return view.getInputSlot(i);
            }
        }

        for(int i = 0; i < node.getOutputSlotCount(); i++) {
            if(node.getOutputSlot(i) == slot) {
                return view.getOutputSlot(i);
            }
        }

        return null;
    }

    @Override
    public void onNodeAdded(Graph graph, Node newNode) {
        NodeView view = new NodeView(newNode.getProcessor(), graphView);
        graphView.add(view);
        nodeToView.put(newNode, view);
        viewToNode.put(view, newNode);

        view.setNodeEventListener(new NodeEventListener() {
            @Override
            public void onMove(NodeView view, int deltaX, int deltaY) {
                Node node = viewToNode.get(view);
                node.setLocation(node.getX() + deltaX, node.getY() + deltaY);
                updateNodeView(view);
            }
        });

        for(int i = 0; i < view.getInputSlotCount(); i++) {
            final NodeSlotView slotView = view.getInputSlot(i);
            final Node node = viewToNode.get(slotView.getNode());
            final NodeSlot slot = node.getInputSlot(i);

            slotView.setDragDropEventListener(new DragDropEventListener() {
                @Override
                public void onDrop(Object payload, Point location) {
                    NodeSlotView other = (NodeSlotView)payload;
                    Node otherNode = viewToNode.get(other.getNode());
                    NodeSlot otherSlot = findSlotByView(otherNode, other.getNode(), other);
                    slot.connect(otherSlot);
                }

                @Override
                public void onBeginDrag(Object payload) {
                    if(slotView.getType() == NodeSlotType.INPUT && slotView.getConnectionCount() >= 1) {
                        slot.disconnect();
                    }
                }
            });

            slot.addNodeSlotEventListener(new NodeSlotEventListener() {
                @Override
                public void onConnected(NodeSlot source, NodeSlot target) {
                    NodeSlotView sourceView = findViewBySlot(source.getNode(), source);
                    NodeSlotView targetView = findViewBySlot(target.getNode(), target);
                    SlotConnection connection = new SlotConnection(sourceView, targetView);
                    graphView.add(connection);
                    connection.updateBounds();
                    connections.put(target, connection);
                }

                @Override
                public void onDisconnected(NodeSlot source, NodeSlot target) {
                    NodeSlotView sourceView = findViewBySlot(source.getNode(), source);
                    NodeSlotView targetView = findViewBySlot(target.getNode(), target);
                    SlotConnection connection = connections.get(target);
                    connection.remove();
                    graphView.remove(connection);
                    connections.remove(target);
                }
            });
        }

        for(int i = 0; i < view.getOutputSlotCount(); i++) {
            final NodeSlotView slotView = view.getOutputSlot(i);
            final Node node = viewToNode.get(slotView.getNode());
            final NodeSlot slot = node.getOutputSlot(i);

            slotView.setDragDropEventListener(new DragDropEventListener() {
                @Override
                public void onDrop(Object payload, Point location) {
                    NodeSlotView other = (NodeSlotView)payload;
                    Node otherNode = viewToNode.get(other.getNode());
                    NodeSlot otherSlot = findSlotByView(otherNode, other.getNode(), other);
                    otherSlot.connect(slot);
                }

                @Override
                public void onBeginDrag(Object payload) {

                }
            });
        }

        updateNodeView(view);
    }

    @Override
    public void onNodeRemoved(Graph graph, Node node) {
        graphView.remove(nodeToView.get(node));
        viewToNode.remove(nodeToView.get(node));
        nodeToView.remove(node);
    }

    public void updateNodeView(NodeView view) {
        Node node = viewToNode.get(view);
        view.setLocation(node.getX(), node.getY());
        graphView.repaint();
    }
}
