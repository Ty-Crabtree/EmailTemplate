package com.smarsh.pingidentity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is used to serialize and deserialized json response messages from the Smarsh Identify Store Web Service.
 * The structure of this class was determined by the expected request and response json message structures.
 *
 * @author Mark Anderson
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class User {

    private String externalClientId;
    private String externalUserId;
    private String firstName;
    private String lastName;
    private String email;
    private String title;
    private String userRole;
    private String socialMediaChannels;
    private String socialIdentifiers;
    private Boolean isActive;
    private Boolean isUnderLegalHold;
	private Boolean ssoLoginOnly;
    private String smarshUserId;
    private List<String> additionalEmails;
    private List<String> errors;
    private Boolean success = true;

    public String getExternalClientId() {
        return externalClientId;
    }

    public void setExternalClientId(String externalClientId) {
        this.externalClientId = externalClientId;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getSocialMediaChannels() {
        return socialMediaChannels;
    }

    public void setSocialMediaChannels(String socialMediaChannels) {
        this.socialMediaChannels = socialMediaChannels;
    }

    public String getSocialIdentifiers() {
        return socialIdentifiers;
    }

    public void setSocialIdentifiers(String socialIdentifiers) {
        this.socialIdentifiers = socialIdentifiers;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsUnderLegalHold() {
        return isUnderLegalHold;
    }

    public void setIsUnderLegalHold(Boolean isUnderLegalHold) {
        this.isUnderLegalHold = isUnderLegalHold;
    }

	public Boolean getSsoLoginOnly() {
        return ssoLoginOnly;
    }
	
	public void setSsoLoginOnly(Boolean ssoLoginOnly) {
        this.ssoLoginOnly = ssoLoginOnly;
    }
	
    public String getSmarshUserId() {
        return smarshUserId;
    }

    public void setSmarshUserId(String smarshUserId) {
        this.smarshUserId = smarshUserId;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getAdditionalEmails() {
        return additionalEmails;
    }

    public void setAdditionalEmails(List<String> additionalEmails) {
        this.additionalEmails = additionalEmails;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return String.format("User: externalClientId=%s  externalUserId=%s  firstName=%s  lastName=%s  email=%s active=%s smarshUserId=%s  errors=%s additionalEmails=%s success=%s\n",
                externalClientId, externalUserId, firstName, lastName, email, isActive, smarshUserId, errors, additionalEmails, success);
    }

}
