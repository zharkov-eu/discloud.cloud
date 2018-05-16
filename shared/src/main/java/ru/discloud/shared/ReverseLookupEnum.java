package ru.discloud.shared;

import java.util.HashMap;
import java.util.Map;

public class ReverseLookupEnum<T extends Enum<T>> {
    private final Map<String, T> lookup = new HashMap<>();

    public ReverseLookupEnum(Class<T> enumerate) {
        for (T value : enumerate.getEnumConstants()) {
            lookup.put(value.toString(), value);
        }
    }

    public T get(String representation)
    {
        return lookup.get(representation);
    }
}
