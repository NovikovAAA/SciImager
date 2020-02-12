package com.visualipcv.core.io;

import com.visualipcv.core.Graph;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class GraphStore {
    public void save(Graph graph, OutputStream stream) throws IOException {
        GraphEntity graphEntity = new GraphEntity(graph);
        ObjectOutputStream os = new ObjectOutputStream(stream);
        os.writeObject(graphEntity);
        os.close();
    }

    public Graph load(InputStream stream) throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(stream);
        GraphEntity entity = (GraphEntity)is.readObject();
        return new Graph(entity);
    }
}
