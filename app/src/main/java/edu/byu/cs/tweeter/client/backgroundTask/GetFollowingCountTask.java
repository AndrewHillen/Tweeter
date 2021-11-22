package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {
    private static final String LOG_TAG = "LogoutTask";



    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected boolean fetchCount() throws Exception
    {
        GetFollowingCountRequest request = new GetFollowingCountRequest(authToken, userAlias, targetUser);
        GetFollowingCountResponse response = getServerFacade().getFollowingCount(request);
        count = response.getCount();
        return true;
    }
}
