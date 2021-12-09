package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedBatch;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.SQS.SQSUtil;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.JsonSerializer;

public class StatusService extends BaseService
{
    BaseService.FeedDAO feedDAO;
    BaseService.StoryDAO storyDAO;
    FollowDAO followDAO;

    private String FEED_MESSAGES_QUEUE = "https://sqs.us-west-2.amazonaws.com/366733358874/PostStatusQueue";
    private String UPDATE_FEED_QUEUE = "https://sqs.us-west-2.amazonaws.com/366733358874/UpdateFeedQueue";

    public StatusService(DatabaseFactory databaseFactory)
    {
        super(databaseFactory);
        feedDAO = databaseFactory.getFeedDAO();
        storyDAO = databaseFactory.getStoryDAO();
        followDAO = databaseFactory.getFollowDAO();
    }

    // Needs to add status to user story, get all followers, add status to their feeds
    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        long timestamp = new Date().getTime();
        request.getStatus().getUser().setImageBytes(null);
        request.getStatus().setTimestamp(timestamp);
        String userHandle = request.getUserAlias();
        Status status = request.getStatus();

        String messageBody = JsonSerializer.serialize(request);

        SQSUtil SQS = new SQSUtil();

        SQS.sendToQueue(FEED_MESSAGES_QUEUE, messageBody);

        getStoryDAO().addToStory(status, userHandle, timestamp);


        return new PostStatusResponse(true);
    }

    public void generateFeedMessages(Status status)
    {
        System.out.println();
        String alias = status.getUser().getAlias();
        int limit = 25;
        User lastUser = null;
        boolean keepGoing = true;

        while(keepGoing)
        {
            GetFollowersResponse getFollowersResponse = getFollowingDAO().getFollowers(alias, lastUser, limit);
            List<User> users = getFollowersResponse.getItems();
            List<String> aliases = new ArrayList<>();

            for(User user : users)
            {
                aliases.add(user.getAlias());
            }
            if(users.size() > 0)
            {
                lastUser = users.get(users.size() - 1);
                keepGoing = getFollowersResponse.getHasMorePages();
            }
            else
            {
                lastUser = null;
                keepGoing = false;
            }


            FeedBatch feedBatch = new FeedBatch(aliases, status);
            String messageBody = JsonSerializer.serialize(feedBatch);

            SQSUtil SQS = new SQSUtil();

            SQS.sendToQueue(UPDATE_FEED_QUEUE, messageBody);
            //Send users to next part
//            for(User user : users)
//            {
//                getFeedDao().addToFeed(status, user.getAlias(), status.getTimestamp());
//            }
        }
    }

    public void postToFeeds(FeedBatch feedBatch)
    {
        System.out.println("Posting to feeds");
        getFeedDao().batchWritePosts(feedBatch.getUsers(), feedBatch.getStatus(), feedBatch.getStatus().getTimestamp());
    }

    public GetStoryResponse getStory(GetStoryRequest request)
    {
        if(request.getLastItem() != null)
        {
            request.getLastItem().getUser().setImageBytes(null);
        }
        return getStoryDAO().getStory(request.getTargetUser().getAlias(), request.getLastItem(), request.getLimit());
    }

    public GetFeedResponse getFeed(GetFeedRequest request)
    {
        if(request.getLastItem() != null)
        {
            request.getLastItem().getUser().setImageBytes(null);
        }
        return getFeedDao().getFeed(request.getTargetUser().getAlias(), request.getLastItem(), request.getLimit());
    }

    public StatusDAO getStatusDAO()
    {
        return new StatusDAO();
    }

    public BaseService.FeedDAO getFeedDao()
    {
        return feedDAO;
    }

    public BaseService.StoryDAO getStoryDAO()
    {
        return storyDAO;
    }

    FollowDAO getFollowingDAO() {
        return followDAO;
    }
}
