package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;

public abstract class SimpleNotificationHandler<T extends SimpleNotificationObserver> extends BackgroundTaskHandler<T>
{
    protected SimpleNotificationHandler(T observer)
    {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Message msg)
    {
        observer.handleSuccess();
    }
}
