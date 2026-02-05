package sn.ods.obi.domain.enums;

import org.apache.commons.lang3.EnumUtils;


public enum TokenType {
    SIGNING("Signing"),
    SIGNUP("Signup"),
    RESET_PASSWORD("RESET_PASSWORD");

    private final String formattedName;

    TokenType(String formattedName) {
        this.formattedName = formattedName;
    }

    public static boolean findByName(String name) {
        return EnumUtils.isValidEnum(TokenType.class, name.toUpperCase());
    }

    public String getFormattedName() {
        return formattedName;
    }
}
