package com.smarsh.pingidentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to deserialized json response messages from the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected read user response json message structure.
 *
 * @author Mark Anderson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadUserResponse {

    private List<User> users;
    private boolean success;
    private String errorMessages;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    @Override
    public String toString() {
        return String.format("<<< ReadUserResponse: Users=%s  Success: %s  ErrorMessages=%s",
                users, success, errorMessages);
    }
}
