/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

public class Runner {

    public Runner(String appName) {
        JTextField textField = new JTextField();
        JTextFieldRegularPopupMenu.addTo(textField);
        StartWindow startWindow = new StartWindow();
        String startUrl = "<URL>"; // replace <URL> with the web address

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = textField.getText();
                if (validate(url)) {
                    startWindow.close();
                    Thread pageCheckThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StatusWindow statusWindow = new StatusWindow();
                            statusWindow.run(appName);
                            runPageCheck(url, statusWindow);
                        }
                    });
                    pageCheckThread.start();
                } else {
                    startWindow.setErrorText("Osoite ei kelvollinen");
                }
            }
        };
        startWindow.run(textField, al, startUrl, appName);

    }

    private boolean validate(String url) {
        try {
            String s = url.split("/")[0] + "/" + url.split("/")[1] + "/" + url.split("/")[2];
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void runPageCheck(String url, StatusWindow statusWindow) {
        //System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());

        String host = url.split("/")[0] + "/" + url.split("/")[1] + "/" + url.split("/")[2];
        List<Page> pages = new ArrayList<>();
        Set<String> allUrls = new HashSet<>();
        Map<String, Page> allPages = new HashMap<>();
        allUrls.add(url);
        Set<String> lines = new TreeSet<>();
        List<Page> otherPages = new ArrayList<>();

        pages.add(PageChecker.check(url, allUrls, pages, url, statusWindow, host, allPages, lines, otherPages));
        PageChecker.checkOtherPages(allUrls, allPages, statusWindow, lines, otherPages);

        Map<String, Page> allPages2 = null;
        String url2 = null;
        Set<String> lines2 = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String time = LocalDateTime.now().format(formatter);

        try {
            FileOutputStream f = new FileOutputStream(new File("Data-" + time + ".txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(allPages);
            o.writeObject(url);
            o.writeObject(lines);

            o.close();
            f.close();

            FileInputStream fi = new FileInputStream(new File("Data-" + time + ".txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            allPages2 = (Map<String, Page>) oi.readObject();
            url2 = (String) oi.readObject();
            lines2 = (Set<String>) oi.readObject();

            oi.close();
            fi.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
        }

        String filename = "Result-" + time + ".txt";
        List<String> summaryLines = SummaryBuilder.build(allPages2, url2, filename);

        StringBuilder sb = new StringBuilder();
        List<String> allLines = new ArrayList<>();
        allLines.addAll(lines2);
        allLines.add("");
        allLines.addAll(summaryLines);
        for (String line : allLines) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        statusWindow.setText(sb.toString());
        FileWriter.write(filename, allLines);
    }
}
