package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class ChangeFollowRequest extends AuthenticatedRequest
{
    protected String userHandle;
    protected String targetHandle;

    public ChangeFollowRequest()
    {
    }

    public ChangeFollowRequest(AuthToken authToken, String userHandle, String targetHandle)
    {
        super(authToken);
        this.userHandle = userHandle;
        this.targetHandle = targetHandle;
    }

    public String getUserHandle()
    {
        return userHandle;
    }

    public void setUserHandle(String userHandle)
    {
        this.userHandle = userHandle;
    }

    public String getTargetHandle()
    {
        return targetHandle;
    }

    public void setTargetHandle(String targetHandle)
    {
        this.targetHandle = targetHandle;
    }
}
