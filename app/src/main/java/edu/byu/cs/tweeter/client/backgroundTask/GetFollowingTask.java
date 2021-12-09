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
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowingTask";

    public static final String FOLLOWEES_KEY = "followees";



    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler)
    {
        super(messageHandler, authToken, targetUser, limit, lastItem);
    }

    @Override
    public boolean runTask() throws Exception {
        if(lastItem != null)
        {
            lastItem.setImageBytes(null);
        }
        User targetUser2 = new User(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getAlias(), targetUser.getImageUrl());
        GetFollowingRequest request = new GetFollowingRequest(authToken, userAlias, targetUser2, lastItem, limit);
        GetFollowingResponse response = getServerFacade().getFollowing(request);

        if(lastItem != null)
        {
            BackgroundTaskUtils.loadImage(lastItem);
        }

        items = response.getItems();
        hasMorePages = response.getHasMorePages();

        for (User u : items) {
            BackgroundTaskUtils.loadImage(u);
        }

        if(items == null)
        {
            //Probably set error message here as well
            return false;
        }
        return true;
    }

    @Override
    protected void addItemsToBundle(Bundle msgBundle)
    {
        msgBundle.putSerializable(FOLLOWEES_KEY, (Serializable) items);
    }

    private Pair<List<User>, Boolean> getFollowees() {
        return getFakeData().getPageOfUsers((User) lastItem, limit, targetUser);
    }

}
