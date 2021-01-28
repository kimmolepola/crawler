/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class StatusWindow {

    private JFrame frame;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private boolean autoscroll;
    private Set<String> lines;
    private String text;
    private String allText;

    public void run(String appName) {
        lines = new TreeSet<>();
        frame = new JFrame(appName);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        textArea = new JTextArea();
        textArea.setEditable(false);
        panel.add(textArea);
        scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(740, 310));
        frame.pack();
        frame.setVisible(true);
    }

    public void updateLines(Set<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String string : lines) {
            sb.append(string);
            sb.append(System.lineSeparator());
        }
        text = sb.toString();
    }

//    public void addLine(String textString) {
//        lines.add(textString);
//        StringBuilder sb = new StringBuilder();
//
//        for (String string : lines) {
//            sb.append(string);
//            sb.append(System.lineSeparator());
//        }
//        text = sb.toString();
//    }
    public void setCurrentText(String currentMessage) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (scrollPane.getVerticalScrollBar().getValue() + scrollPane.getVerticalScrollBar().getVisibleAmount() < scrollPane.getVerticalScrollBar().getMaximum() - 50) {
                    autoscroll = false;
                } else {
                    autoscroll = true;
                }
                if (textArea.getText().isEmpty()) {
                    allText = currentMessage;
                    textArea.setText(allText);
                } else {
                    allText = text + System.lineSeparator() + currentMessage;
                    textArea.setText(allText);
                }

                if (autoscroll) {
                    textArea.setCaretPosition(textArea.getDocument().getLength() - currentMessage.length());
                }
            }
        });

    }

    public void setText(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (scrollPane.getVerticalScrollBar().getValue() + scrollPane.getVerticalScrollBar().getVisibleAmount() < scrollPane.getVerticalScrollBar().getMaximum() - 50) {
                    autoscroll = false;
                } else {
                    autoscroll = true;
                }
                textArea.setText(text);
                if (autoscroll) {
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                }
            }
        });
    }
}
