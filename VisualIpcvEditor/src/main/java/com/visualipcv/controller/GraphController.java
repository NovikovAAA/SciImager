package com.visualipcv.controller;

import com.visualipcv.Console;
import com.visualipcv.controller.binding.Binder;
import com.visualipcv.controller.binding.BindingHelper;
import com.visualipcv.controller.binding.FactoryFunction;
import com.visualipcv.controller.binding.PropertyChangedEventListener;
import com.visualipcv.controller.binding.UIProperty;
import com.visualipcv.core.Connection;
import com.visualipcv.core.DataBundle;
import com.visualipcv.core.Document;
import com.visualipcv.core.DocumentManager;
import com.visualipcv.core.Graph;
import com.visualipcv.core.GraphElement;
import com.visualipcv.core.GraphExecutionContext;
import com.visualipcv.core.GraphExecutionData;
import com.visualipcv.core.Group;
import com.visualipcv.core.Node;
import com.visualipcv.core.NodeSlot;
import com.visualipcv.core.Processor;
import com.visualipcv.core.ProcessorLibrary;
import com.visualipcv.core.ProcessorVersionMismatchException;
import com.visualipcv.core.io.ConnectionEntity;
import com.visualipcv.core.io.GraphClipboard;
import com.visualipcv.core.io.GraphEntity;
import com.visualipcv.core.io.GroupEntity;
import com.visualipcv.core.io.NodeEntity;
import com.visualipcv.editor.Editor;
import com.visualipcv.editor.EditorCommand;
import com.visualipcv.editor.EditorWindow;
import com.visualipcv.view.CustomDataFormats;
import com.visualipcv.view.FunctionListPopup;
import com.visualipcv.view.GraphView;
import com.visualipcv.view.docking.DockPos;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@EditorWindow(path = "", name = "Graph", dockPos = DockPos.CENTER, prefWidth = 1280.0, prefHeight = 720.0)
public class GraphController extends Controller<GraphView> implements INameable {
    private MouseButton selectionButton = MouseButton.PRIMARY;
    private MouseButton dragButton = MouseButton.SECONDARY;

    private double previousMouseX;
    private double previousMouseY;
    private double initialMouseX;
    private double initialMouseY;

    private ObservableList<GraphElementController<?>> nodes = FXCollections.observableArrayList();
    private ObservableList<ConnectionController> connections = FXCollections.observableArrayList();

    private UIProperty nameProperty = new UIProperty();
    private UIProperty nodesProperty = new UIProperty();
    private UIProperty connectionsProperty = new UIProperty();

    private ConnectionPreviewController connectionPreview;
    private Rectangle selectionPreview;

    private boolean wasDragged = false;

    private Semaphore semaphore = new Semaphore(1);
    private GraphExecutionContext context = new GraphExecutionContext();

    private Timeline timer = null;

    @EditorCommand(path = "Editor/New graph")
    public static void createNewGraphCommand() {
        Document doc = DocumentManager.getActiveDocument();

        if(doc == null)
            doc = DocumentManager.createDocument();

        Graph graph = doc.addGraph();
        Editor.openWindow(new GraphController(graph));
    }

