package sn.ods.obi.infrastructure.config.password;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private final Pattern pattern;
    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9$*+?_&=!%@#\\[\\]\"{}]).{8,20})";

    public boolean validate(String password) {
        Matcher matcher;
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }
}
