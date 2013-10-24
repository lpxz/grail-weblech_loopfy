package weblech.util;

import org.apache.log4j.*;
import java.io.IOException;

public class Log4j {

    private static Category _logClass = Category.getInstance(Log4j.class);

    static {
        Layout l = new PatternLayout("%d [%t] %-5p %F:%L - %m\n");
        ConsoleAppender capp = new ConsoleAppender(l);
        capp.setThreshold(Priority.INFO);
        BasicConfigurator.configure(capp);
        try {
            FileAppender fapp = new FileAppender(l, "weblech.log", false);
            BasicConfigurator.configure(fapp);
            System.err.println("Log4j configured to use weblech.log -- view full logging here");
        } catch (IOException ioe) {
            _logClass.warn("IO Exception when configuring log4j: " + ioe.getMessage(), ioe);
        }
        _logClass.debug("Log4j configured");
    }

    public static void init() {
    }
}