    public GraphController(Graph graph) {
        super(GraphView.class);
        setContext(graph);

        nameProperty.setBinder(new Binder() {
            @Override
            public Object update(Object context) {
                return ((Graph)context).getName();
            }
        });

        nodes.addListener(new ListChangeListener<GraphElementController<?>>() {
            @Override
            public void onChanged(Change<? extends GraphElementController<?>> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(GraphElementController<?> node : c.getAddedSubList()) {
                            getView().getInternalPane().getChildren().add(node.getView());
                        }
                    }
                    if(c.wasRemoved()) {
                        for(GraphElementController<?> node : c.getRemoved()) {
                            getView().getInternalPane().getChildren().remove(node.getView());
                        }
                    }
                }
            }
        });

        connections.addListener(new ListChangeListener<ConnectionController>() {
            @Override
            public void onChanged(Change<? extends ConnectionController> c) {
                while(c.next()) {
                    if(c.wasAdded()) {
                        for(ConnectionController connection : c.getAddedSubList()) {
                            getView().getInternalPane().getChildren().add(connection.getView());
                        }
                    }
                    if(c.wasRemoved()) {
                        for(ConnectionController connection : c.getRemoved()) {
                            getView().getInternalPane().getChildren().remove(connection.getView());
                        }
                    }
                }
            }
        });

        nodesProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                nodes.clear();
                List<GraphElementController<?>> elements = (List<GraphElementController<?>>)newValue;

                elements.sort(new Comparator<GraphElementController<?>>() {
                    @Override
                    public int compare(GraphElementController<?> o1, GraphElementController<?> o2) {
                        if(o1 instanceof GroupController && o2 instanceof NodeController)
                            return -1;
                        else if(o2 instanceof GroupController && o1 instanceof NodeController)
                            return 1;
                        return 0;
                    }
                });

                nodes.addAll(elements);
            }
        });

        connectionsProperty.addEventListener(new PropertyChangedEventListener() {
            @Override
            public void onChanged(Object oldValue, Object newValue) {
                connections.clear();
                connections.addAll((List<ConnectionController>)newValue);
            }
        });

        nodesProperty.setBinder((Object g) -> {
            return BindingHelper.bindList(nodesProperty, ((Graph) g).getNodes(), new FactoryFunction<Controller<?>, GraphElement>() {
                @Override
                public Controller<?> create(GraphElement arg) {
                    if(arg instanceof Node)
                        return new NodeController(GraphController.this);
                    else
                        return new GroupController(GraphController.this);
                }
            });
        });

        connectionsProperty.setBinder((Object g) -> {
            return BindingHelper.bindList(connectionsProperty, ((Graph) g).getConnections(), (Connection connection) -> new ConnectionController(this));
        });

        initialize();

        getView().addEventFilter(DragEvent.DRAG_OVER, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(connectionPreview != null) {
                    Point2D containerPoint = getView().getInternalPane().parentToLocal(new Point2D(event.getX(), event.getY()));
                    connectionPreview.setTarget(containerPoint.getX(), containerPoint.getY());
                    connectionPreview.invalidate();
                }
            }
        });

        getView().addEventFilter(DragEvent.DRAG_DROPPED, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(connectionPreview != null) {
                    stopConnectionDrag();
                }
            }
        });

        getView().addEventFilter(DragEvent.DRAG_DONE, new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                stopConnectionDrag();
            }
        });

        getView().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.DELETE)
                    removeSelected();
                else if(event.getCode() == KeyCode.C && event.isControlDown()) {
                    if(copy()) {
                        event.consume();
                    }
                } else if(event.getCode() == KeyCode.V && event.isControlDown()) {
                    if(past())
                        event.consume();
                } else if(event.getCode() == KeyCode.X && event.isControlDown()) {
                    if(cut())
                        event.consume();
                }
            }
        });

        getView().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(wasDragged) {
                    event.consume();
                }
            }
        });

        getView().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                wasDragged = false;
            }
        });

        getView().addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == dragButton)
                    wasDragged = true;
            }
        });

        getView().addEventHandler(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        getView().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        getView().addEventHandler(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
        getView().addEventHandler(DragEvent.DRAG_OVER, this::onDragOver);
        getView().addEventHandler(DragEvent.DRAG_DROPPED, this::onDragDropped);

        timer = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(getView().getScene() == null) {
                    timer.stop();
                }

                if(semaphore.tryAcquire()) {
                    for (GraphElementController<?> node : getNodes()) {
                        if(node instanceof NodeController) {
                            ((NodeController)node).poll(((NodeController)node).errorProperty());
                        }
                    }

                    for(GraphElementController<?> node : getNodes()) {
                        if(!(node instanceof NodeController))
                            continue;

                        NodeController nodeController = (NodeController)node;
                        Object preview = getExecutionContext().load(node.getContext(), "Preview");

                        if(preview != null) {
                            if(preview instanceof Image) {
                                nodeController.setContent((Image)preview);
                            }
                        } else {
                            nodeController.setContent(null);
                        }
                    }

                    try {
                        ((Graph)getContext()).executeAsync(context, semaphore);
                    } catch (ProcessorVersionMismatchException e) {
                        invalidate();
                    } finally {
                        semaphore.release();
                    }
                }
            }
        }));

        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();

        getView().setZoom(0.75);
        invalidate();
    }

    public NodeSlotController findNodeSlotController(NodeSlot slot) {
        for(GraphElementController controller : (List<GraphElementController>)nodesProperty.getValue()) {
            if(!(controller instanceof NodeController))
                continue;

            NodeController nodeController = (NodeController)controller;

            if(nodeController.getContext() == slot.getNode()) {
                for(AdvancedNodeSlotController slotController : nodeController.getInputSlots()) {
                    if(slotController.getContext() == slot) {
                        return slotController.getSlot();
                    }
                }
                for(AdvancedNodeSlotController slotController : nodeController.getOutputSlots()) {
                    if(slotController.getContext() == slot) {
                        return slotController.getSlot();
                    }
                }
            }
        }
        return null;
    }

    public List<GraphElementController<?>> getNodes() {
        return nodes;
    }

    public List<ConnectionController> getConnections() {
        return connections;
    }

    public List<GraphElementController<?>> getSelectedNodes() {
        List<GraphElementController<?>> selectedNodes = new ArrayList<>();

        for(GraphElementController<?> node : nodes) {
            if(node.isSelected()) {
                selectedNodes.add(node);
            }
        }

        return selectedNodes;
    }

    public GraphExecutionContext getExecutionContext() {
        return context;
    }

    public void startConnectionDrag(NodeSlotController source) {
        connectionPreview = new ConnectionPreviewController(source);
        connectionPreview.setContext(source.getContext());
        getView().getInternalPane().getChildren().add(connectionPreview.getView());
    }

    public void stopConnectionDrag() {
        if(connectionPreview != null) {
            getView().getInternalPane().getChildren().remove(connectionPreview.getView());
            connectionPreview = null;
        }
    }

    public void onMousePressed(MouseEvent event) {
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        Point2D initial = getView().getInternalPane().parentToLocal(event.getX(), event.getY());
        initialMouseX = initial.getX();
        initialMouseY = initial.getY();

        if(event.getButton() == selectionButton) {
            selectionPreview = new Rectangle();
            selectionPreview.setX(initialMouseX);
            selectionPreview.setY(initialMouseY);
            selectionPreview.setFill(new Color(1.0, 1.0, 1.0, 0.5));
            selectionPreview.setStroke(new Color(0, 0, 0, 1));
            getView().getInternalPane().getChildren().add(selectionPreview);

            clearSelection();
            event.consume();
        }
    }

    public void onMouseDragged(MouseEvent event) {
        double deltaX = event.getScreenX() - previousMouseX;
        double deltaY = event.getScreenY() - previousMouseY;
        previousMouseX = event.getScreenX();
        previousMouseY = event.getScreenY();

        if(selectionPreview != null && event.getButton() == selectionButton) {
            Point2D newPos = getView().getInternalPane().parentToLocal(event.getX(), event.getY());

            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            selectionPreview.setWidth(maxX - minX);
            selectionPreview.setHeight(maxY - minY);
            selectionPreview.setX(minX);
            selectionPreview.setY(minY);

            event.consume();
        }
    }

    public List<GraphElementController<?>> getNodesInArea(double minX, double minY, double maxX, double maxY) {
        List<GraphElementController<?>> nodes = new ArrayList<>();

        for(GraphElementController<?> node : this.nodes) {
            if(node.getView().getLayoutX() >= minX && node.getView().getLayoutX() + node.getView().getWidth() < maxX &&
                    node.getView().getLayoutY() >= minY && node.getView().getLayoutY() + node.getView().getHeight() < maxY) {
                nodes.add(node);
            }
        }

        return nodes;
    }

    public void onMouseReleased(MouseEvent event) {
        if(event.getButton() == selectionButton && selectionPreview != null) {
            getView().getInternalPane().getChildren().remove(selectionPreview);
            selectionPreview = null;

            Point2D newPos = getView().getInternalPane().parentToLocal(event.getX(), event.getY());
            double minX = Math.min(initialMouseX, newPos.getX());
            double maxX = Math.max(initialMouseX, newPos.getX());
            double minY = Math.min(initialMouseY, newPos.getY());
            double maxY = Math.max(initialMouseY, newPos.getY());

            for(GraphElementController<?> node : getNodesInArea(minX, minY, maxX, maxY)) {
                node.setSelected(true);
            }
        }

        if(event.getButton() == dragButton) {
            Point2D p = getView().getInternalPane().parentToLocal(event.getX(), event.getY());
            FunctionListPopup popup = new FunctionListPopup(this, p.getX(), p.getY(), event.getScreenX(), event.getScreenY());
            popup.show();
        }

        event.consume();
    }

    private Processor getProcessorFromDragEvent(DragEvent event) {
        Dragboard db = event.getDragboard();

        if(!db.hasString())
            return null;

        String content = db.getString();
        String[] params = content.split("/");

        if(params.length != 2)
            return null;

        String module = params[0];
        String name = params[1];

        return ProcessorLibrary.findProcessor(module, name);
    }

    public void onDragOver(DragEvent event) {
        if(getProcessorFromDragEvent(event) != null)
            event.acceptTransferModes(TransferMode.ANY);
    }

    public void onDragDropped(DragEvent event) {
        Processor processor = getProcessorFromDragEvent(event);

        if(processor == null) {
            event.setDropCompleted(false);
            return;
        }

        try {
            double x = event.getX();
            double y = event.getY();
            Point2D p = getView().getInternalPane().getLocalToParentTransform().inverseTransform(x, y);

            ((Graph)getContext()).addNode(new Node(getContext(), processor, p.getX(), p.getY()));
            poll(nodesProperty);

            event.setDropCompleted(true);
        } catch(Exception e) {
            Console.error(e);
            event.setDropCompleted(false);
        }
    }

    public void moveSelected(double deltaX, double deltaY) {
        List<GraphElementController<?>> nodesForMove = getSelectedNodes();

        for(GraphElementController<?> node : getSelectedNodes()) {
            if(node instanceof GroupController) {
                GroupController group = (GroupController)node;
                nodesForMove.addAll(getNodesInArea(
                        group.getView().getLayoutX(),
                        group.getView().getLayoutY(),
                        group.getView().getWidth() + group.getView().getLayoutX(),
                        group.getView().getHeight() + group.getView().getLayoutY()));
            }
        }

        for(GraphElementController<?> node : nodesForMove) {
            double x = node.getView().getLayoutX();
            double y = node.getView().getLayoutY();
            x += deltaX / getView().getZoom();
            y += deltaY / getView().getZoom();
            node.moveTo(x, y);
        }
    }

    public void removeNode(GraphElementController<?> node) {
        ((Graph)getContext()).removeNode(node.getContext());
        invalidate();
    }

    public void removeSelected() {
        for(GraphElementController<?> node : getSelectedNodes()) {
            removeNode(node);
        }

        clearSelection();
    }

    public void clearSelection() {
        for(GraphElementController<?> node : nodes) {
            node.setSelected(false);
        }
    }

    public void groupSelected() {
        if(getSelectedNodes().isEmpty())
            return;

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

        for(GraphElementController<?> node : getSelectedNodes()) {
            minX = Math.min(minX, node.getView().getLayoutX());
            minY = Math.min(minY, node.getView().getLayoutY());
            maxX = Math.max(maxX, node.getView().getLayoutX() + node.getView().getWidth());
            maxY = Math.max(maxY, node.getView().getLayoutY() + node.getView().getHeight());
        }

        Group group = new Group(getContext(), minX - 10.0, minY - 40.0, maxX - minX + 20.0, maxY - minY + 60.0);
        ((Graph)getContext()).addNode(group);
        invalidate();
    }

    private GraphClipboard createGraphClipboardFromSelection() {
        List<Node> nodes = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Connection> connections = new ArrayList<>();

        for(GraphElementController<?> node : this.nodes) {
            if(node.isSelected()) {
                if(node instanceof NodeController)
                    nodes.add(node.getContext());
                else
                    groups.add(node.getContext());
            }
        }

        Set<Node> nodesHash = new HashSet<>(nodes);

        for(Connection connection : ((Graph)getContext()).getConnections()) {
            if(nodesHash.contains(connection.getSource().getNode()) && nodesHash.contains(connection.getTarget().getNode()))
                connections.add(connection);
        }

        return new GraphClipboard(nodes, connections, groups);
    }

    public boolean copy(GraphClipboard graphClipboard) {
        if(graphClipboard.getNodes().isEmpty())
            return false;

        Clipboard clipboard = Clipboard.getSystemClipboard();
        GraphEntity entity = new GraphEntity(graphClipboard);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        for(NodeEntity node : entity.getNodes()) {
            node.addOffset(20.0, 20.0);
        }

        try (ObjectOutputStream stream = new ObjectOutputStream(byteStream)) {
            stream.writeObject(entity);
            stream.flush();

            byte[] data = byteStream.toByteArray();
            ClipboardContent content = new ClipboardContent();
            content.put(CustomDataFormats.OCTET_STREAM, data);
            clipboard.setContent(content);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean copy() {
        return copy(createGraphClipboardFromSelection());
    }

    public boolean cut() {
        GraphClipboard clipboard = createGraphClipboardFromSelection();
        boolean res = copy(clipboard);

        for(GraphElementController<?> node : getSelectedNodes())
            removeNode(node);

        return res;
    }

    public boolean past() {
        ByteArrayInputStream byteStream;
        byte[] bytes;

        try {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            bytes = (byte[])clipboard.getContent(CustomDataFormats.OCTET_STREAM);
        } catch(Exception e) {
            return false;
        }

        byteStream = new ByteArrayInputStream(bytes);

        try (ObjectInputStream stream = new ObjectInputStream(byteStream)) {
            GraphEntity entity = (GraphEntity)stream.readObject();
            Graph graph = getContext();
            Map<UUID, UUID> uidMapping = new HashMap<>();

            clearSelection();
            Set<Node> nodesToSelect = new HashSet<>();

            for(NodeEntity node : entity.getNodes()) {
                UUID oldId = node.getId();
                node.resetUID();
                UUID newId = node.getId();

                Node newNode = new Node(graph, node);
                graph.addNode(newNode);
                uidMapping.put(oldId, newId);
                nodesToSelect.add(newNode);
            }

            for(ConnectionEntity connection : entity.getConnections()) {
                UUID newSourceId = uidMapping.get(connection.getSourceNodeId());
                UUID newTargetId = uidMapping.get(connection.getTargetNodeId());
                connection.updateUIDs(newSourceId, newTargetId);
                graph.addConnection(new Connection(graph, connection));
            }

            stream.close();
            invalidate();

            for(GraphElementController<?> controller : nodes) {
                if(nodesToSelect.contains(controller.<Node>getContext())) {
                    controller.setSelected(true);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }

        return true;
    }

    public void select(GraphElementController<?> node, boolean ctrlDown) {
        if(!node.isSelected() && !ctrlDown)
            clearSelection();

        node.setSelected(true);
    }

    @Override
    public UIProperty nameProperty() {
        return nameProperty;
    }
}
