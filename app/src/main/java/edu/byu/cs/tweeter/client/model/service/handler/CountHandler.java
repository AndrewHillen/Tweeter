package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;

public abstract class CountHandler<T extends CountObserver> extends BackgroundTaskHandler<T>
{
    public CountHandler(T observer)
    {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Message msg)
    {
        int count = msg.getData().getInt(GetCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
