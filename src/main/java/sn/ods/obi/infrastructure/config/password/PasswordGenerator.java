package sn.ods.obi.infrastructure.config.password;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;



public class PasswordGenerator {
    private static final int MIN_LENGTH = 6;
    private static final int MIN_L_CASE_COUNT = 3;
    private static final int MIN_U_CASE_COUNT = 1;
    private static final int MIN_NUM_COUNT = 1;
    private static final int MIN_SPECIAL_COUNT = 1;
    private static final String  L_CASE ="lcase";
    private static final String  U_CASE = "ucase";
    private static  final  String NUM = "num";
    private  static  final  String SPECIAL = "special";

    private static final Random random = new Random();

    //Without params
    public static String generateRandomString() {
        char[] randomString;

        String lCaseChars = "abcdefgijkmnpqrstwxyz";
        String uCaseChars = "ABCDEFGHJKLMNPQRSTWXYZ";
        String numericChars = "23456789";
        /* String SpecialChars = "*$-+?_&=!%{}/";
         */
        String specialChars = "*-+_!@";

        Map<String, Integer> charGroupsUsed = new HashMap<>();
        charGroupsUsed.put(L_CASE, MIN_L_CASE_COUNT);
        charGroupsUsed.put(U_CASE, MIN_U_CASE_COUNT);
        charGroupsUsed.put(NUM, MIN_NUM_COUNT);
        charGroupsUsed.put(SPECIAL, MIN_SPECIAL_COUNT);

        byte[] randomBytes = new byte[4];

        // Generate 4 random bytes.
        random.nextBytes(randomBytes);

        // Convert 4 bytes into a 32-bit integer value.
        int seed = (randomBytes[0] & 0x7f) << 24
                | randomBytes[1] << 16
                | randomBytes[2] << 8
                | randomBytes[3];

        // Create a randomizer from the seed.
        Random random = new Random(seed);

        // Allocate appropriate memory for the password.
        int randomIndex;
        randomString = new char[MIN_LENGTH];

        int requiredCharactersLeft = MIN_L_CASE_COUNT + MIN_U_CASE_COUNT + MIN_NUM_COUNT + MIN_SPECIAL_COUNT;

        // Build the password.
        for (int i = 0; i < randomString.length; i++) {
            StringBuilder selectableChars = new StringBuilder();

            // if we still have plenty of characters left to acheive our minimum requirements.
            if (requiredCharactersLeft < randomString.length - i) {
                // choose from any group at random
                selectableChars = new StringBuilder(lCaseChars + uCaseChars + numericChars + specialChars);
            } else // we are out of wiggle room, choose from a random group that still needs to have a minimum required.
            {
                // choose only from a group that we need to satisfy a minimum for.
                for (Map.Entry<String, Integer> charGroup : charGroupsUsed.entrySet()) {
                    if (charGroup.getValue() > 0) {
                        if (L_CASE.equals(charGroup.getKey())) {
                            selectableChars.append(lCaseChars);
                        } else if (U_CASE.equals(charGroup.getKey())) {
                            selectableChars.append(uCaseChars);
                        } else if (NUM.equals(charGroup.getKey())) {
                            selectableChars.append(numericChars);
                        } else if (SPECIAL.equals(charGroup.getKey())) {
                            selectableChars.append(specialChars);
                        }
                    }
                }
            }

            // Now that the string is built, get the next random character.
            randomIndex = random.nextInt((selectableChars.length()) - 1);
            char nextChar = selectableChars.charAt(randomIndex);

            // Tac it onto our password.
            randomString[i] = nextChar;

            // Now figure out where it came from, and decrement the appropriate minimum value.
            if (lCaseChars.indexOf(nextChar) > -1) {
                charGroupsUsed.put(L_CASE, charGroupsUsed.get(L_CASE) - 1);
                if (charGroupsUsed.get(L_CASE) >= 0) {
                    requiredCharactersLeft--;
                }
            } else if (uCaseChars.indexOf(nextChar) > -1) {
                charGroupsUsed.put(U_CASE, charGroupsUsed.get(U_CASE) - 1);
                if (charGroupsUsed.get(U_CASE) >= 0) {
                    requiredCharactersLeft--;
                }
            } else if (numericChars.indexOf(nextChar) > -1) {
                charGroupsUsed.put(NUM, charGroupsUsed.get(NUM) - 1);
                if (charGroupsUsed.get(NUM) >= 0) {
                    requiredCharactersLeft--;
                }
            } else if (specialChars.indexOf(nextChar) > -1) {
                charGroupsUsed.put(SPECIAL, charGroupsUsed.get(SPECIAL) - 1);
                if (charGroupsUsed.get(SPECIAL) >= 0) {
                    requiredCharactersLeft--;
                }
            }
        }
        return new String(randomString);
    }

    //Constructor
    private PasswordGenerator() {
    }

    //pour generer les references des operations
    public static String GenerateReferenceString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        /* String reference = "AF-" + formatter.format(date).toString(); */
        return formatter.format(date);
    }

    //pour generer le nom des fichiers stockés sur le serveur
    public static String VerifAndGenerateNameOfStoragedFile(String originalFileName) {
        /*
             faut renommer le fichier avec la date courante
             */

        int pos = originalFileName.lastIndexOf(".");
        String extension = "";
        if (pos > 0) {
            //	nomFichier = fileName.substring(0, pos);
            extension = originalFileName.substring(pos);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        return UUID.randomUUID() + "_" + formatter.format(date) + extension;
    }

    public static String generateNameOfStoragedFile() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        return "B_" + formatter.format(date) + ".pdf";
    }

    //pour generer le nom des pièces jointes à stocker sur le serveur
    public static String GenerateNameOfStoragedFile(String originalFileName) {
        /*
             faut renommer le fichier avec la date courante
             */

        int pos = originalFileName.lastIndexOf(".");
        String extension = "";
        if (pos > 0) {
            // nomFichier = fileName.substring(0, pos);
            extension = originalFileName.substring(pos);
        }
        new Date(System.currentTimeMillis());
        originalFileName = "P_" + UUID.randomUUID() + extension;
        return originalFileName;
    }
}
