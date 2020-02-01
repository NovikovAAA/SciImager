javaclasspath(strcat([pwd(), '/VisualIpcvEditor/out/artifacts/VisualIpcvEditor/VisualIpcvEditor.jar']));
jimport com.visualipcv.Main;
jinvoke(Main, 'main', jarray('java.lang.String', 1));
