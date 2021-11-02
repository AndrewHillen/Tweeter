package edu.byu.cs.tweeter.model.net.response;

public class GetFollowerCountResponse extends GetCountResponse
{
    public GetFollowerCountResponse(int count)
    {
        super(count);
    }

    public GetFollowerCountResponse(String message)
    {
        super(message);
    }
}
