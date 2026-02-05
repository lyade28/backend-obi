package sn.ods.obi.domain.enums;

import org.apache.commons.lang3.EnumUtils;


public enum FileCode {
    CONTRACT_LOADED("Contrat chargé");
    private final String formattedName;

    FileCode(String formattedName) {
        this.formattedName = formattedName;
    }

    public static boolean findByName(String name) {
        return EnumUtils.isValidEnum(FileCode.class, name.toUpperCase());
    }

    public String getFormattedName() {
        return formattedName;
    }
}
