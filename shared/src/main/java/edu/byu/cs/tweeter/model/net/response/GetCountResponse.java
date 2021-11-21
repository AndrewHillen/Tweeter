package edu.byu.cs.tweeter.model.net.response;

public class GetCountResponse extends Response
{
    private int count;

    public GetCountResponse(int count)
    {
        super(true);
        this.count = count;
    }

    public GetCountResponse(String message)
    {
        super(false, message);

    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
