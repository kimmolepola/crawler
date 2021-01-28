/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.net.ssl.SSLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;

public class PageGetter {

    public static Page get(String url, String base, String host, Map<String, Page> allPages) {
        if (url.substring(url.length() - 3).equals("pdf")) {
            Page page = new Page(url, 997, "Not HTML: (PDF)", allPages);
            return page;
        }
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        WebClient webClient = new WebClient(BrowserVersion.CHROME, "<URL>", "<PORT>"); // replace <URL> and <PORT> with url string and port int
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        Object o;
        try {
            o = webClient.getPage(url);
        } catch (SSLException sslexception) {
            Page page = new Page(url, 998, "SSL Exception", allPages);
            return page;
        } catch (IOException ioexception) {
            Page page = new Page(url, 996, "IO Exception", allPages);
            return page;
        } catch (IllegalArgumentException illegalArgumentException){
            Page page = new Page(url, 995, illegalArgumentException.getMessage(), allPages);
            page.setErrorMessage(illegalArgumentException.getMessage());
            return page;
        } catch (FailingHttpStatusCodeException failingHttpStatusCodeException){
            Page page = new Page(url, 994, failingHttpStatusCodeException.getMessage(), allPages);
            page.setErrorMessage(failingHttpStatusCodeException.getMessage());
            return page;
        }
        if (!(o instanceof HtmlPage)) {
            Page page = new Page(url, 999, "Not HTML", allPages);
            return page;
        }

        HtmlPage htmlPage = (HtmlPage) o;
        int statusCode = htmlPage.getWebResponse().getStatusCode();
        String statusMessage = htmlPage.getWebResponse().getStatusMessage();
        Page page = new Page(url, statusCode, statusMessage, allPages);

        if (statusCode == 200 && checkBase(url, base)) {
            List<HtmlDivision> shortcuts = htmlPage.getByXPath("//div[contains(@class, 'shortcut')]");
            int shortcutsAmount = 0;

            for (HtmlDivision hd : shortcuts) {
                shortcutsAmount += hd.getElementsByTagName("a").size();
            }

            page.setShortcutsAmount(shortcutsAmount);
            page.setInner(true);
            page.setDepth(StringUtils.countMatches(url, '/') - 3);
            List<HtmlAnchor> anchors = htmlPage.getAnchors();

            for (HtmlAnchor anchor : anchors) {
                String name = anchor.getTextContent();
                String address = anchor.getHrefAttribute();

                if (address.length() > 0) {
                    if (address.charAt(0) == '/') {
                        address = host + address;
                    }
                    if (address.charAt(0) == 'h') {
                        name = name.replaceAll("(\\r|\\n|\\r\\n)+", "").trim();
                        if (name.equals("")) {
                            name = "-";
                        }
                        page.addLinkName(name);
                        page.addLinkUrl(address);
                        if (name.equals("Svenska") && !base.equals("other page")) {
                            page.setOtherPageUrl(address);
                        }
                    }
                }
            }
        }
        return page;
    }

    private static boolean checkBase(String url, String base) {
        if (base.equals("other page link")){
            return false;
        }
        if (base.equals("other page")) {
            return true;
        }
        if (url.length() < base.length()) {
            return false;
        }
        return url.substring(0, base.length()).equals(base);
    }
}
