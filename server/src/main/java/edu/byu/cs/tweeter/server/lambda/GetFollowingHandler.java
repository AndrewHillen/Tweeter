package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns the users a user is following.
 */
public class GetFollowingHandler extends AuthorizationHandler<GetFollowingRequest> implements RequestHandler<GetFollowingRequest, GetFollowingResponse> {

    @Override
    public GetFollowingResponse handleRequest(GetFollowingRequest request, Context context)
    {
        if(!checkAuthorization(request.getAuthToken(), request.getUserAlias()))
        {
            return badTokenResponse(new GetFollowingResponse(null));
        }
        FollowService service = new FollowService(new DynamoDAOFactory());
        return service.getFollowing(request);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request..
     * @return the followees.
     */

}
