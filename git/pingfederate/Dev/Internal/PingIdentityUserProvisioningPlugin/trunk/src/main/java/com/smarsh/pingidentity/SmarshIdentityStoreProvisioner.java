package com.smarsh.pingidentity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sourceid.saml20.adapter.attribute.AttrValueSupport;
import org.sourceid.saml20.adapter.attribute.AttributeValue;
import org.sourceid.saml20.adapter.conf.Configuration;
import org.sourceid.saml20.adapter.gui.TextFieldDescriptor;
import org.sourceid.util.log.AttributeMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingidentity.sdk.GuiConfigDescriptor;
import com.pingidentity.sdk.IdentityStoreProvisionerDescriptor;
import com.pingidentity.sdk.PluginDescriptor;
import com.pingidentity.sdk.provision.Constants;
import com.pingidentity.sdk.provision.IdentityStoreProvisioner;
import com.pingidentity.sdk.provision.exception.ConflictException;
import com.pingidentity.sdk.provision.exception.IdentityStoreException;
import com.pingidentity.sdk.provision.exception.BadRequestException;
import com.pingidentity.sdk.provision.exception.NotFoundException;
import com.pingidentity.sdk.provision.groups.request.CreateGroupRequestContext;
import com.pingidentity.sdk.provision.groups.request.DeleteGroupRequestContext;
import com.pingidentity.sdk.provision.groups.request.MemberAttribute;
import com.pingidentity.sdk.provision.groups.request.ReadGroupRequestContext;
import com.pingidentity.sdk.provision.groups.request.UpdateGroupRequestContext;
import com.pingidentity.sdk.provision.groups.response.GroupResponseContext;
import com.pingidentity.sdk.provision.groups.response.GroupResponseContextImpl;
import com.pingidentity.sdk.provision.users.request.CreateUserRequestContext;
import com.pingidentity.sdk.provision.users.request.DeleteUserRequestContext;
import com.pingidentity.sdk.provision.users.request.ReadUserRequestContext;
import com.pingidentity.sdk.provision.users.request.UpdateUserRequestContext;
import com.pingidentity.sdk.provision.users.response.UserResponseContextImpl;

/**
 * This class is the primary implementation of the Smarsh Identity Store Provisioner Ping Identify Plugin
 *
 * @author Mark Anderson
 *
 */
public class SmarshIdentityStoreProvisioner implements IdentityStoreProvisioner
{
    private static final Log log = LogFactory.getLog(SmarshIdentityStoreProvisioner.class);

    // Plugin static String values.
    private static final String PLUGIN_TYPE = "Smarsh Identity Store Provisioner";
    private static final String PLUGIN_VERSION = "2.1";
    private static final String PLUGIN_DESCRIPTION = "Feeds user and group received via SCIM to Smarsh Provisioning Service";

    // UI Configurable SmarshIdentifyStorePropertiesFile
    private String configurableSmarshIdentifyStorePropertiesFile = null;
    private static final String defaultSmarshIdentifyStorePropertiesFile = "configuration required";
    protected static final String PROPERTIESFILE_TEXTFIELD_NAME = "Smarsh Identify Store Properties File";
    private static final String PROPERTIESFILE_TEXTFIELD_DESCRIPTION = "Identifies the Properties File for the Smarsh Identity Store Provisioner";

    // Properties File Configurable SmarshIdentifyStoreURL
    private String configurableSmarshIdentifyStoreURL = null;
    private static final String SMARSHIDENTIFYSTOREURL_PROPERTY_NAME = "SmarshIdentifyStoreURL";

    // Properties File Configurable AuthenticationToken
    private String configurableAuthenticationToken = null;
    private static final String AUTHENTICATIONTOKEN_PROPERTY_NAME = "AuthenticationToken";

    // The PluginDescriptor that defines this plugin.
    private final IdentityStoreProvisionerDescriptor descriptor;

    // Plugin Contract Fields
    protected static final String FIELD_ACTUAL_EXTERNAL_CLIENT_ID = "actualExternalClientId";
    protected static final String FIELD_EXPECTED_EXTERNAL_CLIENT_ID = "expectedExternalClientId";
    protected static final String FIELD_EXTERNAL_GROUP_ID = "externalGroupId";
    protected static final String FIELD_EMAIL = "email";
    protected static final String FIELD_ACTIVE = "active";
    protected static final String FIELD_EXTERNAL_USER_ID = "externalUserId";
    protected static final String FIELD_FIRSTNAME = "firstName";
    protected static final String FIELD_LASTNAME = "lastName";
    protected static final String FIELD_GROUPNAME = "groupName";
    protected static final String FIELD_TITLE = "title";
    protected static final String FIELD_ROLE = "role";
    protected static final String FIELD_SOCIAL_MEDIA_CHANNELS = "socialMediaChannels";
    protected static final String FIELD_SOCIAL_IDENTIFIERS = "socialIdentifiers";
    protected static final String FIELD_LEGAL_HOLD = "underLegalHold";
	protected static final String FIELD_SSO_LOGIN_ONLY = "ssoLoginOnly";

