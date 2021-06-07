package com.smarsh.pingidentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to deserialized json response messages from the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected delete user response json message structure.
 *
 * @author Mark Anderson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteUserResponse {

    private List<User> userResults;
    private boolean success;
    private String errorMessages;

    public List<User> getUserResults() {
        return userResults;
    }

    public void setUserResults(List<User> userResults) {
        this.userResults = userResults;
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
        return String.format("<<< DeleteUserResponse: Users=%s  Success=%s  ErrorMessages=%s",
                userResults, success, errorMessages);
    }
}
