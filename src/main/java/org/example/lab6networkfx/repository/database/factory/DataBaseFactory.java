package org.example.lab6networkfx.repository.database.factory;

import org.example.lab6networkfx.domain.validators.Validator;
import org.example.lab6networkfx.repository.database.factory.DataBaseStrategy;
import org.example.lab6networkfx.repository.database.utils.AbstractDataBaseRepo;

/**
 * Interface for a database factory
 */
public interface DataBaseFactory {
    /**
     * Method to create a repository
     * @param strategy - the strategy
     * @param validator - the validator
     * @return AbstractDataBaseRepo
     */
    AbstractDataBaseRepo createRepo(DataBaseStrategy strategy, Validator validator);
}
