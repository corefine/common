package org.corefine.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IoUtil {

    public static String toString(InputStream in) {
        return toString(in, "UTF-8");
    }

    public static String toString(InputStream in, String charset) {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(in, charset)) {
            char[] buffer = new char[8 * 1024];
            int a;
            while ((a = isr.read(buffer, 0, buffer.length)) != -1) {
                sb.append(buffer, 0, a);
            }
        } catch (IOException e) {
            throw new RuntimeException("读取字符失败", e);
        }
        return sb.toString();
    }
}
