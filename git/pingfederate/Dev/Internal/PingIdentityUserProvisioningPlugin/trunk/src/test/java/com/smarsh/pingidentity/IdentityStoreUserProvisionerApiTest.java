package com.smarsh.pingidentity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Matchers;
import org.sourceid.saml20.adapter.conf.Configuration;
import org.sourceid.util.log.AttributeMap;

import com.pingidentity.sdk.provision.Constants;
import com.pingidentity.sdk.provision.exception.BadRequestException;
import com.pingidentity.sdk.provision.exception.IdentityStoreException;
import com.pingidentity.sdk.provision.exception.NotFoundException;
import com.pingidentity.sdk.provision.users.request.CreateUserRequestContext;
import com.pingidentity.sdk.provision.users.request.DeleteUserRequestContext;
import com.pingidentity.sdk.provision.users.request.ReadUserRequestContext;
import com.pingidentity.sdk.provision.users.response.UserResponseContextImpl;

public class IdentityStoreUserProvisionerApiTest {

    @Test
    public void configureHappyDayTest() {

        final String configFilename = "target" + File.separator + "test-classes" + File.separator + "validsample.properties";
        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        Configuration configurationMock = mock(Configuration.class);
        when(configurationMock.getFieldValue(SmarshIdentityStoreProvisioner.PROPERTIESFILE_TEXTFIELD_NAME)).thenReturn(configFilename);

        doCallRealMethod().when(sispMock).configure(configurationMock);
        doCallRealMethod().when(sispMock).getConfigurableSmarshIdentifyStoreURL();
        doCallRealMethod().when(sispMock).getConfigurableAuthenticationToken();

        sispMock.configure(configurationMock);

        assertEquals("http://dev-intweb-01.smarshdev.com/provisioningservice/users", sispMock.getConfigurableSmarshIdentifyStoreURL());
        assertEquals("D37A44F8F96F440680DFDAAB53AA4557", sispMock.getConfigurableAuthenticationToken());
    }

    @Test
    public void configurePropertiesFileNotFoundTest() {

        final String configFilename = "target" + File.separator + "test-classes" + File.separator + "nosuchfile.properties";
        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        Configuration configurationMock = mock(Configuration.class);
        when(configurationMock.getFieldValue(SmarshIdentityStoreProvisioner.PROPERTIESFILE_TEXTFIELD_NAME)).thenReturn(configFilename);

        doCallRealMethod().when(sispMock).configure(configurationMock);
        doCallRealMethod().when(sispMock).getConfigurableSmarshIdentifyStoreURL();
        doCallRealMethod().when(sispMock).getConfigurableAuthenticationToken();

        sispMock.configure(configurationMock);

        assertEquals(null, sispMock.getConfigurableSmarshIdentifyStoreURL());
        assertEquals(null, sispMock.getConfigurableAuthenticationToken());
    }

    @Test
    public void configurePropertiesNotSetSmarshIdentifyStoreURLTest() {

        final String configFilename = "target" + File.separator + "test-classes" + File.separator + "badsample1.properties";
        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        Configuration configurationMock = mock(Configuration.class);
        when(configurationMock.getFieldValue(SmarshIdentityStoreProvisioner.PROPERTIESFILE_TEXTFIELD_NAME)).thenReturn(configFilename);

        doCallRealMethod().when(sispMock).configure(configurationMock);
        doCallRealMethod().when(sispMock).getConfigurableSmarshIdentifyStoreURL();
        doCallRealMethod().when(sispMock).getConfigurableAuthenticationToken();

        sispMock.configure(configurationMock);

        assertEquals(null, sispMock.getConfigurableSmarshIdentifyStoreURL());
        assertEquals("D37A44F8F96F440680DFDAAB53AA4557", sispMock.getConfigurableAuthenticationToken());
    }

