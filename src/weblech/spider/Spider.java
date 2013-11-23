package weblech.spider;

import weblech.util.Logger;
import weblech.util.Log4j;
import java.util.*;
import java.io.*;
import java.net.URL;
import org.apache.log4j.Category;

public class Spider extends  Thread implements Constants {

    /** Config for the spider */
    private SpiderConfig config;

    /**
     * Download queue.
     * Thread safety: To access the queue, first synchronize on it.
     */
    private DownloadQueue queue;

    /**
     * Set of URLs downloaded or scheduled, so we don't download a
     * URL more than once.
     * Thread safety: To access the set, first synchronize on it.
     */
    private Set urlsDownloadedOrScheduled;

    /**
     * Set of URLs currently being downloaded by Spider threads.
     * Thread safety: To access the set, first synchronize on it.
     */
    private Set urlsDownloading;

    /**
     * Number of downloads currently taking place.
     * Thread safety: To modify this value, first synchronize on
     *                the download queue.
     */
    private int downloadsInProgress;

    /** Whether the spider should quit */
    private boolean quit;

    /** Count of running Spider threads. */
    private int running;

    /** Time we last checkpointed. */
    private long lastCheckpoint;

//	private Category _logClass =Logger._logClass;

    public Spider(SpiderConfig config) {
        this.config = config;
        queue = new DownloadQueue(config);
        queue.queueURL(new URLToDownload(config.getStartLocation(), 0));
        urlsDownloadedOrScheduled = new HashSet();
        urlsDownloading = new HashSet();
        downloadsInProgress = 0;
        lastCheckpoint = 0;
    }

    public void start0() {
        quit = false;
        running = 0;
for (int i = 0; i < config.getSpiderThreads(); i++) { 
{
//            _logClass .info("Starting Spider thread");
            Thread t = new Thread(this, "Spider-Thread-" + (i + 1));
            t.start();
            running++;
        }} 

    }



    public boolean isRunning() {
        return running == 0;
    }

    private void checkpointIfNeeded() {
        if (config.getCheckpointInterval() == 0) {
            return;
        }
        if (System.currentTimeMillis() - lastCheckpoint > config.getCheckpointInterval()) {
            synchronized (queue) {
                if (System.currentTimeMillis() - lastCheckpoint > config.getCheckpointInterval()) {
                    writeCheckpoint();
                    lastCheckpoint = System.currentTimeMillis();
                }
            }
        }
    }

    private void writeCheckpoint() {
//        _logClass.debug("writeCheckpoint()");
        try {
            FileOutputStream fos = new FileOutputStream("spider.checkpoint", false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(queue);
            oos.writeObject(urlsDownloading);
            oos.close();
        } catch (IOException ioe) {
//            _logClass.warn("IO Exception attempting checkpoint: " + ioe.getMessage(), ioe);
        }
    }

    public void readCheckpoint() {
        try {
            FileInputStream fis = new FileInputStream("spider.checkpoint");
            ObjectInputStream ois = new ObjectInputStream(fis);
            queue = (DownloadQueue) ois.readObject();
            urlsDownloading = (Set) ois.readObject();
            queue.queueURLs(urlsDownloading);
            urlsDownloading.clear();
        } catch (Exception e) {
//            _logClass.error("Caught exception reading checkpoint: " + e.getMessage(), e);
        }
    }

    public void run() {
        HTMLParser htmlParser = new HTMLParser(config);
        URLGetter urlGetter = new URLGetter(config);
while ((queueSize() > 0 || downloadsInProgress > 0) && quit == false) { 
{
            checkpointIfNeeded();
            URLToDownload nextURL;
           synchronized (queue) //added by lpxz. our: queue, their: Spider.class
            {
				
			
            if (queueSize() == 0 && downloadsInProgress > 0) {
                try {
                    Thread.sleep(QUEUE_CHECK_INTERVAL);
                } catch (InterruptedException ignored) {
                }
                continue;
            } else if (queueSize() == 0) {
                break;
            }
         
            synchronized (queue) {
                nextURL = queue.getNextInQueue();
                downloadsInProgress++;
            }
            
            }
            
            synchronized (urlsDownloading) {
                urlsDownloading.add(nextURL);
            }
            int newDepth = nextURL.getDepth() + 1;
            int maxDepth = config.getMaxDepth();
            synchronized (urlsDownloading) {
                urlsDownloading.remove(nextURL);
            }
            List newURLs = downloadURL(nextURL, urlGetter, htmlParser);
            newURLs = filterURLs(newURLs);
            ArrayList u2dsToQueue = new ArrayList();
for (Iterator i = newURLs.iterator(); i.hasNext(); ) { 
{
                URL u = (URL) i.next();
                synchronized (urlsDownloadedOrScheduled) {
                    if (!urlsDownloadedOrScheduled.contains(u) && (maxDepth == 0 || newDepth <= maxDepth)) {
                        u2dsToQueue.add(new URLToDownload(u, nextURL.getURL(), newDepth));
                        urlsDownloadedOrScheduled.add(u);
                    }
                }
            }} 

            synchronized (queue) {
                queue.queueURLs(u2dsToQueue);
                downloadsInProgress--;
            }
        }} 

//        _logClass.info("Spider thread stopping");
        running--;
    }

    /**
     * Get the size of the download queue in a thread-safe manner.
     */
    private int queueSize() {
        synchronized (queue) {
            return queue.size();
        }
    }

    /**
     * Get a URL, and return new URLs that are referenced from it.
     *
     * @return A List of URL objects.
     */
    private List downloadURL(URLToDownload url, URLGetter urlGetter, HTMLParser htmlParser) {
//        _logClass.debug("downloadURL(" + url + ")");
        URLObject obj = new URLObject(url.getURL(), config);
        if (obj.existsOnDisk()) {
            if (config.refreshHTMLs() && (obj.isHTML() || obj.isXML())) {
//                _logClass.info("Q: [" + queue + "] " + url);
                obj = urlGetter.getURL(url);
            } else if (config.refreshImages() && obj.isImage()) {
//                _logClass.info("Q: [" + queue + "] " + url);
                obj = urlGetter.getURL(url);
            }
        } else {
//            _logClass.info("Q: [" + queue + "] " + url);
            obj = urlGetter.getURL(url);
        }
        if (obj == null) {
            return new ArrayList();
        }
        if (!obj.existsOnDisk()) {
            obj.writeToFile();
        }
        if (obj.isHTML() || obj.isXML()) {
            return htmlParser.parseLinksInDocument(url.getURL(), obj.getStringContent());
        } else if (obj.isImage()) {
            return new ArrayList();
        } else {
//            _logClass.warn("Unsupported content type received: " + obj.getContentType());
//            _logClass.info("URL was " + url);
            return new ArrayList();
        }
    }

    private List filterURLs(List URLs) {
        String match = config.getURLMatch();
        ArrayList retVal = new ArrayList();
        synchronized (urlsDownloadedOrScheduled) {
for (Iterator i = URLs.iterator(); i.hasNext(); ) { 
{
                URL u = (URL) i.next();
                if (urlsDownloadedOrScheduled.contains(u)) {
                    continue;
                }
                String s = u.toExternalForm();
                if (s.indexOf(match) != -1) {
                    retVal.add(u);
                }
            }} 

        }
        return retVal;
    }
}
