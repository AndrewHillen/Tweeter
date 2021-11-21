package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowerCountHandler extends AuthorizationHandler<GetFollowerCountRequest> implements RequestHandler<GetFollowerCountRequest, GetFollowerCountResponse>
{
    @Override
    public GetFollowerCountResponse handleRequest(GetFollowerCountRequest request, Context context)
    {
        return new FollowService().getFollowerCount(request);
    }
}
