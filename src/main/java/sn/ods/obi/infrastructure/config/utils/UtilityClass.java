package sn.ods.obi.infrastructure.config.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for common operations such as date formatting, password generation, hashing, and string manipulation.
 * This class cannot be instantiated.
 *
 * @author Abdou Karim CISSOKHO
 * @created 22/05/2024-18:23
 * @project backend_mfpai
 */
public final class UtilityClass {

    // Private constructor to prevent instantiation
    private UtilityClass() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    // Thread-safe date formatters
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("dd/MM/yyyy"));
    private static final ThreadLocal<SimpleDateFormat> FORMATTER =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    // Constants for password generation
    private static final String WORD_SEPARATOR = " ";
    private static final Random RANDOM = new SecureRandom(); // Use SecureRandom for secure random number generation
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final int PASSWORD_LENGTH = 10;

    // DATE UTILITIES
    public static String formatDate(Date date) {
        return SIMPLE_DATE_FORMAT.get().format(date);
    }

    public static String formatAmount(double amount) {
        return new DecimalFormat().format(amount);
    }

    // PASSWORD UTILITIES
    public static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return returnValue.toString();
    }

    public static String generatePassword() {
        return generatePassword(PASSWORD_LENGTH);
    }

    // PAGINATION UTILITIES
    public static PageRequest formatPageRequest(Map<String, String> filters) {
        int page = Integer.parseInt(filters.getOrDefault("page", "0"));
        int size = Integer.parseInt(filters.getOrDefault("size", "10"));
        String sortBy = filters.getOrDefault("sortBy", "id");
        boolean sortByDescending = Boolean.parseBoolean(filters.getOrDefault("sortByDescending", "true"));
        Sort sort = sortByDescending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(page, size, sort);
    }

    // SALTED PASSWORD HASHER
    public static final class SaltedPasswordHasher {
        private static final int SALT_LENGTH = 16;

        private SaltedPasswordHasher() {
            throw new UnsupportedOperationException("Utility class cannot be instantiated.");
        }

        public static String hashPassword(String password) throws NoSuchAlgorithmException {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hash = md.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(concatenateArrays(salt, hash));
        }

        public static boolean verifyPassword(String password, String saltedHash) throws NoSuchAlgorithmException {
            byte[] saltAndHash = Base64.getDecoder().decode(saltedHash);
            byte[] salt = new byte[SALT_LENGTH];
            byte[] hash = new byte[saltAndHash.length - SALT_LENGTH];
            splitArray(saltAndHash, salt, hash);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] testHash = md.digest(password.getBytes());

            return MessageDigest.isEqual(hash, testHash);
        }

        private static byte[] concatenateArrays(byte[] a, byte[] b) {
            byte[] result = new byte[a.length + b.length];
            System.arraycopy(a, 0, result, 0, a.length);
            System.arraycopy(b, 0, result, a.length, b.length);
            return result;
        }

        private static void splitArray(byte[] source, byte[] dest1, byte[] dest2) {
            System.arraycopy(source, 0, dest1, 0, dest1.length);
            System.arraycopy(source, dest1.length, dest2, 0, dest2.length);
        }
    }

    // DATE UTILITY
    public static final class DateUtility {
        private DateUtility() {
            throw new UnsupportedOperationException("Utility class cannot be instantiated.");
        }

        public static Date atStartOfDay(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        }

        public static Date atEndOfDay(Date date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        }

        public static Date addTimeToDate(Date date, int hours, int minutes, int seconds, int millis) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            calendar.set(Calendar.SECOND, seconds);
            calendar.set(Calendar.MILLISECOND, millis);
            return calendar.getTime();
        }
    }

    // STRING UTILITY
    public static final class StringUtility {
        private StringUtility() {
            throw new UnsupportedOperationException("Utility class cannot be instantiated.");
        }

        public static String titleCaseConversion(String text) {
            if (text == null || text.isEmpty()) {
                return text;
            }
            return Arrays.stream(text.split(WORD_SEPARATOR))
                    .map(word -> word.isEmpty()
                            ? word
                            : Character.toTitleCase(word.charAt(0)) + word.substring(1).toLowerCase())
                    .collect(Collectors.joining(WORD_SEPARATOR));
        }
    }

    // ARRAY UTILITY
    public static final class ArrayUtility {
        private ArrayUtility() {
            throw new UnsupportedOperationException("Utility class cannot be instantiated.");
        }

        public static <T> boolean checkIfAllValuesAreInList(List<T> a, List<T> b) {
            return new HashSet<>(b).containsAll(a);
        }
    }

    // PASSWORD VALIDATION UTILITY
    public static final class PasswordUtility {
        private static final String PASSWORD_PATTERN =
                "((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9*$-+?_&=!%@#\\[\\]\"{}/]).{8,20})";
        private final Pattern pattern;

        public PasswordUtility() {
            this.pattern = Pattern.compile(PASSWORD_PATTERN);
        }

        public boolean validate(String password) {
            return pattern.matcher(password).matches();
        }
    }

    // PASSWORD VALIDATION UTILITY
    public static final class EmailUtility {
        private static final String EMAIL_PATTERN =
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        private final Pattern pattern;

        public EmailUtility() {
            this.pattern = Pattern.compile(EMAIL_PATTERN);
        }

        public boolean validate(String password) {
            return pattern.matcher(password).matches();
        }
    }
}