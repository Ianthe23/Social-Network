package org.example.lab6networkfx.repository.database.factory;

import org.example.lab6networkfx.domain.validators.Validator;
import org.example.lab6networkfx.exceptions.RepoException;
import org.example.lab6networkfx.repository.database.FriendshipDataBaseRepo;
import org.example.lab6networkfx.repository.database.FriendshipRequestDataBaseRepo;
import org.example.lab6networkfx.repository.database.UserDataBaseRepo;
import org.example.lab6networkfx.repository.database.factory.DataBaseStrategy;
import org.example.lab6networkfx.repository.database.utils.AbstractDataBaseRepo;
import org.example.lab6networkfx.repository.database.utils.DataBaseAcces;

/**
 * Factory for creating database repositories
 */
public class DataBaseRepoFactory implements DataBaseFactory {
    private final DataBaseAcces data;

    /**
     * Constructor
     * @param data - the database access
     */
    public DataBaseRepoFactory(DataBaseAcces data ) {
        this.data = data;
    }

    /**
     * Method to create a repository
     * @param strategy - the strategy
     * @param validator - the validator
     * @return AbstractDataBaseRepo
     */
    @Override
    public AbstractDataBaseRepo createRepo(DataBaseStrategy strategy, Validator validator) {
        switch (strategy) {
            case User -> {
                return new UserDataBaseRepo(validator, data, strategy.toString());
            }
            case Friendship -> {
                return new FriendshipDataBaseRepo(validator, data, strategy.toString());
            }
            case FriendshipRequest -> {
                return new FriendshipRequestDataBaseRepo(validator, data, strategy.toString());
            }
            default -> {
                throw new RepoException("Invalid strategy");
            }
        }
    }
}
