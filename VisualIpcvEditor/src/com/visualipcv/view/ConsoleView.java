package com.visualipcv.view;

import com.visualipcv.Console;
import com.visualipcv.events.ConsoleEventListener;

import javax.swing.*;
import java.awt.*;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.StreamHandler;

public class ConsoleView extends JPanel {
    private JTextArea output;

    public ConsoleView() {
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane);

        output = new JTextArea();
        output.setEnabled(false);
        output.setBackground(Color.DARK_GRAY);
        scrollPane.setViewportView(output);

        Console.addEventListener(new ConsoleEventListener() {
            @Override
            public void onRecordAdded(String text) {
                output.append(text + "\n");
            }

            @Override
            public void onCmdWritten(String cmd) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }
}
