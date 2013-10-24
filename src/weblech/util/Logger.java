package weblech.util;

import org.apache.log4j.Category;

public class Logger {

    protected static final Category _logClass = Category.getInstance("WebLech");

    static {
        try {
            Class.forName("weblech.util.Log4j");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Class not found exception: " + cnfe.getMessage());
            cnfe.printStackTrace();
        }
    }
}
