package org.noear.weed.ext;

/**
 * Created by noear on 14-9-16.
 */
public interface Act1Ex <P1,Ex extends Throwable> {
     void run(P1 p1) throws Ex;
}
