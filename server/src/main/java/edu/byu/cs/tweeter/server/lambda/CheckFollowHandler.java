package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.CheckFollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.CheckFollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class CheckFollowHandler extends AuthorizationHandler<CheckFollowRequest> implements RequestHandler<CheckFollowRequest, CheckFollowResponse>
{
    @Override
    public CheckFollowResponse handleRequest(CheckFollowRequest request, Context context)
    {
        if(!checkAuthorization(request.getAuthToken(), request.getUserAlias()))
        {
            return badTokenResponse(new CheckFollowResponse(false));
        }
        FollowService service = new FollowService(new DynamoDAOFactory());
        return service.checkFollow(request);
    }
}
