package com.visualipcv.core;

import com.visualipcv.Console;
import com.visualipcv.core.io.DocumentEntity;
import com.visualipcv.scripts.SciScript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Document implements IDocumentPart {
    private UUID id;
    private Document parent;
    private List<IDocumentPart> parts = new ArrayList<>();
    private File file;
    private String name = "New document*";

    public Document(Document parent) {
        this.parent = parent;
        this.id = UUID.randomUUID();
    }

    public Document(Document parent, DocumentEntity entity) {
        this(parent);
        this.id = entity.getId();

        for(Object part : entity.getEntities()) {
            try {
                Class<?> clazz = Class.forName(entity.getClassName(part));
                IDocumentPart newPart = (IDocumentPart)clazz.getConstructor(Document.class, part.getClass()).newInstance(this, part);
                newPart.onOpen();
                parts.add(newPart);
            } catch (Exception e) {
                Console.error(e);
            }
        }
    }

    public Graph addGraph() {
        Graph graph = new Graph(this);
        parts.add(graph);
        graph.setName("New graph " + parts.size());
        DocumentManager.getInstance().refresh();
        graph.onOpen();
        return graph;
    }

    public SciScript addScript() {
        SciScript script = new SciScript(this);
        parts.add(script);
        script.setName("New script " + parts.size());
        DocumentManager.getInstance().refresh();
        script.onOpen();
        return script;
    }

    public void remove(IDocumentPart part) {
        parts.remove(part);
        DocumentManager.getInstance().refresh();
        part.onClose();
    }

    public IDocumentPart findByName(String name) {
        for(IDocumentPart part : parts) {
            if(part.getName().equals(name))
                return part;
        }

        return null;
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

    public IDocumentPart find(UUID id) {
        for(IDocumentPart part : parts) {
            if(part.getId().equals(id)) {
                return part;
            }

            if(part instanceof Document) {
                IDocumentPart res = ((Document)part).find(id);

                if(res != null)
                    return res;
            }
        }

        return null;
    }

    @Override
    public String getName() {
        return file == null ? name : file.getName();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object getSerializableProxy() {
        return new DocumentEntity(this);
    }

    @Override
    public Document getDocument() {
        return parent;
    }

    @Override
    public Document getRoot() {
        return parent == null ? this : parent.getRoot();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void onClose() {
        for(IDocumentPart part : parts) {
            part.onClose();
        }
    }

    @Override
    public void onOpen() {
        for(IDocumentPart part : parts) {
            part.onOpen();
        }
    }
}
