/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class StartWindow {

    private JTextArea textArea2;
    private JFrame frame;

    public void close() {
        frame.dispose();
    }

    public void run(JTextField textField, ActionListener al, String startUrl, String appName) {

        frame = new JFrame(appName);
        frame.setPreferredSize(new Dimension(740, 310));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JPanel textAreaPanel = new JPanel();
        setupTextAreaPanel(textAreaPanel);

        JPanel textFieldPanel = new JPanel();
        setupTextFieldPanel(textFieldPanel, textField, startUrl);

        JPanel buttonPanel = new JPanel();
        setupButtonPanel(buttonPanel, al);

        panel.add(textAreaPanel);
        panel.add(textFieldPanel);
        panel.add(buttonPanel);


        panel.setBorder(new EmptyBorder(10, 10, 10, 0));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void setErrorText(String text) {
        textArea2.setText(text);
    }

    private void setupTextFieldPanel(JPanel panel, JTextField textField, String startUrl) {
        textField.setText(startUrl);
        textField.setPreferredSize(new Dimension(650, 30));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(textField);
    }

    private void setupButtonPanel(JPanel panel, ActionListener al) {
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        Button button = new Button("OK");
        button.addActionListener(al);
        panel.add(button);
        panel.add(createEmptyPanel(10));
        setupTextArea2();
        panel.add(textArea2);
    }

    private void setupTextAreaPanel(JPanel panel) {
        panel.setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setText(System.lineSeparator() + "Ohjelma tarkastaa syöttämäsi verkkosivun ja sen alasivut sekä sivujen mahdolliset ruotsinkieliset vastineet. Tarkastuksessa testataan linkkien toimivuus ja linkkien määrien vastaavuus suomen- ja ruotsinkielisillä sivuilla. Ohjelma on tehty evira.fi-verkkosivuja varten." + System.lineSeparator() + System.lineSeparator() + "Syötä verkkosivun osoite, josta haluat aloittaa tarkastuksen:" + System.lineSeparator());
        setupTextArea(panel, textArea);
        panel.add(textArea, BorderLayout.NORTH);
    }

    private void setupTextArea2() {
        textArea2 = new JTextArea();
        textArea2.setText("");
        textArea2.setFont(textArea2.getFont().deriveFont(16f));
        textArea2.setEditable(false);
        textArea2.setForeground(Color.red);
    }

    private JPanel createEmptyPanel(int size) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(size, size));
        return panel;
    }

    private void setupTextArea(JPanel panel, JTextArea textArea) {
        textArea.setFont(textArea.getFont().deriveFont(14f));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(panel.getBackground());
    }
}
