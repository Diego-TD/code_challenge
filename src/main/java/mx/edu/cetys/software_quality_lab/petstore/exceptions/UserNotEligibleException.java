package mx.edu.cetys.software_quality_lab.petstore.exceptions;

public class UserNotEligibleException extends RuntimeException {
    public UserNotEligibleException(String message) {
        super(message);
    }
}
