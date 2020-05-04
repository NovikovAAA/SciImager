package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.core.io.DocumentEntity;
import com.visualipcv.editor.Editor;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DocumentManager extends Refreshable {
    private static Set<Document> openedDocuments = new HashSet<>();

    private static DocumentManager instance;

    public static DocumentManager getInstance() {
        if(instance == null)
            instance = new DocumentManager();

        return instance;
    }

    public DocumentManager() {

    }

    public static void loadDocument() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open");
        File file = chooser.showOpenDialog(Editor.getPrimaryStage().getScene().getWindow());

        if(file != null) {
            loadDocument(file);
        }

        getInstance().refresh();
    }

    public static void loadDocument(File file) {
        closeDocument(getActiveDocument());

        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
            DocumentEntity entity = (DocumentEntity)stream.readObject();
            Document document = new Document(null, entity);
            document.setFile(file);
            openedDocuments.add(document);
        } catch (IOException | ClassNotFoundException e) {
            Console.error(e);
        }

        getInstance().refresh();
    }

    public static void saveDocumentAs(Document document) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save");
        File file = chooser.showSaveDialog(Editor.getPrimaryStage().getScene().getWindow());

        if(file != null) {
            try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
                DocumentEntity entity = new DocumentEntity(document);
                stream.writeObject(entity);
                document.setFile(file);
                openedDocuments.add(document);
            } catch (IOException e) {
                Console.error(e);
            }
        }

        getInstance().refresh();
    }

    public static void saveDocument(Document document) {
        File file = document.getFile();

        if(file == null) {
            saveDocumentAs(document);
            return;
        }

        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            DocumentEntity entity = new DocumentEntity(document);
            stream.writeObject(entity);
        } catch (IOException e) {
            Console.error(e);
        }

        getInstance().refresh();
    }

    public static void saveAll() {
        for(Document document : getDocuments()) {
            saveDocument(document);
        }

        getInstance().refresh();
    }

    public static Document createDocument() {
        closeDocument(getActiveDocument());
        Document document = new Document(null);
        document.setName("New document " + (openedDocuments.size() + 1) + "*");
        openedDocuments.add(document);
        getInstance().refresh();
        return document;
    }

    public static void closeDocument(Document document) {
        if(document == null)
            return;

        openedDocuments.remove(document);
        document.onClose();
        getInstance().refresh();
    }

    public static Set<Document> getDocuments() {
        return openedDocuments;
    }

    public static Document getActiveDocument() {
        return getDocuments().stream().findAny().orElse(null);
    }
}
