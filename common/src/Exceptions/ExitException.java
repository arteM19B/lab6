package Exceptions;

public class ExitException extends RuntimeException {
    public ExitException() {
        super("exit");
    }
}
