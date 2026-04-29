package mx.edu.cetys.software_quality_lab.pets.exceptions;

public class InvalidPetDataException extends RuntimeException
{
    public InvalidPetDataException(String message)
    {
        super(message);
    }
}
