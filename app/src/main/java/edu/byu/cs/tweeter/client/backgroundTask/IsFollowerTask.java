package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Random;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.CheckFollowRequest;
import edu.byu.cs.tweeter.model.net.response.CheckFollowResponse;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask {
    private static final String LOG_TAG = "IsFollowerTask";

    public static final String IS_FOLLOWER_KEY = "is-follower";


    /**
     * The alleged follower.
     */
    private User follower;
    /**
     * The alleged followee.
     */
    private User followee;

    private boolean isFollower;


    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    public boolean runTask() throws Exception{

        CheckFollowRequest request = new CheckFollowRequest(authToken, follower.getAlias(), followee.getAlias());
        CheckFollowResponse response = getServerFacade().checkFollow(request);

        isFollower = response.getIsFollowing();
        //Add functionality later
        return true;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle)
    {
        //boolean isFollower = new Random().nextInt() > 0;
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }
}
