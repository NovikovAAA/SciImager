package com.visualipcv.core;

import com.visualipcv.core.io.DocumentEntity;
import com.visualipcv.core.io.GraphEntity;
import com.visualipcv.core.io.SciScriptEntity;
import com.visualipcv.scripts.SciScript;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private List<Graph> graphList = new ArrayList<>();
    private List<SciScript> scriptList = new ArrayList<>();

    public Document() {

    }

    public Document(DocumentEntity entity) {
        for(GraphEntity graph : entity.getGraphList()) {
            graphList.add(new Graph(graph));
        }

        for(SciScriptEntity script : entity.getScriptList()) {
            scriptList.add(new SciScript(script));
        }
    }

    public Graph addGraph() {
        Graph graph = new Graph();
        graphList.add(graph);
        graph.setName("New graph " + graphList.size());
        DocumentManager.getInstance().refresh();
        return graph;
    }

    public SciScript addScript() {
        SciScript script = new SciScript();
        scriptList.add(script);
        script.setName("New script " + scriptList.size());
        DocumentManager.getInstance().refresh();
        return script;
    }

    public List<Graph> getGraphList() {
        return graphList;
    }

    public List<SciScript> getScriptList() {
        return scriptList;
    }
}
