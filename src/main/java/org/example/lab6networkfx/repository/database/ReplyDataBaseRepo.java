package org.example.lab6networkfx.repository.database;

import org.example.lab6networkfx.domain.Tuple;
import org.example.lab6networkfx.domain.User;
import org.example.lab6networkfx.domain.messages.Message;
import org.example.lab6networkfx.domain.messages.ReplyMessage;
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
import java.util.Optional;

public class ReplyDataBaseRepo extends AbstractDataBaseRepo<Tuple<Integer, Integer>, ReplyMessage> {
    private final UserDataBaseRepo userRepo;
    private final MessageDataBaseRepo messageRepo;

    public ReplyDataBaseRepo(Validator<ReplyMessage> validator, DataBaseAcces data, String table, UserDataBaseRepo userRepo, MessageDataBaseRepo messageRepo) {
        super(validator, data, table);
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
    }

    private Optional<ReplyMessage> getReplyMessage(ResultSet resultSet) throws SQLException {
        int messageId = resultSet.getInt("message_id");
        int receiverId = resultSet.getInt("receiver_id");
        String messageText = resultSet.getString("message");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        Optional<User> receiver = userRepo.findOne(receiverId);
        Optional<Message> message = messageRepo.findOne(messageId);

        if (receiver.isPresent() && message.isPresent()) {
            ReplyMessage replyMessage = new ReplyMessage(message.get(), messageText, receiver.get(), createdAt);
            replyMessage.setId(new Tuple<>(messageId, receiverId));
            return Optional.of(replyMessage);
        }

        return Optional.empty();
    }

    @Override
    public Optional<ReplyMessage> findOne(Tuple<Integer, Integer> id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        
        String sql = " SELECT * FROM \"" + table + "\" WHERE message_id = ? AND receiver_id = ?";
        
        try {
            PreparedStatement statement = data.createStatement(sql);
            statement.setInt(1, id.getFirst());
            statement.setInt(2, id.getSecond());
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return getReplyMessage(resultSet);
            }
            
            return Optional.empty();
        } catch(SQLException e) {
            throw new RepoException(e);
        }
        
    }

    @Override
    public Iterable<ReplyMessage> findAll() {
        String sql = "SELECT * FROM \"" + table + "\"";
        List<ReplyMessage> replies = new ArrayList<>();
        
        try (PreparedStatement statement = data.createStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                getReplyMessage(resultSet).ifPresent(replies::add);
            }
        } catch (SQLException e) {
            throw new RepoException(e);
        }
        
        return replies;
    }

    @Override
    public Optional<ReplyMessage> save(ReplyMessage entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        
        String sql = "INSERT INTO \"" + table + "\" (message_id, receiver_id, message, created_at) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setInt(1, entity.getReplyFor().getId());
            statement.setInt(2, entity.getReceiver().getId());
            statement.setString(3, entity.getReply());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getDate()));
            
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    @Override
    public Optional<ReplyMessage> delete(Tuple<Integer, Integer> id) {
        Optional<ReplyMessage> entity = findOne(id);
        
        if (entity.isPresent()) {
            String deleteStatement = "DELETE FROM \"" + table + "\" WHERE message_id = ? AND receiver_id = ?";
            
            try (PreparedStatement statement = data.createStatement(deleteStatement)) {
                statement.setInt(1, id.getFirst());
                statement.setInt(2, id.getSecond());
                
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RepoException(e);
            }
        }
        
        return entity;
    }

    @Override
    public Optional<ReplyMessage> update(ReplyMessage entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        String sql = "UPDATE \"" + table + "\" SET message = ?, created_at = ? WHERE message_id = ? AND receiver_id = ?";

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setString(1, entity.getReply());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(entity.getDate()));
            statement.setInt(3, entity.getReplyFor().getId());
            statement.setInt(4, entity.getReceiver().getId());

            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }
}
