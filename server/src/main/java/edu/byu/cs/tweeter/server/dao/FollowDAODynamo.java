package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CheckFollowRequest;
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
import edu.byu.cs.tweeter.server.service.BaseService;
import edu.byu.cs.tweeter.server.util.FakeData;
import edu.byu.cs.tweeter.util.JsonSerializer;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDAODynamo implements BaseService.FollowDAO
{

    String FOLLOW_TABLE = "follows";
    String FOLLOWS_INDEX = "follows_index";
    String PARTITION_KEY = "follower_handle";
    String SORT_KEY = "followee_handle";
    String FOLLOWEE_ATTRIBUTE = "followee_info";
    String FOLLOWER_ATTRIBUTE = "follower_info";

    public FollowResponse follow(FollowRequest request)
    {
        User followee = request.getTargetUser();
        followee.setImageBytes(null);
        User follower = request.getFollower();
        follower.setImageBytes(null);
        String followee_handle = followee.getAlias();
        String follower_handle = follower.getAlias();

        Item item = new Item()
                .withPrimaryKey(PARTITION_KEY, follower_handle, SORT_KEY, followee_handle)
                .withJSON(FOLLOWER_ATTRIBUTE, JsonSerializer.serialize(follower))
                .withJSON(FOLLOWEE_ATTRIBUTE, JsonSerializer.serialize(followee));

        DynamoUtils dynamoUtils = new DynamoUtils(FOLLOW_TABLE);

        dynamoUtils.put(item);

        return new FollowResponse(true);

    }

    public UnFollowResponse unfollow(UnFollowRequest request)
    {
        User followee = request.getTargetUser();
        User follower = request.getFollower();
        String followee_handle = followee.getAlias();
        String follower_handle = follower.getAlias();
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(PARTITION_KEY, follower_handle, SORT_KEY, followee_handle);

        DynamoUtils dynamoUtils = new DynamoUtils(FOLLOW_TABLE);

        dynamoUtils.delete(deleteItemSpec);

        return new UnFollowResponse(true);
    }

    public CheckFollowResponse checkFollow(CheckFollowRequest request)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(FOLLOW_TABLE);
        String follower = request.getUserAlias();
        String followee = request.getTargetHandle();
        GetItemSpec spec = new GetItemSpec()
                .withPrimaryKey(new PrimaryKey(PARTITION_KEY, follower, SORT_KEY, followee));

        Item item = dynamoUtils.get(spec);

        if(item != null)
        {
            return new CheckFollowResponse(true);
        }
        return new CheckFollowResponse(false);
    }


//    public GetFollowersResponse getFollowers(GetFollowersRequest request)
//    {
//        // TODO: Generates dummy data. Replace with a real implementation.
//        assert request.getLimit() > 0;
//        assert request.getTargetUser() != null;
//
//        List<User> allFollowees = getDummyFollowees();
//        List<User> responseFollowees = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowees != null) {
//                int followeesIndex = getFolloweesStartingIndex(request.getLastItem(), allFollowees);
//
//                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//                    responseFollowees.add(allFollowees.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowees.size();
//            }
//        }
//        return new GetFollowersResponse(responseFollowees, hasMorePages);
//    }

    public GetFollowingResponse getFollowing(String follower, User lastUser, int limit)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(FOLLOW_TABLE);

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#handle", PARTITION_KEY);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", follower);

        QuerySpec querySpec = new QuerySpec()
                .withScanIndexForward(true)
                .withMaxResultSize(limit)
                .withKeyConditionExpression("#handle = :handle")
                .withNameMap(nameMap)
                .withValueMap(valueMap);

        if(lastUser != null)
        {
            querySpec.withExclusiveStartKey(new PrimaryKey(PARTITION_KEY, follower, SORT_KEY, lastUser.getAlias()));
        }

        ItemCollection<QueryOutcome> items = dynamoUtils.queryTable(querySpec);
        Item item = null;

        List<User> users = new ArrayList<>();

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext())
        {
            item = iterator.next();
            User user = JsonSerializer.deserialize(item.getJSON(FOLLOWEE_ATTRIBUTE), User.class);
            users.add(user);
        }
        boolean hasMorePages = false;

        if(items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null)
        {
            hasMorePages = true;
        }

        GetFollowingResponse response = new GetFollowingResponse(users, hasMorePages);
        return response;
    }

    public GetFollowersResponse getFollowers(String followee, User lastUser, int limit)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(FOLLOW_TABLE);

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#handle", SORT_KEY);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", followee);

        QuerySpec querySpec = new QuerySpec()
                .withScanIndexForward(true)
                .withMaxResultSize(limit)
                .withKeyConditionExpression("#handle = :handle")
                .withNameMap(nameMap)
                .withValueMap(valueMap);

        if(lastUser != null)
        {
            querySpec.withExclusiveStartKey(new PrimaryKey(PARTITION_KEY, lastUser.getAlias(), SORT_KEY, followee));
        }

        ItemCollection<QueryOutcome> items = dynamoUtils.queryIndex(querySpec, FOLLOWS_INDEX);
        Item item = null;

        List<User> users = new ArrayList<>();

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext())
        {
            item = iterator.next();
            User user = JsonSerializer.deserialize(item.getJSON(FOLLOWER_ATTRIBUTE), User.class);
            users.add(user);
        }
        boolean hasMorePages = false;

        if(items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null)
        {
            hasMorePages = true;
        }

        GetFollowersResponse response = new GetFollowersResponse(users, hasMorePages);
        return response;
    }

//    public GetFollowingResponse getFollowing(GetFollowingRequest request)
//    {
//        // TODO: Generates dummy data. Replace with a real implementation.
//        assert request.getLimit() > 0;
//        assert request.getTargetUser() != null;
//
//        List<User> allFollowees = getDummyFollowees();
//        List<User> responseFollowees = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowees != null) {
//                int followeesIndex = getFolloweesStartingIndex(request.getLastItem(), allFollowees);
//
//                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
//                    responseFollowees.add(allFollowees.get(followeesIndex));
//                }
//
//                hasMorePages = followeesIndex < allFollowees.size();
//            }
//        }
//        return new GetFollowingResponse(responseFollowees, hasMorePages);
//    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowee the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.getAlias().equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
