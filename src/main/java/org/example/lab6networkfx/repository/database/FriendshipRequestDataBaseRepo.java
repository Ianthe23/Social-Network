package org.example.lab6networkfx.repository.database;

import org.example.lab6networkfx.domain.Friendship;
import org.example.lab6networkfx.domain.Tuple;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.friendships.FriendshipRequest;
import org.example.lab6networkfx.domain.friendships.FriendshipStatus;
import org.example.lab6networkfx.domain.validators.Validator;
import org.example.lab6networkfx.exceptions.RepoException;
import org.example.lab6networkfx.repository.database.utils.AbstractDataBaseRepo;
import org.example.lab6networkfx.repository.database.utils.DataBaseAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FriendshipRequestDataBaseRepo extends AbstractDataBaseRepo<Tuple<Integer, Integer>, FriendshipRequest> {
    public FriendshipRequestDataBaseRepo(Validator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    @Override
    public Optional<FriendshipRequest> findOne(Tuple<Integer, Integer> id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }

        String sql = """
        SELECT 
            fr.sender_id, fr.receiver_id,
            u1.firstname AS firstName1, u1.lastname AS lastName1, u1.username AS username1, u1.password AS password1,
            u2.firstname AS firstName2, u2.lastname AS lastName2, u2.username AS username2, u2.password AS password2,
            fr.status, fr.created_at
        FROM 
            "FriendshipRequest" fr
        JOIN 
            "User" u1 ON fr.sender_id = u1.id
        JOIN 
            "User" u2 ON fr.receiver_id = u2.id
        WHERE 
            (fr.sender_id = ? AND fr.receiver_id = ?) OR (fr.sender_id = ? AND fr.receiver_id = ?);
        """;

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setInt(1, id.getFirst());
            statement.setInt(2, id.getSecond());
            statement.setInt(3, id.getSecond());
            statement.setInt(4, id.getFirst());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                FriendshipRequest friendshipRequest = getFriendshipRequest(resultSet);
                return Optional.of(friendshipRequest);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    private FriendshipRequest getFriendshipRequest(ResultSet resultSet) throws SQLException {
        Integer id1 = resultSet.getInt("sender_id");
        Integer id2 = resultSet.getInt("receiver_id");
        String firstName1 = resultSet.getString("firstName1");
        String lastName1 = resultSet.getString("lastName1");
        String username1 = resultSet.getString("username1");
        String password1 = resultSet.getString("password1");
        String firstName2 = resultSet.getString("firstName2");
        String lastName2 = resultSet.getString("lastName2");
        String username2 = resultSet.getString("username2");
        String password2 = resultSet.getString("password2");

        User user1 = new User(firstName1, lastName1, username1, password1);
        user1.setId(id1);
        User user2 = new User(firstName2, lastName2, username2, password2);
        user2.setId(id2);

        FriendshipStatus status = FriendshipStatus.valueOf(resultSet.getString("status"));
        LocalDateTime date = resultSet.getTimestamp("created_at").toLocalDateTime();
        FriendshipRequest friendshipRequest = new FriendshipRequest(user1, user2, status, date);
        return friendshipRequest;
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        String findAllStatement = """
        SELECT 
            fr.sender_id, fr.receiver_id,
            u1.firstname AS firstName1, u1.lastname AS lastName1, u1.username AS username1, u1.password AS password1,
            u2.firstname AS firstName2, u2.lastname AS lastName2, u2.username AS username2, u2.password AS password2,
            fr.status, fr.created_at
        FROM 
            "FriendshipRequest" fr
        JOIN 
            "User" u1 ON fr.sender_id = u1.id
        JOIN 
            "User" u2 ON fr.receiver_id = u2.id;
        """;

        Set<FriendshipRequest> friendshipRequests = new HashSet<>();

        try (PreparedStatement statement = data.createStatement(findAllStatement)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                FriendshipRequest friendshipRequest = getFriendshipRequest(resultSet);
                friendshipRequests.add(friendshipRequest);
            }
        } catch (SQLException e) {
            throw new RepoException(e);
        }

        return friendshipRequests;
    }

    @Override
    public Optional<FriendshipRequest> save(FriendshipRequest entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        String sql = """
        INSERT INTO "FriendshipRequest" (sender_id, receiver_id, status, created_at)
        VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setInt(1, entity.getUser1().getId());
            statement.setInt(2, entity.getUser2().getId());
            statement.setString(3, entity.getStatus().toString());

            String formattedDate = formatter(entity.getDate());
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yy");
            LocalDate parsedDate = LocalDate.parse(formattedDate, dateFormatter);

            statement.setDate(4, java.sql.Date.valueOf(parsedDate));

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                FriendshipRequest friendshipRequest = getFriendshipRequest(resultSet);
                return Optional.of(friendshipRequest);
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    private String formatter(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        return time.format(formatter);
    }

    @Override
    public Optional<FriendshipRequest> delete(Tuple<Integer, Integer> id) {
        Optional<FriendshipRequest> entity = findOne(id);

        if (id != null) {
            String deleteStatement  = "DELETE FROM \"" + table + "\"" + " WHERE (sender_id = ? AND receiver_id = ?) OR (receiver_id = ? AND sender_id = ?)";
            int response = 0;
            try {
                PreparedStatement statement = data.createStatement(deleteStatement);
                statement.setInt(1, id.getFirst());
                statement.setInt(2, id.getSecond());
                statement.setInt(3, id.getFirst());
                statement.setInt(4, id.getSecond());
                if (entity.isPresent()) {
                    response = statement.executeUpdate();
                }
                return response == 0 ? Optional.empty() : entity;
            } catch (SQLException e) {
                throw new RepoException(e);
            }
        }
        else {
            throw new IllegalArgumentException("ID must not be null");
        }
    }

    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        String updateStatement = """
        UPDATE "FriendshipRequest"
        SET status = ?
        WHERE (sender_id = ? AND receiver_id = ?) OR (receiver_id = ? AND sender_id = ?)
        """;

        try (PreparedStatement statement = data.createStatement(updateStatement)) {
            statement.setString(1, entity.getStatus().toString());
            statement.setInt(2, entity.getUser1().getId());
            statement.setInt(3, entity.getUser2().getId());
            statement.setInt(4, entity.getUser1().getId());
            statement.setInt(5, entity.getUser2().getId());

            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }
}
