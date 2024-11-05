package org.example.repository.database.factory;

import org.example.repository.database.utils.AbstractDataBaseRepo;
import org.example.repository.database.factory.DataBaseStrategy;
import org.example.domain.validators.Validator;

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
