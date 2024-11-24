package org.example.lab6networkfx.repository.database;

import org.example.lab6networkfx.domain.Tuple;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.messages.Message;
import org.example.lab6networkfx.domain.validators.Validator;
import org.example.lab6networkfx.exceptions.RepoException;
import org.example.lab6networkfx.repository.database.utils.AbstractDataBaseRepo;
import org.example.lab6networkfx.repository.database.utils.DataBaseAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MessageDataBaseRepo extends AbstractDataBaseRepo<Integer, Message> {
    public MessageDataBaseRepo(Validator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    private Optional<Message> getMessage(ResultSet resultSet, Integer id) throws SQLException {
        try {
            String firstName1 = resultSet.getString("firstname1");
            String lastName1 = resultSet.getString("lastname1");
            String username1 = resultSet.getString("username1");
            String password1 = resultSet.getString("password1");
            User from = new User(firstName1, lastName1, username1, password1);

            String firstName2 = resultSet.getString("firstname2");
            String lastName2 = resultSet.getString("lastname2");
            String username2 = resultSet.getString("username2");
            String password2 = resultSet.getString("password2");
            User to = new User(firstName2, lastName2, username2, password2);

            String message = resultSet.getString("message");
            LocalDateTime date = resultSet.getTimestamp("created_at").toLocalDateTime();
            Message msg = new Message(message, date, to, from);
            msg.setId(id);
            return Optional.of(msg);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        String sql = """
                SELECT 
                    m.id, m.message, m.created_at,
                    u1.firstname AS firstname1, u1.lastname AS lastname1, u1.username AS username1, u1.password AS password1,
                    u2.firstname AS firstname2, u2.lastname AS lastname2, u2.username AS username2, u2.password AS password2
                FROM 
                    "Message" m
                JOIN 
                    "User" u1 ON m.sender_id = u1.id
                JOIN 
                    "User" u2 ON m.receiver_id = u2.id
                WHERE 
                    m.id = ?;
                """;

        try {
            PreparedStatement statement = data.createStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getMessage(resultSet, id);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        String sql = """
                SELECT 
                    m.id, m.message, m.created_at,
                    u1.firstname AS firstname1, u1.lastname AS lastname1, u1.username AS username1, u1.password AS password1,
                    u2.firstname AS firstname2, u2.lastname AS lastname2, u2.username AS username2, u2.password AS password2
                FROM 
                    "Message" m
                JOIN 
                    "User" u1 ON m.sender_id = u1.id
                JOIN 
                    "User" u2 ON m.receiver_id = u2.id;
                """;
        List<Message> messages = new ArrayList<>();

        try (PreparedStatement statement = data.createStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Optional<Message> optionalMessage = getMessage(resultSet, id);
                optionalMessage.ifPresent(messages::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return messages;
    }

    @Override
    public Optional<Message> save(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        String sql = "INSERT INTO \"" + table + "\" (message, created_at, receiver_id, sender_id) VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setString(1, entity.getMessage());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(entity.getDate()));
            statement.setInt(3, entity.getTo().getId());
            statement.setInt(4, entity.getFrom().getId());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                entity.setId(id);
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    @Override
    public Optional<Message> delete(Integer integer) {
        Optional<Message> entity = findOne(integer);
        int response = 0;
        if (integer != null) {
            String deleteStatement = "DELETE FROM \"" + table + "\" WHERE id = ?";
            try {
                PreparedStatement statement = data.createStatement(deleteStatement);
                if (entity.isPresent()) {
                    statement.setInt(1, integer);
                    response = statement.executeUpdate();
                }
                return response > 0 ? entity : Optional.empty();
            } catch (SQLException e) {
                throw new RepoException(e);
            }
        } else {
            throw new IllegalArgumentException("ID must not be null");
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        validator.validate(entity);

        if (findOne(entity.getId()).isEmpty()) {
            throw new RepoException("Entity does not exist");
        }

        String sql = "UPDATE \"" + table + "\" SET message = ?, created_at = ?, receiver_id = ?, sender_id = ? WHERE id = ?";

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setString(1, entity.getMessage());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(entity.getDate()));
            statement.setInt(3, entity.getTo().getId());
            statement.setInt(4, entity.getFrom().getId());
            statement.setInt(5, entity.getId());

            int response = statement.executeUpdate();
            return response > 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }
}
