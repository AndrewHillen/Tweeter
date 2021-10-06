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
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowingTask";

    public static final String SUCCESS_KEY = "success";
    public static final String FOLLOWEES_KEY = "followees";
    public static final String MORE_PAGES_KEY = "more-pages";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";


    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler)
    {
        super(messageHandler, authToken, targetUser, limit, lastItem);
    }

    @Override
    public boolean runTask() throws IOException {
        Pair<List<User>, Boolean> pageOfUsers = getFollowees();

        items = pageOfUsers.getFirst();
        hasMorePages = pageOfUsers.getSecond();

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
