package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.util.JsonSerializer;


public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void>
{
    @Override
    public Void handleRequest(SQSEvent event, Context context)
    {
        FollowService followService = new FollowService(new DynamoDAOFactory());

        for (SQSEvent.SQSMessage msg : event.getRecords())
        {
            PostStatusRequest request = JsonSerializer.deserialize(msg.getBody(), PostStatusRequest.class);
            followService.generateFeedMessages(request.getStatus());
        }

//        StatusService statusService = new StatusService(new DynamoDAOFactory());
//
//        return statusService.postStatus(request);
        return null;
    }
}
