package com.ralfhenze.railplan.application;

/**
 * A marker interface for a command to the domain model. A command actually changes
 * the state of the model (= it writes to the database).
 *
 * In CQRS we separate writing from reading requests (commands and queries) and
 * implement them in different objects, to keep a clear separation of responsibilities.
 *
 * http://www.cqrs.nu/Faq/command-query-responsibility-segregation
 */
public interface Command {}
