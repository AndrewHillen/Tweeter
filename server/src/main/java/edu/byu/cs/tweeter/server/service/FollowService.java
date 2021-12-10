package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CheckFollowRequest;
import edu.byu.cs.tweeter.model.net.request.FeedBatch;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.UnFollowRequest;
import edu.byu.cs.tweeter.model.net.response.CheckFollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;
import edu.byu.cs.tweeter.server.SQS.SQSUtil;
import edu.byu.cs.tweeter.server.dao.FollowDAODynamo;
import edu.byu.cs.tweeter.util.JsonSerializer;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {

    FollowDAO followDAO;
    UserDAO userDAO;

    private String UPDATE_FEED_QUEUE = "https://sqs.us-west-2.amazonaws.com/366733358874/UpdateFeedQueue";

    public FollowService(DatabaseFactory databaseFactory)
    {
        super(databaseFactory);
        this.followDAO = databaseFactory.getFollowDAO();
        this.userDAO = databaseFactory.getUserDAO();

    }
    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAODynamo} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public GetFollowingResponse getFollowing(GetFollowingRequest request) {
        User lastUser = request.getLastItem();
        String follower = request.getTargetUser().getAlias();
        int limit = request.getLimit();
        return getFollowingDAO().getFollowing(follower, lastUser, limit);
    }

    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        User lastUser = request.getLastItem();
        String followee = request.getTargetUser().getAlias();
        int limit = request.getLimit();
        return getFollowingDAO().getFollowers(followee, lastUser, limit);
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request)
    {
        int count = getUserDAO().getFollowingCount(request.getTargetUser().getAlias());
        return new GetFollowingCountResponse(count);
    }

    public GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request)
    {
        int count = getUserDAO().getFollowerCount(request.getTargetUser().getAlias());
        return new GetFollowerCountResponse(count);
    }

    public FollowResponse follow(FollowRequest request)
    {
        User followee = request.getTargetUser();
        User follower = request.getFollower();
        getUserDAO().incrementFollowingCount(follower.getAlias());
        getUserDAO().incrementFollowerCount(followee.getAlias());
        return getFollowingDAO().follow(request);
    }

    public UnFollowResponse unfollow(UnFollowRequest request)
    {
        //Decrement count by 1 (user table probably)
        User followee = request.getTargetUser();
        User follower = request.getFollower();
        getUserDAO().decrementFollowingCount(follower.getAlias());
        getUserDAO().decrementFollowerCount(followee.getAlias());
        return getFollowingDAO().unfollow(request);
    }

    public CheckFollowResponse checkFollow(CheckFollowRequest request)
    {
        return getFollowingDAO().checkFollow(request);
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

    /**
     * Returns an instance of {@link FollowDAODynamo}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return followDAO;
    }

    UserDAO getUserDAO()
    {
        return userDAO;
    }

}
