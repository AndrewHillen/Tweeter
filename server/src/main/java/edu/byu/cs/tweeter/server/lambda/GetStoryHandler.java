package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler extends AuthorizationHandler<GetStoryRequest> implements RequestHandler<GetStoryRequest, GetStoryResponse>
{
    @Override
    public GetStoryResponse handleRequest(GetStoryRequest request, Context context)
    {
        if(!checkAuthorization(request.getAuthToken(), request.getUserAlias()))
        {
            return badTokenResponse(new GetStoryResponse(null));
        }
        StatusService statusService = new StatusService(new DynamoDAOFactory());
        return statusService.getStory(request);
    }
}
