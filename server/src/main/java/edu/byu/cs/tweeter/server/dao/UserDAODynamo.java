package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.service.BaseService;
import edu.byu.cs.tweeter.server.util.FakeData;
import edu.byu.cs.tweeter.util.JsonSerializer;
import edu.byu.cs.tweeter.util.Pair;

public class UserDAODynamo implements BaseService.UserDAO
{
    String USER_TABLE = "User";
    public boolean checkAuthorization(AuthToken authToken)
    {
        return true;
    }

    public User login(LoginRequest request) {

        Pair<User, String> pair = getUser(request.getUsername());
        User user = pair.getFirst();
        //TODO hash this.
        String password = pair.getSecond();

        if(request.getPassword().equals(password))
        {
            System.out.println("Match");
            return user;
        }
        System.out.println("User was null");
        return null;

    }

    public User register(RegisterRequest request) {

        Pair<User, String> pair = getUser(request.getUsername());
        User checkUser = pair.getFirst();
        if(checkUser != null)
        {
            return null;
        }
        DynamoUtils dynamoUtils = new DynamoUtils(USER_TABLE);

        //TODO Add re-register protection.
        ImageUtil.uploadImage(request.getImage(), request.getUsername());
        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), ImageUtil.getURL(request.getUsername()));




        //TODO Hash this.
        Item item = new Item()
                .withPrimaryKey("UserAlias", request.getUsername())
                .withString("Password", request.getPassword())
                .withJSON("UserInfo", JsonSerializer.serialize(user));

        dynamoUtils.put(item);

        return user;
    }

    public LogoutResponse logout(LogoutRequest request)
    {
        return new LogoutResponse(true);
    }

    public GetUserResponse getUser(GetUserRequest request)
    {
        User user = getUser(request.getAlias()).getFirst();


        return new GetUserResponse(user);
    }

    private Pair<User, String> getUser(String alias)
    {

        DynamoUtils dynamoUtils = new DynamoUtils(USER_TABLE);
        GetItemSpec getItemSpec = new GetItemSpec().withPrimaryKey( new PrimaryKey("UserAlias", alias));
        Item item = dynamoUtils.get(getItemSpec);

        User user = null;
        String password = "";

        try
        {
            String json = item.getJSON("UserInfo");
            password = JsonSerializer.deserialize(item.getJSON("Password"), String.class);
            user = JsonSerializer.deserialize(json, User.class);
        }
        catch (Exception ex)
        {
            System.out.println("GetUser messed up");
            ex.printStackTrace();
            //Do something with this later
        }
        return new Pair<User, String>(user, password);
    }





    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
