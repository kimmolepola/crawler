/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sivunlukija;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Page implements Comparable<Page>, Serializable {

    private final String address;
    private String linkName;
    private final int statusCode;
    private final String statusMessage;
    private final List<String> linkNames;
    private final List<String> linkUrls;
    private int depth;
    private final Map<String, Page> allPages;
    private String otherPageUrl;
    private Page otherPage;
    private final List<String> errorLinkInfo;
    private boolean other;
    private boolean inner;
    private int shortcutsAmount;
    private String errorMessage;

    public Page(String addressString, int statusCodeInt, String statusMessageString, Map<String, Page> allPagesMap) {
        address = addressString;
        statusCode = statusCodeInt;
        statusMessage = statusMessageString;
        linkNames = new ArrayList<>();
        linkUrls = new ArrayList<>();
        depth = -1;
        allPages = allPagesMap;
        errorLinkInfo = new ArrayList<>();
        other = false;
        inner = false;
        shortcutsAmount = 0;
    }
    
    public String getErrorMessage(){
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessageString){
        errorMessage = errorMessageString;
    }

    public int getShortcutsAmount() {
        return shortcutsAmount;
    }

    public void setShortcutsAmount(int a) {
        shortcutsAmount = a;
    }

    public boolean getInner() {
        return inner;
    }

    public void setInner(boolean b) {
        inner = b;
    }

    public void updateErrorLinkInfo() {
        for (int i = 0; i < linkUrls.size(); i++) {
            String url = linkUrls.get(i);
            Page linkPage = allPages.get(url);
            if (linkPage.getStatusCode() != 200 && linkPage.getStatusCode() != 999 && linkPage.getStatusCode() != 998 && linkPage.getStatusCode() != 997) {
                errorLinkInfo.add(linkNames.get(i) + " - " + url + " - " + "palvelimen status: " + linkPage.getStatusCode() + " " + linkPage.getStatusMessage());
            }
        }
    }

    public boolean getOther() {
        return other;
    }

    public void setOther(boolean b) {
        other = b;
    }

    public List<String> getErrorLinkInfo() {
        return errorLinkInfo;
    }

    public Page getOtherPage() {
        return otherPage;
    }

    public void setOtherPage(Page page) {
        otherPage = page;
    }

    public String getOtherPageUrl() {
        return otherPageUrl;
    }

    public void setOtherPageUrl(String url) {
        otherPageUrl = url;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setDepth(int depthInt) {
        depth = depthInt;
    }

    public int getDepth() {
        return depth;
    }

    public void addLinkName(String name) {
        linkNames.add(name);
    }

    public void addLinkUrl(String url) {
        linkUrls.add(url);
    }

    public List<String> getLinkNames() {
        return linkNames;
    }

    public List<String> getLinkUrls() {
        return linkUrls;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int compareTo(Page p) {
        //return Integer.compare(address.hashCode(), p.getAddress().hashCode());
        return this.address.compareTo(p.getAddress());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Page)) {
            return false;
        }
        Page p = (Page) o;
        return address.equals(p.getAddress());
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Osoite: ");
        sb.append(address);
        sb.append(System.lineSeparator());
        if (statusCode != 999 && statusCode != 998) {
            sb.append("Palvelimen status: ");
            sb.append(statusMessage);
        } else {
            sb.append(statusMessage);
        }
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        if (linkUrls.size() > 0) {
            sb.append("Linkit:");
            sb.append(System.lineSeparator());
            for (int i = 0; i < linkUrls.size(); i++) {
                sb.append(allPages.get(linkUrls.get(i)).getStatusMessage());
                sb.append(" - ");
                sb.append(linkNames.get(i));
                sb.append(" - ");
                sb.append(linkUrls.get(i));
                sb.append(System.lineSeparator());
            }
        }
        for (int i = 0; i < 200; i++) {
            sb.append("-");
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
