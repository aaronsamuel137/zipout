import java.io.IOException;

/**
 * Signals that a Zip exception of some sort has occurred.
 *
 * @author  unascribed
 * @see     java.io.IOException
 * @since   JDK1.0
 */

public
class ZipException extends IOException {
    /**
     * Constructs an <code>ZipException</code> with <code>null</code>
     * as its error detail message.
     */
    public ZipException() {
        super();
    }

    /**
     * Constructs an <code>ZipException</code> with the specified detail
     * message.
     *
     * @param   s   the detail message.
     */

    public ZipException(String s) {
        super(s);
    }
}
