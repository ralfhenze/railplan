package com.ralfhenze.railplan.domain.common;

/**
 * An unchecked Exception to indicate that an entity of given field did not exist
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(final String entityType, final String id) {
        this(entityType, "ID", id);
    }

    public EntityNotFoundException(
        final String entityType,
        final String field,
        final String value
    ) {
        super("Couldn't find " + entityType + " of " + field + " \"" + value + "\"");
    }
}
