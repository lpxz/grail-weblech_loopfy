package weblech.spider;

import org.apache.log4j.Category;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import weblech.util.Log4j;

public class DumbAuthenticator extends Authenticator {

    private static final Category _logClass = Category.getInstance(DumbAuthenticator.class);

    static {
        Log4j.init();
    }

    private final String user;

    private final String password;

    public DumbAuthenticator(String user, String password) {
        _logClass.debug("DumbAuthenticator(" + user + ", ***)");
        this.user = user;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        _logClass.debug("getPasswordAuthentication()");
        return new PasswordAuthentication(user, password.toCharArray());
    }
}