    @Test
    public void configurePropertiesNotSetAuthenticationTokenTest() {

        final String configFilename = "target" + File.separator + "test-classes" + File.separator + "badsample2.properties";
        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        Configuration configurationMock = mock(Configuration.class);
        when(configurationMock.getFieldValue(SmarshIdentityStoreProvisioner.PROPERTIESFILE_TEXTFIELD_NAME)).thenReturn(configFilename);

        doCallRealMethod().when(sispMock).configure(configurationMock);
        doCallRealMethod().when(sispMock).getConfigurableSmarshIdentifyStoreURL();
        doCallRealMethod().when(sispMock).getConfigurableAuthenticationToken();

        sispMock.configure(configurationMock);

        assertEquals("http://dev-intweb-01.smarshdev.com/provisioningservice/users", sispMock.getConfigurableSmarshIdentifyStoreURL());
        assertEquals(null, sispMock.getConfigurableAuthenticationToken());
    }

    @Test(expected = NotFoundException.class)
    public void readUserBadUserId1Test() throws IdentityStoreException {
        ReadUserRequestContext readUserRequestContextmock = mock(ReadUserRequestContext.class);
        when(readUserRequestContextmock.getUserId()).thenReturn("externalUserId1");

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).readUser(readUserRequestContextmock);

