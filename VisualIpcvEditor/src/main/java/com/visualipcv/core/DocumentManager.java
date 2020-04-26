package com.visualipcv.core;

import com.visualipcv.core.io.DocumentEntity;
import com.visualipcv.editor.Editor;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

public class DocumentManager extends Refreshable {
    private static HashMap<Document, File> openedDocuments = new HashMap<>();

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
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
            DocumentEntity entity = (DocumentEntity)stream.readObject();
            Document document = new Document(entity);
            openedDocuments.put(document, file);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
                openedDocuments.put(document, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getInstance().refresh();
    }

    public static void saveDocument(Document document) {
        File file = openedDocuments.get(document);

        if(file == null) {
            saveDocumentAs(document);
            return;
        }

        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            DocumentEntity entity = new DocumentEntity(document);
            stream.writeObject(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getInstance().refresh();
    }

    public static void saveAll() {
        for(Document document : getDocuments()) {
            saveDocument(document);
        }

        getInstance().refresh();
    }

    public static void createDocument() {
        Document document = new Document();
        openedDocuments.put(document, null);
        getInstance().refresh();
    }

    public static String getDocumentName(Document document) {
        File file = getFile(document);

        if(file != null)
            return file.getName();

        return "New document*";
    }

    public static void closeDocument(Document document) {
        openedDocuments.remove(document);
        getInstance().refresh();
    }

    public static Set<Document> getDocuments() {
        return openedDocuments.keySet();
    }

    public static File getFile(Document document) {
        return openedDocuments.get(document);
    }
}