    protected static final String HEADER_AUTHENTICATION_TOKEN = "AuthenticationToken";

    private static final String SOURCE_APPLICATION_TOKEN = "SourceApplication";
    private static final String SOURCE_APPLICATION_VALUE = "ADSync";

    private static final String USER_NOT_FOUND = "User not found: ";
    private static final String GROUP_NOT_FOUND = "Group not found: ";

    /**
     * Creates a new identity store provisioner and initialize its GUI descriptor.
     */
    public SmarshIdentityStoreProvisioner()
    {
        super();

        TextFieldDescriptor tfd;

        log.debug("SmarshIdentityStoreProvisioner  20170901");

        // Construct a GuiConfigDescriptor to hold custom gui web controls
        GuiConfigDescriptor guiDescriptor = new GuiConfigDescriptor();

        // Add a description.
        guiDescriptor.setDescription(PLUGIN_DESCRIPTION);

        tfd = new TextFieldDescriptor(PROPERTIESFILE_TEXTFIELD_NAME, PROPERTIESFILE_TEXTFIELD_DESCRIPTION);
        tfd.setDefaultValue(defaultSmarshIdentifyStorePropertiesFile);
        guiDescriptor.addField(tfd);

        // Load the guiDescriptor into the PluginDescriptor.
        descriptor = new IdentityStoreProvisionerDescriptor(PLUGIN_TYPE, this, guiDescriptor, new HashSet<String>(), PLUGIN_VERSION);

		// add the core contract field names for users
        String[] contracts = { FIELD_ACTUAL_EXTERNAL_CLIENT_ID, FIELD_EXPECTED_EXTERNAL_CLIENT_ID, FIELD_EXTERNAL_USER_ID, FIELD_FIRSTNAME, FIELD_LASTNAME, FIELD_EMAIL, FIELD_ACTIVE, FIELD_TITLE, FIELD_ROLE, FIELD_SOCIAL_MEDIA_CHANNELS, FIELD_SOCIAL_IDENTIFIERS, FIELD_LEGAL_HOLD, FIELD_SSO_LOGIN_ONLY};
        Set<String> contractSet = new HashSet<String>(Arrays.asList(contracts));
        descriptor.setAttributeContractSet(contractSet);

		// add the core contract fields names for groups - note that members is not included here as it is handled differently
        String[] groupContracts = { FIELD_EXPECTED_EXTERNAL_CLIENT_ID, FIELD_EXTERNAL_GROUP_ID, FIELD_GROUPNAME };
        Set<String> groupContractSet = new HashSet<String>(Arrays.asList(groupContracts));
		descriptor.setGroupAttributeContractSet(groupContractSet);

        // Allow the contract to be extended.
        descriptor.setSupportsExtendedContract(true);
        descriptor.setSupportsExtendedGroupContract(true);
    }

    public String getConfigurableSmarshIdentifyStorePropertiesFile() {
        return configurableSmarshIdentifyStorePropertiesFile;
    }

    public String getConfigurableSmarshIdentifyStoreURL() {
        return configurableSmarshIdentifyStoreURL;
    }

    public String getConfigurableAuthenticationToken() {
        return configurableAuthenticationToken;
    }

    /**
     * Use the Ping UI configured properties file name to load the following configuration values from properties file:
     * Smarsh Identify Store URL
     * Authentication Token
     */
    @Override
    public void configure(Configuration configuration)
    {
        configurableSmarshIdentifyStorePropertiesFile = configuration.getFieldValue(PROPERTIESFILE_TEXTFIELD_NAME);
        log.info(String.format("Read configuration %s = %s", PROPERTIESFILE_TEXTFIELD_NAME, configurableSmarshIdentifyStorePropertiesFile));

        if (configurableSmarshIdentifyStorePropertiesFile == null || configurableSmarshIdentifyStorePropertiesFile.equals(defaultSmarshIdentifyStorePropertiesFile)) {
            log.error(PROPERTIESFILE_TEXTFIELD_NAME + " not properly configured in UI");
        } else {
            try {
                Properties loadedProperties = new Properties();
                loadedProperties.load(new FileReader(new File(configurableSmarshIdentifyStorePropertiesFile)));
                configurableSmarshIdentifyStoreURL = loadedProperties.getProperty(SMARSHIDENTIFYSTOREURL_PROPERTY_NAME);
                log.info(String.format("Read configuration %s = %s", SMARSHIDENTIFYSTOREURL_PROPERTY_NAME, configurableSmarshIdentifyStoreURL));

                configurableAuthenticationToken = loadedProperties.getProperty(AUTHENTICATIONTOKEN_PROPERTY_NAME); // don't log this one
            } catch (IOException e) {
                log.error("Could not read properties file " + configurableSmarshIdentifyStorePropertiesFile);
            }
        }

        if (configurableSmarshIdentifyStoreURL == null) {
            log.error(SMARSHIDENTIFYSTOREURL_PROPERTY_NAME + " not properly configured in properties file");
        }

        if (configurableAuthenticationToken == null) {
            log.error(AUTHENTICATIONTOKEN_PROPERTY_NAME + " not properly configured in properties file");
        }
    }

