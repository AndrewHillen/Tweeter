package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.UserService;

public class LogoutHandler extends AuthorizationHandler<LogoutRequest> implements RequestHandler<LogoutRequest, LogoutResponse>
{
    @Override
    public LogoutResponse handleRequest(LogoutRequest request, Context context)
    {
        if(request.getAuthToken() == null)
        {
            System.out.println("Null AuthToken not serialized");
        }
        if(!checkAuthorization(request.getAuthToken(), request.getUserAlias()))
        {
            return badTokenResponse(new LogoutResponse(false));
        }
        System.out.println("I make it here 1");
        UserService userService = new UserService(new DynamoDAOFactory());
        return userService.logout(request);

    }
}
