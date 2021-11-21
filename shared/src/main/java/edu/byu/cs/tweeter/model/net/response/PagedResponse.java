package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

/**
 * A response that can indicate whether there is more data available from the server.
 */
public class PagedResponse<T> extends Response {

    protected List<T> items;
    protected boolean hasMorePages;

    public PagedResponse(boolean success, List<T> items, boolean hasMorePages)
    {
        super(success);
        this.items = items;
        this.hasMorePages = hasMorePages;
    }

    PagedResponse(String message) {
        super(false, message);
    }

    /**
     * An indicator of whether more data is available from the server. A value of true indicates
     * that the result was limited by a maximum value in the request and an additional request
     * would return additional data.
     *
     * @return true if more data is available; otherwise, false.
     */
    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public List<T> getItems()
    {
        return items;
    }

    public void setItems(List<T> items)
    {
        this.items = items;
    }

    public void setHasMorePages(boolean hasMorePages)
    {
        this.hasMorePages = hasMorePages;
    }
}
