package com.lib.basiclib.utils

import java.io.Closeable
import java.io.IOException

/**

 * @since 3/26/18 10:18 PM
 */
object CloseUtils {

    /**
     * Close the io stream.
     *
     * @param closeables closeables
     */
    fun closeIO(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Close the io stream quietly.
     *
     * @param closeables closeables
     */
    fun closeIOQuietly(vararg closeables: Closeable?) {
        for (closeable in closeables) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (ignored: IOException) {
                }

            }
        }
    }

}