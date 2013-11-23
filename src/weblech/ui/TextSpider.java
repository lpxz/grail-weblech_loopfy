package weblech.ui;

import weblech.spider.SpiderConfig;
import weblech.spider.Spider;
import weblech.spider.Constants;
import weblech.util.Logger;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.log4j.Category;

public class TextSpider implements Constants {

    /** For class-related messages */
    private static Category _logClass = Category.getInstance(TextSpider.class);

    public static void main(String[] args) {
        _logClass.debug("main()");
        if (args.length < 1 || args.length > 2) {
            usage();
            System.exit(0);
        }
        String propsFile = null;
        if (args.length == 1) {
            propsFile = args[0];
        } else if (!args[0].equals("-resume")) {
            usage();
            System.exit(0);
        } else {
            propsFile = args[1];
        }
        Properties props = null;
        try {
            FileInputStream propsIn = new FileInputStream(propsFile);
            props = new Properties();
            props.load(propsIn);
            propsIn.close();
        } catch (FileNotFoundException fnfe) {
            _logClass.error("File not found: " + args[0], fnfe);
            System.exit(1);
        } catch (IOException ioe) {
            _logClass.error("IO Exception caught reading config file: " + ioe.getMessage(), ioe);
            System.exit(1);
        }
        _logClass.debug("Configuring Spider from properties");
        SpiderConfig config = new SpiderConfig(props);
        _logClass.debug(config);
        Spider spider = new Spider(config);
        spider.start0(); // start0 invokes the thread.start() again, see the code for details.
        System.out.println("\nHit any key to stop Spider\n");
        try {
        	while (spider.isRunning()) { 
        		{
                if (System.in.available() != 0) {
                    System.out.println("\nStopping Spider...\n");
                    spider.stop();
                    break;
                }
                pause(SPIDER_STOP_PAUSE);
            }} 

        } catch (IOException ioe) {
            _logClass.error("Unexpected exception caught: " + ioe.getMessage(), ioe);
            System.exit(1);
        }
    }

    private static void pause(long howLong) {
        try {
            Thread.sleep(howLong);
        } catch (InterruptedException ignored) {
        }
    }

    private static void usage() {
        System.out.println("Usage: weblech.ui.TextSpider [-resume] [config file]");
    }
}
