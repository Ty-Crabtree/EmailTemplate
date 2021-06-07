package com.smarsh.pingidentity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class WebServiceResponseTest {

    @Test
    public void readUserResponse() {

        // / Sample read response message from Smarsh Identify Store Web Service, make sure we can deSerialize it
        // {"users":[{"externalUserId":"01da632448b25e4fb4ca0a25c816a70a","firstName":"Buddy35","lastName":"Anderson","email":"buddy35a@smarsh.com"}],"success":true,"errorMessages":null}
        String responseString =
                "{\"users\":[{\"externalUserId\":\"01da632448b25e4fb4ca0a25c816a70a\",\"firstName\":\"Buddy35\",\"lastName\":\"Anderson\",\"email\":\"buddy35a@smarsh.com\"}],\"success\":true,\"errorMessages\":null}";

        SmarshIdentityStoreProvisioner mock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(mock).deSerializeResponse(responseString, ReadUserResponse.class);

        User expectedUser = new User();
        expectedUser.setExternalClientId(null);
        expectedUser.setExternalUserId("01da632448b25e4fb4ca0a25c816a70a");
        expectedUser.setFirstName("Buddy35");
        expectedUser.setLastName("Anderson");
        expectedUser.setEmail("buddy35a@smarsh.com");
        expectedUser.setSmarshUserId(null);

        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser);

        ReadUserResponse expectedReadUserResponse = new ReadUserResponse();
        expectedReadUserResponse.setUsers(expectedUsers);
        expectedReadUserResponse.setSuccess(true);
        expectedReadUserResponse.setErrorMessages(null);

        ReadUserResponse actualReadUserResponse = mock.deSerializeResponse(responseString, ReadUserResponse.class);

        assertEquals(expectedReadUserResponse.isSuccess(), actualReadUserResponse.isSuccess());
        assertEquals(expectedReadUserResponse.getErrorMessages(), actualReadUserResponse.getErrorMessages());
        assertEquals(1, actualReadUserResponse.getUsers().size());
        User actualUser = actualReadUserResponse.getUsers().get(0);
        assertEquals(expectedUser.getExternalClientId(), actualUser.getExternalClientId());
        assertEquals(expectedUser.getExternalUserId(), actualUser.getExternalUserId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getSmarshUserId(), actualUser.getSmarshUserId());
    }

    @Test
    public void updateUserResponse() {

        // Sample update response message from Smarsh Identify Store Web Service, make sure we can deSerialize it
        // {"userResults":[{"externalUserId":"01da632448b25e4fb4ca0a25c816a70a","smarshUserId":"34573","success":true,"errors":null}],"success":true,"errorMessages":null}
        String responseString =
                "{\"userResults\":[{\"externalUserId\":\"01da632448b25e4fb4ca0a25c816a70a\",\"smarshUserId\":\"34573\",\"success\":true,\"errors\":null}],\"success\":true,\"errorMessages\":null}";

        SmarshIdentityStoreProvisioner mock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(mock).deSerializeResponse(responseString, UpdateUserResponse.class);

        User expectedUser = new User();
        expectedUser.setExternalClientId(null);
        expectedUser.setExternalUserId("01da632448b25e4fb4ca0a25c816a70a");
        expectedUser.setFirstName(null);
        expectedUser.setLastName(null);
        expectedUser.setEmail(null);
        expectedUser.setSmarshUserId("34573");

        List<User> expectedUserResults = new ArrayList<>();
        expectedUserResults.add(expectedUser);

        UpdateUserResponse expectedUpdateUserResponse = new UpdateUserResponse();
        expectedUpdateUserResponse.setUserResults(expectedUserResults);
        expectedUpdateUserResponse.setSuccess(true);
        expectedUpdateUserResponse.setErrorMessages(null);

        UpdateUserResponse actualUpdateUserResponse = mock.deSerializeResponse(responseString, UpdateUserResponse.class);

        assertEquals(expectedUpdateUserResponse.isSuccess(), actualUpdateUserResponse.isSuccess());
        assertEquals(expectedUpdateUserResponse.getErrorMessages(), actualUpdateUserResponse.getErrorMessages());
        assertEquals(1, actualUpdateUserResponse.getUserResults().size());
        User actualUser = actualUpdateUserResponse.getUserResults().get(0);
        assertEquals(expectedUser.getExternalClientId(), actualUser.getExternalClientId());
        assertEquals(expectedUser.getExternalUserId(), actualUser.getExternalUserId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getSmarshUserId(), actualUser.getSmarshUserId());
    }

    @Test
    public void deleteUserResponse() {

        // Sample delete response message from Smarsh Identify Store Web Service, make sure we can deSerialize it
        // {"userResults":[{"externalUserId":"01da632448b25e4fb4ca0a25c816a70a","success":true,"errors":null}],"success":true,"errorMessages":null}

        String responseString =
                "{\"userResults\":[{\"externalUserId\":\"01da632448b25e4fb4ca0a25c816a70a\",\"success\":true,\"errors\":null}],\"success\":true,\"errorMessages\":null}";

        SmarshIdentityStoreProvisioner mock = mock(SmarshIdentityStoreProvisioner.class);
        doCallRealMethod().when(mock).deSerializeResponse(responseString, DeleteUserResponse.class);

        User expectedUser = new User();
        expectedUser.setExternalClientId(null);
        expectedUser.setExternalUserId("01da632448b25e4fb4ca0a25c816a70a");
        expectedUser.setFirstName(null);
        expectedUser.setLastName(null);
        expectedUser.setEmail(null);
        expectedUser.setSmarshUserId(null);

        List<User> expectedUserResults = new ArrayList<>();
        expectedUserResults.add(expectedUser);

        DeleteUserResponse expectedDeleteUserResponse = new DeleteUserResponse();
        expectedDeleteUserResponse.setUserResults(expectedUserResults);
        expectedDeleteUserResponse.setSuccess(true);
        expectedDeleteUserResponse.setErrorMessages(null);

        DeleteUserResponse actualDeleteUserResponse = mock.deSerializeResponse(responseString, DeleteUserResponse.class);

        assertEquals(expectedDeleteUserResponse.isSuccess(), actualDeleteUserResponse.isSuccess());
        assertEquals(expectedDeleteUserResponse.getErrorMessages(), actualDeleteUserResponse.getErrorMessages());
        assertEquals(1, actualDeleteUserResponse.getUserResults().size());
        User actualUser = actualDeleteUserResponse.getUserResults().get(0);
        assertEquals(expectedUser.getExternalClientId(), actualUser.getExternalClientId());
        assertEquals(expectedUser.getExternalUserId(), actualUser.getExternalUserId());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getSmarshUserId(), actualUser.getSmarshUserId());
    }

}
