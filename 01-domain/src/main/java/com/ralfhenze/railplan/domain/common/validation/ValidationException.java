package com.ralfhenze.railplan.domain.common.validation;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {

    private final ImmutableMap<String, MutableList<String>> errorMessages;

    public ValidationException(final Map<String, List<String>> errorMessages) {
        final MutableMap<String, MutableList<String>> map = Maps.mutable.empty();
        errorMessages.forEach((field, messages) -> map.put(field, Lists.mutable.ofAll(messages)));

        this.errorMessages = map.toImmutable();
    }

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
