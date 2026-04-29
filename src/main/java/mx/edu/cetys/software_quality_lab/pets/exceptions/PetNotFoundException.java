package mx.edu.cetys.software_quality_lab.pets.exceptions;

public class PetNotFoundException extends RuntimeException{
    public PetNotFoundException(String message){
        super(message);
    }
}
