package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter implements UserService.RegisterObserver
{

    public interface View
    {
        void register(User user, AuthToken authToken);

        void displayErrorMessage(String message);
        void clearErrorMessage();
        void displayInfoMessage(String message);

    }

    private RegisterPresenter.View view;

    public RegisterPresenter(RegisterPresenter.View view)
    {
        this.view = view;
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
            new UserService().register(firstName, lastName, alias, password, imageBytesBase64, this);
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

    @Override
    public void registerSuccess(User user, AuthToken authToken)
    {
        view.displayInfoMessage("Hello " + user.getName());
        view.register(user, authToken);
    }

    @Override
    public void registerFailure(String message)
    {
        view.displayErrorMessage("Failed to register: " + message);
    }

    @Override
    public void registerException(Exception ex)
    {
        view.displayErrorMessage("Failed to register because of exception: " + ex.getMessage());
    }
}


