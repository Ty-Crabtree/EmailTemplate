package com.smarsh.pingidentity;

import java.util.List;

/**
 * This class is used to serialized json request messages for sending to the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected update user request json message structure.
 * This request is used to both update and create users
 *
 * @author Mark Anderson
 *
 */
public class UpdateUserRequest {

    private String externalClientId;
    private List<User> users;

    public String getExternalClientId() {
        return externalClientId;
    }

    public void setExternalClientId(String externalClientId) {
        this.externalClientId = externalClientId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return String.format("User: externalClientId=%s users=%s\n", externalClientId, users);
    }
}
