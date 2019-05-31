package org.noear.weed.ext;

/**
 * Created by yuety on 14-9-16.
 */
public interface Act1Ex <P1,Ex extends Throwable> {
    public void run(P1 p1) throws Ex;
}
