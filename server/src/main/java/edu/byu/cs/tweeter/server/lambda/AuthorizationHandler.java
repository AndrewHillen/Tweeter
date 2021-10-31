package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthenticateRequest;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.UserService;

//TODO Maybe have this extend Authorization handler with T extends AuthenticatedRequest, U extends Response
public class AuthorizationHandler<T extends AuthenticatedRequest>
{

    protected boolean isAuthorized(AuthToken authToken)
    {
        return new UserService().checkAuthorization(authToken);
    }
}
