package org.idea.live.common.interfaces.utils;

import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;

import ch.qos.logback.core.PropertyDefinerBase;

public class IpLogConversionRule extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        return this.getLogIndex();
    }

    private String getLogIndex() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000));
    }
    
}
