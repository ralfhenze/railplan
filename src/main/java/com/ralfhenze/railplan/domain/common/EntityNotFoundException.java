package com.ralfhenze.railplan.domain.common;

/**
 * An unchecked Exception to indicate that an entity of given ID
 * did not exist in the persistence mechanism
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityType, String id) {
        super("Couldn't find " + entityType + " of ID \"" + id + "\"");
    }
}
