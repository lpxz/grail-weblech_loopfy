package weblech.spider;

import java.net.URL;

public class URLToDownload implements java.io.Serializable {

    private final URL url;

    private final URL referer;

    private final int depth;

    public URLToDownload(URL url, int depth) {
        this(url, null, depth);
    }

    public URLToDownload(URL url, URL referer, int depth) {
        this.url = url;
        this.referer = referer;
        this.depth = depth;
    }

    public URL getURL() {
        return url;
    }

    public URL getReferer() {
        return referer;
    }

    public int getDepth() {
        return depth;
    }

    public String toString() {
        return url + ", referer " + referer + ", depth " + depth;
    }
}
