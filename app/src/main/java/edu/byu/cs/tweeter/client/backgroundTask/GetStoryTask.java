package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetStoryTask";

    public static final String STATUSES_KEY = "statuses";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastItem, Handler messageHandler)
    {
        super(messageHandler, authToken, targetUser, limit, lastItem);
    }

    @Override
    public boolean runTask() throws Exception
    {
        if(lastItem != null)
        {
            lastItem.getUser().setImageBytes(null);
        }
        User targetUser2 = new User(targetUser.getFirstName(), targetUser.getLastName(), targetUser.getAlias(), targetUser.getImageUrl());
        GetStoryRequest request = new GetStoryRequest(authToken, userAlias, targetUser2, lastItem, limit);

        GetStoryResponse response = getServerFacade().getStory(request);

        if(lastItem != null)
        {
            BackgroundTaskUtils.loadImage(lastItem.getUser());
        }


        items = response.getItems();
        hasMorePages = response.getHasMorePages();

        for (Status s : items) {
            BackgroundTaskUtils.loadImage(s.getUser());
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
        msgBundle.putSerializable(STATUSES_KEY, (Serializable) items);
    }


    private Pair<List<Status>, Boolean> getStory() {
        Pair<List<Status>, Boolean> pageOfStatus = getFakeData().getPageOfStatus(lastItem, limit);
        return pageOfStatus;
    }
}