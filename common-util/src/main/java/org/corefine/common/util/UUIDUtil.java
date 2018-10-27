package org.corefine.common.util;

import java.util.UUID;

public class UUIDUtil {
    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createToken(Long userId) {
        return createUUID();
    }
}
