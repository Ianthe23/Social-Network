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
    private final UserDataBaseRepo userRepo;

    public MessageDataBaseRepo(Validator validator, DataBaseAcces data, String table, UserDataBaseRepo userRepo) {
        super(validator, data, table);
        this.userRepo = userRepo;
    }

    private Optional<Message> getMessage(ResultSet resultSet, Integer id) throws SQLException {
        int senderId = resultSet.getInt("sender_id");
        String messageText = resultSet.getString("message");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        Optional<User> sender = userRepo.findOne(senderId);
        if (sender.isEmpty()) {
            return Optional.empty();
        }

        List<User> receivers = getReceiversForMessage(id);
        Message message = new Message(sender.get(), messageText, createdAt);
        message.setTo(receivers);
        message.setId(id);

        return Optional.of(message);
    }

    private List<User> getReceiversForMessage(Integer messageId) throws SQLException {
        String sql = "SELECT * FROM \"ReplyMessage\" WHERE message_id = ?";
        List<User> receivers = new ArrayList<>();

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setInt(1, messageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int receiverId = resultSet.getInt("receiver_id");
                    userRepo.findOne(receiverId).ifPresent(receivers::add);
                }
            }
        }
        return receivers;
    }

    @Override
    public Optional<Message> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        String sql = "SELECT * FROM \"" + table + "\" WHERE id = ?";

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
        String sql = "SELECT * FROM \"" + table + "\"";
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

        String sql = "INSERT INTO \"" + table + "\" (sender_id, message, created_at) VALUES (?, ?, ?) RETURNING id";

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setInt(1, entity.getFrom().getId());
            statement.setString(2, entity.getMessage());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(entity.getDate()));

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
        if (integer != null) {
            String deleteStatement = "DELETE FROM \"" + table + "\" WHERE id = ?";
            try {
                PreparedStatement statement = data.createStatement(deleteStatement);
                statement.setInt(1, integer);
                int response = statement.executeUpdate();
                return response == 0 ? entity : Optional.empty();
            } catch (SQLException e) {
                throw new RepoException(e);
            }
        } else {
            throw new IllegalArgumentException("ID must not be null");
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
}
