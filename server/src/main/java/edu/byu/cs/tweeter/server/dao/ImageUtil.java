package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.Base64;
import com.amazonaws.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class ImageUtil
{
    private static String BUCKET_NAME = "andy-tweeter-profile-photo-bucket";
    private static AmazonS3 s3 = AmazonS3ClientBuilder
        .standard()
        .withRegion("us-west-2")
                .build();

    public ImageUtil()
    {
    }

    public static void uploadImage(String image, String alias)
    {
        try
        {


            byte[] imageBytes = Base64.decode(image);
            ByteArrayInputStream stream = new ByteArrayInputStream(imageBytes);
            s3.putObject(BUCKET_NAME, alias, stream, new ObjectMetadata());
        }
        catch (Exception ex)
        {
            System.out.println("Inputstream problesm");
            ex.printStackTrace();
        }
    }

//    public static byte[] getImage(String alias) throws IOException
//    {
//        S3Object obj = s3.getObject(BUCKET_NAME, alias);
//        S3ObjectInputStream stream = obj.getObjectContent();
//        return IOUtils.toByteArray(stream);
//    }

    public static String getURL(String alias)
    {
        return s3.getUrl(BUCKET_NAME, alias).toString();
    }
}
