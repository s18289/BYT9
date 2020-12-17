package exceptions;

public class WebPageMonitorException extends RuntimeException {

    public WebPageMonitorException(Throwable cause) {
        super(cause);
    }

    public WebPageMonitorException(String message) {
        super(message);
    }

    public WebPageMonitorException(String message, Throwable cause) {
        super(message, cause);
    }
}