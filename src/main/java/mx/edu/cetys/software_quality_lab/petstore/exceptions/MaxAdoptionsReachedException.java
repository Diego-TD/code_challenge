package mx.edu.cetys.software_quality_lab.petstore.exceptions;

public class MaxAdoptionsReachedException extends RuntimeException {
    public MaxAdoptionsReachedException(String message) {
        super(message);
    }
}
