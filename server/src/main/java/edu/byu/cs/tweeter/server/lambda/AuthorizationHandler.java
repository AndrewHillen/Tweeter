package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthenticateRequest;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.UserService;
import edu.byu.cs.tweeter.util.JsonSerializer;

//TODO Maybe have this extend Authorization handler with T extends AuthenticatedRequest, U extends Response
public class AuthorizationHandler<T extends AuthenticatedRequest>
{

    protected boolean checkAuthorization(AuthToken authToken)
    {
        return new UserService().checkAuthorization(authToken);

    }

    protected void notAuthorized(Context context)
    {
//        Map<String, Object> errorPayload = new HashMap();
//        errorPayload.put("errorType", "BadToken");
//        errorPayload.put("httpStatus", 400);
//        errorPayload.put("requestId", context.getAwsRequestId());
//        errorPayload.put("message", "Invalid AuthToken.");
//
//        String message = JsonSerializer.serialize(errorPayload);

        throw new RuntimeException("Bad AuthToken");
    }

    protected  <T extends Response> T badTokenResponse(T response)
    {
        response.setErrorMessage("Bad AuthToken");
        response.setSuccess(false);
        return response;
    }
}
