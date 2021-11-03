package edu.byu.cs.tweeter.model.net.response;

public class CheckFollowResponse extends Response
{
    private boolean isFollowing;

    public CheckFollowResponse(boolean isFollowing)
    {
        super(true);
        this.isFollowing = isFollowing;
    }

    public CheckFollowResponse(String message)
    {
        super(false, message);
    }

    public boolean getIsFollowing()
    {
        return isFollowing;
    }

    public void setIsFollowing(boolean isFollowing)
    {
        this.isFollowing = isFollowing;
    }
}
