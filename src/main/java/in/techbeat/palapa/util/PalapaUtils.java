package in.techbeat.palapa.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PalapaUtils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^[A-Z0-9_]{4,15}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,20}$");

    public static boolean validate(final String input, final Pattern validationPattern) {
        final Matcher matcher = validationPattern.matcher(input);
        return matcher.find();
    }
}