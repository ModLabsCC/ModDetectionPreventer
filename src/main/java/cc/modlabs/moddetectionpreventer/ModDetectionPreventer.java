package cc.modlabs.moddetectionpreventer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ModDetectionPreventer {
    private ModDetectionPreventer() { throw new UnsupportedOperationException("Consider this class abstract-final"); }

    public static Logger LOGGER = LoggerFactory.getLogger("ModDetectionPreventer");


    /// Called when the server tries to abuse the exploit.
    public static void warnAccess(String action, String kind, String key) {
        LOGGER.warn("{} incoming request for {} `{}`", action, kind, key);
    }

}
