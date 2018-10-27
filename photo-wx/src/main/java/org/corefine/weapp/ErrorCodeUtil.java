package org.corefine.weapp;

import java.io.IOException;
import java.util.Properties;

public class ErrorCodeUtil {
    private static final Properties configs;
    static {
        configs = new Properties();
        try {
            configs.load(ErrorCodeUtil.class.getClassLoader().getResourceAsStream("errormsg.properties"));
        } catch (IOException e) {
            throw new RuntimeException("加载配置异常:errormsg.properties", e);
        }
    }

    public static String getMessage(int errorcode) {
        return configs.getProperty(Integer.toString(errorcode));
    }
}
