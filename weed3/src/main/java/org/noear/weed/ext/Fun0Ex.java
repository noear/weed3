package org.noear.weed.ext;

/**
 * Created by noear on 14-9-12.
 */
public interface Fun0Ex <T,Ex extends Throwable> {
    T run() throws Ex;
}
