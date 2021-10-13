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
    public boolean runTask() throws IOException
    {
        Pair<List<User>, Boolean> pageOfUsers = getFollowers();

        items = pageOfUsers.getFirst();
        hasMorePages = pageOfUsers.getSecond();

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

    private Pair<List<User>, Boolean> getFollowers() {
        Pair<List<User>, Boolean> pageOfUsers = getFakeData().getPageOfUsers(lastItem, limit, targetUser);
        return pageOfUsers;
    }



}
