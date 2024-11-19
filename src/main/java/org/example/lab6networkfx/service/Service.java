package org.example.lab6networkfx.service;

import org.example.lab6networkfx.domain.User;

import java.util.List;

/**
 * Interface for the service
 * @param <ID> - the type of the id of the entities
 */
public interface Service<ID> {
    /**
     * Method for adding a user
     * @param firstName - the first name of the user
     * @param lastName - the last name of the user
     * @param username - the username of the user
     * @param password - the password of the user
     * @return true - if the user was added
     *         false - if the user already exists
     */
    boolean addUser(String firstName, String lastName, String username, String password);

    /**
     * Method for removing a user
     * @param username - the username of the user to be removed
     * @return true - if the user was removed
     *         false - if the user does not exist
     */
    User removeUser(String username);

    /**
     * Method for sending a friendship request
     * @param username1 - the username of the first user
     * @param username2 - the username of the second user
     * @return
     */
    boolean pendingFriendshipRequest(String username1, String username2);

    /**
     * Method for rejecting a friendship request
     * @param username1 - the username of the first user
     * @param username2 - the username of the second user
     * @return
     */
    boolean rejectFriendshipRequest(String username1, String username2);

    /**
     * Method for accepting a friendship request
     * @param username1 - the username of the first user
     * @param username2 - the username of the second user
     * @return
     */
    boolean acceptFriendshipRequest(String username1, String username2);

    /**
     * Method for adding a friendship
     * @param username1 - the username of the first user
     * @param username2 - the username of the second user
     * @return true - if the friendship was added
     *         false - if the friendship already exists
     */
    boolean addFriendship(String username1, String username2);

    /**
     * Method for removing a friendship
     * @param username1 - the username of the first user
     * @param username2 - the username of the second user
     * @return true - if the friendship was removed
     *         false - if the friendship does not exist
     */
    boolean removeFriendship(String username1, String username2);

    /**
     * Method for getting all users
     * @return all users
     */
    Iterable getAllUsers();

    /**
     * Method for getting all friendships
     * @return all friendships
     */
    Iterable getAllFriendships();

    /**
     * Method for getting all friendship requests
     * @return all friendship requests
     */
    Iterable getAllFriendshipRequests();

    /**
     * Method for getting the number of communities
     * @return the number of communities
     */
    int numberOfCommunities();

    /**
     * Method for getting the biggest community
     * @return the biggest community
     */
    List<List<User>> biggestCommunity();
}
