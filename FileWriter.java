/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileWriter {

    public static boolean write(String filename, List<String> lines) {
        Path file = Paths.get(filename);
        try {
            //Files.write(file, lines, Charset.forName("ISO-8859-1"));
            Files.write(file, lines, Charset.forName("UTF-8"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
