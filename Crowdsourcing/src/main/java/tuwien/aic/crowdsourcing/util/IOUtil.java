
package tuwien.aic.crowdsourcing.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class IOUtil {

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            ;
        }
    }

    public static void flush(Flushable flushable) {
        try {
            flushable.flush();
        } catch (IOException e) {
            ;
        }
    }

    public static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            ;
        }
    }

    public static void copy(InputStream source, OutputStream destination, boolean closeDestination) {
        if ((source == null) || (destination == null)) {
            close(source);
            close(destination);

            return;
        }
        source = new BufferedInputStream(source);
        destination = new BufferedOutputStream(destination);

        try {
            final byte[] buffer = new byte[1024];
            int len;

            while ((len = source.read(buffer)) != -1) {
                destination.write(buffer, 0, len);
            }
            // LOGGER.debug("Flushing destination .");
            flush(destination);
            // LOGGER.debug("Destination flushed.");

        } catch (final IOException e) {
            // LOGGER.warn("Cannot flush: "+e.getMessage());
            e.getMessage();
        } finally {
            // LOGGER.debug("Entered in finally clause.");
            if (closeDestination) {
                close(destination);
            }
            close(source);
            // LOGGER.debug("Finished finally clause.");

        }
    }
}
