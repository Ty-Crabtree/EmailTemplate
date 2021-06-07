package com.smarsh.pingidentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is used to serialize and deserialized json response messages from the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected request and response json message structures.
 *
 * @author Steve Fusti
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class GroupMember {

    private String externalMemberId;
    private String memberType;

    public String getExternalMemberId() {
        return externalMemberId;
    }

    public void setExternalMemberId(String externalMemberId) {
        this.externalMemberId = externalMemberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    @Override
    public String toString() {
        return String.format("GroupMember: externalMemberId=%s  memberType=%s\n",
                externalMemberId, memberType);
    }

}
