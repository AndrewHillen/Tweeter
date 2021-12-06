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
    String PARTITION_KEY = "UserAlias";
    String PASSWORD_ATTRIBUTE = "Password";
    String USER_INFO_ATTRIBUTE = "UserInfo";
    String FOLLOWER_COUNT_ATTRIBUTE = "follower_count";
    String FOLLOWING_COUNT_ATTRIBUTE = "following_count";

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
                .withPrimaryKey(PARTITION_KEY, request.getUsername())
                .withString(PASSWORD_ATTRIBUTE, request.getPassword())
                .withJSON(USER_INFO_ATTRIBUTE, JsonSerializer.serialize(user))
                .withInt(FOLLOWER_COUNT_ATTRIBUTE, 0)
                .withInt(FOLLOWING_COUNT_ATTRIBUTE, 0);

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

        if(user == null)
        {
            return new GetUserResponse(false, "User does not exist");
        }


        return new GetUserResponse(user);
    }

    public void incrementFollowerCount(String alias)
    {
        incrementCount(FOLLOWER_COUNT_ATTRIBUTE, alias);
    }

    public void decrementFollowerCount(String alias)
    {
        decrementCount(FOLLOWER_COUNT_ATTRIBUTE, alias);
    }

    public int getFollowerCount(String alias)
    {
        return getCount(FOLLOWER_COUNT_ATTRIBUTE, alias);
    }

    public void incrementFollowingCount(String alias)
    {
        incrementCount(FOLLOWING_COUNT_ATTRIBUTE, alias);
    }

    public void decrementFollowingCount(String alias)
    {
        decrementCount(FOLLOWING_COUNT_ATTRIBUTE, alias);
    }

    public int getFollowingCount(String alias)
    {
        return getCount(FOLLOWING_COUNT_ATTRIBUTE, alias);
    }

    private void incrementCount(String attribute, String alias)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(USER_TABLE);
        Item item = getUserItem(alias);

        item.withInt(attribute, item.getInt(attribute) + 1);

        dynamoUtils.put(item);
    }

    private void decrementCount(String attribute, String alias)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(USER_TABLE);
        Item item = getUserItem(alias);

        item.withInt(attribute, item.getInt(attribute) - 1);

        dynamoUtils.put(item);
    }

    private int getCount(String attribute, String alias)
    {
        Item item = getUserItem(alias);
        return item.getInt(attribute);
    }

    private Item getUserItem(String alias)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(USER_TABLE);
        GetItemSpec getItemSpec = new GetItemSpec().withPrimaryKey( new PrimaryKey(PARTITION_KEY, alias));
        Item item = dynamoUtils.get(getItemSpec);

        return item;
    }

    private Pair<User, String> getUser(String alias)
    {

        Item item = getUserItem(alias);

        if(item == null)
        {
            return new Pair<>(null, null);
        }

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
