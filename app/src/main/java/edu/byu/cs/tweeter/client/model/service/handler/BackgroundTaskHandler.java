package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class BackgroundTaskHandler<T extends ServiceObserver> extends Handler
{
    private T observer;

    protected BackgroundTaskHandler(T observer)
    {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

//    protected BackgroundTaskHandler(Looper looper, T observer)
//    {
//        super(looper);
//        this.observer = observer;
//    }

    @Override
    public void handleMessage(@NonNull Message msg)
    {
        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
        if (success)
        {
            handleSuccessMessage(observer, msg);
        }
        else if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY))
        {
            String message = getFailurePrefix() + ": " +msg.getData().getString(BackgroundTask.MESSAGE_KEY);
            observer.handleFailure(message);
        }
        else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY))
        {
            Exception ex = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);

            //Pass this as a message later. Maybe just handle failure?
            String message = getFailurePrefix() + " because of Exception: " + ex.getMessage();
            observer.handleException(ex);
        }
    }

    protected abstract void handleSuccessMessage(T observer, Message msg);
    protected abstract String getFailurePrefix();
}
