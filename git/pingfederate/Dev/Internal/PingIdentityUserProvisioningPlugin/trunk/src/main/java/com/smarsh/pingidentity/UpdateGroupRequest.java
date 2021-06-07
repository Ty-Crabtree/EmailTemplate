package com.smarsh.pingidentity;

import java.util.List;

/**
 * This class is used to serialized json request messages for sending to the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected update group request json message structure.
 * This request is used to both update and create groups
 *
 * @author Steve Fusti
 *
 */
public class UpdateGroupRequest {

    private String externalClientId;
    private Group group;

    public String getExternalClientId() {
        return externalClientId;
    }

    public void setExternalClientId(String externalClientId) {
        this.externalClientId = externalClientId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return String.format("User: externalClientId=%s group=%s\n", externalClientId, group);
    }
}
