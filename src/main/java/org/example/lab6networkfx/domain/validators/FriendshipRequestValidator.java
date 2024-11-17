package org.example.lab6networkfx.domain.validators;

import org.example.lab6networkfx.domain.friendships.FriendshipRequest;
import org.example.lab6networkfx.exceptions.ValidationException;

import java.util.Objects;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) throws ValidationException {
        String toThrow="";

        if (entity.getDate() == null) {
            toThrow += "Date is null";
        }

        if (entity.getStatus() == null || (!entity.getStatus().equals("PENDING") && !entity.getStatus().equals("ACCEPTED") && !entity.getStatus().equals("REJECTED"))) {
            toThrow += "Status is invalid";
        }

        if (entity.getId().getFirst() == null || entity.getId().getSecond() == null) {
            toThrow += "Id is null";
        }

        if (Objects.equals(entity.getId().getFirst(), entity.getId().getSecond())) {
            toThrow += "Id is the same";
        }

        if (!toThrow.isEmpty()) {
            throw new ValidationException(toThrow);
        }
    }
}
