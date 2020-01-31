package com.visualipcv.controller;

import com.visualipcv.core.*;
import com.visualipcv.view.*;
import com.visualipcv.view.events.*;
import org.w3c.dom.views.AbstractView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                graph.addNode(new Node(GraphController.this.graph, (Processor)payload, location.x, location.y));
            }
            @Override
            public void onBeginDrag(Object payload) {

            }
        });

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graph.execute();
            }
        });
        timer.start();
    }

    private NodeSlot findSlotByView(Node node, NodeView nodeView, NodeSlotView slotView) {
        for(int i = 0; i < node.getInputSlots().size(); i++) {
            if(nodeView.getInputSlots().get(i).getSlotView() == null)
                continue;

            if(nodeView.getInputSlots().get(i).getSlotView() == slotView) {
                return node.getInputSlots().get(i);
            }
        }
        for(int i = 0; i < node.getOutputSlots().size(); i++) {
            if(nodeView.getOutputSlots().get(i) == slotView) {
                return node.getOutputSlots().get(i);
            }
        }
        return null;
    }

    private NodeSlotView findViewBySlot(Node node, NodeSlot slot) {
        NodeView view = nodeToView.get(node);

        for(int i = 0; i < node.getInputSlots().size(); i++) {
            if(node.getInputSlots().get(i) == slot) {
                return view.getInputSlots().get(i).getSlotView();
            }
        }

        for(int i = 0; i < node.getOutputSlots().size(); i++) {
            if(node.getOutputSlots().get(i) == slot) {
                return view.getOutputSlots().get(i);
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
            public void onMove(AbstractNodeView view, int deltaX, int deltaY) {
                Node node = viewToNode.get(view);
                updateNodeModel(view);
            }
            @Override
            public void onDelete(AbstractNodeView view) {
                Node node = viewToNode.get(view);
                graph.removeNode(node);
            }
        });

        for(int i = 0; i < view.getInputSlots().size(); i++) {
            final NodeSlotView slotView = view.getInputSlots().get(i).getSlotView();
            final InputNodeSlot slot = newNode.getInputSlots().get(i);

            view.getInputSlots().get(i).updateValue(slot.getValue());
            view.getInputSlots().get(i).setInputNodeSlotEventListener(new InputNodeSlotEventListener() {
                @Override
                public void onValueChanged(Object newValue) {
                    slot.setValue(newValue);
                }
            });

            if(slotView != null) {
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
            }

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

        for(int i = 0; i < view.getOutputSlots().size(); i++) {
            final NodeSlotView slotView = view.getOutputSlots().get(i);
            final Node node = viewToNode.get(slotView.getNode());
            final NodeSlot slot = node.getOutputSlots().get(i);

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

    public void updateNodeView(AbstractNodeView view) {
        Node node = viewToNode.get(view);
        view.setLocation(node.getX(), node.getY());
        graphView.repaint();
    }

    public void updateNodeModel(AbstractNodeView view) {
        Node node = viewToNode.get(view);
        node.setLocation(view.getX(), view.getY());
        graphView.repaint();
    }
}
