package com.visualipcv.core;

import com.visualipcv.editor.Editor;

import java.util.UUID;

public interface IDocumentPart {
    void setName(String name);
    String getName();
    Object getSerializableProxy();
    Document getDocument();
    Document getRoot();
    UUID getId();

    default void onOpen() {

    }

    default void onClose() {
        Editor.closeWindow(null, this);
    }
}
