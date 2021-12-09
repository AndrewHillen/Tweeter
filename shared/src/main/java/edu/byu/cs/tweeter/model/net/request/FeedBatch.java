package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedBatch
{
    List<String> users;
    Status status;

    public FeedBatch(List<String> users, Status status)
    {
        this.users = users;
        this.status = status;
    }

    public FeedBatch()
    {
    }

    public List<String> getUsers()
    {
        return users;
    }

    public void setUsers(List<String> users)
    {
        this.users = users;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}
