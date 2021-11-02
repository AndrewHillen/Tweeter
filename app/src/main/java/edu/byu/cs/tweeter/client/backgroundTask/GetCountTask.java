package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class GetCountTask extends AuthenticatedTask
{
    public static final String COUNT_KEY = "count";
    protected int count;
    /**
     * The user whose follower count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;

    public GetCountTask(Handler messageHandler, AuthToken authToken, User targetUser)
    {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected boolean runTask() throws Exception
    {
        if(fetchCount())
        {
            return true;
        }
        return false;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle)
    {
        msgBundle.putInt(COUNT_KEY, count);
    }

    protected abstract boolean fetchCount() throws Exception;
}
