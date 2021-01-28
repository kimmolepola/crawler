/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SummaryBuilder {

    public static List<String> build(Map<String, Page> allPages, String startUrl, String filename) {
        int linkAmount = allPages.size();
        int fiAmount = 0;
        int svAmount = 0;
        Set<Page> sameLinkAmount = new TreeSet<>();
        Set<Page> sameLinkAmountExceptShortcuts = new TreeSet<>();
        Set<Page> differentLinkAmount = new TreeSet<>();
        Set<Page> noOtherPage = new TreeSet<>();

        for (Page page : allPages.values()) {
            if (page.getInner()) {
                if (page.getOther()) {
                    svAmount++;
                } else {
                    fiAmount++;
                    if (page.getOtherPage() != null) {
                        if (page.getLinkUrls().size() == page.getOtherPage().getLinkUrls().size()) {
                            sameLinkAmount.add(page);
                        } else if (page.getLinkUrls().size() - page.getShortcutsAmount() == page.getOtherPage().getLinkUrls().size() - page.getOtherPage().getShortcutsAmount()) {
                            sameLinkAmountExceptShortcuts.add(page);
                        } else {
                            differentLinkAmount.add(page);
                        }
                    } else {
                        noOtherPage.add(page);
                    }
                }
            }
        }

        int errorLinkAmount = 0;
        Set<Page> errorLinkPages = new TreeSet<>();
        for (Page page : allPages.values()) {
            page.updateErrorLinkInfo();
            if (page.getErrorLinkInfo().size() != 0) {
                errorLinkPages.add(page);
                errorLinkAmount += page.getErrorLinkInfo().size();
            }
        }

        List<String> lines = new ArrayList<>();
        int total = fiAmount + svAmount;
        lines.add("");
        lines.add("");
        lines.add("*** Sivujen tarkastus valmis ***");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("Tarkastettu sivu " + startUrl + " ja sen alasivut.");
        lines.add("");
        lines.add("Sivuja yhteensä " + total + " kpl");
        lines.add("Näistä suomenkielisiä " + fiAmount + " kpl ja ruotsinkielisiä " + svAmount + " kpl");
        lines.add("Tarkastettuja linkkejä yhteensä " + linkAmount + " kpl");
        lines.add("Sivut, joilla sama määrä linkkejä suomen- ja ruotsinkielisellä puolella: " + sameLinkAmount.size() + " kpl");
        lines.add("Sivut, joilla eri määrä \"oikopolut\"-linkkejä suomen- ja ruotsinkielisellä puolella, mutta muutoin sama määrä linkkejä: " + sameLinkAmountExceptShortcuts.size() + " kpl");
        lines.add("Sivut, joilla eri määrä linkkejä suomen- ja ruotsinkielisellä puolella: " + differentLinkAmount.size() + " kpl");
        lines.add("Sivut, joilla ei ruotsinkielistä vastinetta: " + noOtherPage.size() + " kpl");
        lines.add("Rikkinäisiä linkkejä " + errorLinkAmount + " kpl");
        lines.add("");
        if (sameLinkAmount.size() > 0) {
            lines.add("Sivut, joilla sama määrä linkkejä suomen- ja ruotsinkielisellä puolella:");
            for (Page page : sameLinkAmount) {
                lines.add("- " + page.getAddress() + " - Linkit fi/sv: " + page.getLinkUrls().size() + "/" + page.getOtherPage().getLinkUrls().size());
            }
            lines.add("");
        }
        if (sameLinkAmountExceptShortcuts.size() > 0) {
            lines.add("Sivut, joilla eri määrä \"oikopolut\"-linkkejä suomen- ja ruotsinkielisellä puolella, mutta muutoin sama määrä linkkejä:");
            for (Page page : sameLinkAmountExceptShortcuts) {
                lines.add("- " + page.getAddress() + " - Linkit fi/sv: " + page.getLinkUrls().size() + "/" + page.getOtherPage().getLinkUrls().size());
            }
            lines.add("");
        }
        if (differentLinkAmount.size() > 0) {
            lines.add("Sivut, joilla eri määrä linkkejä suomen- ja ruotsinkielisellä puolella:");
            for (Page page : differentLinkAmount) {
                lines.add("- " + page.getAddress() + " - Linkit fi/sv: " + page.getLinkUrls().size() + "/" + page.getOtherPage().getLinkUrls().size());
            }
            lines.add("");
        }
        if (noOtherPage.size() > 0) {
            lines.add("Sivut, joilla ei ruotsinkielistä vastinetta:");
            for (Page page : noOtherPage) {
                lines.add("- " + page.getAddress() + " - Linkit fi: " + page.getLinkUrls().size());
            }
            lines.add("");
        }
        if (!errorLinkPages.isEmpty()) {
            lines.add("Sivut, joilla rikkinäisiä linkkejä:");
            for (Page page : errorLinkPages) {
                lines.add(page.getAddress());
                for (String line : page.getErrorLinkInfo()) {
                    lines.add("- " + line);
                }
                lines.add("");
            }
        } 
        lines.add("");
        lines.add("");
        lines.add("-- loppu --");
        lines.add("");
        lines.add("");
        lines.add("");
        lines.add("Tiedosto luotu: " + filename);
        lines.add("");
        return lines;
    }
}
