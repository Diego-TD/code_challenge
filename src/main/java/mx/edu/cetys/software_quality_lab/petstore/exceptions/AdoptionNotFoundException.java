package mx.edu.cetys.software_quality_lab.petstore.exceptions;

public class AdoptionNotFoundException extends RuntimeException {
    public AdoptionNotFoundException(String message) {
        super(message);
    }
}
