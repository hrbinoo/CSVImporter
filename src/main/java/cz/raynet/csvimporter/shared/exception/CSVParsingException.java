package cz.raynet.csvimporter.shared.exception;

public class CSVParsingException extends RuntimeException {
    public CSVParsingException(String message, Class<?> originClass) {
        super(String.format("[%s] %s", originClass.getSimpleName(), message));
    }

    public CSVParsingException(String message, Class<?> originClass, Throwable cause) {
        super(String.format("[%s] %s", originClass.getSimpleName(), message), cause);
    }
}
