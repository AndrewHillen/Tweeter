package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UnFollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnFollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";

    private User loggedInUser;
    /**
     * The user that is being followed.
     */
    private User followee;


    public UnfollowTask(AuthToken authToken, User loggedInUser, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public boolean runTask() throws Exception{
        UnFollowRequest request = new UnFollowRequest(authToken, loggedInUser.getAlias(), followee, loggedInUser);
        UnFollowResponse response = getServerFacade().unfollow(request);
        // Add functionality later
        return true;
    }
}
