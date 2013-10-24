package weblech.spider;

import java.util.*;
import java.net.URL;
import java.io.Serializable;

public class DownloadQueue implements Serializable {

    private SpiderConfig config;

    private List interestingURLsToDownload;

    private List averageURLsToDownload;

    private List boringURLsToDownload;

    private Set urlsInQueue;

    public DownloadQueue(SpiderConfig config) {
        this.config = config;
        interestingURLsToDownload = new ArrayList();
        averageURLsToDownload = new ArrayList();
        boringURLsToDownload = new ArrayList();
        urlsInQueue = new HashSet();
    }

    public void queueURL(URLToDownload url) {
        URL u = url.getURL();
        if (urlsInQueue.contains(u)) {
            return;
        }
        if (config.isInteresting(u)) {
            if (config.isDepthFirstSearch()) {
                interestingURLsToDownload.add(0, url);
            } else {
                interestingURLsToDownload.add(url);
            }
        } else if (config.isBoring(u)) {
            if (config.isDepthFirstSearch()) {
                boringURLsToDownload.add(0, url);
            } else {
                boringURLsToDownload.add(url);
            }
        } else {
            if (config.isDepthFirstSearch()) {
                averageURLsToDownload.add(0, url);
            } else {
                averageURLsToDownload.add(url);
            }
        }
        urlsInQueue.add(u);
    }

    public void queueURLs(Collection urls) {
        edu.hkust.clap.monitor.Monitor.loopBegin(13);
for (Iterator i = urls.iterator(); i.hasNext(); ) { 
edu.hkust.clap.monitor.Monitor.loopInc(13);
{
            URLToDownload u2d = (URLToDownload) i.next();
            queueURL(u2d);
        }} 
edu.hkust.clap.monitor.Monitor.loopEnd(13);

    }

    public URLToDownload getNextInQueue() {
        if (interestingURLsToDownload.size() > 0) {
            return returnURLFrom(interestingURLsToDownload);
        } else if (averageURLsToDownload.size() > 0) {
            return returnURLFrom(averageURLsToDownload);
        } else if (boringURLsToDownload.size() > 0) {
            return returnURLFrom(boringURLsToDownload);
        } else {
            return null;
        }
    }

    private URLToDownload returnURLFrom(List urlList) {
        URLToDownload u2d = (URLToDownload) urlList.get(0);
        urlList.remove(0);
        urlsInQueue.remove(u2d.getURL());
        return u2d;
    }

    public int size() {
        return interestingURLsToDownload.size() + averageURLsToDownload.size() + boringURLsToDownload.size();
    }

    public String toString() {
        return size() + " URLs";
    }
}
