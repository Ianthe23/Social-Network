package org.example.lab6networkfx.domain.validators;

import org.example.lab6networkfx.exceptions.ValidationException;

/**
 * Interface for a validator
 * @param <T> - the type of the entity to be validated
 */
public interface Validator<T> {
    /**
     * Method for validating an entity
     * @param entity - the entity to be validated
     * @throws ValidationException if the entity is not valid
     */
    void validate(T entity) throws ValidationException;
}