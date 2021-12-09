package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.model.net.request.FeedBatch;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;
import edu.byu.cs.tweeter.util.JsonSerializer;


public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void>
{
    @Override
    public Void handleRequest(SQSEvent event, Context context)
    {
        StatusService statusService = new StatusService(new DynamoDAOFactory());

        for (SQSEvent.SQSMessage msg : event.getRecords())
        {
            FeedBatch feedBatch = JsonSerializer.deserialize(msg.getBody(), FeedBatch.class);
            statusService.postToFeeds(feedBatch);
        }

//        StatusService statusService = new StatusService(new DynamoDAOFactory());
//
//        return statusService.postStatus(request);
        return null;
    }
}
