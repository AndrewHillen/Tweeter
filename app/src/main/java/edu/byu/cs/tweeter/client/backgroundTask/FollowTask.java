package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "FollowTask";

    private User loggedInUser;

    /**
     * The user that is being followed.
     */
    private User followee;
    /**
     * Message handler that will receive task results.
     */
    private Handler messageHandler;

    public FollowTask(AuthToken authToken, User loggedInUser, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public boolean runTask() throws Exception{
        FollowRequest request = new FollowRequest(authToken, loggedInUser.getAlias(), followee.getAlias());
        FollowResponse response = getServerFacade().follow(request);
        // Add functionality later
        return true;
    }

    // Change load bundle later maybe
}
