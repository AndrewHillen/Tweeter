package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class CheckFollowRequest extends AuthenticatedRequest
{
    public String targetHandle;
    public CheckFollowRequest()
    {
    }

    public CheckFollowRequest(AuthToken authToken, String userHandle, String targetHandle)
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
