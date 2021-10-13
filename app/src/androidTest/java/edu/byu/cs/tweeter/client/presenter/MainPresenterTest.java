package edu.byu.cs.tweeter.client.presenter;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.Mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

class MainPresenterTest
{
    private MainPresenter.View mockView;
    private StatusService mockStatusService;
    private Cache mockCache;
    private User user;
    private AuthToken authToken;
    private User fakeUser;
    private AuthToken fakeAuthToken;
    private Status fakeStatus;

    private MainPresenter mainPresenterSpy;
    private FakeData fakeData;

    @BeforeEach
    void setup()
    {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);
        fakeData = new FakeData();

        user = fakeData.getFirstUser();
        authToken = fakeData.getAuthToken();



        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView, authToken, user, user));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

    }

    @Test
    void postStatus_success()
    {
        String post = "post";
        Answer<Void> callHandleSucceededAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                fakeAuthToken = invocation.getArgumentAt(0, AuthToken.class);
                fakeStatus = invocation.getArgumentAt(1, Status.class);
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(2, StatusService.PostStatusObserver.class);
                observer.handleSuccess();
                return null;
            }
        };



        Mockito.doAnswer(callHandleSucceededAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

        mainPresenterSpy.postStatus(post);
        Mockito.verify(mockView).displayInfoMessage("Posting Status...");
        Mockito.verify(mockView).onStatusPost("Successfully Posted!");

        assertEquals(authToken, fakeAuthToken);
        assertEquals(user, fakeStatus.getUser());
        assertEquals(post, fakeStatus.getPost());

    }

    @Test
    void postStatus_failed()
    {
        String post = "post";
        Answer<Void> callHandleFailedAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                fakeAuthToken = invocation.getArgumentAt(0, AuthToken.class);
                fakeStatus = invocation.getArgumentAt(1, Status.class);
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(2, StatusService.PostStatusObserver.class);
                observer.handleFailure("Failure Message");
                return null;
            }
        };

        Mockito.doAnswer(callHandleFailedAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus(post);

        Mockito.verify(mockView).displayErrorMessage("Failure Message");
    }

    @Test
    void postStatus_exception()
    {
        String post = "post";
        Answer<Void> callHandleFailedAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                fakeAuthToken = invocation.getArgumentAt(0, AuthToken.class);
                fakeStatus = invocation.getArgumentAt(1, Status.class);
                StatusService.PostStatusObserver observer = invocation.getArgumentAt(2, StatusService.PostStatusObserver.class);
                observer.handleException(new Exception("Exception message"));
                return null;
            }
        };

        Mockito.doAnswer(callHandleFailedAnswer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.postStatus(post);

        Mockito.verify(mockView).displayErrorMessage("Failed to post status because of exception: Exception message");
    }
}