package com.smarsh.pingidentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used to deserialized json response messages from the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected update group response json message structure.
 *
 * @author Steve Fusti
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateGroupResponse {

    private Group groupResult;
    private boolean success;
    private String errorMessages;

    public Group getGroupResult() {
        return groupResult;
    }

    public void setGroupResult(Group groupResult) {
        this.groupResult = groupResult;
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
        return String.format("<<< UpdateGroupResponse:  Group=%s  Success=%s  ErrorMessages=%s",
                groupResult, success, errorMessages);
    }
}
