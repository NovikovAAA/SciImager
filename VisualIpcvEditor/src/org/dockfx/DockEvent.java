package org.dockfx;

import com.sun.javafx.scene.input.InputEventUtils;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.PickResult;

public class DockEvent extends Event {
    private static final long serialVersionUID = 4413700316447127355L;
    public static final EventType<DockEvent> ANY = new EventType<DockEvent>(Event.ANY, "DOCK");
    public static final EventType<DockEvent> DOCK_ENTER =
            new EventType<DockEvent>(DockEvent.ANY, "DOCK_ENTER");

    public static final EventType<DockEvent> DOCK_OVER =
            new EventType<DockEvent>(DockEvent.ANY, "DOCK_OVER");

    public static final EventType<DockEvent> DOCK_EXIT =
            new EventType<DockEvent>(DockEvent.ANY, "DOCK_EXIT");

    public static final EventType<DockEvent> DOCK_RELEASED =
            new EventType<DockEvent>(DockEvent.ANY, "DOCK_RELEASED");

    private transient double x;

    public final double getX() {
        return x;
    }

    private transient double y;

    public final double getY() {
        return y;
    }

    private transient double z;

    public final double getZ() {
        return z;
    }

    private final double screenX;

    public final double getScreenX() {
        return screenX;
    }

    private final double screenY;

    public final double getScreenY() {
        return screenY;
    }

    private final double sceneX;

    public final double getSceneX() {
        return sceneX;
    }

    private final double sceneY;

    public final double getSceneY() {
        return sceneY;
    }

    private PickResult pickResult;

    public final PickResult getPickResult() {
        return pickResult;
    }

    private Node contents;

    public final Node getContents() {
        return contents;
    }

    public DockEvent(EventType<? extends DockEvent> eventType, double x, double y, double screenX,
                     double screenY, PickResult pickResult) {
        this(null, null, eventType, x, y, screenX, screenY, pickResult);
    }

    public DockEvent(Object source, EventTarget target, EventType<? extends DockEvent> eventType,
                     double x, double y, double screenX, double screenY, PickResult pickResult) {
        this(source, target, eventType, x, y, screenX, screenY, pickResult, null);
    }

    public DockEvent(Object source, EventTarget target, EventType<? extends DockEvent> eventType,
                     double x, double y, double screenX, double screenY, PickResult pickResult, Node contents) {
        super(source, target, eventType);
        this.x = x;
        this.y = y;
        this.screenX = screenX;
        this.screenY = screenY;
        this.sceneX = x;
        this.sceneY = y;
        this.pickResult = pickResult != null ? pickResult : new PickResult(target, x, y);
        final Point3D p = InputEventUtils.recomputeCoordinates(this.pickResult, null);
        this.x = p.getX();
        this.y = p.getY();
        this.z = p.getZ();
        this.contents = contents;
    }
}
