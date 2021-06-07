package com.smarsh.pingidentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to deserialized json response messages from the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected read group response json message structure.
 *
 * @author Steve Fusti
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadGroupResponse {

    private Group group;
    private boolean success;
    private String errorMessages;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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
        return String.format("<<< ReadGroupResponse: Group=%s  Success: %s  ErrorMessages=%s",
                group, success, errorMessages);
    }
}
