package mx.edu.cetys.software_quality_lab.petstore.exceptions;

public class PetAlreadyAdoptedException extends RuntimeException {
    public PetAlreadyAdoptedException(String message) {
        super(message);
    }
}
