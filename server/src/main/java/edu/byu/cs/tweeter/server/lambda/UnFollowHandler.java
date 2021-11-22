package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.UnFollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class UnFollowHandler extends AuthorizationHandler<UnFollowRequest> implements RequestHandler<UnFollowRequest, UnFollowResponse>
{
    @Override
    public UnFollowResponse handleRequest(UnFollowRequest request, Context context)
    {
        if(!checkAuthorization(request.getAuthToken(), request.getUserAlias()))
        {
            return badTokenResponse(new UnFollowResponse(false));
        }
        FollowService service = new FollowService(new DynamoDAOFactory());
        return service.unfollow(request);
    }
}
