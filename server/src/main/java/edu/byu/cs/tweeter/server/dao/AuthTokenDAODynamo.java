package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

import java.util.Date;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.service.BaseService;
import edu.byu.cs.tweeter.util.JsonSerializer;

public class AuthTokenDAODynamo implements BaseService.AuthTokenDAO
{
    private String AUTHTOKEN_TABLE = "AuthToken";
    private DynamoUtils dynamoUtils;
    public AuthTokenDAODynamo()
    {
        dynamoUtils = new DynamoUtils(AUTHTOKEN_TABLE);
    }

    @Override
    public AuthToken generateAuthToken(String userHandle)
    {
        String token = UUID.randomUUID().toString();
        long timestamp = new Date().getTime();

        //TODO Hash this.
        Item item = new Item()
                .withPrimaryKey("Token", token)
                .withString("Alias", userHandle)
                .withLong("Timestamp", timestamp);

        dynamoUtils.put(item);



        String timestampString = Long.toString(timestamp);
        return new AuthToken(token, timestampString);
    }

    @Override
    public boolean checkAuthToken(AuthToken authToken, String userHandle)
    {
        if(authToken == null)
        {
            System.out.println("Null AuthToken");
        }
        GetItemSpec getItemSpec = new GetItemSpec().withPrimaryKey( new PrimaryKey("Token", authToken.getToken()));
        Item item = dynamoUtils.get(getItemSpec);

        if(item != null)
        {
            return true;
        }
        return false;
    }

    @Override
    public LogoutResponse logout(LogoutRequest request)

    {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("Token", request.getAuthToken().getToken()));
        dynamoUtils.delete(deleteItemSpec);
        return new LogoutResponse(true);
    }
}
