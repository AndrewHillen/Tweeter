package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedHandler<T extends PagedObserver, U> extends BackgroundTaskHandler<T>
{
    public PagedHandler(T observer)
    {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(T observer, Message msg)
    {
        boolean hasMorePages = msg.getData().getBoolean(PagedTask.MORE_PAGES_KEY);
        List<U> items = fetchItems(msg);
        U item = (items.size() > 0) ? items.get(items.size() - 1) : null;

        observer.handleSuccess(items, hasMorePages, item);
    }

    protected abstract List<U> fetchItems(Message msg);


}
