package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";


    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private Status status;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(messageHandler, authToken);
        //status.getUser().setImageBytes(null);
        this.status = status;
    }

    @Override
    public boolean runTask() throws Exception{
        PostStatusRequest request = new PostStatusRequest(authToken, status);
        request.getStatus().getUser().setImageBytes(null);
        PostStatusResponse response = getServerFacade().postStatus(request);
        BackgroundTaskUtils.loadImage(request.getStatus().getUser());
        return true;
    }
}
