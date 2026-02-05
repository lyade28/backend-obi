package sn.ods.obi.infrastructure.config.exceptions;

/**
 * @author Abdou Karim CISSOKHO
 * @created 07/01/2025-11:48
 * @project obi
 */


public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message){
        super(message);
    }

}