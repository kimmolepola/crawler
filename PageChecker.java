/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageChecker {

    public static void checkOtherPages(Set<String> allUrls, Map<String, Page> allPages, StatusWindow statusWindow, Set<String> lines, List<Page> otherPages) {
        for (Page otherPage : otherPages) {
            for (String oneUrl : otherPage.getLinkUrls()) {
                if (!allPages.containsKey(oneUrl)) {
                    statusWindow.setCurrentText("Tarkastetaan: " + oneUrl);
                    allUrls.add(oneUrl);
                    Page otherPageLinkPage = PageGetter.get(oneUrl, "other page link", "irrelevant", allPages);
                    allPages.put(oneUrl, otherPageLinkPage);
                    buildText(lines, otherPageLinkPage, otherPageLinkPage.getAddress());
                    statusWindow.updateLines(lines);
                }
            }
        }
    }

    public static Page check(String url, Set<String> allUrls, List<Page> pages, String base, StatusWindow statusWindow, String host, Map<String, Page> allPages, Set<String> lines, List<Page> otherPages) {
        statusWindow.setCurrentText("Tarkastetaan: " + url);
        Page page = PageGetter.get(url, base, host, allPages);
        allPages.put(url, page);
        buildText(lines, page, url);
        statusWindow.updateLines(lines);
        if (page.getOtherPageUrl() != null) {
            if (allPages.containsKey(page.getOtherPageUrl())) {
                Page otherPage = allPages.get(page.getOtherPageUrl());
                page.setOtherPage(otherPage);
                otherPage.setOther(true);
                otherPage.setInner(true);
            } else {
                statusWindow.setCurrentText("Tarkastetaan: " + page.getOtherPageUrl());
                Page otherPage = PageGetter.get(page.getOtherPageUrl(), "other page", host, allPages);
                page.setOtherPage(otherPage);
                otherPage.setOther(true);
                allPages.put(otherPage.getAddress(), otherPage);
                allUrls.add(otherPage.getAddress());
                buildText(lines, otherPage, otherPage.getAddress());
                statusWindow.updateLines(lines);
                otherPages.add(otherPage);
            }
        }

        if (url.length() >= base.length()) {
            if (url.substring(0, base.length()).equals(base)) {
                for (String oneUrl : page.getLinkUrls()) {
                    if (!allUrls.contains(oneUrl)) {
                        allUrls.add(oneUrl);
                        pages.add(check(oneUrl, allUrls, pages, base, statusWindow, host, allPages, lines, otherPages));
                    }
                }
            }
        }
        return page;
    }

    private static void buildText(Set<String> lines, Page page, String url) {
        if (page.getStatusCode() == 999) {
            lines.add("Ei tarkastettu, syy: ei HTML - " + url);
        } else if (page.getStatusCode() == 998) {
            lines.add("Ei tarkastettu, syy: SSL Exception - " + url);
        } else if (page.getStatusCode() == 997) {
            lines.add("Ei tarkastettu, syy: ei HTML: (PDF) - " + url);
        } else if (page.getStatusCode() == 996) {
            lines.add("Ei tarkastettu, syy: IO Exception - " + url);
        } else if (page.getStatusCode() == 995) {
            lines.add("Ei tarkastettu, syy: IllegalArgumentException: " + page.getErrorMessage() + " - " + url);
        } else if (page.getStatusCode() == 994) {
            lines.add("Ei tarkastettu, syy: FailingHttpStatusCodeException: " + page.getErrorMessage() + " - " + url);
        } else {
            String serverMessage = "";
            String otherServerMessage = "";
            if (page.getStatusCode() == 200) {
                serverMessage = page.getStatusMessage();
            } else {
                serverMessage = page.getStatusCode() + " " + page.getStatusMessage();
            }
            if (page.getOtherPage() == null) {
                lines.add("Palvelin: "
                        + serverMessage
                        + " - "
                        + url);
            } else {
                if (page.getOtherPage().getStatusCode() == 200) {
                    otherServerMessage = page.getOtherPage().getStatusMessage();
                } else {
                    otherServerMessage = page.getOtherPage().getStatusCode() + " " + page.getOtherPage().getStatusMessage();
                }
                lines.add("Palvelin fi/sv: "
                        + serverMessage
                        + "/"
                        + otherServerMessage
                        + " - Linkit fi/sv: "
                        + (page.getLinkUrls().size() - page.getShortcutsAmount())
                        + "/"
                        + (page.getOtherPage().getLinkUrls().size() - page.getOtherPage().getShortcutsAmount())
                        + " - "
                        + url);
            }
        }
    }
}
