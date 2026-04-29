package mx.edu.cetys.software_quality_lab.commons;

// Response Generic Wrapper to include standarized info in all our APIs
public record ApiResponse<T>(String info, T response, String error){}
