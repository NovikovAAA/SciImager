package com.visualipcv.core.io;

import com.visualipcv.core.Document;
import com.visualipcv.core.Graph;
import com.visualipcv.scripts.SciScript;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DocumentEntity implements Serializable {
    List<GraphEntity> graphList = new ArrayList<>();
    List<SciScriptEntity> scriptList = new ArrayList<>();

    public DocumentEntity(Document document) {
        for(Graph graph : document.getGraphList()) {
            graphList.add(new GraphEntity(graph));
        }

        for(SciScript sciScript : document.getScriptList()) {
            scriptList.add(new SciScriptEntity(sciScript));
        }
    }

    public List<GraphEntity> getGraphList() {
        return graphList;
    }

    public List<SciScriptEntity> getScriptList() {
        return scriptList;
    }
}
