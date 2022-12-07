package com.team5.team5_backend.response;

import com.team5.team5_backend.entity.User;

public class CreationResponse {
    String outcome;
    User user;
    public CreationResponse(String outcome, User user) {
        this.outcome = outcome;
        this.user = user;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
