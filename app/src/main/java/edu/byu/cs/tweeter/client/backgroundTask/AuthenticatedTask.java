package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedTask extends BackgroundTask
{
    /**
     * Auth token for logged-in user.
     * This user is the "follower" in the relationship.
     */
    protected AuthToken authToken;
    protected String userAlias = Cache.getInstance().getCurrUser().getAlias();

    public AuthenticatedTask(Handler messageHandler, AuthToken authToken)
    {
        super(messageHandler);
        this.authToken = authToken;
    }
}