        sispMock.readUser(readUserRequestContextmock);
    }

    @Test(expected = NotFoundException.class)
    public void readUserBadUserId2Test() throws IdentityStoreException {
        ReadUserRequestContext readUserRequestContextmock = mock(ReadUserRequestContext.class);
        when(readUserRequestContextmock.getUserId()).thenReturn("externalUserId1:");

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).readUser(readUserRequestContextmock);

        sispMock.readUser(readUserRequestContextmock);
    }

    @Test(expected = NotFoundException.class)
    public void readUserBadUserId3Test() throws IdentityStoreException {
        ReadUserRequestContext readUserRequestContextmock = mock(ReadUserRequestContext.class);
        when(readUserRequestContextmock.getUserId()).thenReturn(":externalUserId1");

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).readUser(readUserRequestContextmock);

        sispMock.readUser(readUserRequestContextmock);
    }

    @Test
    public void readUserTest() throws IdentityStoreException {

        ReadUserRequestContext readUserRequestContextmock = mock(ReadUserRequestContext.class);
        when(readUserRequestContextmock.getUserId()).thenReturn("externalClientId1:01da632448b25e4fb4ca0a25c816a70a");

        User expectedUser = new User();
        expectedUser.setExternalClientId(null);
        expectedUser.setExternalUserId("01da632448b25e4fb4ca0a25c816a70a");
        expectedUser.setFirstName("Buddy35");
        expectedUser.setLastName("Anderson");
        expectedUser.setEmail("buddy35a@smarsh.com");
        expectedUser.setSmarshUserId(null);
        expectedUser.setIsActive(true);

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser);

        ReadUserResponse respMock = mock(ReadUserResponse.class);
        when(respMock.isSuccess()).thenReturn(true);
        when(respMock.getUsers()).thenReturn(expectedUsers);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).readUser(readUserRequestContextmock);
        when(sispMock.callSmarshService(anyString(), any(), Matchers.<Class<ReadUserResponse>> any())).thenReturn(respMock);

        UserResponseContextImpl result = sispMock.readUser(readUserRequestContextmock);
        assertEquals("externalClientId1:01da632448b25e4fb4ca0a25c816a70a", result.getUserAttributes().get(Constants.ID).getValue());
        assertEquals(expectedUser.getExternalUserId(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_EXTERNAL_USER_ID).getValue());
        assertEquals(expectedUser.getFirstName(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_FIRSTNAME).getValue());
        assertEquals(expectedUser.getLastName(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_LASTNAME).getValue());
        assertEquals(expectedUser.getEmail(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_EMAIL).getValue());
		assertNull(result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_TITLE));
		assertNull(result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_ROLE));
		assertNull(result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_SOCIAL_MEDIA_CHANNELS));
        assertNotNull(result.getUserAttributes().get(Constants.WHEN_CREATED).getValue());
        assertNotNull(result.getUserAttributes().get(Constants.WHEN_CHANGED).getValue());
    }

    @Test
    public void readUserWithOptionalFieldsTest() throws IdentityStoreException {

        ReadUserRequestContext readUserRequestContextmock = mock(ReadUserRequestContext.class);
        when(readUserRequestContextmock.getUserId()).thenReturn("externalClientId1:01da632448b25e4fb4ca0a25c816a70a");

        User expectedUser = new User();
        expectedUser.setExternalClientId(null);
        expectedUser.setExternalUserId("01da632448b25e4fb4ca0a25c816a70a");
        expectedUser.setFirstName("Buddy35");
        expectedUser.setLastName("Anderson");
        expectedUser.setEmail("buddy35a@smarsh.com");
        expectedUser.setTitle("cool guy");
        expectedUser.setUserRole("cool role");
        expectedUser.setSocialMediaChannels("social,channels");
        expectedUser.setSmarshUserId(null);
        expectedUser.setIsActive(true);

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser);

        ReadUserResponse respMock = mock(ReadUserResponse.class);
        when(respMock.isSuccess()).thenReturn(true);
        when(respMock.getUsers()).thenReturn(expectedUsers);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).readUser(readUserRequestContextmock);
        when(sispMock.callSmarshService(anyString(), any(), Matchers.<Class<ReadUserResponse>> any())).thenReturn(respMock);

        UserResponseContextImpl result = sispMock.readUser(readUserRequestContextmock);
        assertEquals("externalClientId1:01da632448b25e4fb4ca0a25c816a70a", result.getUserAttributes().get(Constants.ID).getValue());
        assertEquals(expectedUser.getExternalUserId(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_EXTERNAL_USER_ID).getValue());
        assertEquals(expectedUser.getFirstName(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_FIRSTNAME).getValue());
        assertEquals(expectedUser.getLastName(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_LASTNAME).getValue());
        assertEquals(expectedUser.getEmail(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_EMAIL).getValue());
        assertEquals(expectedUser.getTitle(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_TITLE).getValue());
        assertEquals(expectedUser.getUserRole(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_ROLE).getValue());
        assertEquals(expectedUser.getSocialMediaChannels(), result.getUserAttributes().get(SmarshIdentityStoreProvisioner.FIELD_SOCIAL_MEDIA_CHANNELS).getValue());
        assertNotNull(result.getUserAttributes().get(Constants.WHEN_CREATED).getValue());
        assertNotNull(result.getUserAttributes().get(Constants.WHEN_CHANGED).getValue());
    }

    @Test(expected = NotFoundException.class)
    public void readUserErrorMatchesNoUsersTest() throws IdentityStoreException {

        ReadUserRequestContext readUserRequestContextmock = mock(ReadUserRequestContext.class);
        when(readUserRequestContextmock.getUserId()).thenReturn("externalClientId1:01da632448b25e4fb4ca0a25c816a70a");

        List<User> expectedUsers = new ArrayList<>();

        ReadUserResponse respMock = mock(ReadUserResponse.class);
        when(respMock.isSuccess()).thenReturn(true);
        when(respMock.getUsers()).thenReturn(expectedUsers);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).readUser(readUserRequestContextmock);
        when(sispMock.callSmarshService(anyString(), any(), Matchers.<Class<ReadUserResponse>> any())).thenReturn(respMock);

        sispMock.readUser(readUserRequestContextmock);
    }

    @Test(expected = NotFoundException.class)
    public void readUserErrorMatchesTwoUsersTest() throws IdentityStoreException {

        ReadUserRequestContext readUserRequestContextmock = mock(ReadUserRequestContext.class);
        when(readUserRequestContextmock.getUserId()).thenReturn("externalClientId1:01da632448b25e4fb4ca0a25c816a70a");

        User user = new User();
        user.setExternalClientId(null);
        user.setExternalUserId("01da632448b25e4fb4ca0a25c816a70a");
        user.setFirstName("Buddy35");
        user.setLastName("Anderson");
        user.setEmail("buddy35a@smarsh.com");
        user.setSmarshUserId(null);
        user.setIsActive(true);

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(user);
        expectedUsers.add(user); // add second user, to cause error

        ReadUserResponse respMock = mock(ReadUserResponse.class);
        when(respMock.isSuccess()).thenReturn(true);
        when(respMock.getUsers()).thenReturn(expectedUsers);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).readUser(readUserRequestContextmock);
        when(sispMock.callSmarshService(anyString(), any(), Matchers.<Class<ReadUserResponse>> any())).thenReturn(respMock);

        sispMock.readUser(readUserRequestContextmock);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserBadUserId1Test() throws IdentityStoreException {
        DeleteUserRequestContext deleteUserRequestContextmock = mock(DeleteUserRequestContext.class);
        when(deleteUserRequestContextmock.getUserId()).thenReturn("externalUserId1");

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).deleteUser(deleteUserRequestContextmock);

        sispMock.deleteUser(deleteUserRequestContextmock);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserBadUserId2Test() throws IdentityStoreException {
        DeleteUserRequestContext deleteUserRequestContextmock = mock(DeleteUserRequestContext.class);
        when(deleteUserRequestContextmock.getUserId()).thenReturn("externalUserId1:");

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).deleteUser(deleteUserRequestContextmock);

        sispMock.deleteUser(deleteUserRequestContextmock);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserBadUserId3Test() throws IdentityStoreException {
        DeleteUserRequestContext deleteUserRequestContextmock = mock(DeleteUserRequestContext.class);
        when(deleteUserRequestContextmock.getUserId()).thenReturn(":externalUserId1");

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).deleteUser(deleteUserRequestContextmock);

        sispMock.deleteUser(deleteUserRequestContextmock);
    }

    @Test
    public void deleteUserTest() throws IdentityStoreException {

        DeleteUserRequestContext deleteUserRequestContextmock = mock(DeleteUserRequestContext.class);
        when(deleteUserRequestContextmock.getUserId()).thenReturn("externalClientId1:01da632448b25e4fb4ca0a25c816a70a");

        User expectedUser = new User();
        expectedUser.setExternalClientId(null);
        expectedUser.setExternalUserId("01da632448b25e4fb4ca0a25c816a70a");
        expectedUser.setFirstName("Buddy35");
        expectedUser.setLastName("Anderson");
        expectedUser.setEmail("buddy35a@smarsh.com");
        expectedUser.setSmarshUserId(null);
        expectedUser.setIsActive(true);

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser);

        DeleteUserResponse respMock = mock(DeleteUserResponse.class);
        when(respMock.isSuccess()).thenReturn(true);
        when(respMock.getUserResults()).thenReturn(expectedUsers);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).deleteUser(deleteUserRequestContextmock);
        when(sispMock.callSmarshService(anyString(), any(), Matchers.<Class<DeleteUserResponse>> any())).thenReturn(respMock);

        sispMock.deleteUser(deleteUserRequestContextmock);
    }

    @Test(expected = BadRequestException.class)
    public void createUserNoExpectedExternalClientIdTest() throws IdentityStoreException {

        AttributeMap attr = new AttributeMap();
        attr.put("externalUserId", "externalUserId1");
        attr.put("actualExternalClientId", "externalClientId1");
        CreateUserRequestContext createUserRequestContextmock = mock(CreateUserRequestContext.class);
        when(createUserRequestContextmock.getUserAttributes()).thenReturn(attr);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).createUser(createUserRequestContextmock);
        doCallRealMethod().when(sispMock).createAndUpdateUserHelper(attr);

        sispMock.createUser(createUserRequestContextmock);
    }

    @Test(expected = BadRequestException.class)
    public void createUserNoActualExternalClientIdTest() throws IdentityStoreException {

        AttributeMap attr = new AttributeMap();
        attr.put("externalUserId", "externalUserId1");
        attr.put("expectedExternalClientId", "externalClientId1");
        CreateUserRequestContext createUserRequestContextmock = mock(CreateUserRequestContext.class);
        when(createUserRequestContextmock.getUserAttributes()).thenReturn(attr);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).createUser(createUserRequestContextmock);
        doCallRealMethod().when(sispMock).createAndUpdateUserHelper(attr);

        sispMock.createUser(createUserRequestContextmock);
    }

    @Test(expected = BadRequestException.class)
    public void createUserNoExternalUserIdTest() throws IdentityStoreException {

        AttributeMap attr = new AttributeMap();
        attr.put("actualExternalClientId", "externalClientId1");
        attr.put("expectedExternalClientId", "externalClientId1");
        CreateUserRequestContext createUserRequestContextmock = mock(CreateUserRequestContext.class);
        when(createUserRequestContextmock.getUserAttributes()).thenReturn(attr);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).createUser(createUserRequestContextmock);
        doCallRealMethod().when(sispMock).createAndUpdateUserHelper(attr);

        sispMock.createUser(createUserRequestContextmock);
    }

    @Test
    public void createUserTest() throws IdentityStoreException {

        AttributeMap attr = new AttributeMap();
        attr.put("actualExternalClientId", "externalClientId1");
        attr.put("expectedExternalClientId", "externalClientId1");
        attr.put("externalUserId", "externalUserId1");

        User user = new User();
        List<User> users = new ArrayList<>();
        users.add(user);

        UpdateUserResponse respMock = mock(UpdateUserResponse.class);
        when(respMock.isSuccess()).thenReturn(true);
        when(respMock.getUserResults()).thenReturn(users);

        CreateUserRequestContext createUserRequestContextMock = mock(CreateUserRequestContext.class);
        when(createUserRequestContextMock.getUserAttributes()).thenReturn(attr);
        when(createUserRequestContextMock.getUserAttributes()).thenReturn(attr);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).createUser(createUserRequestContextMock);
        doCallRealMethod().when(sispMock).createAndUpdateUserHelper(attr);
        when(sispMock.callSmarshService(anyString(), any(), Matchers.<Class<UpdateUserResponse>> any())).thenReturn(respMock);

        UserResponseContextImpl result = sispMock.createUser(createUserRequestContextMock);
        assertEquals("externalClientId1:externalUserId1", result.getUserAttributes().get(Constants.ID).getValue());
    }


    @Test
    public void createUserDifferentCaseExtClientIdTest() throws IdentityStoreException {

        AttributeMap attr = new AttributeMap();
        attr.put("actualExternalClientId", "externalClientId1");
        attr.put("expectedExternalClientId", "EXTERNALCLIENTID1");
        attr.put("externalUserId", "externalUserId1");

        User user = new User();
        List<User> users = new ArrayList<>();
        users.add(user);

        UpdateUserResponse respMock = mock(UpdateUserResponse.class);
        when(respMock.isSuccess()).thenReturn(true);
        when(respMock.getUserResults()).thenReturn(users);

        CreateUserRequestContext createUserRequestContextMock = mock(CreateUserRequestContext.class);
        when(createUserRequestContextMock.getUserAttributes()).thenReturn(attr);
        when(createUserRequestContextMock.getUserAttributes()).thenReturn(attr);

        SmarshIdentityStoreProvisioner sispMock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(sispMock).createUser(createUserRequestContextMock);
        doCallRealMethod().when(sispMock).createAndUpdateUserHelper(attr);
        when(sispMock.callSmarshService(anyString(), any(), Matchers.<Class<UpdateUserResponse>> any())).thenReturn(respMock);

        UserResponseContextImpl result = sispMock.createUser(createUserRequestContextMock);
        assertEquals("externalClientId1:externalUserId1", result.getUserAttributes().get(Constants.ID).getValue());
    }

}
