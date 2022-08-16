package consoleApp.exceptions;

public class UserQuitException extends Exception {
    public UserQuitException(String message) {
        super(message);
    }
}