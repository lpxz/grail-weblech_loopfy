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
        boolean resume = false;
        if (args.length == 1) {
            propsFile = args[0];
        } else if (!args[0].equals("-resume")) {
            usage();
            System.exit(0);
        } else {
            resume = true;
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
        if (resume) {
            _logClass.info("Reading checkpoint...");
            spider.readCheckpoint();
        }
        _logClass.info("Starting Spider...");
        spider.start();
        System.out.println("\nHit any key to stop Spider\n");
        try {
            edu.hkust.clap.monitor.Monitor.loopBegin(10);
while (spider.isRunning()) { 
edu.hkust.clap.monitor.Monitor.loopInc(10);
{
                if (System.in.available() != 0) {
                    System.out.println("\nStopping Spider...\n");
                    spider.stop();
                    break;
                }
                pause(SPIDER_STOP_PAUSE);
            }} 
edu.hkust.clap.monitor.Monitor.loopEnd(10);

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
