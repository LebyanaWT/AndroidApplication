package mycompany.com.androidapplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LebyanaWT on 2017/11/15.
 */

public class Utility {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String BASE_IP = "http://192.168.2.237/";
    public static final String LOGIN_URL = BASE_IP + "login.php";

    /**
     * Assigning the Email pattern to be alowed as valid Email
     * */

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
            "@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    /**
     * Validating the email address to check if it is in the form of e.g lebyanawt@gmail.com
     * */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    /**
     * Checking to see if the client has inserted/Inputed values
     * */
    public static boolean isNotNull(String fieldValue){
        return fieldValue != null && fieldValue.trim().length() > 0;
    }
}
