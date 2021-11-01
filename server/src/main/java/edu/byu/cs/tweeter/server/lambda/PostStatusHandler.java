package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostStatusHandler extends AuthorizationHandler<PostStatusRequest> implements RequestHandler<PostStatusRequest, PostStatusResponse>
{
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context)
    {
        if(!checkAuthorization(request.getAuthToken()))
        {
            return badTokenResponse(new PostStatusResponse(false));
        }
        return new StatusService().postStatus(request);
    }
}
