package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";
    public static final String FOLLOWERS_KEY = "followers";


    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler)
    {
        super(messageHandler, authToken, targetUser, limit, lastItem);
    }

    @Override
    public boolean runTask() throws Exception
    {
        GetFollowersRequest request = new GetFollowersRequest(authToken, targetUser, lastItem, limit);
        GetFollowersResponse response = getServerFacade().getFollowers(request);

        items = response.getItems();
        hasMorePages = response.getHasMorePages();

        for (User u : items) {
            BackgroundTaskUtils.loadImage(u);
        }

        if(items == null)
        {
            return false;
        }
        return true;

    }

    @Override
    protected void addItemsToBundle(Bundle msgBundle)
    {
        msgBundle.putSerializable(FOLLOWERS_KEY, (Serializable) items);
    }



}