    @Override
    public PluginDescriptor getPluginDescriptor()
    {
        return descriptor;
    }

    @Override
    public IdentityStoreProvisionerDescriptor getIdentityStoreProvisionerDescriptor()
    {
        return descriptor;
    }

    @Override
    public UserResponseContextImpl createUser(CreateUserRequestContext createRequestCtx) throws IdentityStoreException
    {
        log.debug(String.format("CreateUserRequestContext getEntityId() = %s", createRequestCtx.getEntityId()));
        return createAndUpdateUserHelper(createRequestCtx.getUserAttributes());
    }

    @Override
    public UserResponseContextImpl updateUser(UpdateUserRequestContext updateRequestCtx) throws IdentityStoreException
    {
        log.debug(String.format("UpdateUserRequestContext getEntityId() = %s", updateRequestCtx.getEntityId()));
        log.debug(String.format("UpdateUserRequestContext getUserId() = %s", updateRequestCtx.getUserId()));
        return createAndUpdateUserHelper(updateRequestCtx.getUserAttributes());

    }

    public UserResponseContextImpl createAndUpdateUserHelper(AttributeMap attributeMap) throws IdentityStoreException {

        String externalClientIdFromReq = attributeMap.getSingleValue(FIELD_ACTUAL_EXTERNAL_CLIENT_ID);
        String externalClientIdFromPF = attributeMap.getSingleValue(FIELD_EXPECTED_EXTERNAL_CLIENT_ID);
        String externalUserIdFromReq = attributeMap.getSingleValue(FIELD_EXTERNAL_USER_ID);

        log.debug("Attributes:");
        for (Map.Entry<String, AttributeValue> e : attributeMap.entrySet())
        {
            log.debug(String.format("    %s => %s", e.getKey(), e.getValue().getValue()));
        }

        if (externalClientIdFromReq == null || "".equals(externalClientIdFromReq) ||
                externalUserIdFromReq == null || "".equals(externalUserIdFromReq) ||
                externalClientIdFromPF == null || !externalClientIdFromReq.toLowerCase().equals(externalClientIdFromPF.toLowerCase())) {
            log.error(String.format("Invalid request externalClientId=%s externalUserId=%s expectedExternalClientId=%s",
                    externalClientIdFromReq, externalUserIdFromReq, externalClientIdFromPF));
            throw new BadRequestException("Invalid request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
        }

        List<User> users = new ArrayList<>();
        User userInfo = new User();
        userInfo.setExternalUserId(externalUserIdFromReq);
        userInfo.setFirstName(attributeMap.getSingleValue(FIELD_FIRSTNAME));
        userInfo.setLastName(attributeMap.getSingleValue(FIELD_LASTNAME));
        userInfo.setIsActive(Boolean.parseBoolean(attributeMap.getSingleValue(FIELD_ACTIVE)));

        if (attributeMap.getSingleValue(FIELD_ROLE) != null)
	        userInfo.setUserRole(attributeMap.getSingleValue(FIELD_ROLE));

        if (attributeMap.getSingleValue(FIELD_TITLE) != null)
	        userInfo.setTitle(attributeMap.getSingleValue(FIELD_TITLE));

        if (attributeMap.getSingleValue(FIELD_SOCIAL_MEDIA_CHANNELS) != null)
	        userInfo.setSocialMediaChannels(attributeMap.getSingleValue(FIELD_SOCIAL_MEDIA_CHANNELS));

        if (attributeMap.getSingleValue(FIELD_SOCIAL_IDENTIFIERS) != null)
	        userInfo.setSocialIdentifiers(attributeMap.getSingleValue(FIELD_SOCIAL_IDENTIFIERS));

        if (attributeMap.getSingleValue(FIELD_LEGAL_HOLD) != null)
	        userInfo.setIsUnderLegalHold(Boolean.parseBoolean(attributeMap.getSingleValue(FIELD_LEGAL_HOLD)));
		
		if (attributeMap.getSingleValue(FIELD_SSO_LOGIN_ONLY) != null)
	        userInfo.setSsoLoginOnly(Boolean.parseBoolean(attributeMap.getSingleValue(FIELD_SSO_LOGIN_ONLY)));
		
		String primaryEmail = "";
		List<String> otherEmails = new ArrayList<>();
		String emails = attributeMap.getSingleValue(FIELD_EMAIL);

		if (emails != null)
		{
			String[] splitResult = emails.split("\\|");

			if (splitResult.length == 1)
			{
				primaryEmail = emails;
			}
			else
			{
				for(int i = 0; i < splitResult.length; i++)
				{
					String value = splitResult[i];
					if ((i + 1) < splitResult.length)
					{
						String type = splitResult[i + 1];
						i++; // skip ahead past the type
						if (type.equals("primary"))
						{
							primaryEmail = value;
						}
						else if (type.equals("other"))
						{
							otherEmails.add(value);
						}
						else
						{
							log.error(String.format("Invalid request (bad email type) externalClientId=%s externalUserId=%s expectedExternalClientId=%s emails=%s",
									externalClientIdFromReq, externalUserIdFromReq, externalClientIdFromPF, emails));
							throw new BadRequestException("Invalid request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
						}
					}
					else
					{
						log.error(String.format("Invalid request (missing email type) externalClientId=%s externalUserId=%s expectedExternalClientId=%s emails=%s",
								externalClientIdFromReq, externalUserIdFromReq, externalClientIdFromPF, emails));
						throw new BadRequestException("Invalid request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
					}
				}
			}
		}

		// if no primary email was given, use the first other email as the primary
        if (primaryEmail.equals("") && otherEmails.size() > 0)
        {
			primaryEmail = otherEmails.get(0);
		}

        userInfo.setEmail(primaryEmail);
        if (otherEmails.size() > 0)
        {
			userInfo.setAdditionalEmails(otherEmails);
		}

    	userInfo.setSuccess(null);
        users.add(userInfo);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setExternalClientId(externalClientIdFromReq);
        request.setUsers(users);
        UpdateUserResponse resp = callSmarshService(configurableSmarshIdentifyStoreURL + "/users/update", request, UpdateUserResponse.class);

        if (resp == null) {
            log.error(String.format("Smarsh Service Update for externalClientId=%s externalUserId=%s returned null",
                    externalClientIdFromReq, externalUserIdFromReq));
            throw new BadRequestException("Failed to process request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
        } else if (!resp.isSuccess()) {
            log.error(String.format("Smarsh Service Update for externalClientId=%s externalUserId=%s failed errorMessages=%s",
                    externalClientIdFromReq, externalUserIdFromReq, resp.getErrorMessages()));
            throw new BadRequestException("Failed to process request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
        } else if (resp.getUserResults().size() != 1) {
            log.error(String.format("Smarsh Service Update for externalClientId=%s externalUserId=%s returned %d users",
                    externalClientIdFromReq, externalUserIdFromReq, resp.getUserResults().size()));
            throw new BadRequestException("Failed to process request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
        }

        User user = resp.getUserResults().get(0);

        if (!user.isSuccess()) {
            log.error(String.format("Smarsh Service Update for externalUserId=%s failed errors=%s", externalUserIdFromReq, user.getErrors()));
            throw new BadRequestException("Failed to process request: " + externalUserIdFromReq);
        }

        // Success
        attributeMap.put(Constants.ID, externalClientIdFromReq + ":" + externalUserIdFromReq);
        AttributeValue now = AttrValueSupport.make(new Date());
        attributeMap.put(Constants.WHEN_CREATED, now);
        attributeMap.put(Constants.WHEN_CHANGED, now);

        log.info(String.format("Successfully processed update for externalUserId=%s from externalClientId=%s", externalUserIdFromReq, externalClientIdFromReq));

        return new UserResponseContextImpl(attributeMap);
    }

    @Override
    public UserResponseContextImpl readUser(ReadUserRequestContext req) throws IdentityStoreException
    {
        log.debug(String.format("ReadUserRequestContext getEntityId() = %s", req.getEntityId()));
        log.debug(String.format("ReadUserRequestContext getUserId() = %s", req.getUserId()));

        int delimeterIndex = req.getUserId().indexOf(':');

        // Expected format of req.getUserId is externalClientId:externalUserId
        if (delimeterIndex < 1 || delimeterIndex == req.getUserId().length() - 1) {
            log.error(String.format("ReadUserRequestContext getUserId=%s invalid Id", req.getUserId()));
            throw new NotFoundException("Failed to process readUser request: " + req.getUserId());
        }

        String externalClientIdFromReq = req.getUserId().substring(0, delimeterIndex);
        String externalUserIdFromReq = req.getUserId().substring(delimeterIndex + 1);

        List<String> externalUserIds = new ArrayList<>();
        externalUserIds.add(externalUserIdFromReq);

        ReadUserRequest request = new ReadUserRequest();
        request.setExternalClientId(externalClientIdFromReq);
        request.setExternalUserIds(externalUserIds);
        ReadUserResponse resp = callSmarshService(configurableSmarshIdentifyStoreURL + "/users/read", request, ReadUserResponse.class);

        if (resp == null) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalUserId=%s returned null",
                    externalClientIdFromReq, externalUserIdFromReq));
            throw new BadRequestException("Failed to process readUser request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
        } else if (!resp.isSuccess()) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalUserId=%s failed errorMessages=%s",
                    externalClientIdFromReq, externalUserIdFromReq, resp.getErrorMessages()));
            throw new NotFoundException(USER_NOT_FOUND + externalClientIdFromReq + ":" + externalUserIdFromReq);
        } else if (resp.getUsers().size() != 1) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalUserId=%s returned %d users",
                    externalClientIdFromReq, externalUserIdFromReq, resp.getUsers().size()));
            throw new NotFoundException(USER_NOT_FOUND + externalClientIdFromReq + ":" + externalUserIdFromReq);
        }

        User user = resp.getUsers().get(0);

        if (!user.isSuccess()) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalUserId=%s failed errors=%s",
                    externalClientIdFromReq, externalUserIdFromReq, user.getErrors()));
            throw new NotFoundException(USER_NOT_FOUND + externalUserIdFromReq);
        }

        // Success
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.put(Constants.ID, req.getUserId());
        attributeMap.put(FIELD_EXTERNAL_USER_ID, user.getExternalUserId());
        attributeMap.put(FIELD_FIRSTNAME, user.getFirstName());
        attributeMap.put(FIELD_LASTNAME, user.getLastName());
        attributeMap.put(FIELD_ACTIVE, user.getIsActive() ? "true" : "false");

		String emailValue = user.getEmail();
        if (user.getAdditionalEmails() != null && user.getAdditionalEmails().size() > 0)
        {
			emailValue = emailValue + "|primary";
			for (int i = 0; i < user.getAdditionalEmails().size(); i++)
				emailValue = emailValue + "|" + user.getAdditionalEmails().get(i) + "|other";
		}

        attributeMap.put(FIELD_EMAIL, emailValue);

		if (user.getIsUnderLegalHold() != null)
	        attributeMap.put(FIELD_LEGAL_HOLD, user.getIsUnderLegalHold() ? "true" : "false");
		
		if (user.getSsoLoginOnly() != null)
	        attributeMap.put(FIELD_SSO_LOGIN_ONLY, user.getSsoLoginOnly() ? "true" : "false");
		
		if (user.getTitle() != null)
			attributeMap.put(FIELD_TITLE, user.getTitle());

		if (user.getUserRole() != null)
			attributeMap.put(FIELD_ROLE, user.getUserRole());

		if (user.getSocialMediaChannels() != null)
			attributeMap.put(FIELD_SOCIAL_MEDIA_CHANNELS, user.getSocialMediaChannels());

		if (user.getSocialIdentifiers() != null)
			attributeMap.put(FIELD_SOCIAL_IDENTIFIERS, user.getSocialIdentifiers());

        AttributeValue now = AttrValueSupport.make(new Date());
        attributeMap.put(Constants.WHEN_CREATED, now);
        attributeMap.put(Constants.WHEN_CHANGED, now);

        return new UserResponseContextImpl(attributeMap);
    }

    @Override
    public void deleteUser(DeleteUserRequestContext req) throws IdentityStoreException
    {
        log.debug(String.format("DeleteUserRequestContext getEntityId() = %s", req.getEntityId()));
        log.debug(String.format("DeleteUserRequestContext getUserId() = %s", req.getUserId()));

        int delimeterIndex = req.getUserId().indexOf(':');

        // Expected format of req.getUserId is externalClientId:externalUserId
        if (delimeterIndex < 1 || delimeterIndex == req.getUserId().length() - 1) {
            log.error(String.format("DeleteUserRequestContext getUserId=%s invalid Id", req.getUserId()));
            throw new NotFoundException("Failed to process deleteUser request: " + req.getUserId());
        }

        String externalClientIdFromReq = req.getUserId().substring(0, delimeterIndex);
        String externalUserIdFromReq = req.getUserId().substring(delimeterIndex + 1);

        List<String> externalUserIds = new ArrayList<>();
        externalUserIds.add(externalUserIdFromReq);

        DeleteUserRequest request = new DeleteUserRequest();
        request.setExternalClientId(externalClientIdFromReq);
        request.setExternalUserIds(externalUserIds);
        DeleteUserResponse resp = callSmarshService(configurableSmarshIdentifyStoreURL + "/users/delete", request, DeleteUserResponse.class);

        if (resp == null) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalUserId=%s returned null",
                    externalClientIdFromReq, externalUserIdFromReq));
            throw new BadRequestException("Failed to process deleteUser request: " + externalClientIdFromReq + ":" + externalUserIdFromReq);
        } else if (!resp.isSuccess()) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalUserId=%s failed errorMessages=%s",
                    externalClientIdFromReq, externalUserIdFromReq, resp.getErrorMessages()));
            throw new NotFoundException(USER_NOT_FOUND + externalClientIdFromReq + ":" + externalUserIdFromReq);
        } else if (resp.getUserResults().size() != 1) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalUserId=%s returned %d users",
                    externalClientIdFromReq, externalUserIdFromReq, resp.getUserResults().size()));
            throw new NotFoundException(USER_NOT_FOUND + externalClientIdFromReq + ":" + externalUserIdFromReq);
        }

        User user = resp.getUserResults().get(0);

        if (!user.isSuccess()) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalUserId=%s failed errors=%s",
                    externalClientIdFromReq, externalUserIdFromReq, user.getErrors()));
            throw new NotFoundException(USER_NOT_FOUND + externalUserIdFromReq);
        }

        log.info(String.format("Successfully deleted externalUserId=%s for externalClientId=%s", externalUserIdFromReq, externalClientIdFromReq));
    }

    @Override
    public GroupResponseContext readGroup(ReadGroupRequestContext readRequestCtx) throws IdentityStoreException
    {
        log.debug(String.format("ReadGroupRequestContext getEntityId() = %s", readRequestCtx.getEntityId()));
        log.debug(String.format("ReadGroupRequestContext getGroupId() = %s", readRequestCtx.getGroupId()));

 		String groupId = readRequestCtx.getGroupId();
		if (groupId.startsWith("GROUP:"))
			groupId = groupId.substring(6);

        int delimeterIndex = groupId.indexOf(':');

        // Expected format of readRequestCtx.getGroupId is externalClientId:externalGroupId
        if (delimeterIndex < 1 || delimeterIndex == groupId.length() - 1) {
            log.error(String.format("ReadGroupRequestContext getGroupId=%s invalid Id", readRequestCtx.getGroupId()));
            throw new NotFoundException("Failed to process readGroup request: " + readRequestCtx.getGroupId());
        }

        String externalClientIdFromReq = groupId.substring(0, delimeterIndex);
        String externalGroupIdFromReq = groupId.substring(delimeterIndex + 1);

        ReadGroupRequest request = new ReadGroupRequest();
        request.setExternalClientId(externalClientIdFromReq);
        request.setExternalGroupId(externalGroupIdFromReq);
        ReadGroupResponse resp = callSmarshService(configurableSmarshIdentifyStoreURL + "/groups/read", request, ReadGroupResponse.class);

        if (resp == null) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalGroupId=%s returned null",
                    externalClientIdFromReq, externalGroupIdFromReq));
            throw new BadRequestException("Failed to process readGroup request: " + externalClientIdFromReq + ":" + externalGroupIdFromReq);
        } else if (!resp.isSuccess()) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalGroupId=%s failed errorMessages=%s",
                    externalClientIdFromReq, externalGroupIdFromReq, resp.getErrorMessages()));
            throw new NotFoundException(GROUP_NOT_FOUND + externalClientIdFromReq + ":" + externalGroupIdFromReq);
        } else if (resp.getGroup() ==  null) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalGroupId=%s returned 0 groups",
                    externalClientIdFromReq, externalGroupIdFromReq));
            throw new NotFoundException(GROUP_NOT_FOUND + externalClientIdFromReq + ":" + externalGroupIdFromReq);
        }

        Group group = resp.getGroup();

        if (!group.isSuccess()) {
            log.error(String.format("Smarsh Service Read for externalClientId=%s externalGroupId=%s failed errors=%s",
                    externalClientIdFromReq, externalGroupIdFromReq, group.getErrors()));
            throw new NotFoundException(GROUP_NOT_FOUND + externalGroupIdFromReq);
        }

        // Success
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.put(Constants.ID, readRequestCtx.getGroupId());
        attributeMap.put(FIELD_EXTERNAL_GROUP_ID, group.getExternalGroupId());
        attributeMap.put(FIELD_GROUPNAME, group.getName());
        AttributeValue now = AttrValueSupport.make(new Date());
        attributeMap.put(Constants.WHEN_CREATED, now);
        attributeMap.put(Constants.WHEN_CHANGED, now);

        return new GroupResponseContextImpl(attributeMap);
	}

	@Override
	public void deleteGroup(DeleteGroupRequestContext deleteRequestCtx) throws IdentityStoreException
	{
        log.debug(String.format("DeleteGroupRequestContext getEntityId() = %s", deleteRequestCtx.getEntityId()));
        log.debug(String.format("DeleteGroupRequestContext getGroupId() = %s", deleteRequestCtx.getGroupId()));

		String groupId = deleteRequestCtx.getGroupId();
		if (groupId.startsWith("GROUP:"))
			groupId = groupId.substring(6);

        int delimeterIndex = groupId.indexOf(':');

        // Expected format of deleteRequestCtx.getGroupId is externalClientId:externalGroupId
        if (delimeterIndex < 1 || delimeterIndex == groupId.length() - 1) {
            log.error(String.format("DeleteGroupRequestContext getGroupId=%s invalid Id", deleteRequestCtx.getGroupId()));
            throw new NotFoundException("Failed to process deleteGroup request: " + deleteRequestCtx.getGroupId());
        }

        String externalClientIdFromReq = groupId.substring(0, delimeterIndex);
        String externalGroupIdFromReq = groupId.substring(delimeterIndex + 1);

        DeleteGroupRequest request = new DeleteGroupRequest();
        request.setExternalClientId(externalClientIdFromReq);
        request.setExternalGroupId(externalGroupIdFromReq);
        DeleteGroupResponse resp = callSmarshService(configurableSmarshIdentifyStoreURL + "/groups/delete", request, DeleteGroupResponse.class);

        if (resp == null) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalGroupId=%s returned null",
                    externalClientIdFromReq, externalGroupIdFromReq));
            throw new BadRequestException("Failed to process deleteGroup request: " + externalClientIdFromReq + ":" + externalGroupIdFromReq);
        } else if (!resp.isSuccess()) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalGroupId=%s failed errorMessages=%s",
                    externalClientIdFromReq, externalGroupIdFromReq, resp.getErrorMessages()));
            throw new NotFoundException(GROUP_NOT_FOUND + externalClientIdFromReq + ":" + externalGroupIdFromReq);
        } else if (resp.getGroupResult() == null) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalGroupId=%s returned 0 groups",
                    externalClientIdFromReq, externalGroupIdFromReq));
            throw new NotFoundException(GROUP_NOT_FOUND + externalClientIdFromReq + ":" + externalGroupIdFromReq);
        }

        Group group = resp.getGroupResult();

        if (!group.isSuccess()) {
            log.error(String.format("Smarsh Service Delete for externalClientId=%s externalGroupId=%s failed errors=%s",
                    externalClientIdFromReq, externalGroupIdFromReq, group.getErrors()));
            throw new NotFoundException(GROUP_NOT_FOUND + externalGroupIdFromReq);
        }

        log.info(String.format("Successfully deleted externalGroupId=%s for externalClientId=%s", externalGroupIdFromReq, externalClientIdFromReq));
	}

    @Override
    public GroupResponseContext updateGroup(UpdateGroupRequestContext updateRequestContext) throws IdentityStoreException
    {
        log.debug(String.format("UpdateGroupRequestContext getEntityId() = %s", updateRequestContext.getEntityId()));
        log.debug(String.format("UpdateGroupRequestContext getGroupId() = %s", updateRequestContext.getGroupId()));
        return createAndUpdateGroupHelper(updateRequestContext.getGroupAttributes());
	}

    @Override
    public GroupResponseContext createGroup(CreateGroupRequestContext createRequestContext) throws IdentityStoreException
    {
        log.debug(String.format("CreateGroupRequestContext getEntityId() = %s", createRequestContext.getEntityId()));
        return createAndUpdateGroupHelper(createRequestContext.getGroupAttributes());
	}

    public GroupResponseContext createAndUpdateGroupHelper(AttributeMap attributeMap) throws IdentityStoreException
	{
        String externalClientIdFromPF = attributeMap.getSingleValue(FIELD_EXPECTED_EXTERNAL_CLIENT_ID);
        String externalGroupIdFromReq = attributeMap.getSingleValue(FIELD_EXTERNAL_GROUP_ID);

        log.debug("Attributes:");
        for (Map.Entry<String, AttributeValue> e : attributeMap.entrySet())
        {
			if (e.getKey() == "members")
			{
				AttributeValue mems = attributeMap.get(IdentityStoreProvisionerDescriptor.DEFAULT_MEMBERS_ATTR_NAME);
				if (mems != null && mems.getAllObjectValues() != null)
				{
					// handle members
					if (mems != null)
					{
						int i = 0;
						for (Object o : mems.getAllObjectValues())
						{
							MemberAttribute mem = (MemberAttribute) o;
			        	    log.debug(String.format("    members[%d] => %s (%s)", i, mem.getId(), mem.getType()));
			        	    i++;
						}
					}
				}
			}
			else
			{
        	    log.debug(String.format("    %s => %s", e.getKey(), e.getValue().getValue()));
			}
        }

        if (externalClientIdFromPF == null || "".equals(externalClientIdFromPF) ||
                externalGroupIdFromReq == null || "".equals(externalGroupIdFromReq)) {
            log.error(String.format("Invalid request externalClientId=%s externalGroupId=%s",
                    externalClientIdFromPF, externalGroupIdFromReq));
            throw new BadRequestException("Invalid request: " + externalClientIdFromPF + ":" + externalGroupIdFromReq);
        }

        Group groupInfo = new Group();
        groupInfo.setExternalGroupId(externalGroupIdFromReq);
        groupInfo.setName(attributeMap.getSingleValue(FIELD_GROUPNAME));
        groupInfo.setSuccess(null);

        List<GroupMember> members = new ArrayList<>();
		AttributeValue membersAttribute = attributeMap.get(IdentityStoreProvisionerDescriptor.DEFAULT_MEMBERS_ATTR_NAME);
		if (membersAttribute != null && membersAttribute.getAllObjectValues() != null)
		{
			// handle members
			if (membersAttribute != null)
			{
				for (Object o : membersAttribute.getAllObjectValues())
				{
					MemberAttribute member = (MemberAttribute) o;
					GroupMember groupMember = new GroupMember();
					String memberId = member.getId();
					if (memberId.startsWith("GROUP:"))
						memberId = memberId.substring(6);

					int delimeterIndex = memberId.indexOf(':');
					memberId = memberId.substring(delimeterIndex + 1);

					groupMember.setExternalMemberId(memberId);

					if (member.getType() == MemberAttribute.Type.USER)
					{
						groupMember.setMemberType("User");
					}
					else if (member.getType() == MemberAttribute.Type.GROUP)
					{
						groupMember.setMemberType("Group");
					}
					else if (member.getId().startsWith("GROUP:"))
					{
						groupMember.setMemberType("Group");
					}
					else
					{
						groupMember.setMemberType("User");
					}

					members.add(groupMember);
				}
			}
		}


		groupInfo.setMembers(members);

        UpdateGroupRequest request = new UpdateGroupRequest();
        request.setExternalClientId(externalClientIdFromPF);
        request.setGroup(groupInfo);
        UpdateGroupResponse resp = callSmarshService(configurableSmarshIdentifyStoreURL + "/groups/update", request, UpdateGroupResponse.class);

        if (resp == null) {
            log.error(String.format("Smarsh Service Update for externalClientId=%s externalGroupId=%s returned null",
                    externalClientIdFromPF, externalGroupIdFromReq));
            throw new BadRequestException("Failed to process request: " + externalClientIdFromPF + ":" + externalGroupIdFromReq);
        } else if (!resp.isSuccess()) {
            log.error(String.format("Smarsh Service Update for externalClientId=%s externalGroupId=%s failed errorMessages=%s",
                    externalClientIdFromPF, externalGroupIdFromReq, resp.getErrorMessages()));
            throw new BadRequestException("Failed to process request: " + externalClientIdFromPF + ":" + externalGroupIdFromReq);
        } else if (resp.getGroupResult() == null) {
            log.error(String.format("Smarsh Service Update for externalClientId=%s externalGroupId=%s returned 0 groups",
                    externalClientIdFromPF, externalGroupIdFromReq));
            throw new BadRequestException("Failed to process request: " + externalClientIdFromPF + ":" + externalGroupIdFromReq);
        }

        Group group = resp.getGroupResult();

        if (!group.isSuccess()) {
            log.error(String.format("Smarsh Service Update for externalGroupId=%s failed errors=%s", externalGroupIdFromReq, group.getErrors()));
            throw new BadRequestException("Failed to process request: " + externalGroupIdFromReq);
        }

        // Success
        attributeMap.put(Constants.ID, "GROUP:" + externalClientIdFromPF + ":" + externalGroupIdFromReq);
        AttributeValue now = AttrValueSupport.make(new Date());
        attributeMap.put(Constants.WHEN_CREATED, now);
        attributeMap.put(Constants.WHEN_CHANGED, now);

        log.info(String.format("Successfully processed update for externalGroupId=%s from externalClientId=%s", externalGroupIdFromReq, externalClientIdFromPF));

        return new GroupResponseContextImpl(attributeMap);
	}

    @Override
    public boolean isGroupProvisioningSupported()
    {
        return true;
    }

    public <REQUEST, RESPONSE> RESPONSE callSmarshService(String url, REQUEST request, Class<RESPONSE> responseClazz) {

        RESPONSE result;
        String jsonResponse;

        if (url == null) {
            log.error("Bad call to callSmarshService: url is null");
            return null;
        }

        if (request == null) {
            log.error("Bad call to callSmarshService: request is null");
            return null;
        }

        if (responseClazz == null) {
            log.error("Bad call to callSmarshService: responseClazz is null");
            return null;
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTHENTICATION_TOKEN, configurableAuthenticationToken);
        headers.add(SOURCE_APPLICATION_TOKEN, SOURCE_APPLICATION_VALUE);
        final HttpEntity<REQUEST> requestWithHeaders = new HttpEntity<REQUEST>(request, headers);

        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor ri = new LoggingRequestInterceptor();
        List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
        ris.add(ri);
        restTemplate.setInterceptors(ris);

        jsonResponse = restTemplate.postForObject(url, requestWithHeaders, String.class);
        log.debug("Response json Body: " + jsonResponse);
        result = deSerializeResponse(jsonResponse, responseClazz);
        log.debug("Response parsed Body: " + result);

        return result;
    }

    public <T> T deSerializeResponse(String stringResponse, Class<T> clazz) {

        if (stringResponse == null) {
            log.error("Bad call to deSerializeResponse: stringResponse is null");
            return null;
        }

        if (clazz == null) {
            log.error("Bad call to deSerializeResponse: clazz is null");
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();

        InputStream jsonInput = new ByteArrayInputStream(stringResponse.getBytes());

        T readUserResponse = null;
        try {
            readUserResponse = mapper.readValue(jsonInput, clazz);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readUserResponse;
    }
}
