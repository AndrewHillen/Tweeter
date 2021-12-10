package edu.byu.cs.tweeter.client.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import android.os.Looper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.util.FakeData;


class IntegrationStatus
{
    private MainPresenter.View mockView;
    private User user;
    private AuthToken authToken;
    private MainPresenter mainPresenterSpy;
    private StatusService statusServiceSpy;
    private MainPresenter mainPresenter;
    private CountDownLatch countDownLatch;
    private PostStatusObserver postStatusObserver;



    public class PostStatusObserver implements StatusService.PostStatusObserver
    {
        MainPresenter.View view;
        public PostStatusObserver(MainPresenter.View view)
        {
            this.view = view;
        }

        @Override
        public void handleSuccess()
        {
            view.onStatusPost("Successfully Posted!");
            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message)
        {
            view.displayErrorMessage(message);
            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception ex)
        {
            view.displayErrorMessage("Failed to post status because of exception: " + ex.getMessage());
            countDownLatch.countDown();
        }
    }

    @BeforeEach
    void setup()
    {
        //Looper looper = Looper.getMainLooper();
        //RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        mockView = Mockito.mock(MainPresenter.View.class);
        postStatusObserver = new PostStatusObserver(mockView);
        statusServiceSpy = Mockito.spy(new StatusService());
        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    void Login_PostStatus()
    {

        //Login and generate status data -------------------------------------------------------------------------
        LoginRequest loginRequest = new LoginRequest("@Alias1", "Password");

        AuthenticateResponse authenticateResponse = null;
        String dateTime = "";
        try
        {
            authenticateResponse = new ServerFacade().login(loginRequest);
            //mainPresenter = new MainPresenter(mockView, authenticateResponse.getAuthToken(), authenticateResponse.getUser(), authenticateResponse.getUser());
            mainPresenterSpy = Mockito.spy(new MainPresenter(mockView, authenticateResponse.getAuthToken(),
                    authenticateResponse.getUser(), authenticateResponse.getUser()));
            Mockito.when(mainPresenterSpy.getPostStatusObserver()).thenReturn(postStatusObserver);
            SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");
            dateTime = statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
            Mockito.when(mainPresenterSpy.getFormattedDateTime()).thenReturn(dateTime);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Assertions.assertTrue(false, "Test failed because of exception");
        }
        String expectedPost = "Post";

        setupStatusService();
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(statusServiceSpy);

        //Post status and verify view calls ----------------------------------------------------------------------


        mainPresenterSpy.postStatus(expectedPost);
        //mainPresenter.postStatus(expectedPost);
        try
        {
            awaitCountDownLatch();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Assertions.assertTrue(false, "Countdown latch had issues");
        }
        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).onStatusPost("Successfully Posted!");

        //Retrieve story and verify contents ---------------------------------------------------------------------

        GetStoryRequest request = new GetStoryRequest(authenticateResponse.getAuthToken(), "@Alias1", authenticateResponse.getUser(), null, 10);
        GetStoryResponse response = null;
        try
        {
            response = new ServerFacade().getStory(request);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Assertions.assertTrue(false, "Test failed because of exception");
        }

        Assertions.assertTrue(response.isSuccess());

        List<Status> story = response.getItems();

        Assertions.assertTrue(story.size() > 0);
        Status testPost = story.get(0);

        Assertions.assertEquals(expectedPost, testPost.getPost());
        Assertions.assertEquals(dateTime, testPost.getDate());



    }

    //Voodoo to skip the android parts that don't work in testing
    void setupStatusService()
    {
        Answer<Void> CallServerAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                AuthToken authToken = invocation.getArgumentAt(0, AuthToken.class);
                Status status = invocation.getArgumentAt(1, Status.class);
                PostStatusResponse response = new ServerFacade().postStatus(new PostStatusRequest(authToken, status));
                PostStatusObserver postStatusObserver2 = (PostStatusObserver) invocation.getArgumentAt(2, StatusService.PostStatusObserver.class);

                if(response.isSuccess())
                {
                    postStatusObserver2.handleSuccess();
                }
                else
                {
                    postStatusObserver2.handleFailure("Failed");
                }
                return null;
            }
        };

        Mockito.doAnswer(CallServerAnswer).when(statusServiceSpy).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

    }

}