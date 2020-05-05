package com.visualipcv.core.io;

import com.visualipcv.core.Document;
import com.visualipcv.core.Graph;
import com.visualipcv.core.IDocumentPart;
import com.visualipcv.scripts.SciScript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DocumentEntity implements Serializable {
    private UUID id;
    private List<Object> entities = new ArrayList<>();
    private List<String> classes = new ArrayList<>();

    public DocumentEntity(Document document) {
        this.id = document.getId();

        for(IDocumentPart part : document.getParts()) {
            Object entity = part.getSerializableProxy();
            entities.add(entity);
            classes.add(part.getClass().getTypeName());
        }
    }

    public List<Object> getEntities() {
        return entities;
    }

    public String getClassName(Object entity) {
        int index = entities.indexOf(entity);
        return classes.get(index);
    }

    public UUID getId() {
        return id;
    }
}
