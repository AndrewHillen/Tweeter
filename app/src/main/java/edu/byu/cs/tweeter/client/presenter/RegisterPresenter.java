package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.views.AuthenticateView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends AuthenticatePresenter<RegisterPresenter.View>
{
//    private class RegisterObserver implements UserService.RegisterObserver
//    {
//        @Override
//        public void handleSuccess(User user, AuthToken authToken)
//        {
//            view.displayInfoMessage("Hello " + user.getName());
//            view.authenticate(user, authToken);
//        }
//
//        @Override
//        public void handleFailure(String message)
//        {
//            view.displayErrorMessage(message);
//        }
//
//        @Override
//        public void handleException(Exception ex)
//        {
//            view.displayErrorMessage("Failed to register because of exception: " + ex.getMessage());
//            //Do exception stuff
//        }
//    }
//
//    private RegisterObserver registerObserver = new RegisterObserver();

    public interface View extends AuthenticateView
    {
    }

    public RegisterPresenter(View view)
    {
        super(view);
    }

    public void register(String firstName, String lastName,
                         String alias, String password,
                         BitmapDrawable imageToUpload)
    {
        view.displayInfoMessage("Registering...");
        view.clearErrorMessage();
        try
        {
            validateRegistration(firstName, lastName, alias, password, imageToUpload);
            Bitmap image = imageToUpload.getBitmap();
            String imageBytesBase64 = processBitmap(image);
            new UserService().register(firstName, lastName, alias, password, imageBytesBase64, authenticateObserver);
        }
        catch (Exception ex)
        {
            view.displayErrorMessage(ex.getMessage());
            //error
        }
    }

    private String processBitmap(Bitmap image)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();
        String imageBytesBase64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return imageBytesBase64;
    }

    public void validateRegistration(String firstName, String lastName,
                                     String alias, String password,
                                     BitmapDrawable imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

}


