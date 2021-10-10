package edu.byu.cs.tweeter.client.model.service.handler;

import edu.byu.cs.tweeter.client.model.service.observer.FollowObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UnFollowObserver;

public class UnfollowHandler extends SimpleNotificationHandler<UnFollowObserver>
{
    protected UnfollowHandler(UnFollowObserver observer)
    {
        super(observer);
    }

    @Override
    protected String getFailurePrefix()
    {
        return "Failed to follow";
    }
}
