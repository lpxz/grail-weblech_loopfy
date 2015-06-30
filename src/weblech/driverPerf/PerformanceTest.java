package driverPerf;

import weblech.spider.SpiderConfig;
import weblech.spider.Spider;
import weblech.spider.Constants;
import weblech.util.Logger;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.datatype.Duration;

import org.apache.log4j.Category;

public class PerformanceTest implements Constants {

    /** For class-related messages */
//    private static Category _logClass = Category.getInstance(PerformanceTest.class);

    public static void main(String[] args) throws Exception{
//        _logClass.debug("main()");
        if (args.length < 1 || args.length > 2) {
            usage();
            System.exit(0);
        }
        String propsFile = null;
        if (args.length >= 1) {
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
//            _logClass.error("File not found: " + args[0], fnfe);
            System.exit(1);
        } catch (IOException ioe) {
//            _logClass.error("IO Exception caught reading config file: " + ioe.getMessage(), ioe);
            System.exit(1);
        }
//        _logClass.debug("Configuring Spider from properties");
        SpiderConfig config = new SpiderConfig(props);
//        _logClass.debug(config);
        
        
        
        
        
        
        
        
        //===lpxz, come here.
        //Spider.run()
        // 9754 msec    10303(our)  12311 their
        
        int threadNo = Integer.parseInt(args[1]);        
        Spider[] spiders = new Spider[threadNo];
        for(int i=0; i< threadNo; i++)
        {
        	spiders[i] = new Spider(config);
        }
        
        long start = System.currentTimeMillis();
        for(int i=0; i< threadNo; i++)
        {
        	spiders[i].start();
        }
        
        
        for(int i=0; i< threadNo; i++)
        {
        	spiders[i].join();
        }
        long end  = System.currentTimeMillis();
    //    System.out.println("duration: " + (end-start));
        
        
// the above is the warm-up run
        for(int i=0; i< threadNo; i++)
        {
        	spiders[i] = new Spider(config);
        }
         start = System.currentTimeMillis();
        for(int i=0; i< threadNo; i++)
        {
        	spiders[i].start();
        }
        
        
        for(int i=0; i< threadNo; i++)
        {
        	spiders[i].join();
        }
         end  = System.currentTimeMillis();
         long duration = (end-start);
         
         
         
         for(int i=0; i< threadNo; i++)
         {
         	spiders[i] = new Spider(config);
         }
         start = System.currentTimeMillis();
         for(int i=0; i< threadNo; i++)
         {
         	spiders[i].start();
         }
         
         
         for(int i=0; i< threadNo; i++)
         {
         	spiders[i].join();
         }
          end  = System.currentTimeMillis();
          long duration2 = (end-start);
          System.out.println((duration + duration2)/2);
          
     
          
    
        
        
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
