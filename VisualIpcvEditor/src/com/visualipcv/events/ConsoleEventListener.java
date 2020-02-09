package com.visualipcv.events;

public interface ConsoleEventListener {
    void onRecordAdded(String text);
    void onCmdWritten(String cmd);
    void onResponse(String response);
}
