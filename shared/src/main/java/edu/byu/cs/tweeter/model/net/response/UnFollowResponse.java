package edu.byu.cs.tweeter.model.net.response;

public class UnFollowResponse extends Response
{
    public UnFollowResponse(boolean success)
    {
        super(success);
    }

    public UnFollowResponse(boolean success, String message)
    {
        super(success, message);
    }
}
