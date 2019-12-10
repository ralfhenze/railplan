package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {

    private final ImmutableMap<String, MutableList<String>> errorMessages;

    ValidationException(final ImmutableMap<String, MutableList<String>> errorMessages) {
        super(errorMessages.collect(m -> "\n* " + m).toString());
        this.errorMessages = errorMessages;
    }

    public ImmutableMap<String, MutableList<String>> getErrorMessages() {
        return errorMessages;
    }

    public Map<String, List<String>> getErrorMessagesAsHashMap() {
        final Map<String, List<String>> hashMap = new HashMap<>();

        errorMessages.forEachKeyValue((String key, MutableList<String> value) ->
            hashMap.put(key, value.toImmutable().castToList())
        );

        return hashMap;
    }
}
