package com.visualipcv.controller.binding;

import com.visualipcv.controller.Controller;
import com.visualipcv.controller.NodeController;
import com.visualipcv.core.Graph;
import com.visualipcv.core.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BindingHelper {
    public static <C extends Controller<?>, M> List<C> bindList(UIProperty property, Collection<M> models, FactoryFunction<C, M> factory) {
        List<C> nodeControllers = new ArrayList<>();
        HashMap<M, C> oldNodesHash = new HashMap<>();
        List<C> oldNodes = (List<C>)property.getValue();

        if(oldNodes != null) {
            for(C node : oldNodes) {
                oldNodesHash.put(node.getContext(), node);
            }
        }

        for(M node : models) {
            if(oldNodesHash.containsKey(node)) {
                nodeControllers.add(oldNodesHash.get(node));
                oldNodesHash.get(node).invalidate();
            } else {
                try {
                    C nodeController = factory.create(node);
                    nodeControllers.add(nodeController);
                    nodeController.setContext(node);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return nodeControllers;
    }
}
