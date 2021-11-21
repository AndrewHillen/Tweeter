package edu.byu.cs.tweeter.client.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable
{
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";
    private static final String LOG_TAG = "Task";
    protected String errorMessage;

    protected Handler messageHandler;

    protected BackgroundTask(Handler messageHandler)
    {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            if(runTask())
            {
                sendSuccessMessage();
            }
            else
            {
                sendFailedMessage();
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private void sendSuccessMessage() throws Exception{
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);

        loadSuccessBundle(msgBundle);

        sendMessage(msgBundle);
    }

    private void sendFailedMessage() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putString(MESSAGE_KEY, errorMessage);

        sendMessage(msgBundle);
    }

    private void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);

        sendMessage(msgBundle);
    }

    private void sendMessage(Bundle msgBundle)
    {
        Message msg = Message.obtain();
        msg.setData(msgBundle);
        messageHandler.sendMessage(msg);
    }

    protected FakeData getFakeData() {
        return new FakeData();
    }

    protected ServerFacade getServerFacade() {return new ServerFacade();}

    protected abstract boolean runTask() throws Exception;
    protected void loadSuccessBundle(Bundle msgBundle) throws Exception
    {

        // Do nothing
        return;
    }

}
