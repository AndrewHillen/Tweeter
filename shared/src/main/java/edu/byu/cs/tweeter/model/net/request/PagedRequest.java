package edu.byu.cs.tweeter.model.net.request;


import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedRequest<T> extends AuthenticatedRequest
{
    public User targetUser;
    public T lastItem;
    public int limit;

    public PagedRequest()
    {
    }

    public PagedRequest(AuthToken authToken, String userAlias, User targetUser, T lastItem, int limit)
    {
        super(authToken, userAlias);
        this.targetUser = targetUser;
        this.lastItem = lastItem;
        this.limit = limit;
    }

    public User getTargetUser()
    {
        return targetUser;
    }

    public void setTargetUser(User targetUser)
    {
        this.targetUser = targetUser;
    }

    public T getLastItem()
    {
        return lastItem;
    }

    public void setLastItem(T lastItem)
    {
        this.lastItem = lastItem;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }
}
