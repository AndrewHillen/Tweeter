package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryResponse extends PagedResponse<Status>
{
    public GetStoryResponse(List<Status> items, boolean hasMorePages)
    {
        super(true, items, hasMorePages);
    }

    public GetStoryResponse(String message)
    {
        super(message);
    }

    @Override
    public boolean equals(Object obj)
    {
        return      this.getClass() == obj.getClass()
                &&  this.hasMorePages == ((GetStoryResponse)obj).hasMorePages
                &&  compareStatuses((GetStoryResponse)obj);
    }

    private boolean compareStatuses(GetStoryResponse obj)
    {
        if(this.items.size() != obj.getItems().size())
        {
            return false;
        }

        for(int i = 0; i < this.getItems().size(); i++)
        {
            if(!this.getItems().get(i).equals(obj.getItems().get(i)))
            {
                return false;
            }
        }
        return true;
    }
}
