package edu.byu.cs.tweeter.model.net.response;

public class GetFollowingCountResponse extends GetCountResponse
{
    public GetFollowingCountResponse(int count)
    {
        super(count);
    }

    public GetFollowingCountResponse(String message)
    {
        super(message);
    }
}
