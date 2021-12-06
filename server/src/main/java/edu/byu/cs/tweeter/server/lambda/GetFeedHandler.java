package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler extends AuthorizationHandler<GetFeedRequest> implements RequestHandler<GetFeedRequest, GetFeedResponse>
{
    @Override
    public GetFeedResponse handleRequest(GetFeedRequest request, Context context)
    {
        if(!checkAuthorization(request.getAuthToken(), request.getUserAlias()))
        {
            return badTokenResponse(new GetFeedResponse(null));
        }
        StatusService statusService = new StatusService(new DynamoDAOFactory());
        return statusService.getFeed(request);
    }
}
