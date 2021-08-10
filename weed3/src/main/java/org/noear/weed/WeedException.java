package org.noear.weed;

/**
 * @author noear
 * @since 3.3
 */
public class WeedException extends RuntimeException {
    public WeedException(String message) {
        super(message);
    }

    public WeedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeedException(Throwable cause) {
        super(cause);
    }
}
