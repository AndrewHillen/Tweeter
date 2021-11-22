package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class ChangeFollowRequest extends AuthenticatedRequest
{
    protected String targetHandle;

    public ChangeFollowRequest()
    {
    }

    public ChangeFollowRequest(AuthToken authToken, String userHandle, String targetHandle)
    {
        super(authToken, userHandle);
        this.targetHandle = targetHandle;
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
