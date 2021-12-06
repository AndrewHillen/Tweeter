package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.service.BaseService;
import edu.byu.cs.tweeter.server.util.FakeData;
import edu.byu.cs.tweeter.server.util.Pair;
import edu.byu.cs.tweeter.util.JsonSerializer;

public class FeedDAODynamo implements BaseService.FeedDAO
{
    private String FEED_TABLE = "Feed";
    private String PARTITION_KEY = "UserAlias";
    private String SORT_KEY = "Timestamp";
    private String STATUS_ATTRIBUTE = "Status";
    @Override
    public PostStatusResponse addToFeed(Status status, String userHandle, long timestamp)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(FEED_TABLE);
        Item item = new Item()
                .withPrimaryKey(PARTITION_KEY, userHandle, SORT_KEY, timestamp)
                .withJSON(STATUS_ATTRIBUTE, JsonSerializer.serialize(status));

        dynamoUtils.put(item);
        return new PostStatusResponse(true);
    }

    public GetFeedResponse getFeed(String alias, Status lastStatus, int limit)
    {
        DynamoUtils dynamoUtils = new DynamoUtils(FEED_TABLE);

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#handle", PARTITION_KEY);

        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":handle", alias);

        QuerySpec querySpec = new QuerySpec()
                .withScanIndexForward(false)
                .withMaxResultSize(limit)
                .withKeyConditionExpression("#handle = :handle")
                .withNameMap(nameMap)
                .withValueMap(valueMap);

        if(lastStatus != null)
        {
            System.out.println("Last status not null");
            System.out.println(JsonSerializer.serialize(lastStatus));
            querySpec.withExclusiveStartKey(new PrimaryKey(PARTITION_KEY, alias, SORT_KEY, lastStatus.getTimestamp()));
        }

        ItemCollection<QueryOutcome> items = dynamoUtils.queryTable(querySpec);
        Item item = null;

        List<Status> statuses = new ArrayList<>();

        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext())
        {
            item = iterator.next();
            Status status = JsonSerializer.deserialize(item.getJSON(STATUS_ATTRIBUTE), Status.class);
            status.setTimestamp(item.getLong(SORT_KEY));
            statuses.add(status);
        }
        boolean hasMorePages = false;

        if(items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null)
        {
            hasMorePages = true;
        }

        return new GetFeedResponse(statuses, hasMorePages);
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
