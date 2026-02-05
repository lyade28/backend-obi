package sn.ods.obi.presentation.dto.responses;

import org.springframework.http.HttpStatus;

import static sn.ods.obi.infrastructure.config.constant.AppConstants.ATTEMPT_CACHE_DURATION_TIME;


/**
 * @author Abdou Karim CISSOKHO
 * @created 23/01/2024-14:31
 * @project backend_mfpai
 */
public enum APIMessage {
    GENERIC_ERROR(HttpStatus.BAD_REQUEST.value(), "400014", "%s"),
    GENERIC_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404000", "There is no %s"),

    // -- internal server error messages
    UNKNOWN_ERROR(HttpStatus.SERVICE_UNAVAILABLE.value(), "500000",
            "An internal problem has occurred, please contact Petrosen support %s"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "500001",
            "Internal Server Error, please contact customer service %s"),
    WS_AMAZON_ERROR(HttpStatus.BAD_REQUEST.value(), "500002",
            "Unknown error occurred, please contact customer service %s"),
    // -- end internal server error messages

    // -- not found messages
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404100", "Not found %s"),
    STATUS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404101", "Status not found %s"),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404102", "Aucun compte n'a été trouvé"),
    SITE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404103", "Site not found %s"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404104", "No user were found %s"),
    ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "404005", "No roles were found %s"),
    CASH_REGISTER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "404006", "No cash register were found %s"),
    APPOINTMENT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "404007", "No Appointment were found %s"),
    INCOME_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404008", "No income were found %s"),
    LINE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "404009", "No Line were found %s"),
    ROLE_EDIT_UNAUTHORIZED(HttpStatus.BAD_REQUEST.value(), "401015", "The names of the basic roles cannot be changed %s"),
   // NON_STRONG_PASSWORD(HttpStatus.BAD_REQUEST.value(), "401016", "Non-strong password"),
    NON_STRONG_PASSWORD(HttpStatus.BAD_REQUEST.value(), "401016", "Merci de fournir  un de passe fort"),
    NON_MATCH_PASSWORD_CONFIRMED(HttpStatus.BAD_REQUEST.value(), "401016", "Le mots de passe et le mot de confirmé ne corresponde pas "),
    MANAGER_NOT_FOUND((HttpStatus.BAD_REQUEST.value()), "401017", "Manager not found %s"),
    SLIDER_NOT_FOUND((HttpStatus.BAD_REQUEST.value()), "401024", "Slider not found %s"),
    REQUIRED_FILE((HttpStatus.BAD_REQUEST.value()), "401030", "Attached document is required"),

    // -- end unauthorized messages

    // -- forbidden messages
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "403000", "is not allowed %s"),
    VALIDATION_NOT_AUTHORIZED(HttpStatus.FORBIDDEN.value(), "403001", "validation not allowed %s"),
    CONFIRMATION_NOT_AUTHORIZED(HttpStatus.FORBIDDEN.value(), "403002", "confirmation not allowed %s"),
    REJECTION_NOT_AUTHORIZED(HttpStatus.FORBIDDEN.value(), "403003", "rejection not allowed %s"),
    APPROBATION_NOT_AUTHORIZED(HttpStatus.FORBIDDEN.value(), "403004", "approbation not allowed %s"),
    BOOKING_NOT_AUTHORIZED(HttpStatus.FORBIDDEN.value(), "403005", "Booking for another site other than your own is not permitted %s"),
    // -- end forbidden messages

    // bad request
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "400000", "Bad request %s"),
    APP_NOT_VALID(HttpStatus.BAD_REQUEST.value(), "400001",
            "The source application is not recognized, please contact customer service %s"),

    FILE_EXTENSION_NOT_VALID(HttpStatus.BAD_REQUEST.value(), "400085",
            "File extension not valid"),

    URL_FILE_NOT_VALID(HttpStatus.BAD_REQUEST.value(), "400086",
            "File extension not valid"),
    LOGIN_MAX_ATTEMPT(HttpStatus.BAD_REQUEST.value(), "400002",
            "You have exceeded the number of attempts allowed for the connection. Please try again in %s"
            + ATTEMPT_CACHE_DURATION_TIME + " minutes."),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST.value(), "400003", "Not valid due to validation error %s"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "401003", "is not allowed%s"),
    LOGIN_BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED.value(), "401004", "Incorrect login or password %s"),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED.value(), "401020", "User account is disabled %s"),
    OLD_PASSWORD_INVALID(HttpStatus.UNAUTHORIZED.value(), "401021", "Old password is invalid %s"),
    NEW_AND_CONFIRM_PASSWORD_MUST_MATCH(HttpStatus.UNAUTHORIZED.value(), "401012",
            "New and confirm password must match"),
    SITE_DISABLED(HttpStatus.UNAUTHORIZED.value(), "401022", "Site is disabled %s"),
    LINE_DISABLED(HttpStatus.UNAUTHORIZED.value(), "401023", "Line is disabled %s"),

    // already exist messages
    ACCOUNT_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "400004", "Account already exist : %s"),
    SLIDER_TITLE_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "400012", "Slider title already exist %s"),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "401007",
            "The token provided is not valid for the following reason: %s"),
    INVALID_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "401013",
            "The token provided is not valid for the following reason: JWT expired %s"),

    // -- exist messages
    PHONE_NUMBER_ALREADY_EXIST(HttpStatus.CONFLICT.value(), "409001", "Le numéro de téléphone fourni est déjà utilisé"),

    CONFIGURATION_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "409004", "code for this configuration  already exist %s"),
    CONFIGURATION_CODE_IS_NOT_ALLOWED(HttpStatus.CONFLICT.value(), "409005", "code for this configuration is not allowed %s"),
    PARENT_CODE_UPDATE_NOT_ALLOWED(HttpStatus.CONFLICT.value(), "409006", "update field parent code of label is not allowed %s"),
    LABEL_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "409007", "code for this label already exists %s"),

    ROLE_ALREADY_ASSIGNED(HttpStatus.CONFLICT.value(), "409008", "This role is already assigned to the user %s"),
    ROLE_ALREADY_UNASSIGNED(HttpStatus.CONFLICT.value(), "409009", "The user does not have this role %s"),
    ROLE_ALREADY_EXIST(HttpStatus.CONFLICT.value(), "409000", "There is already a role with this %s"),
    ACCOUNT_ALREADY_INITIALIZED(HttpStatus.CONFLICT.value(), "409004", "Account already initialized %s"),
    USER_COMPTE_DISABLED(HttpStatus.CONFLICT.value(), "409005" , "User compte is disabled %s" ),
    FILE_FORMAT_INCORRECT(HttpStatus.BAD_REQUEST.value(), "556", "Error occurred: %s"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "409000", " L'émail de l'utilisateur existe déjà %s" ),
    REGION_OB(HttpStatus.CONFLICT.value(), "409000", " %s" ),
    USER_COMPTE_ALREADY_CONNECTED(HttpStatus.CONFLICT.value(), "409000", "L'utilisateur a une fois connecté %s"),
    MATFONC_OBLIGATOIRE(HttpStatus.CONFLICT.value(), "409000", " %s" ),
    MATRICULE_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "409000", " %s" ),
    CORPS_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "409000", " %s" ),
    GRADE_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "409000", " %s" ),
    INVALID_CONF_KEYCLOAK_USER(HttpStatus.BAD_REQUEST.value(), "556","user n'est pas ajouter au niveau de keyclaok" ),
    CODE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "409000","le code %s n'existe pas"),
    CODE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "409000","%s"),
    CONNEXION_TOKEN_INVALIDE(HttpStatus.BAD_REQUEST.value(),"556","token invalide "),
    QUANTUM_INVALIDE(HttpStatus.CONFLICT.value(), "409000", " %s" ),
    MATRICULE_OBLIGATOIRE_FONCTIONAIRE(HttpStatus.BAD_REQUEST.value(), "400004", " %s" ),
    CHECK_DATE_EN(HttpStatus.BAD_REQUEST.value(), "400004", " %s" ),
    PASSWORD_OLD_PASSWORD_ARE_NOT_IDENTIQUE(HttpStatus.CONFLICT.value(),"400004", "Merci de fournir l'ancien mot de passe %s" ),
    PASSWORD_OLD_PASSWORD_ARE_IDENTIQUE(HttpStatus.CONFLICT.value(),"400004", "L'ancien mot de passe passe est le nouveau mot de passe sont identiques %s" ),
    ACCOUNT_ALREADY_ACTIVATED(HttpStatus.CONFLICT.value(),"400004", "Le compte de l'utilisateur est déjà activer" ),
    INVALID_CODE_EXPIRED(HttpStatus.CONFLICT.value(),"400004", "Le code est déjà expiré" ),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST.value(),"400004" , "Email n'existe pas"),
    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST.value(),"400004" , "L'email n'est pas valide" );


    private int httpStatus;
    private String code;
    private String message;

    APIMessage(int httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
