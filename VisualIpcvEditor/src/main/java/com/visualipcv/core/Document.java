package com.visualipcv.core;

import com.visualipcv.core.io.DocumentEntity;
import com.visualipcv.scripts.SciScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Document implements IDocumentPart {
    private List<IDocumentPart> parts = new ArrayList<>();
    private File file;
    private String name = "New document*";

    public Document() {

    }

    public Document(DocumentEntity entity) {
        for(Object part : entity.getEntities()) {
            try {
                Class<?> clazz = Class.forName(entity.getClassName(part));
                parts.add((IDocumentPart)clazz.getConstructor(part.getClass()).newInstance(part));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Graph addGraph() {
        Graph graph = new Graph();
        parts.add(graph);
        graph.setName("New graph " + parts.size());
        DocumentManager.getInstance().refresh();
        return graph;
    }

    public SciScript addScript() {
        SciScript script = new SciScript();
        parts.add(script);
        script.setName("New script " + parts.size());
        DocumentManager.getInstance().refresh();
        return script;
    }

    public List<IDocumentPart> getParts() {
        return parts;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return file == null ? name : file.getName();
    }

    @Override
    public Object getSerializableProxy() {
        return new DocumentEntity(this);
    }
}
