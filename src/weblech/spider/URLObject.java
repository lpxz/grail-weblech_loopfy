package weblech.spider;

import org.apache.log4j.Category;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import weblech.util.Log4j;

public class URLObject {

    private static final Category _logClass = Category.getInstance(URLObject.class);

    static {
        Log4j.init();
    }

    private final URL sourceURL;

    private final String contentType;

    private final byte[] content;

    private final SpiderConfig config;

    public URLObject(URL sourceURL, String contentType, byte[] content, SpiderConfig config) {
        this.sourceURL = sourceURL;
        this.contentType = contentType;
        this.content = content;
        this.config = config;
    }

    public URLObject(URL sourceURL, SpiderConfig config) {
        this.sourceURL = sourceURL;
        this.config = config;
        String s = sourceURL.toExternalForm().toLowerCase();
        if (s.indexOf(".jpg") != -1) {
            contentType = "image/jpeg";
        } else if (s.indexOf(".gif") != -1) {
            contentType = "image/gif";
        } else {
            contentType = "text/html";
        }
        if (existsOnDisk()) {
            File f = new File(convertToFileName());
            if (f.isDirectory()) {
                f = new File(f, "index.html");
            }
            content = new byte[(int) f.length()];
            try {
                FileInputStream in = new FileInputStream(f);
                in.read(content);
                in.close();
            } catch (IOException ioe) {
                _logClass.warn("IO Exception reading disk version of URL " + sourceURL, ioe);
            }
        } else {
            content = new byte[0];
        }
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isHTML() {
        return contentType.toLowerCase().startsWith("text/html");
    }

    public boolean isXML() {
        return contentType.toLowerCase().startsWith("text/xml");
    }

    public boolean isImage() {
        return contentType.startsWith("image/");
    }

    public String getStringContent() {
        return new String(content);
    }

    private String convertToFileName() {
        String url = sourceURL.toExternalForm();
        int httpIdx = url.indexOf("http://");
        if (httpIdx == 0) {
            url = url.substring(7);
        }
        if (url.indexOf("/") < 0) {
            url = url + "/";
        }
        if (url.endsWith("/")) {
            url = url + "index.html";
        }
        url = textReplace("?", URLEncoder.encode("?"), url);
        url = textReplace("&", URLEncoder.encode("&"), url);
        return config.getSaveRootDirectory().getPath() + "/" + url;
    }

    public boolean existsOnDisk() {
        File f = new File(convertToFileName());
        return (f.exists() && !f.isDirectory());
    }

    public void writeToFile() {
        writeToFile(convertToFileName());
    }

    public void writeToFile(String fileName) {
        _logClass.debug("writeToFile(" + fileName + ")");
        try {
            File f = new File(fileName);
            f.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException ioe) {
            _logClass.warn("IO Exception" );
            
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("URLObject: ");
        sb.append(contentType);
        if (false) {
            sb.append("\n");
            sb.append(getStringContent());
        }
        return sb.toString();
    }

    private String textReplace(String find, String replace, String input) {
        int startPos = 0;
while (true) { 
{
            int textPos = input.indexOf(find, startPos);
            if (textPos < 0) {
                break;
            }
            input = input.substring(0, textPos) + replace + input.substring(textPos + find.length());
            startPos = textPos + replace.length();
        }} 

        return input;
    }
}
