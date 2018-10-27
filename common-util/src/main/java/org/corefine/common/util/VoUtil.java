package org.corefine.common.util;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class VoUtil {

    public static <Entity, Vo> List<Vo> converts(List<Entity> list, Converter<Entity, Vo> converter) {
        if (list == null) {
            return null;
        }
        List<Vo> resultList = new ArrayList<>(list.size());
        for (Entity entity : list) {
            if (entity == null) {
                resultList.add(null);
            } else {
                resultList.add(converter.toVo(entity));
            }
        }
        return resultList;
    }

    public static <Entity, Vo> Vo convert(Entity entity, Converter<Entity, Vo> converter) {
        if (entity == null) {
            return null;
        }
        return converter.toVo(entity);
    }

    public static <T> TempValue<T> temp() {
        return new TempValue<>();
    }

    public static <T> TempValue<T> temp(T value) {
        return new TempValue<>(value);
    }

    public interface Converter<Entity, Vo> {
        Vo toVo(Entity entity);
    }

    public static class TempValue<T> {
        public T value;

        public TempValue() {}
        public TempValue(T value) {
            this.value = value;
        }
    }
}
