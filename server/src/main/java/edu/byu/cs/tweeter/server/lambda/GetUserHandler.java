package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetUserHandler extends AuthorizationHandler<GetUserRequest> implements RequestHandler<GetUserRequest, GetUserResponse>
{
    @Override
    public GetUserResponse handleRequest(GetUserRequest request, Context context)
    {
        if(isAuthorized(request.getAuthToken()))
        {
            UserService userService = new UserService();
            return userService.getUser(request);
        }
        return null;
    }
}
