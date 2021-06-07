package com.smarsh.pingidentity;

import java.util.List;

/**
 * This class is used to serialized json request messages for sending to the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected delete user request json message structure.
 *
 * @author Mark Anderson
 *
 */
public class DeleteUserRequest {

    private String externalClientId;
    private List<String> externalUserIds;

    public String getExternalClientId() {
        return externalClientId;
    }

    public void setExternalClientId(String externalClientId) {
        this.externalClientId = externalClientId;
    }

    public List<String> getExternalUserIds() {
        return externalUserIds;
    }

    public void setExternalUserIds(List<String> externalUserIds) {
        this.externalUserIds = externalUserIds;
    }

}
