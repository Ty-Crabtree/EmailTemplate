package com.smarsh.pingidentity;

import java.util.List;

/**
 * This class is used to serialized json request messages for sending to the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected read group request json message structure.
 *
 * @author Steve Fusti
 *
 */
public class ReadGroupRequest {

    private String externalClientId;
    private String externalGroupId;

    public String getExternalClientId() {
        return externalClientId;
    }

    public void setExternalClientId(String externalClientId) {
        this.externalClientId = externalClientId;
    }

    public String getExternalGroupId() {
        return externalGroupId;
    }

    public void setExternalGroupId(String externalGroupId) {
        this.externalGroupId = externalGroupId;
    }

}
